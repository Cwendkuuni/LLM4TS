
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testCanProcess() {
        // Test when commandLine is null, should return false
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };

        WriteableCommandLine commandLine = null;
        ListIterator<String> arguments = new ArrayList<String>().listIterator();
        
        boolean result = option.canProcess(commandLine, arguments);
        
        assertEquals(false, result);
        
        // Test when commandLine is not null, should return the result of canProcess method
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        commandLine = new WriteableCommandLineImpl(null);
        arguments = new ArrayList<String>().listIterator();
        
        result = option.canProcess(commandLine, arguments);
        
        assertEquals(false, result);
    }

    @Test
    public void testToString() {
        // Test when DisplaySetting is null, should return a empty string
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, Set<Option> processedOptions) {
                // do nothing
            }
        };
        
        String result = option.toString();
        
        assertEquals("", result);
        
        // Test when DisplaySetting is not null, should return the result of appendUsage method
        option = new OptionImpl(2, false) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, Set<Option> processedOptions) {
                buffer.append("Usage: ...");
            }
        };
        
        result = option.toString();
        
        assertEquals("Usage: ...", result);
    }

    @Test
    public void testEquals() {
        // Test when thatObj is not an instance of OptionImpl, should return false
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };
        
        Object thatObj = new Object();
        
        boolean result = option.equals(thatObj);
        
        assertEquals(false, result);
        
        // Test when thatObj is an instance of OptionImpl with different id, should return false
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        thatObj = new OptionImpl(1, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };
        
        result = option.equals(thatObj);
        
        assertEquals(false, result);
        
        // Test when thatObj is an instance of OptionImpl with different preferredName, should return false
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        thatObj = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            public String getPreferredName() {
                return "name";
            }
        };
        
        result = option.equals(thatObj);
        
        assertEquals(false, result);
        
        // Test when thatObj is an instance of OptionImpl with different description, should return false
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        thatObj = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            public String getDescription() {
                return "description";
            }
        };
        
        result = option.equals(thatObj);
        
        assertEquals(false, result);
        
        // Test when thatObj is an instance of OptionImpl with different prefixes, should return false
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        thatObj = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("--");
                return prefixes;
            }
        };
        
        result = option.equals(thatObj);
        
        assertEquals(false, result);
        
        // Test when thatObj is an instance of OptionImpl with different triggers, should return false
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        thatObj = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("-t");
                return triggers;
            }
        };
        
        result = option.equals(thatObj);
        
        assertEquals(false, result);
        
        // Test when thatObj is an instance of OptionImpl with same id, preferredName, description, prefixes, and triggers, should return true
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        thatObj = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        result = option.equals(thatObj);
        
        assertEquals(true, result);
    }

    @Test
    public void testHashCode() {
        // Test when prefixes and triggers are empty, should return the hashcode based on id, preferredName, and description
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };
        
        int result = option.hashCode();
        
        assertEquals(-2017017471, result);
        
        // Test when prefixes and triggers are not empty, should return the hashcode based on id, preferredName, description, prefixes, and triggers
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("--");
                return prefixes;
            }
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("-t");
                return triggers;
            }
        };
        
        result = option.hashCode();
        
        assertEquals(-2014182747, result);
    }

    @Test
    public void testFindOption() {
        // Test when trigger is in the triggers set, should return this Option
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("-t");
                return triggers;
            }
        };
        
        Option result = option.findOption("-t");
        
        assertEquals(option, result);
        
        // Test when trigger is not in the triggers set, should return null
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("-t");
                return triggers;
            }
        };
        
        result = option.findOption("--t");
        
        assertEquals(null, result);
    }

    @Test
    public void testIsRequired() {
        // Test when required is true, should return true
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };
        
        boolean result = option.isRequired();
        
        assertEquals(true, result);
        
        // Test when required is false, should return false
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        result = option.isRequired();
        
        assertEquals(false, result);
    }

    @Test
    public void testDefaults() {
        // Test when commandLine is null, should do nothing
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };
        
        WriteableCommandLine commandLine = null;
        
        option.defaults(commandLine);
        
        // Test when commandLine is not null, should do nothing
        option = new OptionImpl(2, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };
        
        commandLine = new WriteableCommandLineImpl(null);
        
        option.defaults(commandLine);
    }


    private boolean equals(Object left,
                           Object right) {
        if ((left == null) && (right == null)) {
            return true;
        } else if ((left == null) || (right == null)) {
            return false;
        } else {
            return left.equals(right);
        }
    }
}
