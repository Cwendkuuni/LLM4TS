package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNull(cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithEmptyOptionsAndArgumentsWithProperties()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertNull(cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testParseWithEmptyOptionsAndArgumentsAndStopAtNonOption()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, true);
        assertNull(cmd.getArgs());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--foo", "-b", "bar"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option option1 = optionsList.get(0);
        assertEquals("foo", option1.getKey());
        assertFalse(option1.hasArg());
        Option option2 = optionsList.get(1);
        assertEquals("b", option2.getKey());
        assertFalse(option2.hasArg());
        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("bar", args.get(0));
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndProperties()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--foo", "-b", "bar"};
        Properties properties = new Properties();
        properties.setProperty("--foo.longOpt", "Long option for foo");
        CommandLine cmd = parser.parse(options, arguments, properties);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option option1 = optionsList.get(0);
        assertEquals("foo", option1.getKey());
        assertEquals("Long option for foo", option1.getLongOpt());
        assertFalse(option1.hasArg());
        Option option2 = optionsList.get(1);
        assertEquals("b", option2.getKey());
        assertFalse(option2.hasArg());
        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("bar", args.get(0));
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndStopAtNonOption()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--foo", "-b", "bar", "nonOption"};
        CommandLine cmd = parser.parse(options, arguments, true);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option option1 = optionsList.get(0);
        assertEquals("foo", option1.getKey());
        assertFalse(option1.hasArg());
        Option option2 = optionsList.get(1);
        assertEquals("b", option2.getKey());
        assertFalse(option2.hasArg());
        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("bar", args.get(0));
        assertEquals("nonOption", args.get(1));
    }

    @Test
    public void testParseWithOptionsAndArgumentsWithOptionalArg()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--foo", "arg1", "arg2"};
        options.addOption("foo", true, "f", "Optional argument for foo");
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("foo", option.getKey());
        assertTrue(option.hasArg());
        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithOptionsAndArgumentsWithOptionalArgAndProperties()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--foo", "arg1", "arg2"};
        options.addOption("foo", true, "f", "Optional argument for foo");
        Properties properties = new Properties();
        properties.setProperty("--foo.longOpt", "Long option for foo");
        CommandLine cmd = parser.parse(options, arguments, properties);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("foo", option.getKey());
        assertEquals("Long option for foo", option.getLongOpt());
        assertTrue(option.hasArg());
        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndPropertiesWithMissingRequiredOption()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--bar"};
        options.addOption("foo", true, "f", "Optional argument for foo");
        options.addOption("bar", true, "b", "Optional argument for bar");
        options.addRequiredOption("baz", false, null, "Required option for baz");
        Properties properties = new Properties();
        try {
            CommandLine cmd = parser.parse(options, arguments, properties);
            fail();
        } catch (MissingOptionException e) {
            List<String> expectedMissingOptions = Arrays.asList("baz");
            List<String> actualMissingOptions = Arrays.asList(e.getMessage().split(", "));
            assertEquals(expectedMissingOptions, actualMissingOptions);
        }
    }

    @Test
    public void testParseWithOptionsAndArgumentsWithMultipleValues()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--foo", "arg1", "arg2", "arg3"};
        options.addOption("foo", true, "f", "Optional argument for foo");
        options.addOption("foo", true, null, "Multiple values for foo option");
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("foo", option.getKey());
        assertTrue(option.hasArg());
        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
        List<String> values = option.getValues();
        assertEquals(3, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("arg3", values.get(2));
    }

    @Test
    public void testParseWithOptionsAndArgumentsWithOptionGroup()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--group", "-x"};
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption(new Option("x", "x", false, "Option x"));
        group.addOption(new Option("y", "y", false, "Option y"));
        options.addOptionGroup(group);
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("x", option.getKey());
        assertFalse(option.hasArg());
        List<String> args = cmd.getArgs();
        assertEquals(0, args.size());
    }

    @Test
    public void testParseWithOptionsAndArgumentsWithOptionGroupAndRequiredOption()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--group", "-x"};
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(true);
        group.addOption(new Option("x", "x", false, "Option x"));
        group.addOption(new Option("y", "y", false, "Option y"));
        options.addOptionGroup(group);
        options.addRequiredOption("z", false, null, "Required option z");
        try {
            CommandLine cmd = parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            List<String> expectedMissingOptions = Arrays.asList("z");
            List<String> actualMissingOptions = Arrays.asList(e.getMessage().split(", "));
            assertEquals(expectedMissingOptions, actualMissingOptions);
        }
    }

    @Test
    public void testParseWithOptionsAndArgumentsWithOptionGroupAndRequiredOptionAndProperties()
            throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = {"--group", "-x"};
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(true);
        group.addOption(new Option("x", "x", false, "Option x"));
        group.addOption(new Option("y", "y", false, "Option y"));
        options.addOptionGroup(group);
        options.addRequiredOption("z", false, null, "Required option z");
        Properties properties = new Properties();
        properties.setProperty("--group.longOpt", "Long option for group");
        try {
            CommandLine cmd = parser.parse(options, arguments, properties);
            fail();
        } catch (MissingOptionException e) {
            List<String> expectedMissingOptions = Arrays.asList("z");
            List<String> actualMissingOptions = Arrays.asList(e.getMessage().split(", "));
            assertEquals(expectedMissingOptions, actualMissingOptions);
        }
    }
}