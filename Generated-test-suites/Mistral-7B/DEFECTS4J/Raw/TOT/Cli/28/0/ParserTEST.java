package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        String[] args = {"-a", "arg1", "-b", "arg2", "--", "arg3"};

        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.getOptionValue("a", "defaultValueA").equals("arg1"));
        Assert.assertTrue(cmd.getOptionValue("b", "defaultValueB").equals("arg2"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException
    {
        String[] args = {"--optionA", "--optionB"};

        Options options = new Options();
        options.addOption("a", true, "option A", true, "An option is required");
        options.addOption("b", true, "option B", true, "An option is required");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        String[] args = {"--unrecognizedOption"};

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        String[] args = {"--optionA"};

        Options options = new Options();
        options.addOption("a", true, "option A", true, "An option is required");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException
    {
        String[] args = {"--optionGroup", "group1"};

        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "group1");
        group1.setRequired(false);
        group1.addOption(new Option("g", "groupOption", true, "Group option"));
        options.addOptionGroup(group1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("groupOption"));
        Assert.assertNull(cmd.getOptionValue("groupOption"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        String[] args = {"--longOption", "longValue"};

        Options options = new Options();
        options.addOption("lo", "longOption", true, "Long option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("lo"));
        Assert.assertTrue(cmd.getOptionValue("lo").equals("longValue"));
    }
}