import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        Options options = new Options();
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        Options options = new Options();
        parser.setOptions(options);

        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        Options options = new Options();
        Option option1 = new Option("opt1", true, "o1");
        Option option2 = new Option("opt2", true, "o2");
        parser.setOptions(options);
        options.addOption(option1);
        options.addOption(option2);

        List<Option> requiredOptions = parser.getRequiredOptions();
        assertTrue(requiredOptions.isEmpty());

        option1.setRequired(true);
        option2.setRequired(true);

        requiredOptions = parser.getRequiredOptions();
        ListIterator<Option> iterator = requiredOptions.listIterator();
        assertSame(option1, iterator.next());
        assertSame(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0][], null, false);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"arg"};
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"arg"}, null, false);
        assertEquals("arg", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-o"};
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        Option option = new Option("o", true, null, "value");
        parser.setOptions(options);
        options.addOption(option);

        CommandLine cmd = parser.parse(options, new String[]{"-o"}, null, false);
        assertFalse(cmd.hasOption("o"));
        assertNull(cmd.getOptionValue("o"));

        cmd = parser.parse(options, new String[]{"-o"}, null, true);
        assertTrue(cmd.hasOption("o"));
        assertNull(cmd.getOptionValue("o"));

        cmd = parser.parse(options, new String[]{"-o", "value"}, null, false);
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithSingleOptionWithMultipleValues() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-o", "value1", "-o", "value2"};
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        Option option = new Option("o", true, null, "value");
        parser.setOptions(options);
        options.addOption(option);

        CommandLine cmd = parser.parse(options, new String[]{"-o", "value1", "-o", "value2"}, null, false);
        List<String> values = cmd.getOptionValues("o");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-o1", "-o2"};
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        Option option1 = new Option("o1", true, "o1");
        Option option2 = new Option("o2", true, "o2");
        parser.setOptions(options);
        options.addOption(option1);
        options.addOption(option2);

        CommandLine cmd = parser.parse(options, new String[]{"-o1", "-o2"}, null, false);
        List<Option> optionsList = cmd.getOptions();
        ListIterator<Option> iterator = optionsList.listIterator();
        assertSame(option1, iterator.next());
        assertSame(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testParseWithMultipleOptionsWithValues() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-o1", "val1", "-o2", "val2"};
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        Option option1 = new Option("o1", true, "o1");
        Option option2 = new Option("o2", true, "o2");
        parser.setOptions(options);
        options.addOption(option1);
        options.addOption(option2);

        CommandLine cmd = parser.parse(options, new String[]{"-o1", "val1", "-o2", "val2"}, null, false);
        List<Option> optionsList = cmd.getOptions();
        ListIterator<Option> iterator = optionsList.listIterator();

        Option option1Cmd = iterator.next();
        assertEquals(option1, option1Cmd);
        assertEquals("val1", option1Cmd.getValues().get(0));

        Option option2Cmd = iterator.next();
        assertEquals(option2, option2Cmd);
        assertEquals("val2", option2Cmd.getValues().get(0));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"arg1", "arg2"};
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"}, null, false);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"-u"};
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        parser.setOptions(options);

        parser.parse(options, new String[]{"-u"}, null, false);
        fail("Expected UnrecognizedOptionException");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        Option option = new Option("o", true, null, "value");
        option.setRequired(true);
        parser.setOptions(options);
        options.addOption(option);

        try {
            parser.parse(options, new String[0], null, false);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertTrue(e.getMessage().contains("Missing required option"));
        }
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            protected CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
                    throws ParseException {
                return new CommandLine();
            }
        };

        Options options = new Options();
        Option option = new Option("o", true, null, "value");
        parser.setOptions(options);
        options.addOption(option);

        try {
            parser.parse(options, new String[]{"-o"}, null, true);
            fail("Expected MissingArgumentException");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:o", e.getMessage());
        }
    }
}