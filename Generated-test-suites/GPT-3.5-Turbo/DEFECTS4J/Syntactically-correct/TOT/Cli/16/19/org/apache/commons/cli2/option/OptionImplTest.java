import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setup() {
        option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting settings, Set< ? >triggers) {
                buffer.append("usage: test");
            }
            @Override
            public String getPreferredName() {
                return "test";
            }
            @Override
            public String getDescription() {
                return "test description";
            }
            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("test");
                return triggers;
            }
            @Override
            public Set<?> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                return prefixes;
            }        
        };
    }

    @Test
    public void testCanProcess_ValidArguments_ReturnsFalse() {
        assertFalse(option.canProcess(null, null));
    }

    @Test
    public void testToString_ExpectedOutput() {
        String expected = "usage: test";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals_SameOption_ReturnsTrue() {
        OptionImpl sameOption = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting settings, Set< ? >triggers) {
                buffer.append("usage: test");
            }
            @Override
            public String getPreferredName() {
                return "test";
            }
            @Override
            public String getDescription() {
                return "test description";
            }
            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("test");
                return triggers;
            }
            @Override
            public Set<?> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                return prefixes;
            }        
        };
        assertTrue(option.equals(sameOption));
    }

    @Test
    public void testEquals_DifferentOption_ReturnsFalse() {
        OptionImpl differentOption = new OptionImpl(2, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting settings, Set< ? >triggers) {
                buffer.append("usage: different");
            }
            @Override
            public String getPreferredName() {
                return "test";
            }
            @Override
            public String getDescription() {
                return "different description";
            }
            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("test");
                return triggers;
            }
            @Override
            public Set<?> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                return prefixes;
            }        
        };
        assertFalse(option.equals(differentOption));
    }

    @Test
    public void testHashCode_SameOption_ReturnsTrue() {
        OptionImpl sameOption = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting settings, Set< ? >triggers) {
                buffer.append("usage: test");
            }
            @Override
            public String getPreferredName() {
                return "test";
            }
            @Override
            public String getDescription() {
                return "test description";
            }
            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("test");
                return triggers;
            }
            @Override
            public Set<?> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                return prefixes;
            }        
        };
        assertEquals(option.hashCode(), sameOption.hashCode());
    }

    @Test
    public void testHashCode_DifferentOption_ReturnsFalse() {
        OptionImpl differentOption = new OptionImpl(2, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting settings, Set< ? >triggers) {
                buffer.append("usage: different");
            }
            @Override
            public String getPreferredName() {
                return "test";
            }
            @Override
            public String getDescription() {
                return "different description";
            }
            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("test");
                return triggers;
            }
            @Override
            public Set<?> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                return prefixes;
            }        
        };
        assertNotEquals(option.hashCode(), differentOption.hashCode());
    }

    @Test
    public void testFindOption_ExistingTrigger_ReturnsOption() {
        Option foundOption = option.findOption("test");
        assertNotNull(foundOption);
        assertEquals(option, foundOption);
    }

    @Test
    public void testFindOption_NonExistingTrigger_ReturnsNull() {
        Option foundOption = option.findOption("nonexisting");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired_IsRequired_ReturnsTrue() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequired_IsNotRequired_ReturnsFalse() {
        Option notRequiredOption = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting settings, Set< ? >triggers) {
                buffer.append("usage: notRequired");
            }
            @Override
            public String getPreferredName() {
                return "notRequired";
            }
            @Override
            public String getDescription() {
                return "notRequired description";
            }
            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<String>();
                triggers.add("notRequired");
                return triggers;
            }
            @Override
            public Set<?> getPrefixes() {
                Set<String> prefixes = new HashSet<String>();
                prefixes.add("-");
                return prefixes;
            }        
        };
        assertFalse(notRequiredOption.isRequired());
    }

    @Test
    public void testDefaults_DoNothing() {
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
        // Nothing to assert, just checking if no error occurs
    }

    @Test
    public void testCheckPrefixes_EmptyPrefixList_DoNothing() {
        Set<String> prefixes = new HashSet<String>();
        option.checkPrefixes(prefixes);
        // Nothing to assert, just checking if no error occurs
    }

    @Test
    public void testCheckPrefixes_PrefixMatch_DoNothing() {
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");

        option.checkPrefixes(prefixes);
        // Nothing to assert, just checking if no error occurs
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_PrefixNotMatch_ThrowsException() {
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("#");

        option.checkPrefixes(prefixes);
    }

}