import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        options = new Options();
    }

    // Test for parse(Options options, String[] arguments)
    @Test
    public void testParse_OptionsAndArguments_Success() throws ParseException {
        // Arrange
        String[] arguments = {"-a", "--option", "value"};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertEquals(3, result.getOptions().length);
    }

    // Test for parse(Options options, String[] arguments, boolean stopAtNonOption)
    @Test
    public void testParse_OptionsAndArgumentsAndStopAtNonOption_Success() throws ParseException {
        // Arrange
        String[] arguments = {"-a", "--option", "value", "extra"};

        // Act
        CommandLine result = parser.parse(options, arguments, true);

        // Assert
        assertEquals(3, result.getOptions().length);
        assertEquals(1, result.getArgList().size());
        assertEquals("extra", result.getArgList().get(0));
    }

    // Test for parse(Options options, String[] arguments, Properties properties)
    @Test
    public void testParse_OptionsArgumentsAndProperties_Success() throws ParseException {
        // Arrange
        String[] arguments = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("option", "value");

        // Act
        CommandLine result = parser.parse(options, arguments, properties);

        // Assert
        assertEquals(2, result.getOptions().length);
        assertEquals("value", result.getOptionValue("option"));
    }

    // Test for parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption)
    @Test
    public void testParse_AllParameters_Success() throws ParseException {
        // Arrange
        String[] arguments = {"-a", "--option", "value", "extra"};
        Properties properties = new Properties();
        properties.setProperty("option", "value");

        // Act
        CommandLine result = parser.parse(options, arguments, properties, true);

        // Assert
        assertEquals(2, result.getOptions().length);
        assertEquals(1, result.getArgList().size());
        assertEquals("value", result.getOptionValue("option"));
        assertEquals("extra", result.getArgList().get(0));
    }

    // Test for processProperties(Properties properties)
    @Test
    public void testProcessProperties_PropertiesWithOptionValue_Success() {
        // Arrange
        Properties properties = new Properties();
        properties.setProperty("option", "value");

        // Act
        parser.processProperties(properties);

        // Assert
        assertEquals(1, parser.getCommand().getOptions().length);
        assertEquals("value", parser.getCommand().getOptionValue("option"));
    }

    // Test for checkRequiredOptions()
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_RequiredOptionMissing_ExceptionThrown() throws MissingOptionException {
        // Arrange
        Option option = new Option("a", "option", true, "option description");
        option.setRequired(true);
        options.addOption(option);

        // Act
        parser.checkRequiredOptions();
    }

    // Test for processArgs(Option opt, ListIterator iter)
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_OptionWithMissingArgument_ExceptionThrown() throws ParseException {
        // Arrange
        Option option = new Option("a", "option", true, "option description");
        String[] arguments = {"-a"};
        ListIterator iterator = new Arrays.asList(arguments).listIterator();

        // Act
        parser.processArgs(option, iterator);
    }

    // Test for processOption(String arg, ListIterator iter)
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption_ExceptionThrown() throws ParseException {
        // Arrange
        String[] arguments = {"-b"};
        ListIterator iterator = new Arrays.asList(arguments).listIterator();

        // Act
        parser.processOption("-b", iterator);
    }

}