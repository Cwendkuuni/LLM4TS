package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithNoOptionsOrArguments() throws ParseException {
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[0];

        CommandLine result = parser.parse(options, arguments);

        assertNull(result.getArgs());
        assertTrue(result.getOptions().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "an option");
        option.setRequired(true);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] { "-o", "value" };

        CommandLine result = parser.parse(options, arguments);

        assertEquals(Arrays.asList("value"), result.getArgs());
        assertEquals(Collections.singletonList(option), result.getOptions());
    }

    @Test
    public void testParseWithArgumentsOnly() throws ParseException {
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] { "arg1", "arg2" };

        CommandLine result = parser.parse(options, arguments);

        assertArrayEquals(new String[] { "arg1", "arg2" }, result.getArgs().toArray());
        assertTrue(result.getOptions().isEmpty());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "an option");
        option.setRequired(true);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[0];

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithOptionalOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "an option");
        option.setRequired(false);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] { "-o" };

        CommandLine result = parser.parse(options, arguments);

        assertNull(result.getArgs());
        assertEquals(Collections.singletonList(option), result.getOptions());
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "a group");
        group.setRequired(false);
        Option option = new Option("o", "option", true, "an option");
        option.setRequired(false);
        option.setOptionalArg(true);
        option.setLongOpt("long-option");
        group.addOption(option);
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] { "--group", "value" };

        CommandLine result = parser.parse(options, arguments);

        assertEquals(Arrays.asList("value"), result.getArgs());
        assertEquals(Collections.singletonList(group), result.getOptions());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        String[] arguments = new String[] { "--unknown" };

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "an option");
        option.setRequired(true);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("o", "value");

        CommandLine result = parser.parse(options, new String[0], properties);

        assertEquals(Arrays.asList("value"), result.getArgs());
        assertEquals(Collections.singletonList(option), result.getOptions());
    }
}