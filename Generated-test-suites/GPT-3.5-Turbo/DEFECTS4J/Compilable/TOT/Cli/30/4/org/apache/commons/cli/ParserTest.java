package org.apache.commons.cli;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new TestParser();
    }

    @Test
    public void testParse_WithValidOptionsAndArguments_ShouldReturnParsedCommandLine() throws ParseException {
        // Arrange
        Options options = getValidOptions();
        String[] arguments = {"-f", "file.txt"};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasOption("f"));
        Assert.assertEquals("file.txt", result.getOptionValue("f"));
    }

    @Test(expected = ParseException.class)
    public void testParse_WithInvalidOption_ShouldThrowParseException() throws ParseException {
        // Arrange
        Options options = getValidOptions();
        String[] arguments = {"-x"};

        // Act
        parser.parse(options, arguments);

        // Assert
        // Expecting ParseException
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_WithMissingRequiredOption_ShouldThrowMissingOptionException() throws ParseException {
        // Arrange
        Options options = getValidOptions();
        String[] arguments = {};

        // Act
        parser.parse(options, arguments);

        // Assert
        // Expecting MissingOptionException
    }

    @Test
    public void testParse_WithNonOptionArgument_ShouldAddToCommandLineArgs() throws ParseException {
        // Arrange
        Options options = getValidOptions();
        String[] arguments = {"-f", "file.txt", "nonOption"};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasOption("f"));
        Assert.assertEquals("file.txt", result.getOptionValue("f"));
        Assert.assertEquals("nonOption", result.getArgs()[0]);
    }

    @Test
    public void testParse_WithStopAtNonOptionFlagSet_ShouldStopParsingAtNonOption() throws ParseException {
        // Arrange
        Options options = getValidOptions();
        String[] arguments = {"-f", "file.txt", "-x", "nonOption"};

        // Act
        CommandLine result = parser.parse(options, arguments, true);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.hasOption("f"));
        Assert.assertEquals("file.txt", result.getOptionValue("f"));
        Assert.assertFalse(result.hasOption("x"));
        Assert.assertEquals("nonOption", result.getArgs()[0]);
    }

    private Options getValidOptions() {
        Options options = new Options();
        Option fileOption = new Option("f", "file", true, "File option");
        fileOption.setRequired(true);
        options.addOption(fileOption);
        return options;
    }

    private class TestParser extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}