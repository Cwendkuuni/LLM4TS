import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        parser.setOptions(options);
        List<Option> requiredOptions = parser.getRequiredOptions();
        assertEquals(0, requiredOptions.size());

        options.addRequiredOption(option1);
        requiredOptions = parser.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals(option1, requiredOptions.get(0));

        options.addRequiredOption(option2);
        requiredOptions = parser.getRequiredOptions();
        assertEquals(2, requiredOptions.size());
        assertEquals(option1, requiredOptions.get(0));
        assertEquals(option2, requiredOptions.get(1));
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertNull(commandLine);
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(false);
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"option", "value"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-o", "value"});
        assertNotNull(commandLine);
        assertEquals("value", commandLine.getOptionValue("option"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(false);
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"option1", "option2"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-o", "value1", "-o", "value2"});
        fail("Expected ParseException");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[0]);
        fail("Expected MissingOptionException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(false);
        options.addOption(option);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"--unknown-option"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"--unknown-option"});
        fail("Expected UnrecognizedOptionException");
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(false);
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return null;
            }

            @Override
            protected void processProperties(Properties properties) {
                super.processProperties(properties);
            }
        };
        parser.setOptions(options);
        parser.parse(options, new String[0], properties);
        Option optionInCommandLine = parser.cmd.getOption("option");
        assertEquals("value", optionInCommandLine.getValue());
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        parser.parse(options, new String[0]);
        try {
            parser.checkRequiredOptions();
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            // Expected
        }
    }

    @Test
    public void testCheckRequiredOptionsWithAllOptionsPresent() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[]{"o1", "option1Value", "o2", "option2Value"};
            }

            @Override
            public CommandLine parse(Options options, String[] arguments, Properties properties)
                    throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        parser.parse(options, new String[]{"o1", "option1Value", "o2", "option2Value"});
        parser.checkRequiredOptions();
    }
}