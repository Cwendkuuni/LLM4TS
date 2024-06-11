package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().length);
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        String[] arguments = {"arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertArrayEquals(new String[]{"arg"}, cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        options.addOption("opt", true, "Opition with no argument");
        String[] arguments = {"-opt"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption("arg"));
    }

    @Test
    public void testParseWithOptionWithArgument() throws ParseException {
        options.addOption("opt", true, "Option with no argument");
        options.addOption("arg", true, "Option with argument");
        String[] arguments = {"-opt", "arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("arg"));
        assertEquals("arg", cmd.getOptionValue("arg"));
        assertNull(cmd.getOptionValue("opt"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        options.addOption("opt", true, "Option with required argument");
        String[] arguments = {"-opt"};
        CommandLine cmd = parser.parse(options, arguments);
        throw new AssertionError("Parsing should fail with missing argument");
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        options.addOption("opt1", true, "Option1 with no argument");
        options.addOption("opt2", true, "Option2 with no argument");
        String[] arguments = {"-opt1", "-opt2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        options.addOption("opt1", true, "Option1 with no argument");
        options.addOption("opt2", true, "Option2 with argument.");
        String[] arguments = {"-opt1", "arg", "-opt2", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertArrayEquals(new String[]{"arg", "arg2"}, cmd.getArgs());
        assertTrue(cmd.hasOption("opt1"));
        assertFalse(cmd.hasOption("arg"));
        assertTrue(cmd.hasOption("opt2"));
        assertEquals("arg2", cmd.getOptionValue("arg2"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        options.addOption("long-opt", true, "Long option with no argument");
        String[] arguments = {"--long-opt"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("long-opt"));
        assertFalse(cmd.hasOption("arg"));
    }

    @Test
    public void testParseWithLongOptionWithArgument() throws ParseException {
        options.addOption("long-opt", true, "Long option with argument");
        String[] arguments = {"--long-opt=arg"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("long-opt"));
        assertTrue(cmd.hasOption("arg"));
        assertEquals("arg", cmd.getOptionValue("arg"));
        assertNull(cmd.getOptionValue("long-opt"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] arguments = {"--unknown-opt"};
        CommandLine cmd = parser.parse(options, arguments);
        throw new AssertionError("Parsing should fail with unrecognized option");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption("opt", true, "Option with no argument");
        Properties properties = new Properties();
        properties.setProperty("opt", "arg");
        CommandLine cmd = parser.parse(options, null, properties);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("opt"));
        assertEquals("arg", cmd.getOptionValue("opt"));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        options.addOption("opt", true, "Option with required argument");
        String[] arguments = {"--opt"};
        CommandLine cmd = parser.parse(options, arguments);
        throw new AssertionError("Parsing should fail with missing required option");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        options.addOption("opt", true, "Option with required argument");
        List required = Collections.singletonList("opt");
        CommandLine cmd = parser.parse(options, new String[0], required);
        throw new AssertionError("Parsing should fail with missing required option");
    }
}