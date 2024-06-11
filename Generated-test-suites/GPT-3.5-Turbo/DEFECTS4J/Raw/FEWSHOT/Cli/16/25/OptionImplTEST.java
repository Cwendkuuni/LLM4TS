package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {

    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPrefixes() {
                return null;
            }

            @Override
            public String getTriggers() {
                return null;
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testToString() {
        assertEquals("", option.toString());
    }

    @Test
    public void testEquals() {
        Object obj = null;
        assertFalse(option.equals(obj));

        obj = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPrefixes() {
                return null;
            }

            @Override
            public String getTriggers() {
                return null;
            }
        };
        assertTrue(option.equals(obj));

        obj = new OptionImpl(2, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPrefixes() {
                return null;
            }

            @Override
            public String getTriggers() {
                return null;
            }
        };
        assertFalse(option.equals(obj));

        obj = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPrefixes() {
                return null;
            }

            @Override
            public String getTriggers() {
                return null;
            }
        };
        assertFalse(option.equals(obj));

        obj = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return "";
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPrefixes() {
                return null;
            }

            @Override
            public String getTriggers() {
                return null;
            }
        };
        assertFalse(option.equals(obj));

        obj = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public String getPrefixes() {
                return null;
            }

            @Override
            public String getTriggers() {
                return null;
            }
        };
        assertFalse(option.equals(obj));

        obj = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPrefixes() {
                return "";
            }

            @Override
            public String getTriggers() {
                return null;
            }
        };
        assertFalse(option.equals(obj));

        obj = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPrefixes() {
                return null;
            }

            @Override
            public String getTriggers() {
                return "";
            }
        };
        assertFalse(option.equals(obj));
    }

    @Test
    public void testHashCode() {
        option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String separator) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public String getPrefixes() {
                return null;
            }

            @Override
            public String getTriggers() {
                return null;
            }
        };
        assertEquals(-1871367066, option.hashCode());
    }

    @Test
    public void testFindOption() {
        String trigger = null;
        assertEquals(null, option.findOption(trigger));
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
        option.checkPrefixes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefix() {
        option.checkPrefix(null, null);
    }
}