package com.plusonelabs.calendar;

import java.util.List;

import com.plusonelabs.calendar.R;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WidgetConfigurationActivity extends PreferenceActivity {

	static final String CONFIG_CALLED_FROM_ACTION_BAR_KEY = "com.plusonelabs.calendar.configCalledFromActionBar";
	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appWidgetId=getAppWidgetIdFromIntent();
		if (hasHeaders() && showAddWidgetButton()) {
			Button button = new Button(this);
			button.setText(R.string.prefs_add_widget);
			button.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent resultValue = new Intent();
					resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
					setResult(RESULT_OK, resultValue);
					EventAppWidgetProvider.updateWidget(WidgetConfigurationActivity.this,
							appWidgetId);
					finish();
				}
			});
			setListFooter(button);
		}
	}

	private boolean showAddWidgetButton() {
		Bundle extras = getIntent().getExtras();
		int configCalledFromActionBar=0;
		if (extras != null) {
			configCalledFromActionBar = extras.getInt(CONFIG_CALLED_FROM_ACTION_BAR_KEY,0);
		}
		return appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID && configCalledFromActionBar==0;
	}

	private int getAppWidgetIdFromIntent() {
		Bundle extras = getIntent().getExtras();
		return getAppWidgetIdFromBundle(extras);
	}

	public static int getAppWidgetIdFromBundle(Bundle bundle) {
		if (bundle != null) {
			return bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		return AppWidgetManager.INVALID_APPWIDGET_ID;
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preferences_header, target);
	}
	
	@Override
	public Intent onBuildStartFragmentIntent(String fragmentName, Bundle args,
			int titleRes, int shortTitleRes) {
		args = addAppWidgetIdToBundle(args);
		return super.onBuildStartFragmentIntent(fragmentName, args, titleRes,
				shortTitleRes);
	}

	private Bundle addAppWidgetIdToBundle(Bundle bundle) {
		if(bundle==null){
			bundle = new Bundle();
		}
		bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		return bundle;
	}
}