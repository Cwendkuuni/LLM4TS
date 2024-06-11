package org.apache.commons.cli.test;

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
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testParser() throws ParseException
    {
        String[] arguments = {"--option1", "-o1", "value1", "--option2", "-o2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option1", true, "o1", "Value for option1");
        options.addOption("option2", false, "o2", "Value for option2");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertFalse(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.getOptionValue("option2") == null);
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals(Arrays.asList("value1"), cmd.getArgs());
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] arguments = {"--option1"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option1", true, "o1", "Value for option1").isRequired();

        parser.setOptions(options);
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] arguments = {"--unknownOption"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option1", true, "o1", "Value for option1");

        parser.setOptions(options);
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingOptionArgument() throws ParseException
    {
        String[] arguments = {"--option1"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("option1", true, "o1", "Value for option1");
        option.hasArg();

        options.addOption(option);
        parser.setOptions(options);
        parser.parse(options, arguments);
    }

    @Test
    public void testMultipleArguments() throws ParseException
    {
        String[] arguments = {"--option1", "arg1", "arg2", "--option2", "arg3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("option1", true, "o1", "Value for option1");
        option1.hasArg();
        Option option2 = new Option("option2", false, "o2", "Value for option2");
        option2.hasArg(true);

        options.addOption(option1);
        options.addOption(option2);

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("arg1", cmd.getOptionValue("option1"));
        Assert.assertEquals("arg3", cmd.getOptionValue("option2"));
        Assert.assertFalse(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals(Arrays.asList("arg2"), cmd.getArgs());
    }

    @Test
    public void testProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.put("option1", "value1");
        properties.put("option2.arg", "value2");

        String[] arguments = {"--", "--option1", "--option2=value2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("option1", true, "o1");
        Option option2 = new Option("option2", false, "o2");
        option2.hasArg();

        options.addOption(option1);
        options.addOption(option2);

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, properties);

        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertFalse(cmd.getOptionValue("option2") instanceof String[]);
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertEquals(Arrays.asList(new String[]{"value2"}), cmd.getValuesForOption("option2"));
    }

    @Test
    public void testStopAtNonOption() throws ParseException
    {
        String[] arguments = {"--", "--option1", "--", "--option2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option1 = new Option("option1", true, "o1");
        Option option2 = new Option("option2", false, "o2");

        options.addOption(option1);
        options.addOption(option2);

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, null, true);

        Assert.assertEquals(2, cmd.getArgs().size());
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException
    {
        String[] arguments = {};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("option1", true, "o1", "Value for option1");

        options.addOption(option);
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithEmptyArgumentsAndProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.put("option1", "value1");
        String[] arguments = {};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("option1", true, "o1");

        options.addOption(option);
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, properties);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
    }

    @Test
    public void testParseWithEmptyArgumentsAndOptions() throws ParseException
    {
        String[] arguments = {};
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithEmptyArgumentsAndPropertiesAndOptions() throws ParseException
    {
        Properties properties = new Properties();
        properties.put("option1", "value1");
        String[] arguments = {};
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, properties);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
    }

    @Test
    public void testParseWithEmptyArgumentsAndOptionsAndMultipleArgs() throws ParseException
    {
        String[] arguments = {};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("option1", true, "o1", "Value for option1");
        option.hasArg();

        options.addOption(option);
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithEmptyArgumentsAndPropertiesAndOptionsAndMultipleArgs() throws ParseException
    {
        Properties properties = new Properties();
        properties.put("option1", "value1");
        String[] arguments = {};
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("option1", true, "o1", "Value for option1");
        option.hasArg();

        options.addOption(option);
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, properties);
        Assert.assertNotNull(cmd);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
    }
}