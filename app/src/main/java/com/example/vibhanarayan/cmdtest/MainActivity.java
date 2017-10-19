package com.example.vibhanarayan.cmdtest;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.content.Context;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.net.wifi.WifiManager;
import com.example.vibhanarayan.cmdtest.Utils;
import java.io.*;
import android.util.*;

import java.io.FileOutputStream;


public class MainActivity extends Activity {
 private static final String TAG ="blah";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.text2);
        textView.setText("Your IP is:" + Utils.getIPAddress(true));
        final Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                executeCommand();
            }
        });
        final Button button2 = (Button) findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);
                textView.setText("Your IP is:" + Utils.getIPAddress(true));
                if (wifi.setWifiEnabled(false))
                    Toast.makeText(getApplicationContext(), "Wifi is turned off", Toast.LENGTH_LONG).show();


            }
        });
        final Button button3 = (Button) findViewById(R.id.btn3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);
                textView.setText("Your IP is:" + Utils.getIPAddress(true));
                if (wifi.setWifiEnabled(true))
                    Toast.makeText(getApplicationContext(), "Wifi is on", Toast.LENGTH_LONG).show();


            }
        });
        final Button button4 = (Button) findViewById(R.id.btn4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

            }
        });
        TextView tv = (TextView) findViewById(R.id.text3);
        String textToSaveString = tv.getText().toString();
        writeToFile(textToSaveString);
    }

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dmformat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
    String strDate = dmformat.format(calendar.getTime());
    StringBuilder log = new StringBuilder();
    String line;
    private boolean executeCommand() {

        EditText tb = (EditText) findViewById(R.id.textBox1);
        String ip = tb.getText().toString();
        if (ip.matches("")) {
            Toast.makeText(this, "Pinging localhost", Toast.LENGTH_LONG).show();
            ip = "localhost";
        }
        EditText tb1 = (EditText) findViewById(R.id.textBox2);
        String size = tb1.getText().toString();
        if (size.matches("")) {
            size = "64";
        }
        EditText tb2 = (EditText) findViewById(R.id.textBox3);
        String count = tb2.getText().toString();
        if (count.matches("")) {
            count = "3";
        }
        try {
            String command = "/system/bin/ping -c" + " " + count + " " + "-s" + " " + size + " " + ip;
            //System.out.println(command);
            Runtime runtime = Runtime.getRuntime();
            Process mIpAddrProcess = runtime.exec(command);
            int mExitValue = mIpAddrProcess.waitFor();
            TextView tv = (TextView) findViewById(R.id.text3);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(mIpAddrProcess.getInputStream()));

            // Grab the results
            log.append(strDate + ">\n");
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("time=")) {
                    String[] result = line.split("ttl=64");
                    log.append(result[1] + "\n");
                }
                if (line.contains("rtt")) log.append(line + "\n");

            }
            tv.setText(log.toString());

            System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }
        return false;
    }
    private void writeToFile(String data) {
        String filename="myfile.txt";
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }

    }
}

















