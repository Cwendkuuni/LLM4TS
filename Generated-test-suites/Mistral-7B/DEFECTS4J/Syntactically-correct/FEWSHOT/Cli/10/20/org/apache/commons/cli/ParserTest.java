package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(arguments);

        assertTrue(cmd.getArgs().isEmpty());
        assertFalse(cmd.getOptions().isEmpty());

        Options parsedOptions = cmd.getOptions();
        assertSame(options, parsedOptions);
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"arg"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[]{"arg"}, cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-o"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testParseWithSingleOptionValue() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-o", "value"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
        assertEquals("value", cmd.getOptionValue("opt"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Option 1 description");
        Option option2 = new Option("o2", "opt2", false, "Option 2 description");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-o1", "-o2"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
    }

    @Test
    public void testParseWithMultipleOptionsValues() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-o", "value1", "-o", "value2"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
        String[] values = cmd.getOptionValues("opt");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        Option longOption = new Option("long-option", "long opt", false, "A long option");
        options.addOption(longOption);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"--long-option"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("long opt"));
    }

    @Test
    public void testParseWithLongOptionsValues() throws ParseException {
        Options options = new Options();
        Option longOption = new Option("long-option", true, "A long option");
        options.addOption(longOption);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"--long-option", "value"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("long opt"));
        assertEquals("value", cmd.getOptionValue("long opt"));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-o", "value1", "arg1", "-o", "value2"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[]{"arg1"}, cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
        String[] values = cmd.getOptionValues("opt");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnknownOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-x"};
        CommandLine cmd = parser.parse(arguments);

        // The test should fail with an IllegalArgumentException
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"arg1", "arg2"};
        CommandLine cmd = parser.parse(arguments);

        // The test should fail with an IllegalArgumentException
        fail();
    }

    @Test
    public void testParseWithEmptyRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));
    }

    @Test
    public void testParseWithSingleRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"arg1"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
        assertEquals("arg1", cmd.getOptionValue("opt"));
    }

    @Test
    public void testParseWithMultipleRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Option 1 description");
        option1.setRequired(true);
        Option option2 = new Option("o2", "opt2", true, "Option 2 description");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"arg1", "arg2"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertEquals("arg1", cmd.getOptionValue("opt1"));
        assertEquals("arg2", cmd.getOptionValue("opt2"));
    }

    @Test
    public void testParseWithMissingOptionalOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"arg1", "arg2"};
        CommandLine cmd = parser.parse(arguments, true, true);

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertFalse(cmd.hasOption("opt"));
    }

    @Test
    public void testParseWithSingleOptionalOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"arg1", "-o", "value"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[]{"arg1"}, cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
        assertEquals("value", cmd.getOptionValue("opt"));
    }

    @Test
    public void testParseWithMultipleOptionalOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Option 1 description");
        Option option2 = new Option("o2", "opt2", true, "Option 2 description");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"arg1", "-o1", "value1", "-o2", "value2"};
        CommandLine cmd = parser.parse(arguments);

        assertArrayEquals(new String[]{"arg1"}, cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertEquals("value1", cmd.getOptionValue("opt1"));
        assertEquals("value2", cmd.getOptionValue("opt2"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("opt", "value");

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(arguments, properties);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
        assertEquals("value", cmd.getOptionValue("opt"));
    }

    @Test
    public void testParseWithLongProperties() throws ParseException {
        Options options = new Options();
        Option longOption = new Option("long-option", true, "A long option");
        options.addOption(longOption);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("long-option", "value");

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(arguments, properties);

        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("long-option"));
        assertEquals("value", cmd.getOptionValue("long-option"));
    }

    @Test
    public void testParseWithMixedPropertiesAndArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("opt", "value1");

        String[] arguments = new String[]{"arg1", "-o", "value2"};
        CommandLine cmd = parser.parse(arguments, properties);

        assertArrayEquals(new String[]{"arg1"}, cmd.getArgs());
        assertFalse(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("opt"));
        assertEquals("value2", cmd.getOptionValue("opt"));
        assertEquals("value1", cmd.getOptionValue("opt", "defaultValue"));
    }
}