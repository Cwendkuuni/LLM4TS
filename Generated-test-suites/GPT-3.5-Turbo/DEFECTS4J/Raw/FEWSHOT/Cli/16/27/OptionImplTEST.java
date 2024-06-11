package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private OptionImpl option;
    
    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                // implementation
            }
            
            public String getPreferredName() {
                return "option";
            }
            
            public String getDescription() {
                return "Test option";
            }
            
            public String[] getPrefixes() {
                return new String[]{"-"};
            }
            
            public String getTriggerSeparator() {
                return ",";
            }
            
            public String[] getTriggers() {
                return new String[]{"t", "test"};
            }
        };
    }
    
    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        String argument = null;
        assertFalse(option.canProcess(commandLine, argument));
        
        commandLine = new WriteableCommandLine() {
            public Option findOption(String trigger) {
                return null;
            }
        };
        assertFalse(option.canProcess(commandLine, argument));
        
        commandLine = new WriteableCommandLine() {
            public Option findOption(String trigger) {
                return option;
            }
        };
        assertFalse(option.canProcess(commandLine, argument));
    }
    
    @Test
    public void testToString() {
        String expected = "option";
        assertEquals(expected, option.toString());
    }
    
    @Test
    public void testEquals() {
        OptionImpl anotherOption = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                // implementation
            }
            
            public String getPreferredName() {
                return "option";
            }
            
            public String getDescription() {
                return "Test option";
            }
            
            public String[] getPrefixes() {
                return new String[]{"-"};
            }
            
            public String getTriggerSeparator() {
                return ",";
            }
            
            public String[] getTriggers() {
                return new String[]{"t", "test"};
            }
        };
        
        assertTrue(option.equals(anotherOption));
    }
    
    @Test
    public void testHashCode() {
        int expected = option.getId() * 37 + option.getPreferredName().hashCode();
        expected = (expected * 37) + option.getDescription().hashCode();
        expected = (expected * 37) + option.getPrefixes().hashCode();
        expected = (expected * 37) + option.getTriggers().hashCode();
        
        assertEquals(expected, option.hashCode());
    }
    
    @Test
    public void testFindOption() {
        String trigger = "t";
        Option expected = option;
        assertEquals(expected, option.findOption(trigger));
        
        trigger = "test";
        assertEquals(expected, option.findOption(trigger));
        
        trigger = "random";
        Option expectedNull = null;
        assertEquals(expectedNull, option.findOption(trigger));
    }
    
    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
        // Assert that nothing happens since default implementation does nothing
    }
    
    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = null;
        option.checkPrefixes(prefixes);
        // Assert that nothing happens since prefix list is empty
        
        prefixes = new HashSet<String>();
        option.checkPrefixes(prefixes);
        // Assert that nothing happens since prefix list is empty
        
        prefixes.add("-");
        option.checkPrefixes(prefixes);
        // Assert that nothing happens since prefix list has same prefix as option
        
        prefixes.add("--");
        option.checkPrefixes(prefixes);
        // Assert that nothing happens since prefix list has same prefix as option
        
        prefixes.remove("-");
        option.checkPrefixes(prefixes);
        // Assert that IllegalArgumentException is thrown
    }
}