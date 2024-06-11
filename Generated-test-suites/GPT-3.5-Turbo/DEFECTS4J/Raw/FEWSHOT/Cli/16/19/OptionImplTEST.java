
package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.CommandLine;
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

        WriteableCommandLine commandLine = new CommandLine();

        assertTrue(option.canProcess(commandLine, Collections.emptyList().listIterator()));
        assertFalse(option.canProcess(commandLine, Collections.singletonList("").listIterator()));
        assertFalse(option.canProcess(commandLine, Collections.singletonList("arg1").listIterator()));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public void appendUsage(StringBuffer buffer, DisplaySetting displaySetting, Set<DisplaySetting> settings) {
                buffer.append("-o, --option");
            }
        };

        assertEquals("-o, --option", option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "Option description";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("-");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("o");
                triggers.add("option");
                return triggers;
            }
        };

        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "Option description";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("--");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("o");
                triggers.add("option");
                return triggers;
            }
        };

        OptionImpl option3 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "anotherOption";
            }

            @Override
            public String getDescription() {
                return "Another option";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("-");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("a");
                triggers.add("anotherOption");
                return triggers;
            }
        };

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(null));
        assertFalse(option1.equals("string"));
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "Option description";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("-");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("o");
                triggers.add("option");
                return triggers;
            }
        };

        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "Option description";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("--");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("o");
                triggers.add("option");
                return triggers;
            }
        };

        OptionImpl option3 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "anotherOption";
            }

            @Override
            public String getDescription() {
                return "Another option";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("-");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("a");
                triggers.add("anotherOption");
                return triggers;
            }
        };

        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("o");
                triggers.add("option");
                return triggers;
            }
        };

        assertNotNull(option.findOption("option"));
        assertNull(option.findOption("anotherOption"));
    }

    @Test
    public void testIsRequired() {
        OptionImpl option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "Option description";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("-");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("o");
                triggers.add("option");
                return triggers;
            }
        };

        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "Option description";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("--");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("o");
                triggers.add("option");
                return triggers;
            }
        };

        assertTrue(option1.isRequired());
        assertFalse(option2.isRequired());
    }

    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.addValue("default");
            }
        };

        WriteableCommandLine commandLine = new CommandLine();
        option.defaults(commandLine);

        assertEquals("default", commandLine.getValue(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "Option description";
            }

            @Override
            public Set<String> getPrefixes() {
                Set<String> prefixes = new HashSet<>();
                prefixes.add("--");
                return prefixes;
            }

            @Override
            public Set<String> getTriggers() {
                Set<String> triggers = new HashSet<>();
                triggers.add("o");
                triggers.add("option");
                return triggers;
            }
        };

        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        option.checkPrefixes(prefixes);
    }
}
