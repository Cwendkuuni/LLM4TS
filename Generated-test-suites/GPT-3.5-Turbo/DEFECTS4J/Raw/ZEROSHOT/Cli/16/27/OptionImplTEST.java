import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.cli2.*;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setup() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return false;
            }

            public void appendUsage(StringBuffer buffer, DisplaySetting arg1, String arg2) {}

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return null;
            }

            public Set<String> getPrefixes() {
                return null;
            }

            public Set<String> getTriggers() {
                return null;
            }

            public void process(final WriteableCommandLine commandLine,
                                final ListIterator arguments) {}

            public void defaults(final WriteableCommandLine commandLine) {}
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator arguments = new DefaultListIterator();

        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        String expected = "";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testGetId() {
        int expected = 1;
        assertEquals(expected, option.getId());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return false;
            }

            public void appendUsage(StringBuffer buffer, DisplaySetting arg1, String arg2) {}

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return null;
            }

            public Set<String> getPrefixes() {
                return null;
            }

            public Set<String> getTriggers() {
                return null;
            }

            public void process(final WriteableCommandLine commandLine,
                                final ListIterator arguments) {}

            public void defaults(final WriteableCommandLine commandLine) {}
        };

        OptionImpl option2 = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final ListIterator arguments) {
                return false;
            }

            public void appendUsage(StringBuffer buffer, DisplaySetting arg1, String arg2) {}

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return null;
            }

            public Set<String> getPrefixes() {
                return null;
            }

            public Set<String> getTriggers() {
                return null;
            }

            public void process(final WriteableCommandLine commandLine,
                                final ListIterator arguments) {}

            public void defaults(final WriteableCommandLine commandLine) {}
        };

        assertTrue(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        int expected = 1;
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testFindOption() {
        String trigger = "trigger";
        assertNull(option.findOption(trigger));
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultCommandLine();

        option.defaults(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        option.checkPrefixes(null);
    }
}