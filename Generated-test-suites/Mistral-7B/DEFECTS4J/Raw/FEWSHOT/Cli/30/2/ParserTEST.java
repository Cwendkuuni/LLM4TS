package org.apache.commons.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithNoOptions() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertArrayEquals(new String[0], cmd.getArgs());
        assertFalse(cmd.hasOption("help"));
        assertFalse(cmd.hasOption("foo"));
    }

    @Test
    public void testParseWithSingleValueOption() throws Exception {
        Options options = new Options();
        Option option = new Option("o", "option", true, "default");
        options.addOption(option);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "value"});
        assertEquals("value", cmd.getOptionValue("option"));
        assertTrue(cmd.hasOption("option"));
        assertFalse(cmd.hasOption("foo"));
    }

    @Test
    public void testParseWithMultipleValueOption() throws Exception {
        Options options = new Options();
        Option option = new Option("o", "option", true, "default");
        option.setArgName("NAME");
        options.addOption(option);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "VALUE1", "-o", "VALUE2"});
        String[] values = cmd.getOptionValues("option");
        assertArrayEquals(new String[]{"VALUE1", "VALUE2"}, values);
        assertTrue(cmd.hasOption("option"));
        assertFalse(cmd.hasOption("foo"));
    }

    @Test
    public void testParseWithNoValueOption() throws Exception {
        Options options = new Options();
        Option option = new Option("o", "option", false);
        options.addOption(option);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o"});
        assertTrue(cmd.hasOption("option"));
        assertNull(cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithLongOption() throws Exception {
        Options options = new Options();
        Option option = new Option("long-option", "long option", false);
        options.addOption(option);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--long-option"});
        assertTrue(cmd.hasOption("long-option"));
        assertNull(cmd.getOptionValue("long-option"));
    }

    @Test
    public void testParseWithOptionGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        Option option1 = new Option("o1", "Option 1", true, "value1", group);
        Option option2 = new Option("o2", "Option 2", true, "value2", group);
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-g", "-o1", "value1", "-o2", "value2"});
        assertSame(group, cmd.getOptionGroup("group"));
        String[] values = cmd.getOptionValues("option1");
        assertArrayEquals(new String[]{"value1"}, values);
        String[] values2 = cmd.getOptionValues("option2");
        assertArrayEquals(new String[]{"value2"}, values2);
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertFalse(cmd.hasOption("foo"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws Exception {
        Options options = new Options();
        Option option = new Option("o", "option", true, "default");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-o"});
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-foo"});
    }

    @Test(expected = ParseException.class)
    public void testParseWithMixedOptionsAndArguments() throws Exception {
        Options options = new Options();
        Option option = new Option("o", "option", true, "default");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-o", "value", "-foo"});
    }

    @Test(expected = ParseException.class)
    public void testParseWithNoOptionsOrArguments() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithEmptyOptions() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithEmptyArguments() throws Exception {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws Exception {
        Options options = new Options();
        Option option = new Option("o", "option", true, "default");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOptionAndArgument() throws Exception {
        Options options = new Options();
        Option option = new Option("o", "option", true, "default");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-foo"});
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOptionAndEmptyArguments() throws Exception {
        Options options = new Options();
        Option option = new Option("o", "option", true, "default");
        options.addOption(option);
        Parser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }
}