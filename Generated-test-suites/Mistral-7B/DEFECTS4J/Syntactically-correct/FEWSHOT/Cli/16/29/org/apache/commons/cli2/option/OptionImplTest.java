package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.parser.DefaultParser;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testEquals() {
        final OptionImpl option1 = new OptionImpl(1, true, "opt", "o") {
            @Override
            public String getDescription() {
                return "Test option 1";
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o", "opt"};
            }
        };

        final OptionImpl option2 = new OptionImpl(1, true, "opt", "o") {
            @Override
            public String getDescription() {
                return "Test option 2";
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o", "opt"};
            }
        };

        final OptionImpl option3 = new OptionImpl(1, true, "opt2", "o2") {
            @Override
            public String getDescription() {
                return "Test option 3";
            }

            @Override
            public String getPreferredName() {
                return "opt2";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t2"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o2", "opt2"};
            }
        };

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testFindOption() {
        final OptionImpl option = new OptionImpl(1, true, "opt", "o") {
            @Override
            public String getDescription() {
                return "Test option";
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o", "opt"};
            }
        };

        assertEquals(option, option.findOption("o"));
        assertEquals(option, option.findOption("opt"));
        assertNull(option.findOption("no"));
    }

    @Test
    public void testHashCode() {
        final OptionImpl option = new OptionImpl(1, true, "opt", "o") {
            @Override
            public String getDescription() {
                return "Test option";
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o", "opt"};
            }
        };

        final int hashCode1 = option.hashCode();

        final OptionImpl option2 = new OptionImpl(1, true, "opt", "o") {
            @Override
            public String getDescription() {
                return "Test option 2";
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o", "opt"};
            }
        };

        final int hashCode2 = option2.hashCode();

        assertEquals(hashCode1, hashCode2);

        final OptionImpl option3 = new OptionImpl(1, true, "opt2", "o2") {
            @Override
            public String getDescription() {
                return "Test option 2";
            }

            @Override
            public String getPreferredName() {
                return "opt2";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t2"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o2", "opt2"};
            }
        };

        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testCanProcess() {
        final OptionImpl option = new OptionImpl(1, true, "opt", "o") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      ListIterator<String> arguments) {
                return false;
            }
        };

        final WriteableCommandLine commandLine = new DefaultParser().parse(new String[]{"--test", "-t", "opt"});
        assertFalse(option.canProcess(commandLine, commandLine.getArguments().listIterator()));

        final WriteableCommandLine commandLine2 = new DefaultParser().parse(new String[]{"--test", "-t2", "opt2"});
        assertFalse(option.canProcess(commandLine2, commandLine2.getArguments().listIterator()));

        final WriteableCommandLine commandLine3 = new DefaultParser().parse(new String[]{"--test", "-t", "opt2"});
        assertFalse(option.canProcess(commandLine3, commandLine3.getArguments().listIterator()));

        final WriteableCommandLine commandLine4 = new DefaultParser().parse(new String[]{"--test", "--opt"});
        assertFalse(option.canProcess(commandLine4, commandLine4.getArguments().listIterator()));

        final WriteableCommandLine commandLine5 = new DefaultParser().parse(new String[]{"--test", "-t", "o"});
        assertFalse(option.canProcess(commandLine5, commandLine5.getArguments().listIterator()));

        final WriteableCommandLine commandLine6 = new DefaultParser().parse(new String[]{"--test", "-t", "opt", "arg"});
        assertTrue(option.canProcess(commandLine6, commandLine6.getArguments().listIterator()));
    }

    @Test
    public void testToString() {
        final OptionImpl option = new OptionImpl(1, true, "opt", "o") {
            @Override
            public String getDescription() {
                return "Test option";
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o", "opt"};
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue(getId(), "default");
            }
        };

        final WriteableCommandLine commandLine = new DefaultParser().parse(new String[]{"--test", "-t", "opt", "arg"});
        option.getValue(commandLine);
        assertEquals("arg", option.getValue(commandLine));
    }

    @Test
    public void testCheckPrefixes() {
        final OptionImpl option = new OptionImpl(1, true, "opt", "o") {
            @Override
            public String getDescription() {
                return "Test option";
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public String[] getPrefixes() {
                return new String[]{"-t"};
            }

            @Override
            public String[] getTriggers() {
                return new String[]{"o", "opt"};
            }
        };

        final ResourceHelper helper = ResourceHelper.getResourceHelper();

        try {
            option.checkPrefixes(java.util.Collections.singleton("-x"));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals(helper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "o", "-x"), e.getMessage());
        }

        option.checkPrefixes(java.util.Collections.singleton("-t"));
    }
}