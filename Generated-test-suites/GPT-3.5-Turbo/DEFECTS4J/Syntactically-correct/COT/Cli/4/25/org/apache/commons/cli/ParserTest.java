
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        options = new Options();
    }

    // Test parse method with options and arguments
    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));

        String[] arguments = new String[] { "-a", "-b", "value" };

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    // Test parse method with properties
    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value");

        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    // Test parse method with stopAtNonOption set to true
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));

        String[] arguments = new String[] { "-a", "-b", "value", "nonOption" };

        CommandLine cmd = parser.parse(options, arguments, true);

        assertFalse(cmd.hasOption("nonOption"));
    }

    // Test parse method with missing required option
    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Option requiredOption = new Option("a", false, "Required Option a");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);

        String[] arguments = new String[0];

        parser.parse(options, arguments);
    }

    // Test processArgs method with option and argument values
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option a");

        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();

        parser.processArgs(option, iterator);

        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    // Test processArgs method with missing argument value for required option
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Option requiredOption = new Option("a", true, "Required Option a");
        requiredOption.setRequired(true);

        ListIterator<String> iterator = Arrays.asList().listIterator();

        parser.processArgs(requiredOption, iterator);
    }

    // Test processOption method with recognized option
    @Test
    public void testProcessOptionWithRecognizedOption() throws ParseException {
        Option option = new Option("a", false, "Option a");
        options.addOption(option);

        ListIterator<String> iterator = Arrays.asList("-a").listIterator();

        parser.processOption("-a", iterator);

        assertTrue(parser.cmd.hasOption("a"));
    }

    // Test processOption method with unrecognized option
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        ListIterator<String> iterator = Arrays.asList("-x").listIterator();

        parser.processOption("-x", iterator);
    }
}

