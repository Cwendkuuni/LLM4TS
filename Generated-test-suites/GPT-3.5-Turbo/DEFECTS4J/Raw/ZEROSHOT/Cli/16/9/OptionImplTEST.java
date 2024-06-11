import org.apache.commons.cli2.WriteableCommandLine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class OptionImplTest {

    private OptionImpl option; // The option instance to be tested

    @Before
    public void setUp() {
        option = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public String getPreferredName() {
                return "test";
            }

            @Override
            public String getDescription() {
                return "This is a test option";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("t");
                triggers.add("test");
                return triggers;
            }

            @Override
            public Option findOption(String trigger) {
                if (getTriggers().contains(trigger)) {
                    return this;
                } else {
                    return null;
                }
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        List<String> arguments = new ArrayList<>();
        arguments.add("test");
        ListIterator<String> listIterator = arguments.listIterator();
        assertFalse(option.canProcess(commandLine, listIterator));
    }

    @Test
    public void testToString() {
        String expected = "OptionImpl[id=1, preferredName=test, description=This is a test option, prefixes=[-, --], triggers=[t, test]]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl equalOption = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public String getPreferredName() {
                return "test";
            }

            @Override
            public String getDescription() {
                return "This is a test option";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("t");
                triggers.add("test");
                return triggers;
            }

            @Override
            public Option findOption(String trigger) {
                if (getTriggers().contains(trigger)) {
                    return this;
                } else {
                    return null;
                }
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }
        };
        assertTrue(option.equals(equalOption));
    }

    @Test
    public void testHashCode() {
        OptionImpl equalOption = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public String getPreferredName() {
                return "test";
            }

            @Override
            public String getDescription() {
                return "This is a test option";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("-");
                prefixes.add("--");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("t");
                triggers.add("test");
                return triggers;
            }

            @Override
            public Option findOption(String trigger) {
                if (getTriggers().contains(trigger)) {
                    return this;
                } else {
                    return null;
                }
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }
        };
        assertEquals(option.hashCode(), equalOption.hashCode());
    }

    @Test
    public void testFindOption() {
        Option foundOption = option.findOption("test");
        assertNotNull(foundOption);
        assertEquals(option, foundOption);

        foundOption = option.findOption("unknown");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
        // Test that no exception is thrown
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");

        option.checkPrefixes(prefixes);
        // Test that no exception is thrown
    }
}