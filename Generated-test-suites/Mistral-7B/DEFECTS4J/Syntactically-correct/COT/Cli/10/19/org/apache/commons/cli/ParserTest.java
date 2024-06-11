package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        Options options = new Options();
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testSetOptions() {
        assertNotNull(parser.getOptions());
        assertEquals(0, parser.getOptions().getRequiredOptions().size());
    }

    @Test
    public void testFlatten_NoArguments() throws ParseException {
        String[] arguments = new String[0];
        String[] expected = new String[0];
        String[] result = parser.flatten(parser.getOptions(), arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_Arguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "help option");
        options.addOption("b", 1, "an integer option");
        String[] arguments = {"a", "-b", "1"};
        String[] expected = {"a", "-b", "1"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testParse_NoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine commandLine = parser.parse(parser.getOptions(), arguments);
        assertNotNull(commandLine);
        assertTrue(commandLine.getArgs().isEmpty());
    }

    @Test
    public void testParse_Arguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "help option");
        options.addOption("b", 1, "an integer option");
        String[] arguments = {"--help", "-b", "1"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertNotNull(commandLine);
        assertEquals(2, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("help"));
        assertEquals("1", commandLine.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "help option");
        String[] arguments = {"--miss"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "help option");
        String[] arguments = {"--unknown"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "help option");
        options.addRequiredOption("b", false, "required option");
        String[] arguments = {"--help"};
        try {
            parser.parse(options, arguments);
        } catch (MissingOptionException e) {
            throw new ParseException("Expected MissingOptionException", e);
        }
    }

}