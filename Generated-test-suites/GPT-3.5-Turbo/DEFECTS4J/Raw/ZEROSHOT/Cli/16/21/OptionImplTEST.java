import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return false;
            }
        };
        assertFalse(option.canProcess(null, null));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, Set<DisplaySetting> settings) {
                buffer.append("Usage");
            }
        };
        assertEquals("Usage", option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "name";
            }

            public String getDescription() {
                return "description";
            }

            public Set<String> getPrefixes() {
                return null;
            }

            public Set<String> getTriggers() {
                return null;
            }
        };

        OptionImpl option2 = new OptionImpl(2, false) {
            public String getPreferredName() {
                return "name";
            }

            public String getDescription() {
                return "description";
            }

            public Set<String> getPrefixes() {
                return null;
            }

            public Set<String> getTriggers() {
                return null;
            }
        };

        assertFalse(option1.equals(option2));
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
                return null;
            }

            public Set<String> getTriggers() {
                return null;
            }
        };
        assertEquals(243029, option.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger");
                return triggers;
            }
        };
        Option result = option.findOption("trigger");
        assertEquals(option, result);
    }

    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true) {
            public boolean isRequired() {
                return true;
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
        option.defaults(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set<String> getPreferredName() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("pre");
                return prefixes;
            }
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("trigger");
                return triggers;
            }
        };
        option.checkPrefixes(null);
    }

}