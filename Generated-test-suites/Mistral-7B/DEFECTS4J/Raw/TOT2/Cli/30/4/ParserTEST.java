package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", 1, "Option C");

        String[] arguments = {"-a", "valueA", "--optionB", "--optionC=3"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));

        List<String> args = cmd.getArgs();
        Assert.assertEquals(0, args.size());
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {"--optionB"};
        CommandLineParser parser = new DefaultParser();

        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {"--unrecognizedOption"};
        CommandLineParser parser = new DefaultParser();

        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgumentType() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", 1, "Option A");

        String[] arguments = {"-a", "valueA", "--optionB"};
        CommandLineParser parser = new DefaultParser();

        parser.parse(options, arguments, false);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");

        String[] arguments = {"--"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, properties, false);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));

        List<String> args = cmd.getArgs();
        Assert.assertEquals(0, args.size());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {"--optionA", "valueA", "unrecognizedOption"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments, false, true);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));

        List<String> args = cmd.getArgs();
        Assert.assertArrayEquals(new String[]{"unrecognizedOption"}, args.toArray(new String[0]));
    }
}