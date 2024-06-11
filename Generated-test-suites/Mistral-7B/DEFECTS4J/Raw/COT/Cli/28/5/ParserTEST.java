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
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testParse() throws Exception
    {
        String[] arguments = {"-a", "1", "--optionB", "2", "--optionC=3", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", 1, "Option C");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, null, false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertFalse(cmd.hasOption("d"));
        Assert.assertNull(cmd.getOptionValue("d"));
        Assert.assertEquals("Option Group 1", options.getOptions().get(0).getLongOpt());
        Assert.assertEquals("Option Group 2", options.getOptions().get(1).getLongOpt());
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception
    {
        String[] arguments = {"-a", "1", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "requiredOption");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception
    {
        String[] arguments = {"-a", "1", "--optionB"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", 1, "arg");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception
    {
        String[] arguments = {"-x", "1", "--optionB"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionFormat() throws Exception
    {
        String[] arguments = {"--optionA=1--optionB"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionValueFormat() throws Exception
    {
        String[] arguments = {"-a", "1", "--optionB", "abcd efgh"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, arguments, null, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithProperties() throws Exception
    {
        String[] arguments = {"--propertyFile", "test.properties"};

        Options options = new Options();
        options.addOption("propertyFile", true, "Property file to load");

        Properties properties = new Properties();
        properties.setProperty("test.prop", "value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("test.properties", cmd.getOptionValue("propertyFile"));
        Assert.assertEquals("value", properties.getProperty("test.prop"));
    }
}