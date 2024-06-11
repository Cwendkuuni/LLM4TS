package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class OptionImplTest {
    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return true;
            }
        };

        assertTrue(option.canProcess(null, null));

        option = new OptionImpl(2, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }
        };

        assertFalse(option.canProcess(null, null));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void appendUsage(StringBuffer buffer, int displaySetting, String separator) {
                buffer.append("usage");
            }
        };

        assertEquals("usage", option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option";
            }
        };

        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }
        };

        assertTrue(option1.equals(option2));

        option2 = new OptionImpl(2, true) {
            @Override
            public String getPreferredName() {
                return "option";
            }
        };

        assertFalse(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-"};
            }

            @Override
            public String findOption(String trigger) {
                return "option";
            }
        };

        assertEquals(2152024, option.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Set getTriggers() {
                Set triggers = new HashSet<>();
                triggers.add("option");
                return triggers;
            }
        };

        assertEquals(option, option.findOption("option"));
        assertNull(option.findOption("other"));
    }

    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // do nothing
            }
        };

        assertTrue(option.isRequired());

        option = new OptionImpl(2, false) {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // do nothing
            }
        };

        assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.addOption(this);
            }
        };

        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public void addOption(Option option) {
                assertTrue(option instanceof OptionImpl);
            }
        };

        option.defaults(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefix() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Set getTriggers() {
                Set triggers = new HashSet<>();
                triggers.add("-option");
                return triggers;
            }
        };

        Set prefixes = new HashSet<>();
        prefixes.add("--");

        option.checkPrefix(prefixes);
    }
}