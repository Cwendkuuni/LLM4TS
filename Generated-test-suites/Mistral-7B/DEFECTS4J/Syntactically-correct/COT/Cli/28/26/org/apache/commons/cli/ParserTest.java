package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testSetAndGetOptions()
    {
        Options options1 = new Options();

        parser.setOptions(options1);
        assertNull(parser.getOptions());

        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions()
    {
        List<Option> requiredOptions1 = new ArrayList<>();
        List<Option> requiredOptions2 = Arrays.asList(new Option("a", "a", true, "a help message"));

        parser.setOptions(new Options());
        parser.setRequiredOptions(requiredOptions1);
        assertSame(requiredOptions1, parser.getRequiredOptions());

        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions2);
        assertSame(requiredOptions2, parser.getRequiredOptions());
    }

    @Test
    public void testFlatten() throws ParseException
    {
        String[] arguments = {"arg1", "--option=value", "-o", "arg3", "-a", "arg4"};
        String[] expected = {"arg1", "--option", "value", "-o", "arg3", "-a"};

        List<Option> options = new ArrayList<>();
        options.add(new Option("o", null, true, "An option that takes an argument"));
        options.add(new Option("a", null, true, "An option that takes an argument"));

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testParseWithNoArguments() throws ParseException
    {
        String[] arguments = {};
        CommandLine expected = new CommandLine();
        CommandLine result = parser.parse(options, arguments, null, false);

        assertArrayEquals(expected.getArgs(), result.getArgs());
        assertSame(expected.getOptions(), result.getOptions());
    }

    @Test
    public void testParseWithValidArguments() throws ParseException
    {
        String[] arguments = {"arg1", "--option=value", "-o", "arg3", "-a", "arg4"};
        Option optionA = new Option("a", "a", true, "An option that takes an argument");
        Option optionO = new Option("o", "o", true, "An option that takes an argument");

        Option option1 = new Option("i", null, false, "An option without value");
        Option option2 = new Option("h", "h", false, "An option with shortcut");
        Option option3 = new Option("l", null, false, "An option without value or shortcut");

        options.addAll(Arrays.asList(option1, option2, option3, optionA, optionO));

        CommandLine expected = new CommandLine();
        expected.addOption(option1);
        expected.addOption(option2);
        expected.addOption(option3);
        expected.addOption(optionA.setValue("arg4"));
        expected.addOption(optionO.setValue("arg3"));

        CommandLine result = parser.parse(options, arguments, null, false);
        assertArrayEquals(expected.getArgs(), result.getArgs());
        assertSame(expected.getOptions(), result.getOptions());
    }

    @Test
    public void testParseWithInvalidArguments() throws ParseException
    {
        String[] arguments = {"arg1", "--invalid", "-o", "arg3", "-a", "arg4"};
        Option optionA = new Option("a", "a", true, "An option that takes an argument");

        Option option1 = new Option("i", null, false, "An option without value");
        Option option2 = new Option("h", "h", false, "An option with shortcut");
        Option option3 = new Option("l", null, false, "An option without value or shortcut");

        options.addAll(Arrays.asList(option1, option2, option3, optionA));
        options.addOption("invalid");

        assertThrows(ParseException.class, () -> parser.parse(options, arguments, null, false));
    }

    @Test
    public void testParseWithMissingArguments() throws ParseException
    {
        String[] arguments = {"arg1", "--option=value", "-o", "arg3"};
        Option optionA = new Option("a", "a", true, "An option that takes an argument");

        Option option1 = new Option("i", null, false, "An option without value");
        Option option2 = new Option("h", "h", false, "An option with shortcut");
        Option option3 = new Option("l", null, false, "An option without value or shortcut");

        options.addAll(Arrays.asList(option1, option2, option3, optionA));

        CommandLine expected = new CommandLine();
        expected.addOption(option1);
        expected.addOption(option2);
        expected.addOption(option3);
        expected.addOption(optionA);

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments, null, false));
        CommandLine result = parser.parse(options, arguments, null, true);
        assertArrayEquals(expected.getArgs(), result.getArgs());
        assertSame(expected.getOptions(), result.getOptions());
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] arguments = {"--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Option option1 = new Option("p", "p", true, "An option that takes a property");

        options.addOption(option1);

        CommandLine expected = new CommandLine();
        expected.addOption(option1.setValue("value1"));

        CommandLine result = parser.parse(options, arguments, properties, false);
        assertArrayEquals(expected.getArgs(), result.getArgs());
        assertSame(expected.getOptions(), result.getOptions());
    }

    @Test
    public void testParseWithInvalidShortcutOption() throws ParseException
    {
        String[] arguments = {"--h", "--invalid"};
        Option optionH = new Option("h", "h", false, "An option with shortcut");

        options.addOption(optionH);

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments, null, false));
    }

    @Test
    public void testParseWithInvalidLongOption() throws ParseException
    {
        String[] arguments = {"--invalid"};
        Option option = new Option("a", "a", false, "An option with long name");

        options.addOption(option);

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments, null, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullOptions() throws ParseException
    {
        parser.parse(null, new String[0], null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullArguments() throws ParseException
    {
        parser.parse(options, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullProperties() throws ParseException
    {
        parser.parse(options, new String[0], (Properties) null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyOptions() throws ParseException
    {
        parser.parse(new Options(), new String[0], null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyArguments() throws ParseException
    {
        parser.parse(options, new String[0], null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyProperties() throws ParseException
    {
        parser.parse(options, new String[0], new Properties(), false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionGroup() throws ParseException
    {
        OptionGroup group = new OptionGroup();
        group.setName("group");

        Option option = new Option("o", "o", false, "An option in a group");
        group.addOption(option);

        options.addOptionGroup(group);

        String[] arguments = {"--group"};
        CommandLine result = parser.parse(options, arguments, null, false);

        assertFalse(result.hasOption("o"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        Option option = new Option("r", "r", true, "A required option");
        options.addOption(option);

        String[] arguments = {"arg1", "arg2"};

        parser.parse(options, arguments, null, false);

        Throwable throwable = assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments, null, false));
        assertEquals("Option 'r' is missing", throwable.getMessage());
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseWithInvalidOption() throws ParseException
    {
        Option option = new Option("w", "w", false, "An invalid option");
        options.addOption(option);

        String[] arguments = {"arg1", "--w"};

        parser.parse(options, arguments, null, false);

        Throwable throwable = assertThrows(NoSuchOptionException.class, () -> parser.parse(options, arguments, null, false));
        assertEquals("Invalid option: w", throwable.getMessage());
    }
}