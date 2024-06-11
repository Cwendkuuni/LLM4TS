package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParseWithSimpleArguments() throws ParseException
    {
        String[] args = {"-a", "arg1", "--optionB", "arg2"};

        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("arg1", cmd.getOptionValue("a"));
        Assert.assertEquals("arg2", cmd.getOptionValue("optionB"));
        Assert.assertEquals(2, cmd.getArgs().size());
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedArgument() throws ParseException
    {
        String[] args = {"--unknownOption", "arg1"};

        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedShortOption() throws ParseException
    {
        String[] args = {"-x", "arg1"};

        parser.parse(options, args);
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException
    {
        options.addRequiredOption("a");
        options.addRequiredOption("b");

        String[] args = {"-a", "arg1"};

        try
        {
            parser.parse(options, args);
        }
        catch (ParseException e)
        {
            List<String> missingOptions = e.getParseErrors().get(0).getMissingOptions();
            Assert.assertEquals(1, missingOptions.size());
            Assert.assertEquals("b", missingOptions.get(0));
        }
    }

    @Test
    public void testParseWithGroups() throws ParseException
    {
        OptionGroup group = new OptionGroup("group1");
        group.addOption(new Option("g", "groupOption").required(true));

        options.addOptionGroup(group);

        String[] args = {"--group1", "-g"};

        parser.parse(options, args);
    }

    @Test
    public void testParseWithMissingGroup() throws ParseException
    {
        OptionGroup group = new OptionGroup("group1");
        group.addOption(new Option("g", "groupOption").required(true));

        options.addOptionGroup(group);

        String[] args = {"-g"};

        try
        {
            parser.parse(options, args);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("options: unrecognized option: -g", e.getMessage());
        }
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        Option longOption = new Option("longOption", "long option");
        longOption.setLongOpt("long-option");

        options.addOption(longOption);

        String[] args = {"--long-option", "arg1"};

        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("arg1", cmd.getOptionValue("longOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingLongOptionValue() throws ParseException
    {
        Option longOption = new Option("longOption", "long option");
        longOption.setLongOpt("long-option");

        options.addOption(longOption);

        String[] args = {"--long-option"};

        parser.parse(options, args);
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException
    {
        Option option = new Option("option", "option");
        option.setArgName("value");
        option.setAllowMultipleValues(true);

        options.addOption(option);

        String[] args = {"-o", "val1", "--option", "val2"};

        CommandLine cmd = parser.parse(options, args);

        String[] values = cmd.getOptionValues("option");

        Assert.assertArrayEquals(new String[]{"val1", "val2"}, values);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingValueForMultipleValueOption() throws ParseException
    {
        Option option = new Option("option", "option");
        option.setArgName("value");
        option.setAllowMultipleValues(true);

        options.addOption(option);

        String[] args = {"-o", "val1"};

        try
        {
            parser.parse(options, args);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("option: value 'val1' is missing an argument value", e.getMessage());
        }
    }

    @Test
    public void testParseWithMultiplePositionalArguments() throws ParseException
    {
        String[] args = {"arg1", "arg2"};

        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));
    }
}