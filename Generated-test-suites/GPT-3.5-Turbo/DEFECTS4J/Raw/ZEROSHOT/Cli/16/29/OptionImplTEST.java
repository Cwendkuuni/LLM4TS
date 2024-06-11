import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import static org.junit.Assert.*;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(
                    WriteableCommandLine commandLine,
                    ListIterator arguments) {
                return true;
            }

            public void appendUsage(
                    StringBuffer buffer,
                    DisplaySetting displaySetting,
                    Set optionFlags) {
                // do nothing
            }

            public Set getPrefixes() {
                Set prefixes = new HashSet();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }

            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("h");
                triggers.add("help");
                return triggers;
            }

            public String getPreferredName() {
                return "help";
            }

            public String getDescription() {
                return "Display help information";
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        String expected = "help";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testGetId() {
        int expected = 1;
        assertEquals(expected, option.getId());
    }

    @Test
    public void testEquals() {
        OptionImpl option2 = new OptionImpl(1, true) {
            public boolean canProcess(
                    WriteableCommandLine commandLine,
                    ListIterator arguments) {
                return false;
            }

            public void appendUsage(
                    StringBuffer buffer,
                    DisplaySetting displaySetting,
                    Set optionFlags) {
                // do nothing
            }

            public Set getPrefixes() {
                Set prefixes = new HashSet();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }

            public Set getTriggers() {
                Set triggers = new HashSet();
                triggers.add("h");
                triggers.add("help");
                return triggers;
            }

            public String getPreferredName() {
                return "help";
            }

            public String getDescription() {
                return "Display help information";
            }
        };
        assertEquals(option, option2);
    }

    @Test
    public void testHashCode() {
        int expected = 1;
        expected = (expected * 37) + option.getPreferredName().hashCode();
        expected = (expected * 37) + option.getDescription().hashCode();
        expected = (expected * 37) + option.getPrefixes().hashCode();
        expected = (expected * 37) + option.getTriggers().hashCode();
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testFindOption() {
        String trigger = "help";
        assertEquals(option, option.findOption(trigger));
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

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        Set prefixes = new HashSet();
        prefixes.add("-");
        prefixes.add("--");
        option.checkPrefixes(prefixes);
    }
}