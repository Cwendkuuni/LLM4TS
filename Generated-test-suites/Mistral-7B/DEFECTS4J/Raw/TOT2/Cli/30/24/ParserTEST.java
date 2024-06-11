package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testParseSimpleCommandLine() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"-a", "1"};
        Options options = new Options();
        Option optionA = new Option("a", true, false, "value for option A");
        options.addOption(optionA);

        // When
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        // Then
        Assert.assertFalse(cmd.hasOption("h"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseCommandLineWithLongOptions() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"--long-option", "value"};
        Options options = new Options();
        Option optionLong = new Option("long-option", true, false, "value for long option");
        options.addOption(optionLong);

        // When
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        // Then
        Assert.assertFalse(cmd.hasOption("h"));
        Assert.assertTrue(cmd.hasOption("long-option"));
        Assert.assertEquals("value", cmd.getOptionValue("long-option"));
    }

    @Test(expected = ParseException.class)
    public void testParseCommandLineWithUnrecognizedOption() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"--unknown-option"};
        Options options = new Options();

        // When
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        // Then
        // An exception should be thrown
    }

    @Test(expected = ParseException.class)
    public void testParseCommandLineWithMissingRequiredOption() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"-a"};
        Options options = new Options();
        Option optionA = new Option("a", true, true, "value for option A");
        options.addOption(optionA);

        // When
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        // Then
        // An exception should be thrown
    }

    @Test
    public void testParseCommandLineWithMultipleValuesForOption() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"-a", "1", "-a", "2"};
        Options options = new Options();
        Option optionA = new Option("a", true, false, "value for option A");
        options.addOption(optionA);

        // When
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        // Then
        Assert.assertFalse(cmd.hasOption("h"));
        Assert.assertTrue(cmd.hasOption("a"));
        List<String> values = Arrays.asList(cmd.getOptionValues("a"));
        Assert.assertEquals(2, values.size());
        Assert.assertTrue(values.contains("1"));
        Assert.assertTrue(values.contains("2"));
    }

    @Test
    public void testParseCommandLineWithProperties() throws ParseException
    {
        // Given
        String[] arguments = new String[]{"--property1=value1", "--property2=value2"};
        Options options = new Options();
        Option optionProperty1 = new Option("property1", true, false, "value for property1");
        Option optionProperty2 = new Option("property2", true, false, "value for property2");
        options.addOption(optionProperty1);
        options.addOption(optionProperty2);

        // When
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        // Then
        Properties properties = cmd.getOptions();
        Assert.assertEquals("value1", properties.getProperty("property1"));
        Assert.assertEquals("value2", properties.getProperty("property2"));
    }
}