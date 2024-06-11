package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    /**
     * Test parse method with valid arguments.
     *
     * @throws ParseException if parse fails
     */
    @Test
    public void testParseValidArguments() throws ParseException
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        options.addOption("f", 6, "Option F");

        // create parser
        Parser parser = new DefaultParser();

        // parse arguments
        String[] arguments = new String[] { "--a", "--b", "1", "--c", "2", "3", "--d", "4", "--e", "5", "--f", "6" };
        CommandLine cmd = parser.parse(options, arguments);

        // assert parsed options
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("2", cmd.getOptionValue("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals("4", cmd.getOptionValue("d"));
        assertTrue(cmd.hasOption("e"));
        assertEquals("5", cmd.getOptionValue("e"));
        assertTrue(cmd.hasOption("f"));
        assertEquals("6", cmd.getOptionValue("f"));
        assertNull(cmd.getOptionValue("g"));
        assertNull(cmd.getArgs());
    }

    /**
     * Test parse method with invalid arguments.
     *
     * @throws ParseException if parse fails
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidArguments() throws ParseException
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");

        // create parser
        Parser parser = new DefaultParser();

        // parse arguments
        String[] arguments = new String[] { "--a", "--b", "1", "--c", "2", "--d", "4", "--e", "5", "--f" };

        // parse
        parser.parse(options, arguments);

        // should throw ParseException
    }

    /**
     * Test parse method with missing required options.
     *
     * @throws ParseException if parse fails
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A", true);
        options.addOption("b", true, "Option B", true);

        // create parser
        Parser parser = new DefaultParser();

        // parse arguments without option A or option B
        String[] arguments = new String[] {};

        // parse
        parser.parse(options, arguments);

        // should throw MissingOptionException
    }

    /**
     * Test parse method with multiple values for an option.
     *
     * @throws ParseException if parse fails
     */
    @Test
    public void testParseMultipleValuesForAnOption() throws ParseException
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B", true);

        // create parser
        Parser parser = new DefaultParser();

        // parse arguments
        String[] arguments = new String[] { "--a", "--b", "value1", "--b", "value2" };
        CommandLine cmd = parser.parse(options, arguments);

        // assert parsed options
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(new String[] { "value1", "value2" }, cmd.getOptionValues("b"));
        assertNull(cmd.getOptionValue("b"));
        assertNull(cmd.getArgs());
    }

    /**
     * Test parse method with properties.
     *
     * @throws ParseException if parse fails
     */
    @Test
    public void testParseProperties() throws ParseException
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");

        // create parser
        Parser parser = new DefaultParser();

        // set properties
        Properties properties = new Properties();
        properties.put("a", "value");

        // parse arguments with properties
        String[] arguments = new String[] {};
        CommandLine cmd = parser.parse(options, arguments, properties);

        // assert parsed options
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
        assertNull(cmd.getArgs());
    }

    /**
     * Test parse method with stopAtNonOption set to true.
     *
     * @throws ParseException if parse fails
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");

        // create parser
        Parser parser = new DefaultParser();

        // parse arguments with stopAtNonOption set to true
        String[] arguments = new String[] { "--a", "arg1", "--non-option" };
        CommandLine cmd = parser.parse(options, arguments, true);

        // assert parsed options
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertTrue(cmd.hasArg("non-option"));
        assertEquals(new String[] { "non-option" }, cmd.getArgs());
    }
}