import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
    }

    @Test
    public void testParse_OptionsAndArguments_ReturnsCommandLine() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").hasArg().build());
        String[] arguments = {"-a", "value", "-b"};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("a"));
        assertNull(result.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidOption_ThrowsParseException() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-c"};

        // Act
        parser.parse(options, arguments);

        // Assert - Expecting ParseException
    }

    @Test
    public void testParse_StopAtNonOption_ReturnsCommandLineWithArgs() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-a", "value", "-b", "nonOption"};

        // Act
        CommandLine result = parser.parse(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("a"));
        assertNotNull(result.getArgList());
        assertEquals(1, result.getArgList().size());
        assertEquals("nonOption", result.getArgList().get(0));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption_ThrowsMissingOptionException() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().required().build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-b"};

        // Act
        parser.parse(options, arguments);

        // Assert - Expecting MissingOptionException
    }

    @Test
    public void testProcessArgs_OptionWithValues_AddsValuesToOption() throws ParseException {
        // Arrange
        Option option = Option.builder("a").hasArgs().build();
        ListIterator iterator = Arrays.asList("value1", "value2").listIterator();

        // Act
        parser.processArgs(option, iterator);

        // Assert
        assertNotNull(option.getValues());
        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
    }

    @Test
    public void testProcessArgs_OptionWithOptionalArg_AddsValueToOption() throws ParseException {
        // Arrange
        Option option = Option.builder("a").optionalArg(true).build();
        ListIterator iterator = Arrays.asList("value").listIterator();

        // Act
        parser.processArgs(option, iterator);

        // Assert
        assertNotNull(option.getValues());
        assertEquals(1, option.getValues().length);
        assertEquals("value", option.getValues()[0]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_OptionWithRequiredArgAndNoValues_ThrowsMissingArgumentException() throws ParseException {
        // Arrange
        Option option = Option.builder("a").hasArg().build();
        ListIterator iterator = Arrays.asList().listIterator();

        // Act
        parser.processArgs(option, iterator);

        // Assert - Expecting MissingArgumentException
    }

    @Test
    public void testProcessOption_ValidOption_AddsOptionToCommandLine() throws ParseException {
        // Arrange
        String arg = "-a";
        ListIterator iterator = Arrays.asList("value").listIterator();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        CommandLine commandLine = new CommandLine();
        parser.setOptions(options);
        parser.setCmd(commandLine);

        // Act
        parser.processOption(arg, iterator);

        // Assert
        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_InvalidOption_ThrowsUnrecognizedOptionException() throws ParseException {
        // Arrange
        String arg = "-c";
        ListIterator iterator = Arrays.asList().listIterator();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        CommandLine commandLine = new CommandLine();
        parser.setOptions(options);
        parser.setCmd(commandLine);

        // Act
        parser.processOption(arg, iterator);

        // Assert - Expecting UnrecognizedOptionException
    }

    @Test
    public void testCheckRequiredOptions_AllRequiredOptionsPresent_NoExceptionThrown() throws MissingOptionException {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().required().build());
        options.addOption(Option.builder("b").required().build());
        parser.setOptions(options);

        // Act & Assert - No exception should be thrown
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_RequiredOptionMissing_ThrowsMissingOptionException() throws MissingOptionException {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().required().build());
        options.addOption(Option.builder("b").required().build());
        parser.setOptions(options);
        List requiredOptions = parser.getRequiredOptions();
        requiredOptions.add("a");
        requiredOptions.add("b");

        // Act & Assert - Expecting MissingOptionException
        parser.checkRequiredOptions();
    }
}