package com.antest1.gotobrowser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.antest1.gotobrowser.Constants.PREF_ADJUSTMENT;
import static com.antest1.gotobrowser.Constants.PREF_CONNECTOR;
import static com.antest1.gotobrowser.Constants.PREF_LANDSCAPE;
import static com.antest1.gotobrowser.Constants.URL_LIST;
import static com.antest1.gotobrowser.Constants.URL_NITRABBIT;
import static com.antest1.gotobrowser.Constants.URL_OOI;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class EntranceActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    private TextView startButton, selectButton, versionText;
    private Switch landscapeSwitch, adjustmentSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_key), Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(PREF_LANDSCAPE, false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        backPressCloseHandler = new BackPressCloseHandler(this);

        startButton = findViewById(R.id.webview_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String connector = sharedPref.getString(PREF_CONNECTOR, null);
                if (connector == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.select_server_toast), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(EntranceActivity.this, FullscreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        landscapeSwitch = findViewById(R.id.switch_landscape);
        landscapeSwitch.setChecked(sharedPref.getBoolean(PREF_LANDSCAPE, false));
        landscapeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ApplySharedPref")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.edit().putBoolean(PREF_LANDSCAPE, isChecked).commit();
            }
        });

        adjustmentSwitch = findViewById(R.id.switch_adjustment);
        adjustmentSwitch.setChecked(sharedPref.getBoolean(PREF_ADJUSTMENT, false));
        adjustmentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ApplySharedPref")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.edit().putBoolean(PREF_ADJUSTMENT, isChecked).commit();
            }
        });

        final String[] listItems = getResources().getStringArray(R.array.connector_list);
        selectButton = findViewById(R.id.connector_select);
        String connector = sharedPref.getString(PREF_CONNECTOR, null);
        if (connector != null) {
            selectButton.setText(connector);
        } else {
            selectButton.setText(getString(R.string.select_server));
        }

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int connector_idx = -1;
                String connector = sharedPref.getString(PREF_CONNECTOR, null);
                for (int i = 0; i < listItems.length; i++) {
                    if (listItems[i].equals(connector)) {
                        connector_idx = i;
                        break;
                    }
                }
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EntranceActivity.this);
                mBuilder.setTitle("Select Connector");
                mBuilder.setSingleChoiceItems(listItems, connector_idx, new DialogInterface.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPref.edit().putString(PREF_CONNECTOR, listItems[i]).commit();
                        selectButton.setText(listItems[i]);
                        Toast.makeText(getApplicationContext(), URL_LIST[i], Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        versionText = findViewById(R.id.version_info);
        versionText.setText(String.format(Locale.US, getString(R.string.version_format), BuildConfig.VERSION_NAME));
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
