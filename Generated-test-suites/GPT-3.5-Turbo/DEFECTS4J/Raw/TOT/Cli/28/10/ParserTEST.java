import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

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
    }

    @Test
    public void testParse_OptionsAndArguments_ReturnsCommandLine() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "-b", "value"};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertFalse(result.hasOption("c"));
    }

    @Test
    public void testParse_OptionsWithProperties_ReturnsCommandLine() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value");

        String[] arguments = {};

        // Act
        CommandLine result = parser.parse(options, arguments, properties);

        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertFalse(result.hasOption("c"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOption_ThrowsMissingOptionException() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", true, "Required Option");

        String[] arguments = {};

        // Act
        parser.parse(options, arguments);

        // Assert
        // MissingOptionException should be thrown
    }

    @Test
    public void testProcessProperties_OptionsWithProperties_ReturnsModifiedOptions() {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a")
                .hasArg()
                .build());
        options.addOption(Option.builder("b")
                .hasArg()
                .optionalArg(true)
                .build());

        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");

        // Act
        parser.processProperties(properties);

        // Assert
        assertEquals("value1", options.getOption("a").getValues()[0]);
        assertEquals("value2", options.getOption("b").getValues()[0]);
    }

    @Test
    public void testProcessArgs_OptionWithValues_SetsOptionValues() throws ParseException {
        // Arrange
        Option option = new Option("a", true, "Option A");
        ListIterator iterator = new ArrayList<String>().listIterator();
        iterator.add("value1");
        iterator.add("value2");

        // Act
        parser.processArgs(option, iterator);

        // Assert
        assertEquals("value1", option.getValue());
        assertEquals("value2", option.getValues()[0]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_OptionWithNoValuesAndNoOptionalArg_ThrowsMissingArgumentException() throws ParseException {
        // Arrange
        Option option = new Option("a", true, "Option A");
        ListIterator iterator = new ArrayList<String>().listIterator();

        // Act
        parser.processArgs(option, iterator);

        // Assert
        // MissingArgumentException should be thrown
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
        // Arrange
        ListIterator iterator = new ArrayList<String>().listIterator();
        iterator.add("x");

        // Act
        parser.processOption("x", iterator);

        // Assert
        // UnrecognizedOptionException should be thrown
    }

    @Test
    public void testParse_FlattenMethodThrowsParseException_CatchesAndRethrowsException() throws ParseException {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a"};

        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                throw new ParseException("Error flattening arguments");
            }
        };

        // Act and Assert
        try {
            parser.parse(options, arguments);
            fail("ParseException should be rethrown");
        } catch (ParseException e) {
            assertEquals("Error flattening arguments", e.getMessage());
        }
    }
}