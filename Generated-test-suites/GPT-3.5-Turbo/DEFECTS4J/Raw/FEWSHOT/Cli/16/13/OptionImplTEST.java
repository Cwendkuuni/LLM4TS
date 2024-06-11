package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionImplTest {

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, false) {
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };

        WriteableCommandLine commandLine = null;
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, false) {
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                buffer.append("usage");
            }
        };

        String expected = "usage";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, false) {
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                buffer.append("usage");
            }
        };

        OptionImpl option2 = new OptionImpl(2, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                buffer.append("usage");
            }
        };

        assertFalse(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, false) {
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                buffer.append("usage");
            }
        };
        int expected = -29315662;
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, false) {
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                buffer.append("usage");
            }
        };

        String trigger = "";
        assertNull(option.findOption(trigger));
    }

    @Test
    public void testIsRequired() {
        boolean expected = true;
        OptionImpl option = new OptionImpl(2, expected) {
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                buffer.append("usage");
            }
        };

        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, false) {
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                buffer.append("usage");
            }
        };

        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefix() {
        OptionImpl option = new OptionImpl(1, false) {
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, String trigger) {
                buffer.append("usage");
            }
        };

        option.checkPrefix(null);

    }
}