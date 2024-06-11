package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];
        CommandLine commandLine = parser.parse(options, arguments);
        assertArrayEquals(new String[0], commandLine.getArgs());
        assertNull(commandLine.getOptionValues("help"));
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-o", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertArrayEquals(new String[]{"value"}, commandLine.getArgs());
        assertArrayEquals(new String[]{"value"}, commandLine.getOptionValues("option"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o1", "option1", true, "An option with a value 1");
        options.addOption("o2", "option2", true, "An option with a value 2");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-o1", "value1", "-o2", "value2"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertArrayEquals(new String[0], commandLine.getArgs());
        assertArrayEquals(new String[]{"value1"}, commandLine.getOptionValues("option1"));
        assertArrayEquals(new String[]{"value2"}, commandLine.getOptionValues("option2"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-x"};
        CommandLine commandLine = parser.parse(options, arguments);
        fail("ParseException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseMissingValue() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-o"};
        CommandLine commandLine = parser.parse(options, arguments);
        fail("ParseException expected");
    }

    @Test
    public void testParseWithArg() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"arg"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertArrayEquals(new String[]{"arg"}, commandLine.getArgs());
        assertNull(commandLine.getOptionValues("option"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");
        options.addRequiredOption("r", "required", false, "A required option");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-o", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertArrayEquals(new String[]{"value"}, commandLine.getArgs());
        assertArrayEquals(new String[0], commandLine.getOptionValues("required"));
        assertFalse(commandLine.hasOption("required"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with a value");
        options.addRequiredOption("r", "required", false, "A required option");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-o", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        fail("MissingOptionException expected");
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(false);
        options.addOptionGroup(group);
        options.addOption("o", "option", true, "An option with a value");
        options.addOption("g", "group", false, "A group option");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-g", "--group", "-o", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("group"));
        assertFalse(commandLine.hasOption("o"));
        assertSame(group, commandLine.findOption("group"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParseWithRequiredGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        options.addOptionGroup(group);
        options.addOption("o", "option", true, "An option with a value");
        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"-o", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        fail("MissingOptionException expected");
    }
}