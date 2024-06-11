package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testSimpleUsage() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "input.txt", "-o", "output.txt"});

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-o", "output.txt"});
    }

    @Test
    public void testMultipleValuesForOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("l", "list", true, "list of values");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-l", "value1", "-l", "value2"});

        List<String> values = cmd.getListOptions();
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        Assert.assertEquals(expectedValues, values);
    }

    @Test(expected = ParseException.class)
    public void testWrongNumberOfArguments() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addRequiredOption("o", "output", true, "output file");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-i", "input.txt"});
    }

    @Test
    public void testHelp() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("h", "help", false, "prints this help message");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-h"});

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
        System.out.println(formatter.getHelpStatement());
    }

    @Test
    public void testProperties() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        Properties properties = new Properties();
        properties.setProperty("output", "output.txt");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "input.txt"}, properties);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-x", "invalidOption"});
    }

    @Test
    public void testStopAtNonOption() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "input.txt", "--", "output.txt"});

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testEmptyOptionGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{});

        Assert.assertFalse(group.isSelected());
        Assert.assertTrue(options.hasOption("group"));
    }

    @Test
    public void testSelectedOptionGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        options.addOptionGroup(group);

        Option option = new Option("o", "output", true, "output file");
        group.addOption(option);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "output.txt"});

        Assert.assertTrue(group.isSelected());
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }
}