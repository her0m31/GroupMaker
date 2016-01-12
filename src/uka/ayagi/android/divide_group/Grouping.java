package uka.ayagi.android.divide_group;

import java.util.ArrayList;
import java.util.Random;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Grouping extends Activity implements OnClickListener {
	private ArrayList<String> entered_names;
	private String [] items;
	private EditText  input_name_field;
	private TextView  entered_display;
	private TextView  result;
	private TextView  entered_size;
	private Spinner   spinner;
	private Button    start_button;
	private Button    clear_button;
	private Button    set_button;
	private Bundle    instance_state;
	private int       number_of_member;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        entered_names     =  new ArrayList<String>();
        items             =  getResources().getStringArray(R.array.items1);
        input_name_field  =  (EditText)findViewById(R.id.editText1);
        entered_size      =  (TextView)findViewById(R.id.textView3);
        result            =  (TextView)findViewById(R.id.textView5);
        entered_display   =  (TextView)findViewById(R.id.textView4);
        spinner           =  (Spinner)findViewById(R.id.spinner1);
        set_button        =  (Button)findViewById(R.id.button1);
        start_button      =  (Button)findViewById(R.id.button2);
        clear_button      =  (Button)findViewById(R.id.button3);
        instance_state    =  savedInstanceState;
        number_of_member  =  0;
        
        start_button.setOnClickListener(this); 
        set_button.setOnClickListener(this); 
        clear_button.setOnClickListener(this);    
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner, items);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     }
    
	@Override
	public void onClick(View v) {
		if(v == set_button) {
			onSet();
			return;
		}
		else if(v == start_button) {
			onDivide();
			return;
		}
		else if(v == clear_button) {
			onCreate(instance_state);
			return;
		}
	}
	
	public void onInfo(int info) {
		Toast toast;
		if(info == 0) {
			toast = Toast.makeText(this, getResources().getString(R.string.info), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else if(info == 1) {
			toast = Toast.makeText(this, getResources().getString(R.string.info1), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	public void onSet() {
		String name = input_name_field.getText().toString();
		if( trimun(name).equals("")  || 
				trimun(name).equals(" ") ||
					trimun(name).equals("　")  ) {
			onInfo(0);
			input_name_field.setText("");
			return;
		}
		
		if( number_of_member == 0 ) {
			entered_display.setText(getString(R.string.member));
		}
			
		entered_names.add(trimun(name));
		entered_display.append( " " + entered_names.get(number_of_member) );
		number_of_member++;
		input_name_field.setText("");
		entered_size.setText(getString(R.string.numOfmem) + number_of_member );
	}
	
	public int rand(int x) {
		Random rnd = new Random();
		int ran = rnd.nextInt(x);
		
		return ran;
	}

	public void onDivide() {
    	int numGroup = Integer.parseInt(spinner.getSelectedItem().toString());
    	if( number_of_member < numGroup ) {
    		onInfo(1);
    		return;
    	}
    	int numMembe  = (int)Math.ceil((double)number_of_member / (double)numGroup);
    	
    	int    []   check = new int   [entered_names.size()];
    	String [][] group = new String[numGroup][numMembe];
    	
    	for(int x = 0; x < numGroup; x++) {
    		for(int y = 0; y < numMembe; y++) {
    			group[x][y] = null;
    		}
    	}

    	for(int i = 0; i < entered_names.size(); i++) {
    		check[i] = 0;
    	}
    	
    	int y;
    	for(int i = 0; i < numMembe; i++) {
    		for(int k = 0; k < numGroup; k++) {
    			int x = rand(entered_names.size());
    			if(check[x] == 0) {
    				group[k][i] = entered_names.get(x);
    				check[x]    = 1;
    			}
    			else {
    				for(y = 0; y < entered_names.size(); y++) {
    					if(check[y] == 0) {
    						break;
    					}
    				}
    				if(y != entered_names.size())
    					k--;
    			}
    		} 
    	}
    	
    	onDisplay(group, numGroup, numMembe);
    }
    
    public void onDisplay(String [][] group, int numGroup, int numMembe ) {
    	result.setText("");
    	for(int i = 0; i < numGroup; i++) {
    		result.append(getString(R.string.Group) + Integer.toString(i+1) + ": ");
    		for(int k = 0; k < numMembe; k++) {
    			if(group[i][k] != null) {
        			result.append(group[i][k] + "  ");
    			}
    		}
    		result.append("\n");
    	}
    }

    public String trimun(String s){
    	int len = s.length();
    	int st = 0;
    	char[] val = s.toCharArray();

    	while (st < len && (val[st] <= ' ' || val[st] == ' ')) {
    		st++;
    	}
    	while (st < len && (val[len - 1] <= ' ' || val[len - 1] == '　')) {
    		len--;
    	}
    
    	if(st > 0 || len < s.length()) {
    		return s.substring(st, len);
    	}
    
    	return s;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate( R.menu.mainmenu, menu );
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.goto_mail: {
        		if( result.getText().toString().equals("")) {
        			Toast.makeText(this, getResources().getString(R.string.info2), Toast.LENGTH_LONG).show();
        			return false;
        		} else {
        			Intent i = new Intent(Intent.ACTION_SENDTO);
        			i.setData(Uri.parse("mailto:" + ""));
        			i.putExtra(Intent.EXTRA_TEXT, "\n" + result.getText().toString() );
        			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        			startActivity(i);
        			return true;
        		}
        	}
        }
        return super.onOptionsItemSelected(item);
    }
}
