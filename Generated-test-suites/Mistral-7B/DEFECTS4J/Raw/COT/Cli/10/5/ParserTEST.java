package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser
 */
public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "another help message");
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        Assert.assertEquals(requiredOptions, parser.getRequiredOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequiredOptionsWithNull() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        parser.setRequiredOptions(null);
    }

    @Test
    public void testFlattenEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];
        String[] result = parser.flatten(new Options(), arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimple() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "arg2", "--"};
        String[] result = parser.flatten(new Options(), arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--", "-a", "arg2", "--"};
        String[] result = parser.flatten(new Options(), arguments, true);
        Assert.assertArrayEquals(new String[]{"--", "-a"}, result);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        parser.parse(options, arguments);
        Assert.fail("ParseException expected");
    }

    @Test
    public void testParseOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "val1", "--", "-b", "val2"};
        CommandLine cmd = parser.parse(options, arguments);
        Option aOption = options.getOption("a");
        Option bOption = options.getOption("b");
        Assert.assertEquals("val1", aOption.getValue());
        Assert.assertEquals("val2", bOption.getValue());
        Assert.assertEquals(2, cmd.getArgs().size());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        requiredOptions.add(new Option("a", true, "a help message"));
        options.setRequiredOptions(requiredOptions);
        String[] arguments = {"--"};
        parser.parse(options, arguments);
        Assert.fail("MissingOptionException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--x", "arg1", "arg2"};
        parser.parse(options, arguments);
        Assert.fail("UnrecognizedOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a"};
        parser.parse(options, arguments);
        Assert.fail("MissingArgumentException expected");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("a", "val1");
        properties.setProperty("b", "val2");
        String[] arguments = {"--property-file", "test.properties"};
        CommandLine cmd = parser.parse(options, arguments, properties, true);
        Option aOption = options.getOption("a");
        Option bOption = options.getOption("b");
        Assert.assertEquals("val1", aOption.getValue());
        Assert.assertEquals("val2", bOption.getValue());
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--", "-a", "--", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, null, true);
        Assert.assertEquals(2, cmd.getArgs().size());
    }
}