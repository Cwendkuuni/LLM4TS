package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", false, "b option");
        options.addOption("c", 1, "c option");
        options.addOption("d", "default", "d option");
        options.addOptionGroup(new OptionGroup("g", "group", "group options"));
        options.addOptionGroup(new OptionGroup("h", "help", "help options"));
        options.addOption("i", null, "i option", "i value");
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParseEmpty() throws ParseException {
        CommandLine cmd = parser.parse(new String[0][], null, false);
        assertNull(cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-a"}, null, false);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.getArgs().length > 0);
    }

    @Test
    public void testParseSingleArgument() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-i", "value"}, null, false);
        assertTrue(cmd.hasOption("i"));
        assertEquals("value", cmd.getOptionValue("i"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertArrayEquals(new String[]{"value"}, cmd.getArgs());
    }

    @Test
    public void testParseMultipleOptionsAndArguments() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-a", "-b", "--group", "-h", "-i", "value", "--d"}, null, false);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertSame(options.getOptionGroup("group"), cmd.getOptionGroup("g"));
        assertSame(options.getOptionGroup("help"), cmd.getOptionGroup("h"));
        assertSame(options.findOption("i"), cmd.getOption("i"));
        assertEquals("value", cmd.getOptionValue("i"));
        assertArrayEquals(new String[]{"--d"}, cmd.getArgs());
    }

    @Test
    public void testParseOptionsWithValues() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-a", "true", "-c", "value"}, null, false);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("d"));
        assertEquals(1, cmd.getOption("c").getValues().length);
        assertEquals("value", cmd.getOptionValue("c"));
        assertNull(cmd.getOptionValue("a"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParseWithFalseStopAtNonOption() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-x", "--"}, null, false);
        assertTrue(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertArrayEquals(new String[]{"--"}, cmd.getArgs());
    }

    @Test
    public void testParseWithTrueStopAtNonOption() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-x", "--"}, null, true);
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertArrayEquals(new String[]{"--"}, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-x"}, null, false);
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertArrayEquals(new String[0], cmd.getArgs());
        parser.parse(new String[]{"--"}, null, false);
        assertThrows(ParseException.class, () -> parser.parse(new String[]{"-y"}, null, false));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-i"}, null, false);
        assertTrue(cmd.hasOption("i"));
        assertArrayEquals(new String[0], cmd.getArgs());
        assertThrows(MissingArgumentException.class, () -> parser.parse(new String[]{"-i", "--"}, null, false));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        getRequiredOptions().add(new Option("r", true, "required option"));
        CommandLine cmd = parser.parse(new String[0][], null, false);
        assertThrows(MissingOptionException.class, () -> parser.parse(new String[0][], null, false));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("c", "value2");
        CommandLine cmd = parser.parse(new String[0][], properties, false);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.getOption("a").getValues()[0].equals("value1"));
        assertTrue(cmd.getOption("c").getValues()[0].equals("value2"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }
}