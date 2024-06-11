package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableOption;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {

    private WriteableCommandLine commandLine;
    private OptionImpl option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testOptionImplWithIdAndRequiredTrue() {
        option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("-p1", "-opt1");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("o1", "opt1");
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.addOption(this);
            }
        };

        option.defaults(commandLine);
        assertTrue(commandLine.hasOption(1));
        assertTrue(commandLine.isSet(1));
    }

    @Test
    public void testOptionImplWithIdAndRequiredFalse() {
        option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("-p1", "-opt1");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("o1", "opt1");
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // no-op
            }
        };

        option.defaults(commandLine);
        assertFalse(commandLine.hasOption(1));
        assertNull(commandLine.getOptionValue(1));
    }

    @Test
    public void testOptionImplCanProcess() {
        option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("-p1", "-opt1");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("o1", "opt1");
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return arguments.hasNext() && arguments.next().equals("--option1");
            }
        };

        assertFalse(option.canProcess(commandLine, null));
        ListIterator<String> arguments = Arrays.asList("--option1").listIterator();
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testOptionImplFindOption() {
        option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("-p1", "-opt1");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("o1", "opt1");
            }
        };

        assertNull(option.findOption("wrongTrigger"));
        assertNotNull(option.findOption("option1"));
    }

    @Test
    public void testOptionImplHashCode() {
        Set<OptionImpl> options = new HashSet<>();
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("-p1", "-opt1");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("o1", "opt1");
            }
        };
        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("-p2", "-opt2");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("o1", "opt1");
            }
        };

        options.add(option1);
        options.add(option2);
        assertTrue(options.size() == 2);
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.hashCode() == 59);
    }

    @Test
    public void testOptionImplToString() {
        option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("-p1", "-opt1");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("o1", "opt1");
            }
        };

        String expected = "Option[" + option.getId() + "]{" +
                " name='option1'," +
                " usage='<option1>'," +
                " description=''," +
                " required=false," +
                " prefixes=[-p1, -opt1]," +
                " triggers=[o1, opt1], " +
                "syntax=<option1>]";
        String actual = option.toString();
        assertEquals(expected, actual);
    }
}