import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine, final ListIterator arguments) {
                return false;
            }

            public String getPreferredName() {
                return "testOption";
            }

            public String getDescription() {
                return "This is a test option";
            }

            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                return prefixes;
            }

            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("test");
                return triggers;
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;
        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        String expected = "testOption";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testGetId() {
        int expected = 1;
        assertEquals(expected, option.getId());
    }

    @Test
    public void testEquals() {
        OptionImpl otherOption = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine, final ListIterator arguments) {
                return false;
            }

            public String getPreferredName() {
                return "testOption";
            }

            public String getDescription() {
                return "This is a test option";
            }

            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                return prefixes;
            }

            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("test");
                return triggers;
            }
        };
        assertTrue(option.equals(otherOption));
    }

    @Test
    public void testHashCode() {
        int expected = option.getId() * 37 + option.getPreferredName().hashCode() * 37 +
                       option.getDescription().hashCode() * 37 +
                       option.getPrefixes().hashCode() * 37 + option.getTriggers().hashCode();
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testFindOption() {
        String trigger = "test";
        Option expected = option;
        Option result = option.findOption(trigger);
        assertEquals(expected, result);

        trigger = "invalid";
        expected = null;
        result = option.findOption(trigger);
        assertEquals(expected, result);
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesInvalid() {
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("+");
        option.checkPrefixes(prefixes);
    }
}