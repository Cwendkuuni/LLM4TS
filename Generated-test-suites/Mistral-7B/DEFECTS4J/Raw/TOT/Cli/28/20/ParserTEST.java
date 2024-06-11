package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-b", "2", "--", "3", "4", "--", "-c", "5"};

        Parser parser = new DefaultParser();
        Commands commands = new Commands();
        parser.setOptions(commands.getOptions());

        CommandLine cmd = parser.parse(args);

        Options options = parser.getOptions();
        List requiredOptions = parser.getRequiredOptions();

        Assert.assertEquals(options.getOptions().length, 4);
        Assert.assertEquals(requiredOptions.size(), 0);
        Assert.assertEquals(cmd.getArgs().length, 5);
        Assert.assertEquals(cmd.getOptionValues("a").length, 1);
        Assert.assertEquals(cmd.getOptionValues("a")[0], "1");
        Assert.assertEquals(cmd.getOptionValues("b").length, 1);
        Assert.assertEquals(cmd.getOptionValues("b")[0], "2");
        Assert.assertNull(cmd.getOptionValues("c"));
        Assert.assertArrayEquals(cmd.getArgs(), new String[]{"3", "4", "5"});
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-b", "2"};

        Parser parser = new DefaultParser();
        Commands commands = new Commands();
        parser.setOptions(commands.getOptions());

        List requiredOptions = parser.getRequiredOptions();

        parser.parse(args);

        Assert.assertEquals(requiredOptions.size(), 1);
        Assert.assertEquals(requiredOptions.get(0), "option1");
        throw new AssertionError("Should throw MissingOptionException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = new String[]{"--unknown-option"};

        Parser parser = new DefaultParser();
        Commands commands = new Commands();
        parser.setOptions(commands.getOptions());

        parser.parse(args);

        throw new AssertionError("Should throw UnrecognizedOptionException");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        String[] args = new String[]{"-a"};

        Parser parser = new DefaultParser();
        Commands commands = new Commands();
        parser.setOptions(commands.getOptions());

        parser.parse(args);

        throw new AssertionError("Should throw MissingArgumentException");
    }

    @Test
    public void testProperties() throws ParseException
    {
        String[] args = new String[]{"-p", "prop1=value1", "-p", "prop2=value2", "--"};

        Parser parser = new DefaultParser();
        Commands commands = new Commands();
        parser.setOptions(commands.getOptions());

        CommandLine cmd = parser.parse(args);

        Properties properties = cmd.getProperties();

        Assert.assertEquals(properties.getProperty("prop1"), "value1");
        Assert.assertEquals(properties.getProperty("prop2"), "value2");
    }

    @Test
    public void testPropertiesWithValues() throws ParseException
    {
        String[] args = new String[]{"--properties-file", "test.properties"};

        Parser parser = new DefaultParser();
        Commands commands = new Commands();
        parser.setOptions(commands.getOptions());

        CommandLine cmd = parser.parse(args, new Properties());

        Properties properties = cmd.getProperties();

        Assert.assertNotNull(properties.getProperty("option1"));
        Assert.assertNotNull(properties.getProperty("option2"));
    }
}

class Commands extends Options
{
    public Commands()
    {
        Option option1 = new Option("a", true, true, "option1");
        Option option2 = new Option("b", true, true, "option2");
        Option option3 = new Option("c", true, false, "option3");

        addOption(option1);
        addOption(option2);
        addOption(option3);
        addOptionGroup(new OptionGroup("group1", "Group1", true));

        Option groupOption1 = new Option("g", "g", false, "Group Option 1");
        groupOption1.setOptionGroup(getOptionGroup("group1"));

        addOption(groupOption1);
    }
}