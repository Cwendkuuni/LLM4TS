
import org.apache.commons.cli.*;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parse_optionsAndArgumentsProvided_shouldReturnParsedCommandLine() throws ParseException {
        // Arrange
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        String[] arguments = {"-f", "test.txt"};

        // Act
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        CommandLine commandLine = parser.parse(options, arguments);

        // Assert
        assertTrue(commandLine.hasOption("f"));
        assertEquals("test.txt", commandLine.getOptionValue("f"));
        assertArrayEquals(arguments, commandLine.getArgs());
    }

    @Test
    void parse_unknownOptionProvided_shouldThrowUnrecognizedOptionException() {
        // Arrange
        Options options = new Options();
        String[] arguments = {"-u"};

        // Act
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        // Assert
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    void parse_missingOptionArgument_shouldThrowMissingArgumentException() {
        // Arrange
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        String[] arguments = {"-f"};

        // Act
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        // Assert
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    void processProperties_optionWithValueInProperties_shouldAddOptionWithValueToCommandLine() throws ParseException {
        // Arrange
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("file", "test.txt");

        // Act
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        CommandLine commandLine = parser.parse(options, null, properties);

        // Assert
        assertTrue(commandLine.hasOption("f"));
        assertEquals("test.txt", commandLine.getOptionValue("f"));
    }

    @Test
    void processProperties_optionWithoutValueInProperties_shouldAddOptionToCommandLine() throws ParseException {
        // Arrange
        Options options = new Options();
        Option option = new Option("f", "file", false, "input file");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("file", "true");

        // Act
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        CommandLine commandLine = parser.parse(options, null, properties);

        // Assert
        assertTrue(commandLine.hasOption("f"));
    }

    @Test
    void checkRequiredOptions_missingRequiredOption_shouldThrowMissingOptionException() {
        // Arrange
        Options options = new Options();
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = {};

        // Act
        CommandLineParser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        // Assert
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }
}
