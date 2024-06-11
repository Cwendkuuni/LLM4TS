package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = new String[]{"-a", "1", "--option", "value"};
        Options options = new Options();
        options.addRequiredOption("a", "anOption", true, "A value is required");
        options.addOption("option", true, "", "An option without value");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("option"));
        Assert.assertEquals(2, cmd.getArgList().size());
    }

    @Test
    public void testParseRequiredMissing() throws ParseException {
        String[] args = new String[]{"--no-option"};
        Options options = new Options();
        options.addRequiredOption("option", "", true, "An option is required");

        DefaultParser parser = new DefaultParser();

        try {
            parser.parse(options, args, false);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: option", e.getMessage());
        }
    }

    @Test
    public void testParseInvalidOption() {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();

        DefaultParser parser = new DefaultParser();

        try {
            parser.parse(options, args, false);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: invalid", e.getMessage());
        }
    }

    @Test
    public void testParseValueWithQuotes() throws ParseException {
        String[] args = new String[]{"--option", "\"value with quotes\""};
        Options options = new Options();
        options.addOption("option", true, "", "An option with a value");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("value with quotes", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseNoValue() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "", "An optional option without value");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseBooleanOption() throws ParseException {
        String[] args = new String[]{"--boolean"};
        Options options = new Options();
        options.addOption("boolean", false, null, "A boolean option");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertFalse(cmd.hasOption("boolean"));

        String[] args2 = new String[]{"--boolean"};
        cmd = parser.parse(options, args2, false);

        Assert.assertTrue(cmd.hasOption("boolean"));
    }

    @Test
    public void testParseGroup() throws ParseException {
        String[] args = new String[]{"--group", "1", "--group2", "2"};
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Options in this group", 1);
        group.setRequired(true);
        group.addOption(new Option("g1", "group1", true, "Option in group1"));
        group.addOption(new Option("g2", "group2", true, "Option in group2"));

        options.addOptionGroup(group);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(cmd.hasOption("g1"));
        Assert.assertTrue(cmd.hasOption("g2"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        String[] args = new String[]{"--option"};
        Options options = new Options();
        options.addOption("option", true, true, "An option with a required argument");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        Assert.fail();
    }

    @Test
    public void testParseMultipleArguments() throws ParseException {
        String[] args = new String[]{"--option", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option", true, true, "An option with multiple arguments");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(Arrays.asList("arg1", "arg2"), cmd.getOptionValues("option"));
    }
}