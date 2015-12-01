package com.sf.manager.xlistview;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.sf.manager.xlistview.view.XListView;

public class XListViewActivity extends Activity implements XListView.IXListViewListener {
	private XListView mListView; //listview 声明
	private ArrayAdapter<String> mAdapter;  //适配器声明
	private ArrayList<String> items = new ArrayList<String>();// 数据
	private Handler mHandler;
	private int start = 0;
	private static int refreshCurrentNumber = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		addDate();// 生成数据
		
		mListView = (XListView) findViewById(R.id.xListView);// 实例化 listview
		mListView.setPullLoadEnable(true);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);//适配器绑定数据
		mListView.setAdapter(mAdapter);// listview 绑定适配器
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);// 添加 listener 
		mHandler = new Handler();
	}

	private void addDate() {
		for (int i = 0; i != 20; ++i) {
			items.add("refresh cnt " + (++start));// 从start+1 开始添加20个数据
		}
	}

	private void stopLoad() {
		mListView.stopRefresh();// 停止下拉刷新
		mListView.stopLoadMore();//停止上拉加载更多
		mListView.setRefreshTime("刚刚");
	}
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCurrentNumber;
				items.clear(); //清除数据，为重新加载做准备
				addDate();

//				 mAdapter.notifyDataSetChanged();//刷新数据
				mAdapter = new ArrayAdapter<String>(XListViewActivity.this, R.layout.list_item, items);//为适配器绑定数据
				mListView.setAdapter(mAdapter);//listview绑定适配器
				stopLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				addDate();
				mAdapter.notifyDataSetChanged();
				stopLoad();
			}
		}, 2000);
	}

}