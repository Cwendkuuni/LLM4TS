package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for Parser class.
 */
public class ParserTest
{
    @Test
    public void testParseSimple() throws Exception
    {
        // Given
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "option A");

        Parser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, false);

        // Then
        Assert.assertEquals("1", commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getArgs().length);
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test
    public void testParseWithRequiredOption() throws Exception
    {
        // Given
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "option A");
        options.requiredOption("option", true);

        Parser parser = new DefaultParser();

        // When & Then
        Assert.assertThrows(MissingOptionException.class, () -> parser.parse(options, args, null, false));
    }

    @Test
    public void testParseWithOptionalArg() throws Exception
    {
        // Given
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "option A");

        Parser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, false);

        // Then
        Assert.assertEquals("value", commandLine.getOptionValue("a"));
        Assert.assertArrayEquals(new String[]{}, commandLine.getArgs());
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test
    public void testParseWithLongOption() throws Exception
    {
        // Given
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("long-option", true, "long option");

        Parser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, false);

        // Then
        Assert.assertEquals("value", commandLine.getOptionValue("long-option"));
        Assert.assertArrayEquals(new String[]{}, commandLine.getArgs());
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test
    public void testParseWithGroup() throws Exception
    {
        // Given
        String[] args = {"--group", "--optionA", "valueA", "--optionB", "valueB"};
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "group options");
        options.addOptionGroup(group);
        group.addOption(new Option("optionA", true, "option A"));
        group.addOption(new Option("optionB", true, "option B"));

        Parser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, false);

        // Then
        Assert.assertEquals("valueA", commandLine.getOptionValue("optionA"));
        Assert.assertEquals("valueB", commandLine.getOptionValue("optionB"));
        Assert.assertArrayEquals(new String[]{}, commandLine.getArgs());
    }

    @Test
    public void testParseWithMultipleValues() throws Exception
    {
        // Given
        String[] args = {"--option", "value1", "value2"};
        Options options = new Options();
        options.addOption("option", true, "option A");

        Parser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, false);

        // Then
        String[] values = commandLine.getOptionValues("option");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
        Assert.assertArrayEquals(new String[]{}, commandLine.getArgs());
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        // Given
        String[] args = {"--prop", "key=value"};
        Options options = new Options();
        options.addOptionGroup(new PropertySettingOptionGroup("prop"));

        Parser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, null, false);

        // Then
        Properties properties = commandLine.getParsedProperties();
        Assert.assertEquals("value", properties.getProperty("key"));
        Assert.assertArrayEquals(new String[]{}, commandLine.getArgs());
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test
    public void testParseWithTrailingArgs() throws Exception
    {
        // Given
        String[] args = {"-a", "1", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "option A");

        Parser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, false);

        // Then
        Assert.assertEquals("1", commandLine.getOptionValue("a"));
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test
    public void testParseWithNonOptionArg() throws Exception
    {
        // Given
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        Parser parser = new DefaultParser();

        // When & Then
        Assert.assertThrows(ParseException.class, () -> parser.parse(options, args, false));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        // Given
        String[] args = {"arg1", "--", "arg3", "arg4"};
        Options options = new Options();

        Parser parser = new DefaultParser();

        // When
        CommandLine commandLine = parser.parse(options, args, true);

        // Then
        Assert.assertArrayEquals(new String[]{"arg1"}, commandLine.getArgs());
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test
    public void testParseWithUnknownOption() throws Exception
    {
        // Given
        String[] args = {"--unknown-option"};
        Options options = new Options();

        Parser parser = new DefaultParser();

        // When & Then
        Assert.assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, false));
    }
}