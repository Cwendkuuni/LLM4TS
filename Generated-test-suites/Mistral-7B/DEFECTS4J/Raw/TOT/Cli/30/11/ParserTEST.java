package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleParsing() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(false, cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .required(true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail("Expected a ParseException but no exception was thrown");
    }

    @Test
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = new String[]{"--unknownOption"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertFalse(cmd.hasOption("unknownOption"));
    }

    @Test
    public void testOptionsWithArg() throws ParseException
    {
        String[] args = new String[]{"-x", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("x", true, "Option X")
                .longOpt("optionX")
                .required(true)
                .argName("VALUE");
        options.addOption("b", false, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("x"));
        Assert.assertEquals("1", cmd.getOptionValue("x"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("optionX"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOptionWithArg() throws ParseException
    {
        String[] args = new String[]{"--optionX", "VALUE"};
        Options options = new Options();
        options.addOption("x", true, "Option X")
                .longOpt("optionX")
                .required(true)
                .argName("VALUE");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail("Expected a ParseException but no exception was thrown");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = new String[]{"--myProperty", "myValue"};
        Options options = new Options();
        options.addOption("myProperty", true, "My Property")
                .hasArg(true)
                .argName("VALUE");

        Properties properties = new Properties();
        properties.setProperty("--myProperty", "otherValue");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, true);

        Assert.assertEquals(false, cmd.hasOption("myProperty"));
        Assert.assertEquals("myValue", cmd.getOptionValue("myProperty"));
        Assert.assertEquals("otherValue", properties.getProperty("myProperty"));
    }

    @Test
    public void testParseWithOptionsGroup() throws ParseException
    {
        String[] args = new String[]{"--group1", "--optionA", "1", "--group1", "--optionB", "2"};
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("a", true, "Option A in group 1"));
        group1.addOption(new Option("b", true, "Option B in group 1"));
        options.addOptionGroup(group1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws ParseException
    {
        String[] args = new String[]{"--longOptionA", "-s", "shortOptionA", "--longOptionB", "3", "-a"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A")
                .longOpt("longOptionA")
                .required(true);
        options.addOption("s", true, "Short Option A");
        options.addOption("longOptionB", true, "Long Option B")
                .argName("INTEGER");
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("longOptionA"));
        Assert.assertEquals(true, cmd.hasOption("s"));
        Assert.assertEquals("shortOptionA", cmd.getOptionValue("s"));
        Assert.assertEquals(true, cmd.hasOption("longOptionB"));
        Assert.assertEquals("3", cmd.getOptionValue("longOptionB"));
        Assert.assertEquals(true, cmd.hasOption("a"));
    }
}