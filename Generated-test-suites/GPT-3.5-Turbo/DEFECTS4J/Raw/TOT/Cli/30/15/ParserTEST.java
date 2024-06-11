import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Test;

public class ParserTest {

    // Test the setOptions method
    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    // Test the getOptions method
    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new ParserImpl();
        parser.setOptions(options);

        assertEquals(options, parser.getOptions());
    }

    // Test the getRequiredOptions method
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "optional option");
        options.addOption(option);

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        List requiredOptions = parser.getRequiredOptions();

        assertEquals(0, requiredOptions.size());
    }

    // Test the flatten method with stopAtNonOption false
    @Test
    public void testFlattenStopAtNonOptionFalse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new ParserImpl();

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(3, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("-b", flattenedArguments[1]);
        assertEquals("value", flattenedArguments[2]);
    }

    // Test the flatten method with stopAtNonOption true
    @Test
    public void testFlattenStopAtNonOptionTrue() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new ParserImpl();

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals(1, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
    }

    // Test the parse method
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a"};

        Parser parser = new ParserImpl();

        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
    }

    // Test the processProperties method
    @Test
    public void testProcessProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        Parser parser = new ParserImpl();

        CommandLine cmd = parser.parse(options, null, properties);

        assertTrue(cmd.hasOption("a"));
    }

    // Test the checkRequiredOptions method with missing required options
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissingOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "required option");
        option.setRequired(true);
        options.addOption(option);

        Parser parser = new ParserImpl();

        parser.checkRequiredOptions();
    }

    // Test the processArgs method
    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", true, "option a");
        options.addOption(option);

        String[] arguments = {"value"};

        Parser parser = new ParserImpl();
        CommandLine cmd = new CommandLine();

        ListIterator iterator = Arrays.asList(arguments).listIterator();

        parser.processArgs(option, iterator);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    // Test the processOption method with unrecognized option
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Options options = new Options();

        String argument = "x";

        Parser parser = new ParserImpl();

        ListIterator iterator = Arrays.asList(argument).listIterator();

        parser.processOption(argument, iterator);
    }

    // Test the updateRequiredOptions method
    @Test
    public void testUpdateRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", true, "option a");
        option.setRequired(true);
        options.addOption(option);

        Parser parser = new ParserImpl();

        parser.updateRequiredOptions(option);

        List requiredOptions = parser.getRequiredOptions();

        assertEquals(0, requiredOptions.size());
    }

    // Test the parse method with stopAtNonOption true
    @Test
    public void testParseStopAtNonOptionTrue() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a", "-b", "value"};

        Parser parser = new ParserImpl();

        CommandLine cmd = parser.parse(options, arguments, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("-b", cmd.getArgList().get(0));
        assertEquals("value", cmd.getArgList().get(1));
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}