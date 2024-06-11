package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        String[] arguments = new String[] { "arg" };
        CommandLine commandLine = parser.parse(options, arguments);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("arg", commandLine.getArgs().get(0));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        String[] arguments = new String[] { "-a" };
        options.addOption("a", false, null, "Option A");
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testParseWithSingleOptionalArgOption() throws ParseException {
        String[] arguments = new String[] { "-a", "value" };
        options.addOption("a", true, null, "Option A with value");
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        String[] arguments = new String[] { "-a", "-b" };
        options.addOption("a", false, null, "Option A");
        options.addOption("b", false, null, "Option B");
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
    }

    @Test
    public void testParseWithMultipleOptionalArgOptions() throws ParseException {
        String[] arguments = new String[] { "-a", "value1", "-b", "value2" };
        options.addOption("a", true, null, "Option A with value");
        options.addOption("b", true, null, "Option B with value");
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertEquals("value1", commandLine.getOptionValue("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("value2", commandLine.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnknownOption() throws ParseException {
        String[] arguments = new String[] { "-x" };
        options.addOption("a", false, null, "Option A");
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedArgument() throws ParseException {
        String[] arguments = new String[] { "-a", "arg" };
        options.addOption("a", false, null, "Option A");
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] arguments = new String[0];
        options.addOption("a", true, null, "Option A");
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = new String[0];
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        CommandLine commandLine = parser.parse(options, arguments, properties);
        assertTrue(commandLine.hasOption("a"));
        assertEquals("value1", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParseWithMixedArgumentsAndOptions() throws ParseException {
        String[] arguments = new String[] { "-a", "-b", "arg1", "arg2" };
        options.addOption("a", false, null, "Option A");
        options.addOption("b", false, null, "Option B");
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }

    @Test
    public void testParseWithMixedArgumentsAndOptionsWithRequiredOptions() throws ParseException {
        String[] arguments = new String[] { "arg1", "-a", "arg2" };
        options.addOption("a", true, null, "Option A");
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException {
        String[] arguments = new String[0];
        options.addOption("a", true, null, "Option A");
        CommandLine commandLine = parser.parse(options, arguments, null, true);
    }

    @Test
    public void testParseWithOptionsGroup() throws ParseException {
        String[] arguments = new String[] { "-g", "-x", "arg" };
        OptionGroup group = new OptionGroup("Group", "Options group");
        group.addOption(new Option("x", true, "xval", "Option X"));
        group.addOption(new Option("y", true, "yval", "Option Y"));
        options.addOptionGroup(group);
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("x"));
        assertTrue(commandLine.hasOption("y"));
        assertNull(commandLine.getGroup());
    }

    @Test
    public void testParseWithMixedOptionsGroupsAndArguments() throws ParseException {
        String[] arguments = new String[] { "-g", "-x", "-a" };
        OptionGroup group = new OptionGroup("Group", "Options group");
        group.addOption(new Option("x", true, "xval", "Option X"));
        group.addOption(new Option("y", true, "yval", "Option Y"));
        options.addOptionGroup(group);
        options.addOption("a", false, null, "Option A");
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("x"));
        assertTrue(commandLine.hasOption("y"));
        assertTrue(commandLine.hasOption("a"));
        assertEquals(group, commandLine.getGroup());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithEmptyOptions() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(Collections.emptyList(), new String[0]);
    }
}