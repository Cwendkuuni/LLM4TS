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

/**
 * Tests the {@link Parser} class.
 */
public class ParserTest
{
    @Test
    public void testParser() throws Exception
    {
        // Test case for parsing a simple command line
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "alpha", true, "An option with a flag");
        options.addOption("b", "beta", 1, "An option with an integer value");

        String[] arguments = {"-a", "--alpha", "-b", "2"};
        CommandLine cmd = parser.parse(options, arguments, null, false);

        Assert.assertEquals("An option with a flag", cmd.getOptionValue("a"), "true");
        Assert.assertEquals("An option with an integer value", cmd.getOptionValue("b"), "2");
        Assert.assertNull("No other options should be present", cmd.getOptionValue("gamma"));
        Assert.assertNull("No other args should be present", cmd.getArgs().get(0));

        // Test case for parsing a command line with required options
        options.addRequiredOption("c", "char", true, "A required option with a flag");
        List<String> requiredOptions = options.getRequiredOptions();

        String[] argumentsWithMissingOption = {"-a", "-b"};
        try
        {
            parser.parse(options, argumentsWithMissingOption, null, false);
            Assert.fail("Expected ParseException when required option is missing");
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Missing required option: c", e.getMessage());
            Assert.assertEquals(requiredOptions.size(), e.getMissingOptions().size());
        }

        // Test case for parsing a command line with extra arguments
        String[] argumentsWithExtraArgs = {"-a", "--alpha", "-b", "2", "foo", "bar"};
        CommandLine cmdWithExtraArgs = parser.parse(options, argumentsWithExtraArgs, null, false);

        Assert.assertEquals("An option with a flag", cmdWithExtraArgs.getOptionValue("a"), "true");
        Assert.assertEquals("An option with an integer value", cmdWithExtraArgs.getOptionValue("b"), "2");
        Assert.assertNull("No other options should be present", cmdWithExtraArgs.getOptionValue("gamma"));
        Assert.assertEquals("Extra args should not be present", cmdWithExtraArgs.getArgs().size(), 0);

        // Test case for parsing a command line with an invalid option
        options.addOption("d", "delta", true, "An option with a flag");
        String[] argumentsWithInvalidOption = {"-a", "--alpha", "-b", "2", "-d"};
        try
        {
            parser.parse(options, argumentsWithInvalidOption, null, false);
            Assert.fail("Expected UnrecognizedOptionException when invalid option is present");
        }
        catch (UnrecognizedOptionException e)
        {
            Assert.assertEquals("Unrecognized option: d", e.getMessage());
        }

        // Test case for parsing a command line with an option with a value
        options.addOption("e", "epsilon", true, "An option with a value");
        String[] argumentsWithOptionWithValue = {"-e", "epsilonValue"};
        CommandLine cmdWithOptionWithValue = parser.parse(options, argumentsWithOptionWithValue, null, false);

        Assert.assertEquals("An option with a value", cmdWithOptionWithValue.getOptionValue("e"), "epsilonValue");
        Assert.assertNull("No other options should be present", cmdWithOptionWithValue.getOptionValue("gamma"));
        Assert.assertNull("No other args should be present", cmdWithOptionWithValue.getArgs().get(0));

        // Test case for parsing a command line with an option group
        Options optionsWithGroup = new Options();
        OptionGroup commandLineOptionGroup = new OptionGroup("commandline-options", "Options to specify various command line options");
        commandLineOptionGroup.addOption(optionsWithGroup.addOption("i", "integer", true, "An option with an integer value"));
        commandLineOptionGroup.addOption(optionsWithGroup.addOption("f", "flag", false, "A flag option"));
        optionsWithGroup.addOption("group", "group", false, "A group option");
        optionsWithGroup.addOptionGroup(commandLineOptionGroup);

        String[] argumentsWithGroup = {"--group", "--flag", "--i", "42"};
        CommandLine cmdWithGroup = parser.parse(optionsWithGroup, argumentsWithGroup, null, false);

        Assert.assertTrue("A flag option should be set", cmdWithGroup.hasOption("flag"));
        Assert.assertEquals("An option with an integer value", cmdWithGroup.getOptionValue("i"), "42");
        Assert.assertNull("No other options should be present", cmdWithGroup.getOptionValue("gamma"));
        Assert.assertNull("No other args should be present", cmdWithGroup.getArgs().get(0));
    }
}