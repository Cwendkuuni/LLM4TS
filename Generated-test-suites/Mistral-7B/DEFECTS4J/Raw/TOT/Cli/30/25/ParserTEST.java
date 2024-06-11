package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for {@link Parser} class.
 */
public class ParserTest
{
    @Test
    public void testSimpleParsing() throws ParseException
    {
        String[] args = {"--test", "1", "--verbose", "arg1", "--num", "2"};

        Options options = new Options();
        options.addOption("test", true, "Test mode");
        options.addOption("verbose", false, "Verbose mode");
        options.addOption("num", int.class, "Number of tests to run");

        Parser parser = new DefaultParser();
        ParseResult result = parser.parse(options, args, true);
        CommandLine cmd = result.getCommandLine();

        Assert.assertEquals("--test", cmd.getOptionValue("test"));
        Assert.assertEquals("1", cmd.getOptionValue("num"));
        Assert.assertTrue(cmd.hasOption("verbose"));
        Assert.assertNull(cmd.getOptionValue("verbose"));
        Assert.assertArrayEquals(new String[]{"arg1"}, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        String[] args = {"--unrecognized"};

        Options options = new Options();
        options.addOption("test", true, "Test mode");

        Parser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = {"--num", "2"};

        Options options = new Options();
        options.addOption("test", true, "Test mode");
        options.addOption("num", int.class, "Number of tests to run").isRequired();

        Parser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test
    public void testInvalidOptionValue() throws ParseException
    {
        String[] args = {"--num", "abc"};

        Options options = new Options();
        options.addOption("test", true, "Test mode");
        options.addOption("num", int.class, "Number of tests to run");

        Parser parser = new DefaultParser();
        ParseResult result = parser.parse(options, args, true);
        CommandLine cmd = result.getCommandLine();

        Assert.assertNull(cmd.getOptionValue("num"));
        Assert.assertEquals("", cmd.getOptionValue("num"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = {"--propertyFile", "test.properties"};

        Options options = new Options();
        options.addOption("test", true, "Test mode");
        options.addOption("propertyFile", true, "Location of the property file");

        Properties properties = new Properties();
        properties.setProperty("num", "3");

        Parser parser = new DefaultParser();
        ParseResult result = parser.parse(options, args, properties, true);
        CommandLine cmd = result.getCommandLine();

        Assert.assertEquals("test.properties", cmd.getOptionValue("propertyFile"));
        Assert.assertEquals("3", cmd.getOptionValue("num"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidPropertyValue() throws ParseException
    {
        String[] args = {"--propertyFile", "test.properties"};

        Options options = new Options();
        options.addOption("test", true, "Test mode");
        options.addOption("propertyFile", true, "Location of the property file");

        Properties properties = new Properties();
        properties.setProperty("num", "abc");

        Parser parser = new DefaultParser();
        parser.parse(options, args, properties, true);
    }

    @Test
    public void testParseWithOptionsGroup() throws ParseException
    {
        String[] args = {"--group", "group1", "--option1", "value1", "--option2", "value2"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Test group");
        group.addOption(new Option("option1", true, "Option 1"));
        group.addOption(new Option("option2", true, "Option 2"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        ParseResult result = parser.parse(options, args, true);
        CommandLine cmd = result.getCommandLine();

        Assert.assertEquals("group1", cmd.getOptionValue("group"));
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionInGroup() throws ParseException
    {
        String[] args = {"--group", "group1", "--unrecognized"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Test group");
        group.addOption(new Option("option1", true, "Option 1"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingGroupOption() throws ParseException
    {
        String[] args = {"--option1", "value1"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Test group");
        group.addOption(new Option("option1", true, "Option 1"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.parse(options, args, true);
    }
}