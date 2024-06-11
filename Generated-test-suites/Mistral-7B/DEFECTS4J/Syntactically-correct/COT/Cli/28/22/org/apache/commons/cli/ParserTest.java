package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test cases for the Parser class.
 */
public class ParserTest
{
    /**
     * Test case for the setOptions method.
     */
    @Test
    public void testSetOptions()
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    /**
     * Test case for the getOptions method.
     */
    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    /**
     * Test case for the getRequiredOptions method.
     */
    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        List<String> requiredOptions = parser.getRequiredOptions();
        Assert.assertEquals(2, requiredOptions.size());
        Assert.assertEquals("option1", requiredOptions.get(0));
        Assert.assertEquals("option2", requiredOptions.get(1));
    }

    /**
     * Test case for the flatten method with empty arguments.
     */
    @Test
    public void testFlattenEmptyArguments() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test case for the flatten method with required options.
     */
    @Test(expected = ParseException.class)
    public void testFlattenMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "arg2"};
        parser.flatten(options, arguments, false);
    }

    /**
     * Test case for the flatten method with optional arguments.
     */
    @Test
    public void testFlattenOptionalArgument() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"--option", "arg1", "--option=arg2"};
        String[] expected = {"--option", "arg1", "--option=arg2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test case for the parse method with valid arguments.
     */
    @Test
    public void testParseValidArguments() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"--option", "arg1", "--option=arg2"};
        CommandLine commandLine = parser.parse(options, arguments);
        Assert.assertEquals(2, commandLine.getArgs().size());
        Assert.assertEquals("arg1", commandLine.getArgs().get(0));
        Assert.assertEquals("arg2", commandLine.getArgs().get(1));
    }

    /**
     * Test case for the parse method with missing required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1"};
        parser.parse(options, arguments, null, false);
    }

    /**
     * Test case for the parse method with properties.
     */
    @Test
    public void testParseProperties() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("option", "arg1");
        CommandLine commandLine = parser.parse(options, null, properties, false);
        Assert.assertEquals(1, commandLine.getArgs().size());
        Assert.assertEquals("arg1", commandLine.getArgs().get(0));
    }

    /**
     * Test case for the parse method with stopAtNonOption=true.
     */
    @Test
    public void testParseStopAtNonOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "--option", "arg3"};
        CommandLine commandLine = parser.parse(options, arguments, null, true);
        Assert.assertEquals(3, commandLine.getArgs().size());
        Assert.assertEquals("arg1", commandLine.getArgs().get(0));
        Assert.assertEquals("--option", commandLine.getArgs().get(1));
        Assert.assertEquals("arg3", commandLine.getArgs().get(2));
    }
}