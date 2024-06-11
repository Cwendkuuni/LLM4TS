import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {

    private OptionImpl optionImpl;

    @Before
    public void setUp() {
        optionImpl = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
                // Do nothing
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{};
            }

            @Override
            public java.util.Set<String> getTriggers() {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public void checkPrefixes(java.util.Set prefixes) {
                // Do nothing
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        assertFalse(optionImpl.canProcess(commandLine, null));

        commandLine = new WriteableCommandLine() {
            @Override
            public void clear() {
                // Do nothing
            }

            @Override
            public void addOption(Option option, String argument) {
                // Do nothing
            }

            @Override
            public void setOption(Option option, String argument) {
                // Do nothing
            }

            @Override
            public Object getOptionObject(Option option) {
                return null;
            }

            @Override
            public Iterator iterator() {
                return null;
            }

            @Override
            public void removeOption(Option option) {
                // Do nothing
            }
        };

        assertTrue(optionImpl.canProcess(commandLine, null));
    }

    @Test
    public void testToString() {
        assertNotNull(optionImpl.toString());
    }

    @Test
    public void testGetId() {
        assertEquals(1, optionImpl.getId());
    }

    @Test
    public void testEquals() {
        assertFalse(optionImpl.equals(null));
        assertFalse(optionImpl.equals("test"));

        OptionImpl other = new OptionImpl(2, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
                // Do nothing
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{};
            }

            @Override
            public java.util.Set<String> getTriggers() {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public void checkPrefixes(java.util.Set prefixes) {
                // Do nothing
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }
        };

        assertFalse(optionImpl.equals(other));

        OptionImpl same = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
                // Do nothing
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{};
            }

            @Override
            public java.util.Set<String> getTriggers() {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public void checkPrefixes(java.util.Set prefixes) {
                // Do nothing
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }
        };

        assertTrue(optionImpl.equals(same));
    }

    @Test
    public void testHashCode() {
        assertNotNull(optionImpl.hashCode());
    }

    @Test
    public void testFindOption() {
        assertNull(optionImpl.findOption(null));

        OptionImpl other = new OptionImpl(2, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
                // Do nothing
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{};
            }

            @Override
            public java.util.Set<String> getTriggers() {
                return null;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // Do nothing
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public void checkPrefixes(java.util.Set prefixes) {
                // Do nothing
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }
        };

        assertNull(optionImpl.findOption("test"));
        assertNotNull(other.findOption("test"));
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionImpl.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public void clear() {
                // Do nothing
            }

            @Override
            public void addOption(Option option, String argument) {
                // Do nothing
            }

            @Override
            public void setOption(Option option, String argument) {
                // Do nothing
            }

            @Override
            public Object getOptionObject(Option option) {
                return null;
            }

            @Override
            public Iterator iterator() {
                return null;
            }

            @Override
            public void removeOption(Option option) {
                // Do nothing
            }
        };

        optionImpl.defaults(commandLine);
    }

    @Test
    public void testCheckPrefixes() {
        java.util.Set<String> prefixes = new java.util.HashSet<>();
        optionImpl.checkPrefixes(prefixes);

        prefixes.add("-");
        optionImpl.checkPrefixes(prefixes);

        prefixes.add("--");
        optionImpl.checkPrefixes(prefixes);
    }
}