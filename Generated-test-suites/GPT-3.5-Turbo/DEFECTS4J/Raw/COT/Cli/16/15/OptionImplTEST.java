package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        OptionImpl option = new OptionImpl(1, true) {
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return true;
            }
        };
        ListIterator arguments = null;
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, int displaySetting, Set optionTriggers) {
                buffer.append("TestOption");
            }
        };
        String expected = "TestOption";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test";
            }
            public String getDescription() {
                return "Test option";
            }
            public Set getPrefixes() {
                return null;
            }
            public Set getTriggers() {
                return null;
            }
        };
        OptionImpl option2 = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test";
            }
            public String getDescription() {
                return "Test option";
            }
            public Set getPrefixes() {
                return null;
            }
            public Set getTriggers() {
                return null;
            }
        };
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, true) {
            public String getPreferredName() {
                return "test";
            }
            public String getDescription() {
                return "Test option";
            }
            public Set getPrefixes() {
                return null;
            }
            public Set getTriggers() {
                return null;
            }
        };
        int expected = 1;
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set getTriggers() {
                Set triggers = new Set();
                triggers.add("test");
                return triggers;
            }
        };
        String trigger = "test";
        Option result = option.findOption(trigger);
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
        WriteableCommandLine commandLine = null;
        OptionImpl option = new OptionImpl(1, true) {
            public void defaults(WriteableCommandLine commandLine) {
                // do nothing
            }
        };
        option.defaults(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            public Set getTriggers() {
                Set triggers = new Set();
                triggers.add("test");
                return triggers;
            }
        };
        Set prefixes = new Set();
        prefixes.add("pre");
        option.checkPrefixes(prefixes);
    }
}