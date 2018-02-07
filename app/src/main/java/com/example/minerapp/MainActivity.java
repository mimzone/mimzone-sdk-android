package com.example.minerapp;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import zone.mim.sdk.SDK;
import zone.mim.sdk.SDKException;
import zone.mim.sdk.enums.ConnectionType;
import zone.mim.sdk.enums.PowerState;
import zone.mim.sdk.enums.Time;
import zone.mim.sdk.enums.WorkLevel;
import zone.mim.sdk.listener.WorkListener;

public class MainActivity extends AppCompatActivity implements WorkListener {


    private LinearLayout minerLinear;
    private Button initializeBtn;
    private TextView initilizingStatus;
    private Button startMining1, stopMining1; // for case no1
    private Button startMining2; // for case no2

    private SDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));

        minerLinear = findViewById(R.id.miner_linear);

        initilizingStatus = findViewById(R.id.main_init_status);

        initializeBtn = findViewById(R.id.main_init_btn);
        initializeBtn.setOnClickListener((View v) -> {
            initilizingStatus.setText("Initializing sdk has started. Please wait..");

            String secret = "";
            String apiKey = "";

            // initialize sdk
            sdk = SDK.getInstance();
            sdk.setWorkListener(this);
            sdk.init(apiKey, secret, PowerState.Any, ConnectionType.Any);
        });

        startMining1 = findViewById(R.id.start_mining_btn1);
        startMining1.setOnClickListener((View v) -> {

            if (sdk != null && sdk.isServiceReady()) {

                try {
                    // start mining with time == infinite, Mining level == High, Power state == Charging, Connection type == Any {Wireless or MobileData}
                    sdk.start(Time.Infinite, WorkLevel.High, PowerState.Charging, ConnectionType.Any);

                    // alternatively, you can use
                    // 1. sdk.start(time)
                    // 2. sdk.start(time, miningLevel)
                } catch (SDKException sdkE) {
                    sdkE.printStackTrace();
                }
            }
        });


        stopMining1 = findViewById(R.id.stop_mining_btn1);
        stopMining1.setOnClickListener((View v) -> {
            if (sdk != null)
                sdk.stop();
        });

        startMining2 = findViewById(R.id.start_mining_btn2);
        startMining2.setOnClickListener((View v) -> {

            if (sdk != null && sdk.isServiceReady()) {

                try {
                    // start mining with:
                    // time == 10 minutes,
                    // Mining level == High,
                    // Power state == Battery,
                    // Connection type == Any {Wireless or MobileData}
                    sdk.start(Time.Minutes10, WorkLevel.High, PowerState.Battery, ConnectionType.Any);

                    // alternatively, you can use
                    // 1. sdk.start(time)
                    // 2. sdk.start(time, miningLevel)
                } catch (SDKException sdkE) {
                    sdkE.printStackTrace();
                }
            }
        });
    }


    // WorkListener callbacks
    @Override
    public void onStarted() {
        // mining has been started
    }

    @Override
    public void onInitialized() {

        initilizingStatus.setText("SDK is ready for use");
        initializeBtn.setVisibility(View.GONE);
        minerLinear.setVisibility(View.VISIBLE);

        // SDK has been initialized successfully
        // You can start mining with sdk.start(time)
    }

    @Override
    public void onProgress(int i) {
        // mining is in progress
    }

    @Override
    public void onCompleted(boolean b) {
        // mining has been completed
    }
}
