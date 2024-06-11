package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParse() throws ParseException {
        String[] arguments = {"-a", "1", "--optionB", "2", "--optionC", "3"};
        CommandLine commandLine = parser.parse(options, arguments, false);

        Assert.assertEquals(6, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("1", commandLine.getOptionValue("a"));
        Assert.assertTrue(commandLine.hasOption("optionB"));
        Assert.assertEquals("2", commandLine.getOptionValue("optionB"));
        Assert.assertTrue(commandLine.hasOption("optionC"));
        Assert.assertEquals("3", commandLine.getOptionValue("optionC"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] arguments = {"-a", "1"};
        parser.parse(options, arguments, false);

        Assert.fail("Expected ParseException when required option is missing");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("myoption", "value");

        String[] arguments = {"--property", "myoption=value"};
        CommandLine commandLine = parser.parse(options, arguments, properties, false);

        Assert.assertTrue(commandLine.hasOption("property"));
        Assert.assertEquals("value", commandLine.getOptionValue("property"));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] arguments = {"--unrecognizedOption"};

        try {
            parser.parse(options, arguments, false);
            Assert.fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: unrecognizedOption", e.getMessage());
        }
    }

    // Add more test cases for other methods in the Parser class
}