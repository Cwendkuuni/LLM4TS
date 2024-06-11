package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "a", false, "A required option");
        options.addRequiredOption("b", "b", false, "Another required option");

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "--a", "--b"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testParseOptionalOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "o", true, "An optional option");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "--o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertFalse(cmd.hasOption("n"));
        assertNull(cmd.getOptionValue("n"));
    }

    @Test
    public void testParseLongOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("long-option", "lo", true, "A long option");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "--long-option", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.hasOption("long-option"));
        assertEquals("value", cmd.getOptionValue("long-option"));
        assertFalse(cmd.hasOption("l"));
        assertNull(cmd.getOptionValue("l"));
    }

    @Test
    public void testParseShortOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option('o', "o", true, "A short option");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertFalse(cmd.hasOption("n"));
        assertNull(cmd.getOptionValue("n"));
    }

    @Test
    public void testParseShortOptionsWithArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option('o', "o", true, "An option with an argument");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "-o", "arg"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertTrue(cmd.hasOption("o"));
        assertEquals("arg", cmd.getOptionValue("o"));
        assertFalse(cmd.hasOption("n"));
        assertNull(cmd.getOptionValue("n"));
    }

    @Test
    public void testParseShortOptionsWithMultipleArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option('o', "o", true, "An option with multiple arguments");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "-o", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(3, cmd.getNumArgs());
        assertTrue(cmd.hasOption("o"));
        String[] values = cmd.getOptionValues("o");
        assertEquals(2, values.length);
        assertEquals("arg1", values[0]);
        assertEquals("arg2", values[1]);
        assertFalse(cmd.hasOption("n"));
        assertNull(cmd.getOptionValue("n"));
    }

    @Test
    public void testParseMixedOptions() throws ParseException {
        Options options = new Options();
        Option longOption = new Option("long-option", "lo", true, "A long option");
        Option shortOption = new Option('o', "o", true, "A short option");
        options.addOption(longOption);
        options.addOption(shortOption);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "--long-option", "value", "-o", "otherValue"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(4, cmd.getNumArgs());
        assertTrue(cmd.hasOption("long-option"));
        assertEquals("value", cmd.getOptionValue("long-option"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("otherValue", cmd.getOptionValue("o"));
        assertFalse(cmd.hasOption("n"));
        assertNull(cmd.getOptionValue("n"));
    }

    @Test
    public void testParseOptionsWithProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "o", true, "An option with a property value");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("o", "propertyValue");

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test"};
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertEquals(1, cmd.getNumArgs());
        assertTrue(cmd.hasOption("o"));
        assertEquals("propertyValue", cmd.getOptionValue("o"));
        assertFalse(cmd.hasOption("n"));
        assertNull(cmd.getOptionValue("n"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "a", false, "A required option");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getNumArgs());
        assertFalse(cmd.hasOption("a"));
        throw new AssertionError("Expected MissingOptionException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "o", true, "An option");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "--x", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getNumArgs());
        assertFalse(cmd.hasOption("x"));
        throw new AssertionError("Expected UnrecognizedOptionException");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "o", true, "An option with a required argument");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] arguments = new String[]{"test", "-o"};
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getNumArgs());
        assertTrue(cmd.hasOption("o"));
        assertNull(cmd.getOptionValue("o"));
        throw new AssertionError("Expected MissingArgumentException");
    }
}