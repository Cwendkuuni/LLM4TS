package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Tests the functionality of the Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    

    /**
     * Tests the basic functionality of the parse() method.
     *
     * @throws ParseException if the parse() method fails
     */
    @Test
    public void testParseBasic() throws ParseException {
        options.addOption("a", "optionA", false, "Option A");
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("a"));
    }

    /**
     * Tests the basic functionality of the parse() method with
     * arguments.
     *
     * @throws ParseException if the parse() method fails
     */
    @Test
    public void testParseWithArguments() throws ParseException {
        options.addOption("a", true, "valueA", "Option A with value");
        String[] arguments = {"-a", "valueB"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("valueB", cmd.getOptionValue("a"));
    }

    /**
     * Tests the basic functionality of the parse() method with
     * properties.
     *
     * @throws ParseException if the parse() method fails
     */
    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption("a", true, "valueA", "Option A with value");
        Properties properties = new Properties();
        properties.put("a", "valueB");
        CommandLine cmd = parser.parse(options, null, properties, false);
        Assert.assertEquals("valueB", cmd.getOptionValue("a"));
    }

    

    /**
     * Tests the edge cases of the parse() method with no arguments.
     *
     * @throws ParseException if the parse() method fails
     */
    @Test
    public void testParseNoArguments() throws ParseException {
        options.addOption("a", true, "valueA", "Option A with value");
        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        Assert.assertFalse(cmd.hasOption("a"));
    }

    /**
     * Tests the edge cases of the parse() method with empty arguments.
     *
     * @throws ParseException if the parse() method fails
     */
    @Test
    public void testParseEmptyArguments() throws ParseException {
        options.addOption("a", true, "valueA", "Option A with value");
        String[] arguments = {"", ""};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        Assert.assertFalse(cmd.hasOption("a"));
    }

    /**
     * Tests the edge cases of the parse() method with an empty list.
     *
     * @throws ParseException if the parse() method fails
     */
    @Test
    public void testParseEmptyList() throws ParseException {
        options.addOption("a", true, "valueA", "Option A with value");
        List<String> arguments = new ArrayList<>();
        CommandLine cmd = parser.parse(options, arguments.toArray(new String[0]),
                null, false);
        Assert.assertFalse(cmd.hasOption("a"));
    }

    

    /**
     * Tests the exceptions handling of the parse() method with
     * unrecognized options.
     *
     * @throws ParseException if the parse() method fails
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        options.addOption("a", false, "valueA", "Option A");
        String[] arguments = {"-b"};
        parser.parse(options, arguments, null, false);
    }

    /**
     * Tests the exceptions handling of the parse() method with
     * missing required options.
     *
     * @throws ParseException if the parse() method fails
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        options.addOption("a", true, "valueA", "Option A with value");
        options.addRequiredOption("b", true, "valueB", "Required Option B");
        String[] arguments = {"-a"};
        parser.parse(options, arguments, null, false);
    }

    
}