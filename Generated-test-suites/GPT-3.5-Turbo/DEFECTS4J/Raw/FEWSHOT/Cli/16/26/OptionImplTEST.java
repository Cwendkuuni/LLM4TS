package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {
    private OptionImpl option;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            public boolean hasOption(Option option) {
                return false;
            }
        };
        ListIterator arguments = new ArrayList<String>().listIterator();
        arguments.add("argument");

        assertTrue(option.canProcess(commandLine, arguments));

        commandLine = new WriteableCommandLine() {
            public boolean hasOption(Option option) {
                return true;
            }
        };
        arguments = new ArrayList<String>().listIterator();
        arguments.add("argument");

        assertFalse(option.canProcess(commandLine, arguments));

        arguments = new ArrayList<String>().listIterator();
        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        String expected = "Usage: Option";

        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl equalOption = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }
        };

        OptionImpl differentId = new OptionImpl(2, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }
        };

        OptionImpl differentRequired = new OptionImpl(1, false) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }
        };

        OptionImpl differentPreferredName = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }

            public String getPreferredName() {
                return "differentName";
            }

            public String getDescription() {
                return null;
            }

            public Set getPrefixes() {
                return null;
            }

            public Set getTriggers() {
                return null;
            }
        };

        OptionImpl differentDescription = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return "differentDescription";
            }

            public Set getPrefixes() {
                return null;
            }

            public Set getTriggers() {
                return null;
            }
        };

        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        prefixes.add("--");
        Set<String> triggers = new HashSet<String>();
        triggers.add("-f");
        triggers.add("--foo");

        OptionImpl differentPrefixes = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return null;
            }

            public Set getPrefixes() {
                Set<String> differentPrefixes = new HashSet<String>();
                differentPrefixes.add("/");
                return differentPrefixes;
            }

            public Set getTriggers() {
                return null;
            }
        };

        OptionImpl differentTriggers = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return null;
            }

            public Set getPrefixes() {
                return null;
            }

            public Set getTriggers() {
                Set<String> differentTriggers = new HashSet<String>();
                differentTriggers.add("-g");
                return differentTriggers;
            }
        };

        assertEquals(option, option);
        assertEquals(option, equalOption);

        assertNotEquals(option, differentId);
        assertNotEquals(option, differentRequired);
        assertNotEquals(option, differentPreferredName);
        assertNotEquals(option, differentDescription);
        assertNotEquals(option, differentPrefixes);
        assertNotEquals(option, differentTriggers);
    }

    @Test
    public void testHashCode() {
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        prefixes.add("--");
        Set<String> triggers = new HashSet<String>();
        triggers.add("-f");
        triggers.add("--foo");

        OptionImpl sameOption = new OptionImpl(1, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return null;
            }

            public Set getPrefixes() {
                return null;
            }

            public Set getTriggers() {
                return null;
            }
        };

        OptionImpl differentId = new OptionImpl(2, true) {
            public boolean canProcess(final WriteableCommandLine commandLine,
                                      final String argument) {
                return true;
            }

            public void appendUsage(final StringBuffer buffer,
                                    final int detailLevel,
                                    final Set args) {
                buffer.append("Usage: Option");
            }

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return null;
            }

            public Set getPrefixes() {
                return null;
            }

            public Set getTriggers() {
                return null;
            }
        };

        assertEquals(option.hashCode(), option.hashCode());
        assertEquals(option.hashCode(), sameOption.hashCode());

        assertNotEquals(option.hashCode(), differentId.hashCode());
    }

    @Test
    public void testFindOption() {
        String trigger = "-f";
        Option foundOption = option.findOption(trigger);

        assertEquals(option, foundOption);
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            public boolean hasOption(Option option) {
                return false;
            }
        };

        option.defaults(commandLine);

        // Expecting no error or exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<String>();
        prefixes.add("-");
        prefixes.add("--");

        option.checkPrefixes(prefixes);
    }
}