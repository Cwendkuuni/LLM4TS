import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, ResourceHelper helper) {

            }

            public String getPreferredName() {
                return "test";
            }

            public String getDescription() {
                return "Test option";
            }

            public Set<String> getPrefixes() {
                return null;
            }

            public Set<String> getTriggers() {
                return null;
            }

            public void process(WriteableCommandLine commandLine, String argument) {

            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;

        boolean result = option.canProcess(commandLine, arguments);

        assertFalse(result);
    }

    @Test
    public void testToString() {
        String result = option.toString();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetId() {
        int result = option.getId();

        assertEquals(1, result);
    }

    @Test
    public void testEquals() {
        OptionImpl otherOption = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, ResourceHelper helper) {

            }

            public String getPreferredName() {
                return "test";
            }

            public String getDescription() {
                return "Test option";
            }

            public Set<String> getPrefixes() {
                return null;
            }

            public Set<String> getTriggers() {
                return null;
            }

            public void process(WriteableCommandLine commandLine, String argument) {

            }
        };

        boolean result = option.equals(otherOption);

        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        int result = option.hashCode();

        assertNotNull(result);
    }

    @Test
    public void testFindOption() {
        Option result = option.findOption("test");

        assertEquals(option, result);
    }

    @Test
    public void testIsRequired() {
        boolean result = option.isRequired();

        assertTrue(result);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;

        option.defaults(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        Set<String> prefixes = null;

        option.checkPrefixes(prefixes);
    }
}