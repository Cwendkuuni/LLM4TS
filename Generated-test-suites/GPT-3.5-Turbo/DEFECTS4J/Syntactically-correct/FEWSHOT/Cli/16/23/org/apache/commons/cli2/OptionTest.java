package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Test;

public class OptionTest {
	
    @Test
    public void testProcess() {
    	Option option = new OptionImpl();
    	WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    	ListIterator args = Arrays.asList("-f", "file.txt", "-d", "10").listIterator();
    	try {
    		option.process(commandLine, args);
    	} catch(OptionException e) {
    		// Handle exception
    		return;
    	}
    	
    	assertEquals("file.txt", commandLine.getOptionValue("f"));
    	assertEquals("10", commandLine.getOptionValue("d"));
    }
    
    @Test
    public void testDefaults() {
    	Option option = new OptionImpl();
    	WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    	
    	option.defaults(commandLine);
    	
    	// Verify defaults for option
    }
    
    @Test
    public void testCanProcessString() {
    	Option option = new OptionImpl();
    	WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    	
    	assertTrue(option.canProcess(commandLine, "-f"));
    	assertFalse(option.canProcess(commandLine, "file.txt"));
    }
    
    @Test
    public void testCanProcessListIterator() {
    	Option option = new OptionImpl();
    	WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    	ListIterator arguments = Arrays.asList("-f", "file.txt", "-d", "10").listIterator();
    	
    	assertTrue(option.canProcess(commandLine, arguments));
    	assertFalse(option.canProcess(commandLine, arguments));
    }
    
    @Test
    public void testGetTriggers() {
    	Option option = new OptionImpl();
    	
    	Set triggers = option.getTriggers();
    	
    	// Verify triggers
    }
    
    @Test
    public void testGetPrefixes() {
    	Option option = new OptionImpl();
    	
    	Set prefixes = option.getPrefixes();
    	
    	// Verify prefixes
    }
    
    @Test
    public void testValidate() {
    	Option option = new OptionImpl();
    	WriteableCommandLine commandLine = new WriteableCommandLineImpl();
    	
    	try {
    		option.validate(commandLine);
    	} catch (OptionException e) {
    		// Handle exception
    		return;
    	}
    	
    	// Validate command line
    }
    
    @Test
    public void testHelpLines() {
    	Option option = new OptionImpl();
    	int depth = 0;
    	Set helpSettings = null;
    	Comparator comp = null;
    	
    	List helpLines = option.helpLines(depth, helpSettings, comp);
    	
    	// Verify help lines
    }
    
    @Test
    public void testAppendUsage() {
    	Option option = new OptionImpl();
    	StringBuffer buffer = new StringBuffer();
    	Set helpSettings = null;
    	Comparator comp = null;
    	
    	option.appendUsage(buffer, helpSettings, comp);
    	
    	// Verify buffer
    }
    
    @Test
    public void testGetPreferredName() {
    	Option option = new OptionImpl();
    	
    	String preferredName = option.getPreferredName();
    	
    	// Verify preferred name
    }
    
    @Test
    public void testGetDescription() {
    	Option option = new OptionImpl();
    	
    	String description = option.getDescription();
    	
    	// Verify description
    }
    
    @Test
    public void testGetId() {
    	Option option = new OptionImpl();
    	
    	int id = option.getId();
    	
    	// Verify id
    }
    
    @Test
    public void testFindOption() {
    	Option option = new OptionImpl();
    	String trigger = "-f";
    	
    	Option foundOption = option.findOption(trigger);
    	
    	// Verify found option
    }
    
    @Test
    public void testIsRequired() {
    	Option option = new OptionImpl();
    	
    	boolean isRequired = option.isRequired();
    	
    	// Verify isRequired
    }
    
    @Test
    public void testGetParent() {
    	Option option = new OptionImpl();
    	
    	Option parent = option.getParent();
    	
    	// Verify parent
    }
    
    @Test
    public void testSetParent() {
    	Option option = new OptionImpl();
    	Option parent = new OptionImpl();
    	
    	option.setParent(parent);
    	
    	assertSame(parent, option.getParent());
    }

	private class WriteableCommandLineImpl implements WriteableCommandLine {

		@Override
		public void addOption(String name) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setOptionValue(String name, String value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setOptionValues(String name, List<String> values) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getOptionValue(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> getOptionValues(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasOption(String name) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasOptionWithValue(String name) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasOptionWithValues(String name) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public List<Option> getOptions() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addOption(Option option) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeOption(Option option) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Set<Option> getOptionSet() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Option getOption(String trigger) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setOption(Option option, String value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setOption(Option option, List<String> values) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<Object> getValues(Option option) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasOptionWithName(String name) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasOption(Option option) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeOption(String name) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeOptionWithValue(String name) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addValue(Option option, Object value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeValue(Option option, Object value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeValues(Option option) {
			// TODO Auto-generated method stub
			
		}

	}
}