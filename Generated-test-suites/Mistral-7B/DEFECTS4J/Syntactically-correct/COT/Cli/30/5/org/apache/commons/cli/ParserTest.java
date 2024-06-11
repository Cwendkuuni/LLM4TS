package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {
    @Test
    public void testParserInstance() {
        Parser parser = new DefaultParser();
        Assert.assertNotNull(parser);
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        List<String> requiredOptions = new ArrayList<>();
        options.addRequiredOption("o", "option", true, "option description");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        List<String> actualOptions = parser.getRequiredOptions();
        Assert.assertEquals(1, actualOptions.size());
        Assert.assertEquals("o", actualOptions.get(0));
    }

    @Test
    public void testFlattenWithNoArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] result = parser.flatten(options, new String[0], false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {};
        CommandLine commandLine = parser.parse(options, arguments, null, false);
        Assert.assertNotNull(commandLine);
        Assert.assertTrue(commandLine.getArgs().isEmpty());
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "option description");
        Parser parser = new DefaultParser();
        String[] arguments = {"--option", "value"};
        CommandLine commandLine = parser.parse(options, arguments, null, false);
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Option option = commandLine.getOptions().get(0);
        Assert.assertEquals("value", option.getValue());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "option description");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        CommandLine commandLine = parser.parse(options, null, properties, false);
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Option option = commandLine.getOptions().get(0);
        Assert.assertEquals("value", option.getValue());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "--invalid", "arg3"};
        parser.parse(options, arguments, null, false);
        Assert.fail("Expected ParseException");
    }

    @Test(expected = ParseException.class)
    public void testParseWithOptionalArgumentMissing() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "option description");
        Parser parser = new DefaultParser();
        String[] arguments = {"--option"};
        parser.parse(options, arguments, null, false);
        Assert.fail("Expected ParseException");
    }

    @Test
    public void testParseWithMultipleOptionalArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "option description");
        Parser parser = new DefaultParser();
        String[] arguments = {"--option1", "arg1", "--option2", "arg2"};
        CommandLine commandLine = parser.parse(options, arguments, null, false);
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(2, commandLine.getOptions().size());
        Option option1 = commandLine.getOptions().get(0);
        Assert.assertEquals("arg1", option1.getValue());
        Option option2 = commandLine.getOptions().get(1);
        Assert.assertEquals("arg2", option2.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {"--unrecognized"};
        parser.parse(options, arguments, null, false);
        Assert.fail("Expected UnrecognizedOptionException");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "option description");
        Parser parser = new DefaultParser();
        String[] arguments = {"--option"};
        parser.parse(options, arguments, null, false);
        Assert.fail("Expected MissingArgumentException");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add("requiredOption");
        options.setRequiredOptions(requiredOptions.toArray(new String[0]));
        Parser parser = new DefaultParser();
        String[] arguments = {};
        parser.parse(options, arguments, null, false);
        Assert.fail("Expected MissingOptionException");
    }
}