package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "arg1", "--another-option").listIterator();

        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
                commandLine.setValue("value", "arg1");
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue("defaultValue", "defaultValue");
            }

            @Override
            public Set getTriggers() {
                return new HashSet(Arrays.asList("--option"));
            }

            @Override
            public Set getPrefixes() {
                return new HashSet(Arrays.asList("--"));
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                // no validation needed
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                return Arrays.asList(new HelpLineImpl("--option", "option description", true, null));
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                buffer.append("--option <value>");
            }

            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "option description";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Option getParent() {
                return null;
            }

            @Override
            public void setParent(Option parent) {
                // not implemented
            }
        };

        option.process(commandLine, args);
        assertEquals("value", commandLine.getValue("value"));
        assertEquals("defaultValue", commandLine.getValue("defaultValue"));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--option", "arg1", "--another-option").listIterator();

        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
                // no-op
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                // no-op
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return arguments.next().equals("arg1");
            }

            @Override
            public Set getTriggers() {
                return new HashSet(Arrays.asList("--option"));
            }

            @Override
            public Set getPrefixes() {
                return new HashSet(Arrays.asList("--"));
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                // no validation needed
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                return Arrays.asList(new HelpLineImpl("--option", "option description", true, null));
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                buffer.append("--option <arg>");
            }

            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "option description";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Option getParent() {
                return null;
            }

            @Override
            public void setParent(Option parent) {
                // not implemented
            }
        };

        assertTrue(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testGetTriggers() {
        Option option = new Option() {
            @Override
            public Set getTriggers() {
                return new HashSet(Arrays.asList("--option", "--another-option"));
            }

            // other methods
        };

        assertNotNull(option.getTriggers());
        assertTrue(option.getTriggers().contains("--option"));
        assertTrue(option.getTriggers().contains("--another-option"));
    }

    @Test
    public void testGetPrefixes() {
        Option option = new Option() {
            @Override
            public Set getPrefixes() {
                return new HashSet(Arrays.asList("--", "-"));
            }

            // other methods
        };

        assertNotNull(option.getPrefixes());
        assertTrue(option.getPrefixes().contains("--"));
        assertTrue(option.getPrefixes().contains("-"));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.setValue("value", "invalidValue");

        Option option = new Option() {
            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                assertEquals("invalidValue", commandLine.getValue("value"));
                throw new OptionException("Error message");
            }

            // other methods
        };

        try {
            option.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("Error message", e.getMessage());
        }
    }

    @Test
    public void testHelpLines() {
        Option option = new Option() {
            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                if (depth > 0) {
                    return Arrays.asList(
                            new HelpLineImpl("--option", "option description", true, null),
                            new HelpLineImpl("--option <value>", "option value description", false, null)
                    );
                } else {
                    return Arrays.asList(new HelpLineImpl("--option", "option description", true, null));
                }
            }

            // other methods
        };

        HelpFormatter formatter = new HelpFormatter();
        String help = option.helpLines(1, new HashSet(), null).stream().map(HelpLine::toString).collect(Collectors.joining("\n"));
        assertEquals("--option\n" +
                "option description\n" +
                "--option <value>\n" +
                "option value description\n", help);
    }

    @Test
    public void testAppendUsage() {
        Option option = new Option() {
            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
                buffer.append("--option <value>");
            }

            // other methods
        };

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet(), null);
        assertEquals("--option <value>", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new Option() {
            @Override
            public String getPreferredName() {
                return "option";
            }

            // other methods
        };

        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option() {
            @Override
            public String getDescription() {
                return "option description";
            }

            // other methods
        };

        assertEquals("option description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new Option() {
            @Override
            public int getId() {
                return 1;
            }

            // other methods
        };

        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option parentOption = new Option() {
            @Override
            public Set getTriggers() {
                return new HashSet(Arrays.asList("--parent"));
            }

            @Override
            public Set getPrefixes() {
                return new HashSet(Arrays.asList("--", "-"));
            }

            @Override
            public Option findOption(String trigger) {
                if (trigger.equals("--child")) {
                    return new Option() {
                        @Override
                        public Set getTriggers() {
                            return new HashSet(Arrays.asList("--child"));
                        }

                        @Override
                        public Set getPrefixes() {
                            return new HashSet(Arrays.asList("--", "-"));
                        }

                        @Override
                        public Option getParent() {
                            return this.parent;
                        }
                    };
                }
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }
        };

        Option option = parentOption.findOption("--child");
        assertNotNull(option);
        assertEquals(parentOption, option.getParent());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option() {
            @Override
            public boolean isRequired() {
                return true;
            }

            // other methods
        };

        assertTrue(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = new Option() {
            @Override
            public Set getTriggers() {
                return new HashSet(Arrays.asList("--parent"));
            }

            @Override
            public Set getPrefixes() {
                return new HashSet(Arrays.asList("--", "-"));
            }

            @Override
            public Option getParent() {
                return null;
            }

            @Override
            public void setParent(Option parent) {
                this.parent = parent;
            }

            private Option parent;
        };

        Option option = new Option() {
            @Override
            public Set getTriggers() {
                return new HashSet(Arrays.asList("--child"));
            }

            @Override
            public Set getPrefixes() {
                return new HashSet(Arrays.asList("--", "-"));
            }

            @Override
            public Option getParent() {
                return parentOption;
            }

            @Override
            public void setParent(Option parent) {
                this.parent = parent;
            }

            private Option parent;
        };

        parentOption.setParent(option);
        assertEquals(option, parentOption.getParent());
    }
}