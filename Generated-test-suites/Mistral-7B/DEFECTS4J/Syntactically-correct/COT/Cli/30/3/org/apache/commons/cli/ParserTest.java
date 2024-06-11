package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    private Parser parser;
    private Options options;

    /**
     * Sets up the test by creating a new Parser instance and some test options.
     *
     * @throws ParseException if the test options cannot be parsed.
     */
    @Before
    public void setUp() throws ParseException
    {
        parser = new DefaultParser();
        options = new Options();

        options.addOption("a", "apple", true, "Apple name");
        options.addOption("b", "banana", true, "Banana name");
        options.addOption("c", "color", true, "Color name");
        options.addOption("d", "delay", true, "Delay time");
        options.addOption("e", "error", false, "Print error message");
        options.addOption("g", "group", false, "Show group");
        options.addOption("h", "help", false, "Print help message");
        options.addOption("i", "integer", false, "Print integer value");
        options.addOption("j", "just", false, "Print just");
        options.addOption("k", "keep", false, "Keep option");
        options.addOption("l", "list", false, "Print list");
        options.addOption("m", "more", false, "Print more");
        options.addOption("n", "no", false, "Print no");
        options.addOption("o", "output", true, "Output file");
        options.addOption("p", "prefix", true, "Prefix string");
        options.addOption("q", "quiet", false, "Print quiet");
        options.addOption("r", "repeat", false, "Print repeat");
        options.addOption("s", "separator", true, "Separator string");
        options.addOption("t", "test", false, "Run tests");
        options.addOption("u", "user", true, "User name");
        options.addOption("v", "version", false, "Print version");
        options.addOption("w", "width", true, "Width of help text");
        options.addOption("x", "xml", false, "Print help message in xml format");
        options.addOption("y", "yes", false, "Print yes");
        options.addOption("z", "zero", false, "Print zero");

        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.addOption("g");
        group.addOption("h");
        options.addOptionGroup(group);

        options.addOption("t", "test", 1, "Run tests", true, "Number of tests to run");
    }

    /**
     * Tears down the test by resetting the Parser instance.
     */
    @After
    public void tearDown()
    {
        parser = null;
        options = null;
    }

    /**
     * Tests the parse method with no arguments.
     *
     * @throws ParseException if the parse method fails.
     */
    @Test
    public void testParseNoArguments() throws ParseException
    {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertNull(cmd.getArgs());
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("i"));
        assertFalse(cmd.hasOption("j"));
        assertFalse(cmd.hasOption("k"));
        assertFalse(cmd.hasOption("l"));
        assertFalse(cmd.hasOption("m"));
        assertFalse(cmd.hasOption("n"));
        assertFalse(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("p"));
        assertFalse(cmd.hasOption("q"));
        assertFalse(cmd.hasOption("r"));
        assertFalse(cmd.hasOption("s"));
        assertFalse(cmd.hasOption("t"));
        assertFalse(cmd.hasOption("u"));
        assertFalse(cmd.hasOption("v"));
        assertFalse(cmd.hasOption("w"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertFalse(cmd.hasOption("z"));
        assertFalse(cmd.hasOption("group1"));
        assertFalse(cmd.getOptionGroups().hasNext());
        assertTrue(cmd.getOptions().size() == 31);
    }

    /**
     * Tests the parse method with required options.
     *
     * @throws ParseException if the parse method fails.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseRequiredOptions() throws ParseException
    {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertThrows(MissingOptionException.class, () -> cmd.getOptionValue("a"));
        assertThrows(MissingOptionException.class, () -> cmd.getOptionValue("b"));
    }

    /**
     * Tests the parse method with optional options.
     *
     * @throws ParseException if the parse method fails.
     */
    @Test
    public void testParseOptionalOptions() throws ParseException
    {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("q"));
        assertTrue(cmd.getArgList().size() == 0);
    }

    /**
     * Tests the parse method with arguments.
     *
     * @throws ParseException if the parse method fails.
     */
    @Test
    public void testParseArguments() throws ParseException
    {
        String[] arguments = {"test", "10"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgList().size() == 2);
        assertEquals("test", cmd.getArgList().get(0));
        assertEquals("10", cmd.getArgList().get(1));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("i"));
        assertFalse(cmd.hasOption("j"));
        assertFalse(cmd.hasOption("k"));
        assertFalse(cmd.hasOption("l"));
        assertFalse(cmd.hasOption("m"));
        assertFalse(cmd.hasOption("n"));
        assertFalse(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("p"));
        assertFalse(cmd.hasOption("q"));
        assertFalse(cmd.hasOption("r"));
        assertFalse(cmd.hasOption("s"));
        assertFalse(cmd.hasOption("t"));
        assertFalse(cmd.hasOption("u"));
        assertFalse(cmd.hasOption("v"));
        assertFalse(cmd.hasOption("w"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertFalse(cmd.hasOption("z"));
        assertFalse(cmd.hasOption("group1"));
        assertFalse(cmd.getOptionGroups().hasNext());
        assertTrue(cmd.getOptions().size() == 31);
    }

    /**
     * Tests the parse method with error option.
     *
     * @throws ParseException if the parse method fails.
     */
    @Test
    public void testParseErrorOption() throws ParseException
    {
        String[] arguments = {"--error"};
        CommandLine cmd = parser.parse(options, arguments, true, true);
        assertTrue(cmd.getOptionValue("e"));
        assertFalse(cmd.getArgList().isEmpty());
        assertTrue(cmd.getArgList().get(0).startsWith("--"));
    }

    /**
     * Tests the parse method with help option.
     */
    @Test
    public void testParseHelpOption() throws ParseException, IOException
    {
        String[] arguments = {"--help"};
        CommandLine cmd = parser.parse(options, arguments, true, true);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ParserTest", options);
        String helpOutput = System.out.getOutputStream().toString();
        assertTrue(helpOutput.contains("--apple"));
        assertTrue(helpOutput.contains("--banana"));
        assertTrue(helpOutput.contains("--color"));
        assertTrue(helpOutput.contains("--delay"));
        assertTrue(helpOutput.contains("--error"));
        assertTrue(helpOutput.contains("--group1"));
        assertTrue(helpOutput.contains("--help"));
        assertTrue(helpOutput.contains("--i"));
        assertTrue(helpOutput.contains("--just"));
        assertTrue(helpOutput.contains("--keep"));
        assertTrue(helpOutput.contains("--list"));
        assertTrue(helpOutput.contains("--more"));
        assertTrue(helpOutput.contains("--no"));
        assertTrue(helpOutput.contains("--output"));
        assertTrue(helpOutput.contains("--prefix"));
        assertTrue(helpOutput.contains("--quiet"));
        assertTrue(helpOutput.contains("--repeat"));
        assertTrue(helpOutput.contains("--separator"));
        assertTrue(helpOutput.contains("--test"));
        assertTrue(helpOutput.contains("--user"));
        assertTrue(helpOutput.contains("--version"));
        assertTrue(helpOutput.contains("--width"));
        assertTrue(helpOutput.contains("--xml"));
        assertTrue(helpOutput.contains("--yes"));
        assertTrue(helpOutput.contains("--zero"));
        assertTrue(helpOutput.contains("group1"));
        assertTrue(helpOutput.contains("Show group"));
        assertTrue(helpOutput.contains("Show help message"));
    }

    /**
     * Tests the parse method with properties.
     *
     * @throws ParseException if the parse method fails.
     */
    @Test
    public void testParseProperties() throws ParseException, IOException
    {
        Properties properties = new Properties();
        properties.setProperty("apple", "apple1");
        properties.setProperty("color", "red");
        String[] arguments = {"--delay", "delay1"};
        CommandLine cmd = parser.parse(options, arguments, properties, true);
        assertEquals("apple1", cmd.getOptionValue("a"));
        assertEquals("red", cmd.getOptionValue("c"));
        assertEquals("delay1", cmd.getOptionValue("d"));
        assertFalse(cmd.getArgList().isEmpty());
        assertTrue(cmd.getArgList().get(0).startsWith("--"));
    }

    /**
     * Tests the parse method with stopAtNonOption.
     *
     * @throws ParseException if the parse method fails.
     */
    @Test
    public void testParseStopAtNonOption() throws ParseException, IOException
    {
        String[] arguments = {"--delay", "delay1", "test"};
        CommandLine cmd = parser.parse(options, arguments, true, true);
        assertEquals("test", cmd.getArgList().get(0));
        assertTrue(cmd.getArgList().size() == 1);
        assertTrue(cmd.getOptionValue("d").equals("delay1"));
    }

    /**
     * Tests the parse method with stopAtNonOption and no options.
     *
     * @throws ParseException if the parse method fails.
     */
    @Test
    public void testParseStopAtNonOptionNoOptions() throws ParseException
    {
        String[] arguments = {"test"};
        CommandLine cmd = parser.parse(options, arguments, true, true);
        assertTrue(cmd.getArgList().get(0).equals("test"));
        assertTrue(cmd.getArgList().size() == 1);
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("i"));
        assertFalse(cmd.hasOption("j"));
        assertFalse(cmd.hasOption("k"));
        assertFalse(cmd.hasOption("l"));
        assertFalse(cmd.hasOption("m"));
        assertFalse(cmd.hasOption("n"));
        assertFalse(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("p"));
        assertFalse(cmd.hasOption("q"));
        assertFalse(cmd.hasOption("r"));
        assertFalse(cmd.hasOption("s"));
        assertFalse(cmd.hasOption("t"));
        assertFalse(cmd.hasOption("u"));
        assertFalse(cmd.hasOption("v"));
        assertFalse(cmd.hasOption("w"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertFalse(cmd.hasOption("z"));
        assertFalse(cmd.hasOption("group1"));
        assertFalse(cmd.getOptionGroups().hasNext());
        assertTrue(cmd.getOptions().size() == 31);
    }
}