package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser class.
 */
public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOptionGroup(new OptionGroup("g", "Group", "Group options"));
        options.addOptionGroup(new OptionGroup("h", "Help", "Help options", true));
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParseWithValidArguments() throws ParseException
    {
        String[] arguments = new String[]{"--a", "--b", "1", "--c", "3"};
        CommandLine cmd = parser.parse(arguments);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("d"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals(1, cmd.getOptionValues("c")[0].intValue());
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException
    {
        String[] arguments = new String[]{"--a", "--c", "1", "--c", "2"};
        CommandLine cmd = parser.parse(arguments);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("d"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertArrayEquals(new String[]{"1", "2"}, cmd.getOptionValues("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException
    {
        String[] arguments = new String[]{"--a", "--c", "1"};
        parser.parse(arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException
    {
        String[] arguments = new String[]{"--a", "--d"};
        parser.parse(arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        String[] arguments = new String[]{"--d", "--a"};
        parser.parse(arguments);
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        options.addOption("long-a", true, "Long Option A");
        String[] arguments = new String[]{"--long-a"};
        CommandLine cmd = parser.parse(arguments);
        Assert.assertTrue(cmd.hasOption("long-a"));
    }

    @Test
    public void testParseWithGroupOptions() throws ParseException
    {
        String[] arguments = new String[]{"--g", "--a"};
        CommandLine cmd = parser.parse(arguments);
        Assert.assertTrue(cmd.hasOption("g"));
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithHelpOption() throws ParseException
    {
        String[] arguments = new String[]{"--help"};
        CommandLine cmd = parser.parse(arguments);
        Assert.assertEquals(Parser.class, cmd.getClass());
        Assert.assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        CommandLine cmd = parser.parse(new String[0], properties);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
    }
}