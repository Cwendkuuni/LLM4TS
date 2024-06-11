package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testSimpleParsing() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        Parser parser = new DefaultParser();
        String[] args = {"-a", "--optionB"};
        CommandLine commandLine = parser.parse(options, args, true);

        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));
        Assert.assertNull(commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("b"));

        Assert.assertEquals(Arrays.asList("--optionB"), commandLine.getAllOptions());
    }

    @Test
    public void testParsingWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        Parser parser = new DefaultParser();
        String[] args = {"-a", "--optionB=valueB", "arg1", "arg2"};
        CommandLine commandLine = parser.parse(options, args, true);

        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertEquals("valueB", commandLine.getOptionValue("b"));
        Assert.assertNull(commandLine.getOptionValue("a"));

        List<String> argsList = commandLine.getArgs();
        Assert.assertEquals(2, argsList.size());
        Assert.assertEquals("arg1", argsList.get(0));
        Assert.assertEquals("arg2", argsList.get(1));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        Parser parser = new DefaultParser();
        String[] args = {"--optionB"};
        parser.parse(options, args, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        Parser parser = new DefaultParser();
        String[] args = {"-x", "--optionA"};
        parser.parse(options, args, true);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        Parser parser = new DefaultParser();
        String[] args = {"-a"};
        parser.parse(options, args, true);
    }

    @Test
    public void testParsingWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");

        Parser parser = new DefaultParser();
        String[] args = {"--optionA"};
        CommandLine commandLine = parser.parse(options, args, properties, true);

        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("valueA", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParsingWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1");
        group.setRequired(false);

        group.addOption("a", "optionA", false, "Option A");
        group.addOption("b", "optionB", false, "Option B");

        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        String[] args = {"-a", "--group1.optionB"};
        CommandLine commandLine = parser.parse(options, args, true);

        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption("b"));

        Option optionA = commandLine.getOption("a");
        Assert.assertNull(optionA.getLongOpt());
        Assert.assertEquals("a", optionA.getKey());

        Option optionB = commandLine.getOption("b");
        Assert.assertEquals("group1.optionB", optionB.getLongOpt());
        Assert.assertEquals("b", optionB.getKey());
    }
}