package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new DefaultParser();
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addRequiredOption("e", true, "Option E");
    }

    

    /**
     * Tests the abstract parse method with valid arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseValidArguments() throws ParseException {
        String[] args = {"--a", "--b", "1", "--c", "3", "--d", "4"};
        CommandLine commandLine = parser.parse(options, args);

        Assert.assertEquals(5, commandLine.getArgs().size());
        Assert.assertEquals("Option A", commandLine.getOptionValue("a"));
        Assert.assertEquals("1", commandLine.getOptionValue("b"));
        Assert.assertEquals("3", commandLine.getOptionValue("c"));
        Assert.assertEquals("4", commandLine.getOptionValue("d"));
        Assert.assertNull(commandLine.getOptionValue("e"));
    }

    /**
     * Tests the parse method with no arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test
    public void testParseNoArguments() throws ParseException {
        String[] args = {};
        CommandLine commandLine = parser.parse(options, args);

        Assert.assertEquals(0, commandLine.getArgs().size());
        Assert.assertNull(commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("b"));
        Assert.assertNull(commandLine.getOptionValue("c"));
        Assert.assertNull(commandLine.getOptionValue("d"));
        Assert.assertNull(commandLine.getOptionValue("e"));
    }

    /**
     * Tests the parse method with only required option missing.
     *
     * @throws ParseException if an error occurs.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = {"--a", "--b", "1", "--c", "3"};
        parser.parse(options, args);
    }

    /**
     * Tests the parse method with invalid arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidArguments() throws ParseException {
        String[] args = {"--a", "--b", "1", "--c", "--e"};
        parser.parse(options, args);
    }

    /**
     * Tests the parse method with mixed valid and invalid arguments.
     *
     * @throws ParseException if an error occurs.
     */
    @Test(expected = ParseException.class)
    public void testParseMixedValidAndInvalidArguments() throws ParseException {
        String[] args = {"--a", "--b", "1", "--c", "--e", "invalid"};
        parser.parse(options, args);
    }

    
}