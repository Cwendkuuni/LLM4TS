package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        String[] args = new String[]{"-a", "1"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
    }

    @Test
    public void testMultipleOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-b", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testNoOptions() throws ParseException
    {
        String[] args = new String[]{"test.sh"};

        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertFalse(cmd.hasOptions());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        String[] args = new String[]{"-c", "test.sh"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, true);

        // Missing option exception should be thrown
    }

    @Test(expected = ParseException.class)
    public void testMissingValue() throws ParseException
    {
        String[] args = new String[]{"-a"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, true);

        // Missing value exception should be thrown
    }

    @Test
    public void testLongOption() throws ParseException
    {
        String[] args = new String[]{"--longOption", "LongValue"};

        Options options = new Options();
        options.addOption("lo", false, "Long Option");
        options.addOption("longOption", true, "Long Option with long name");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(true, cmd.hasOption("longOption"));
        Assert.assertEquals("LongValue", cmd.getOptionValue("longOption"));
        Assert.assertFalse(cmd.hasOption("lo"));
    }

    @Test
    public void testOptionGroup() throws ParseException
    {
        String[] args = new String[]{"-g", "group1", "-a", "1"};

        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption("a", true, "Option A");
        options.addOptionGroup(group1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("Group 1", cmd.getOptionGroup("g").getName());
    }

    @Test(expected = ParseException.class)
    public void testMissingGroup() throws ParseException
    {
        String[] args = new String[]{"-g1", "group1", "-a", "1"};

        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        options.addOptionGroup(group1);

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, true);

        // Missing group exception should be thrown
    }

    @Test
    public void testGroupRequires() throws ParseException
    {
        String[] args = new String[]{"-g", "group1", "arg"};

        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.setRequired(true);
        group1.addOption("a", true, "Option A");
        options.addOptionGroup(group1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertTrue(group1.isSelected());
        Assert.assertEquals(true, cmd.hasOption("a"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException, MissingOptionException
    {
        String[] args = new String[]{"-g", "group1"};

        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption("a", true, "Option A", true);
        options.addOptionGroup(group1);

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, true);

        // Missing required option exception should be thrown
    }
}