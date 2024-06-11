import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.help.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testProcessOption() {
        Option option = new AbstractOption() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.setValue("value");
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        options.addOption(option);

        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--opt", "value").iterator();

        try {
            parser.parse(args, commandLine, options);
            assertEquals("value", commandLine.getValue("opt"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessOptionWithMultipleValues() {
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.addValue("value");
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        options.addOption(option);

        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--opt", "value1", "value2").iterator();

        try {
            parser.parse(args, commandLine, options);
            List<String> values = commandLine.getValues("opt");
            assertEquals(2, values.size());
            assertEquals("value1", values.get(0));
            assertEquals("value2", values.get(1));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessOptionWithArg() {
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.setValue("arg", args.next().getName());
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        options.addOption(option);

        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--opt", "argValue").iterator();

        try {
            parser.parse(args, commandLine, options);
            assertEquals("argValue", commandLine.getValue("arg"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessOptionWithValue() {
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.setValue("value");
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        options.addOption(option);

        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--opt").iterator();

        try {
            parser.parse(args, commandLine, options);
            assertNull(commandLine.getValue("opt"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessOptionWithNoValue() {
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.unsetOption("opt");
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        options.addOption(option);

        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--opt").iterator();

        try {
            parser.parse(args, commandLine, options);
            assertTrue(commandLine.hasOption("opt"));
            commandLine.unsetOption("opt");
            assertFalse(commandLine.hasOption("opt"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessOptionsWithValue() {
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.setValue("value");
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        Option option2 = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.setValue("value2");
            }

            @Override
            public String getPreferredName() {
                return "opt2";
            }
        };

        options.addOptions(Arrays.asList(option, option2));

        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--opt", "--opt2").iterator();

        try {
            parser.parse(args, commandLine, options);
            List<String> values = commandLine.getValues("opt");
            assertEquals(1, values.size());
            assertEquals("value", values.get(0));

            values = commandLine.getValues("opt2");
            assertEquals(1, values.size());
            assertEquals("value2", values.get(0));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testProcessOptionsWithValues() {
        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.addValue("value");
                commandLine.addValue("value2");
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        Option option2 = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.addValue("value2");
                commandLine.addValue("value3");
            }

            @Override
            public String getPreferredName() {
                return "opt2";
            }
        };

        options.addOptions(Arrays.asList(option, option2));

        WriteableCommandLine commandLine = new BasicCommandLine();
        List<String> args = Arrays.asList("--opt", "value", "value3", "--opt2").iterator();

        try {
            parser.parse(args, commandLine, options);
            List<String> values = commandLine.getValues("opt");
            assertEquals(2, values.size());
            assertEquals("value", values.get(0));
            assertEquals("value2", values.get(1));

            values = commandLine.getValues("opt2");
            assertEquals(2, values.size());
            assertEquals("value2", values.get(0));
            assertEquals("value3", values.get(1));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testValidate() {
        Option option = new Option() {
            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
                throw new OptionException("message");
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        WriteableCommandLine commandLine = new BasicCommandLine();
        options.addOption(option);

        try {
            parser.parse(Arrays.asList("--opt").iterator(), commandLine, options);
            fail();
        } catch (ParseException e) {
            assertEquals("message", e.getMessage());
        }
    }

    @Test
    public void testFindOption() {
        Option option = new Option() {
            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        Option option2 = new Option() {
            @Override
            public String getPreferredName() {
                return "opt2";
            }
        };

        options.addOptions(Arrays.asList(option, option2));

        Option foundOption = options.findOption("opt");
        assertSame(option, foundOption);

        foundOption = options.findOption("opt2");
        assertSame(option2, foundOption);

        foundOption = options.findOption("unknown");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new Option() {
            @Override
            public boolean isRequired() {
                return true;
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        Option option2 = new Option() {
            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public String getPreferredName() {
                return "opt2";
            }
        };

        options.addOptions(Arrays.asList(option, option2));

        WriteableCommandLine commandLine = new BasicCommandLine();

        try {
            parser.parse(Arrays.asList("--opt").iterator(), commandLine, options);
            assertFalse(commandLine.hasOption("opt"));
            throw new AssertionError("Expected OptionException");
        } catch (ParseException e) {
            // expected
        }

        try {
            parser.parse(Arrays.asList("--opt", "--opt2").iterator(), commandLine, options);
            assertTrue(commandLine.hasOption("opt"));
            assertTrue(commandLine.hasOption("opt2"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetTriggers() {
        Option option = new Option() {
            @Override
            public Set getTriggers() {
                return new HashSet<>(Arrays.asList("--opt", "-o"));
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        Option option2 = new Option() {
            @Override
            public Set getTriggers() {
                return new HashSet<>(Arrays.asList("--opt2"));
            }

            @Override
            public String getPreferredName() {
                return "opt2";
            }
        };

        options.addOptions(Arrays.asList(option, option2));

        Set triggers = options.getOptionParser().getAcceptedOptions();
        List<String> triggersList = triggers.stream().map(o -> o.getLongOpt()).collect(Collectors.toList());
        List<String> expectedTriggers = Arrays.asList("--opt", "--opt2");
        assertEquals(expectedTriggers, triggersList);
    }

    @Test
    public void testGetPrefixes() {
        Option option = new Option() {
            @Override
            public Set getPrefixes() {
                return new HashSet<>(Arrays.asList("--", "-"));
            }

            @Override
            public String getPreferredName() {
                return "opt";
            }
        };

        Option option2 = new Option() {
            @Override
            public Set getPrefixes() {
                return new HashSet<>(Arrays.asList("-"));
            }

            @Override
            public String getPreferredName() {
                return "opt2";
            }
        };

        options.addOptions(Arrays.asList(option, option2));

        Set prefixes = options.getOptionParser().getAcceptedOptions().stream()
                .map(o -> o.getShortOpt())
                .collect(Collectors.toSet());
        List<String> expectedPrefixes = Arrays.asList("-", "--");
        assertEquals(expectedPrefixes, new ArrayList<>(prefixes));
    }

    @Test
    public void testHelpLines() {
        Option option = new Option() {
            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public Set getPrefixes() {
                return new HashSet<>(Arrays.asList("--"));
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public int getId() {
                return 1;
            }
        };

        Option option2 = new Option() {
            @Override
            public String getPreferredName() {
                return "opt2";
            }

            @Override
            public Set getPrefixes() {
                return new HashSet<>(Arrays.asList("--"));
            }

            @Override
            public String getDescription() {
                return "description2";
            }

            @Override
            public int getId() {
                return 2;
            }
        };

        options.addOptions(Arrays.asList(option, option2));

        HelpFormatter formatter = new HelpFormatter();
        String helpText = formatter.printHelp(
                "MyProgram",
                Arrays.asList("--opt", "--opt2"),
                "",
                1,
                true);

        List<String> helpLines = Arrays.asList(helpText.split("\n"));

        int optionIndex = helpLines.indexOf("option:");
        String optionHelp = helpLines.get(optionIndex + 1);
        String expectedOptionHelp = String.format(
                "--opt%n%s%n",
                option.getDescription());
        assertEquals(expectedOptionHelp, optionHelp);

        int option2Index = helpLines.indexOf("OPTION 2", optionIndex);
        String option2Help = helpLines.get(option2Index + 1);
        String expectedOption2Help = String.format(
                "--opt2%n%s%n",
                option2.getDescription());
        assertEquals(expectedOption2Help, option2Help);
    }

    @Test
    public void testAppendUsage() {
        Option option = new Option() {
            @Override
            public String getPreferredName() {
                return "opt";
            }

            @Override
            public String getDescription() {
                return "description";
            }
        };

        StringBuffer usage = new StringBuffer();
        option.appendUsage(usage, new HashSet(), null);

        String expectedUsage = "--opt DESCRIPTION\n";
        assertEquals(expectedUsage, usage.toString());
    }
}