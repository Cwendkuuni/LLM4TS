import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        parser.setOptions(new Options());
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String[] arguments = new String[]{"-a", "-b", "value", "-c"};
        CommandLine result = parser.parse(options, arguments);
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertTrue(result.hasOption("c"));
        assertEquals("value", result.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String[] arguments = new String[0];
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value");
        properties.setProperty("c", "false");
        CommandLine result = parser.parse(options, arguments, properties);
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertTrue(result.hasOption("c"));
        assertEquals("value", result.getOptionValue("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String[] arguments = new String[]{"-a", "-b", "value", "nonOption"};
        CommandLine result = parser.parse(options, arguments, true);
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertFalse(result.hasOption("c"));
        assertEquals("value", result.getOptionValue("b"));
        assertEquals("nonOption", result.getArgs()[0]);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", false, "Option a");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = new String[0];
        parser.parse(options, arguments);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String argument = "-a";
        parser.processOption(argument, null);
        assertTrue(parser.getCommandLine().hasOption("a"));
        assertFalse(parser.getCommandLine().hasOption("b"));
        assertFalse(parser.getCommandLine().hasOption("c"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String argument = "-d";
        parser.processOption(argument, null);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option a");
        ListIterator iter = Arrays.asList("-a", "value1", "value2").listIterator();
        parser.processArgs(option, iter);
        String[] values = parser.getCommandLine().getOptionValues("a");
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Option option = new Option("a", true, "Option a");
        ListIterator iter = Arrays.asList("-a").listIterator();
        parser.processArgs(option, iter);
    }

}