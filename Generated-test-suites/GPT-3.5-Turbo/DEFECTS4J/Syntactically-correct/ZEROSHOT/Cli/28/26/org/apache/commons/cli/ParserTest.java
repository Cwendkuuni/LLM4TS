import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        parser.setOptions(options);
        List<Options> requiredOptions = parser.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals(option1, requiredOptions.get(0));
    }

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option1");
        options.addOption("b", "option2");
        String[] arguments = {"-a", "value1", "-b", "value2"};
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        assertEquals(4, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("value1", flattenedArguments[1]);
        assertEquals("-b", flattenedArguments[2]);
        assertEquals("value2", flattenedArguments[3]);
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        option1.setArgName("arg1");
        option2.setArgName("arg2");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-a", "value1", "-b", "value2"};
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals("value2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        option1.setArgName("arg1");
        options.addOption(option1);
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        String[] arguments = {"-b", "value2"};
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("value1", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        option1.setArgName("arg1");
        options.addOption(option1);
        String[] arguments = {"-a", "value1", "nonOption"};
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("nonOption"));
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals("nonOption", cmd.getArgList().get(0));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOptionValue() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        option1.setArgName("arg1");
        option1.setRequired(true);
        options.addOption(option1);
        String[] arguments = {"-a"};
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1");
        option1.setArgName("arg1");
        options.addOption(option1);
        String[] arguments = {"-b", "value"};
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    };
        parser.parse(options, arguments);
    }
}