import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {
    
    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;
        
        assertFalse(option.canProcess(commandLine, arguments));
    }
    
    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, Set<String> options) {
                buffer.append("Usage: ");
            }
        };
        
        String expected = "Usage: ";
        assertEquals(expected, option.toString());
    }
    
    @Test
    public void testEquals() {
        OptionImpl option = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "name";
            }
            
            public String getDescription() {
                return "description";
            }
            
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }
            
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        OptionImpl sameOption = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "name";
            }
            
            public String getDescription() {
                return "description";
            }
            
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }
            
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        OptionImpl differentOption = new OptionImpl(2, false) {
            public String getPreferredName() {
                return "name2";
            }
            
            public String getDescription() {
                return "description2";
            }
            
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                prefixes.add("--");
                prefixes.add("/");
                return prefixes;
            }
            
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        assertTrue(option.equals(sameOption));
        assertFalse(option.equals(differentOption));
    }
    
    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "name";
            }
            
            public String getDescription() {
                return "description";
            }
            
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }
            
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        OptionImpl sameOption = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "name";
            }
            
            public String getDescription() {
                return "description";
            }
            
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }
            
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        OptionImpl differentOption = new OptionImpl(2, false) {
            public String getPreferredName() {
                return "name2";
            }
            
            public String getDescription() {
                return "description2";
            }
            
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                prefixes.add("--");
                prefixes.add("/");
                return prefixes;
            }
            
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        assertEquals(option.hashCode(), sameOption.hashCode());
        assertNotEquals(option.hashCode(), differentOption.hashCode());
    }
    
    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        Option result = option.findOption("trigger1");
        assertSame(option, result);
        
        result = option.findOption("trigger3");
        assertNull(result);
    }
    
    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true) {
            public void defaults(WriteableCommandLine commandLine) {
                // do nothing
            }
        };
        
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set<String> getPreferredName() {
                return new HashSet<String>();
            }
            
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        prefixes.add("--");
        prefixes.add("/");
        
        option.checkPrefixes(prefixes);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefix() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set<String> getPreferredName() {
                return new HashSet<String>();
            }
            
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger1");
                triggers.add("trigger2");
                return triggers;
            }
        };
        
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        prefixes.add("--");
        prefixes.add("/");
        
        option.checkPrefix(prefixes, "trigger3");
    }
    
}