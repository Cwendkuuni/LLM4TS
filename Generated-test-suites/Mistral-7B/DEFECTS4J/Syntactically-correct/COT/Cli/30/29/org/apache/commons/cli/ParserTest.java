package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Test class for the Parser class.
 */
public class ParserTest
{
    @Test
    public void testParseWithEmptyArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithValidArguments() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a", "1", "-b", "2", "--"};
        setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(Arrays.asList("1", "2"), cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"--nonexistent"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingArgument() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a"};
        Option option = new Option("a", "a", true, "a help message");
        option.setRequired(true);
        options.addOption(option);
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArgument() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"--invalid"};
        Option option = new Option("i", "i", true, "an invalid option");
        options.addOption(option);
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"--a"};
        Option option = new Option("a", "a", true, "a required option");
        option.setRequired(true);
        options.addOption(option);
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"--x"};
        parser.parse(options, arguments);
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseWithNoSuchOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[]{"-x"};
        Option option = new Option("a", "a", true, "an option");
        options.addOption(option);
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "2");
        CommandLine cmd = parser.parse(options, arguments, properties, false);
        List<String> args = cmd.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("1", args.get(0));
        Assert.assertEquals("2", args.get(1));
    }

    private void setOptions(Options options)
    {
        options.addOption(new Option("a", "a", true, "an option"));
    }
}