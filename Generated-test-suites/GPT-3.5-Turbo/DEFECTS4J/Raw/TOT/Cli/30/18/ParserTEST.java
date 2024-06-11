import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();
    }

    @Test
    public void testParse_NoOptionsAndArguments_ReturnsEmptyCommandLine() throws ParseException {
        String[] arguments = {};
        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
        assertTrue(result.getOptions().isEmpty());
        assertTrue(result.getArgList().isEmpty());
        assertTrue(result.getOptionProperties().isEmpty());
    }

    @Test
    public void testParse_SingleOptionWithoutArgument_CreatesOptionInCommandLine() throws ParseException {
        options.addOption("a", "option a");

        String[] arguments = {"-a"};
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
    }

    @Test
    public void testParse_SingleOptionWithArgument_CreatesOptionWithValueInCommandLine() throws ParseException {
        options.addOption("a", "option a", true);

        String[] arguments = {"-a", "value"};
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("a"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_SingleOptionWithoutArgumentAndRequired_ThrowsMissingArgumentException() throws ParseException {
        options.addRequiredOption("a", "option a");

        String[] arguments = {"-a"};
        CommandLine result = parser.parse(options, arguments);
    }

    @Test
    public void testParse_MultipleOptions_CreatesOptionsInCommandLine() throws ParseException {
        options.addOption("a", "option a");
        options.addOption("b", "option b");

        String[] arguments = {"-a", "-b"};
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
    }

    @Test
    public void testParse_MultipleOptionsWithArguments_CreatesOptionsWithValuesInCommandLine() throws ParseException {
        options.addOption("a", "option a", true);
        options.addOption("b", "option b", true);

        String[] arguments = {"-a", "value1", "-b", "value2"};
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value2", result.getOptionValue("b"));
    }

    @Test
    public void testParse_StopAtNonOption_RecognizesNonOptionArguments() throws ParseException {
        options.addOption("a", "option a", true);

        String[] arguments = {"-a", "value1", "non-option", "-b", "value2"};
        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        assertFalse(result.hasOption("b"));
        assertEquals("non-option", result.getArgs()[0]);
        assertEquals("value2", result.getArgs()[1]);
    }

    @Test
    public void testParse_PropertiesWithExistingOption_AddsOptionWithValueToCommandLine() throws ParseException {
        options.addOption("a", "option a", true);
        properties.setProperty("a", "value");

        String[] arguments = {};
        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("a"));
    }

    @Test
    public void testParse_PropertiesWithExistingOptionGroup_AddsOptionToCommandLine() throws ParseException {
        options.addOption("a", "option a", true);
        options.addOption("b", "option b", true);
        OptionGroup group = new OptionGroup();
        group.addOption(options.getOption("a"));
        group.addOption(options.getOption("b"));
        options.addOptionGroup(group);

        properties.setProperty("a", "true");

        String[] arguments = {};
        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        String[] arguments = {"-c"};
        CommandLine result = parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption_ThrowsMissingOptionException() throws ParseException {
        options.addRequiredOption("a", "option a");

        String[] arguments = {};
        CommandLine result = parser.parse(options, arguments);
    }

    @Test
    public void testParse_StopAtNonOptionAndMissingRequiredOption_DetectsMissingOptionAfterNonOption() throws ParseException {
        options.addRequiredOption("a", "option a");
        String[] arguments = {"non-option"};
        CommandLine result = parser.parse(options, arguments, true);
        assertFalse(result.hasOption("a"));
        assertEquals("non-option", result.getArgs()[0]);
        assertTrue(result.getRequiredOptions().contains("a"));
    }

    @Test
    public void testParse_CombinationOfOptionsAndNonOptionArguments_CreatesCommandLineWithCorrectValues() throws ParseException {
        options.addOption("a", "option a", true);
        options.addOption("b", "option b");

        String[] arguments = {"-a", "value1", "non-option", "-b"};
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertEquals("value1", result.getOptionValue("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("non-option", result.getArgs()[0]);
        assertFalse(result.hasOption("value1"));
    }
}