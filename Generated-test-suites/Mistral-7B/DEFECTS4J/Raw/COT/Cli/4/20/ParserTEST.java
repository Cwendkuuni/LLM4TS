package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParserAbstractClass() {
        assertNotNull(Parser.class);
    }

    // Test methods for parse(Options, String[])

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o"});
        assertTrue(cmd.hasOption("option"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--test"});
        fail("ParseException should be thrown");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--unknown"});
        fail("UnrecognizedOptionException should be thrown");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{});
        fail("MissingOptionException should be thrown");
    }

    @Test
    public void testParseWithOptionalArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "Test option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "value"});
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testParseWithMultipleOptionalArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "Test option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "value1", "-o", "value2"});
        assertTrue(cmd.hasOption("option"));
        assertSize(2, cmd.getOptionValues("option"));
        assertEquals("value1", cmd.getOptionValues("option")[0]);
        assertEquals("value2", cmd.getOptionValues("option")[1]);
    }

    // Test methods for parse(Options, String[], Properties)

    @Test
    public void testParseWithProperties_Empty() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], new Properties());
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithProperties_ValidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        CommandLine cmd = parser.parse(options, new String[]{"-o"}, properties);
        assertTrue(cmd.hasOption("option"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithProperties_InvalidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("unknown", "value");
        CommandLine cmd = parser.parse(options, new String[]{"-o"}, properties);
        fail("ParseException should be thrown");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithProperties_UnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("unknown", "value");
        CommandLine cmd = parser.parse(options, new String[]{"--unknown"}, properties);
        fail("UnrecognizedOptionException should be thrown");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithProperties_MissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        Properties properties = new Properties();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        fail("MissingOptionException should be thrown");
    }

    // Test methods for parse(Options, String[], Properties, boolean)

    @Test
    public void testParseWithProperties_StopAtNonOption_Empty() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], new Properties(), true);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithProperties_StopAtNonOption_ValidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        CommandLineParser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("option", "value");
        CommandLine cmd = parser.parse(options, new String[]{"--option", "value2"}, properties, true);
        assertTrue(cmd.hasOption("option"));
        assertEquals("value", cmd.getOptionValue("option"));
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals("value2", cmd.getArgs()[0]);
    }

    // Test methods for flatten(Options, String[], boolean)

    @Test
    public void testFlatten_Empty() {
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];
        String[] expected = new String[0];
        String[] result = parser.flatten(new Options(), arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ValidArguments() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        String[] arguments = {"--option", "value", "-o", "value2"};
        String[] expected = {"--option", "-o", "value", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_ValidArguments() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Test option");
        String[] arguments = {"--option", "value", "-a", "value2"};
        String[] expected = {"--option", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
}