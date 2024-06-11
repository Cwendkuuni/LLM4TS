package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new ParserImpl();
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        Options options = new Options();
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertEquals(0, commandLine.getOptions().length);
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testParseWithOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        String[] arguments = {"-a"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-c"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("required", true, "Required option");
        String[] arguments = {"-required", "value"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("required"));
        assertEquals("value", commandLine.getOptionValue("required"));
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("required", true, "Required option");
        String[] arguments = {};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String[] arguments = {"-a", "-b", "value", "-c"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertEquals("value", commandLine.getOptionValue("b"));
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a", "value", "arg1", "arg2"};
        CommandLine commandLine = parser.parse(options, arguments, true);
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("value"));
        assertEquals("arg1", commandLine.getArgs()[0]);
        assertEquals("arg2", commandLine.getArgs()[1]);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String[] arguments = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        properties.setProperty("c", "true");
        CommandLine commandLine = parser.parse(options, arguments, properties);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertEquals("value", commandLine.getOptionValue("b"));
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testParseWithDoubleDash() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        String[] arguments = {"-a", "--", "-b"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertEquals(1, commandLine.getArgs().length);
        assertEquals("-b", commandLine.getArgs()[0]);
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}