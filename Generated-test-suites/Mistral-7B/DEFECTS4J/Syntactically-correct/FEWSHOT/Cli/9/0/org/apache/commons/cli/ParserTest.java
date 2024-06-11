package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "a", true, "An option requiring an argument");
        options.addOption("b", "b", false, "An optional option");

        String[] arguments = {"-a", "arg1", "--b"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertFalse(cmd.getArgs()[1].isEmpty());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));

        Option optionA = cmd.getOption("a");
        assertEquals("arg1", optionA.getValue());

        Option optionB = cmd.getOption("b");
        assertNull(optionB.getValue());
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "a", true, "An option requiring an argument");

        String[] arguments = {"-b"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        fail("Expected ParseException to be thrown");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "a", false, "An option");

        String[] arguments = {"-c"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        fail("Expected UnrecognizedOptionException to be thrown");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", "a", true, "An option requiring an argument");

        Properties properties = new Properties();
        properties.put("a", "value");

        String[] arguments = {};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertEquals(1, cmd.getArgs().length);
        assertEquals("value", cmd.getArgs()[0]);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }
}