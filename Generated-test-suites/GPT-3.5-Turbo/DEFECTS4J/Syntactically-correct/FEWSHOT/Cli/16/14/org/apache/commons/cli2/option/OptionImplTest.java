package org.apache.commons.cli2.option;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionImplTest {
    
    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;
        assertTrue(option.canProcess(commandLine, arguments));
        
        option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        assertFalse(option.canProcess(commandLine, arguments));
    }
    
    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, Set helpSettings) {
                buffer.append("Test option");
            }
        };
        assertEquals("Test option", option.toString());
    }
    
    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test1";
            }
            public String getDescription() {
                return "Test option 1";
            }
            public Set getPrefixes() {
                Set prefixes = new HashSet();
                prefixes.add("-");
                return prefixes;
            }
            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("t1");
                return triggers;
            }
        };
        OptionImpl option2 = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test1";
            }
            public String getDescription() {
                return "Test option 1";
            }
            public Set getPrefixes() {
                Set prefixes = new HashSet();
                prefixes.add("-");
                return prefixes;
            }
            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("t1");
                return triggers;
            }
        };
        OptionImpl option3 = new OptionImpl(2, true) {
            public String getPreferredName() {
                return "test2";
            }
            public String getDescription() {
                return "Test option 2";
            }
            public Set getPrefixes() {
                Set prefixes = new HashSet();
                prefixes.add("--");
                return prefixes;
            }
            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("t2");
                return triggers;
            }
        };
        
        // Equal options
        assertTrue(option1.equals(option2));
        assertTrue(option2.equals(option1));
        
        // Different options
        assertFalse(option1.equals(option3));
        assertFalse(option3.equals(option1));
    }
    
    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test1";
            }
            public String getDescription() {
                return "Test option 1";
            }
            public Set getPrefixes() {
                Set prefixes = new HashSet();
                prefixes.add("-");
                return prefixes;
            }
            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("t1");
                return triggers;
            }
        };
        OptionImpl option2 = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test1";
            }
            public String getDescription() {
                return "Test option 1";
            }
            public Set getPrefixes() {
                Set prefixes = new HashSet();
                prefixes.add("-");
                return prefixes;
            }
            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("t1");
                return triggers;
            }
        };
        OptionImpl option3 = new OptionImpl(2, true) {
            public String getPreferredName() {
                return "test2";
            }
            public String getDescription() {
                return "Test option 2";
            }
            public Set getPrefixes() {
                Set prefixes = new HashSet();
                prefixes.add("--");
                return prefixes;
            }
            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("t2");
                return triggers;
            }
        };
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }
    
    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("t1");
                triggers.add("t2");
                return triggers;
            }
        };
        
        assertEquals(option, option.findOption("t1"));
        assertEquals(option, option.findOption("t2"));
        assertNull(option.findOption("t3"));
    }
    
    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test";
            }
        };
        
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true) {
            public void defaults(WriteableCommandLine commandLine) {
                List<String> values = new ArrayList<String>();
                values.add("default1");
                values.add("default2");
                commandLine.addValues(getPreferredName(), values);
            }
            public String getPreferredName() {
                return "test";
            }
        };
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            public void addValue(String preferredName, Object value) {
                assertTrue(preferredName.equals("test") && value instanceof List);
                List<String> values = (List<String>) value;
                assertEquals(2, values.size());
                assertEquals("default1", values.get(0));
                assertEquals("default2", values.get(1));
            }
            public void addValue(String preferredName) {}
            public void applyOptions() {}
            public void clearValues() {}
            public List getList(String preferredName) { return null; }
            public String getValue(String preferredName) { return null; }
            public boolean hasOption(String preferredName) { return false; }
            public boolean hasValue(String preferredName) { return false; }
            public void removeAll(String preferredName) {}
            public void removeValue(String preferredName, Object value) {}
            public void setOption(String preferredName) {}
            public void setOption(String preferredName, Object value) {}
            public void validate() {}   
        };
        
        option.defaults(commandLine);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test";
            }
            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("t1");
                return triggers;
            }
        };
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        prefixes.add("--");
        option.checkPrefixes(prefixes);
        
        prefixes.clear();
        prefixes.add("+");
        option.checkPrefixes(prefixes);
    }
}