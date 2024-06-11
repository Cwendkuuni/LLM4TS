package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertNotNull(commandLine);
        assertTrue(commandLine.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSimpleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", 2, "B value");
        String[] arguments = {"-a", "--b=2"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals(2, commandLine.getOptionValue("b").intValue());
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParseWithSimpleAndComplexOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", 2, "B value");
        options.addOptionGroup(new OptionGroup("group", "Group"));
        options.addOption("c", options.getOptionGroup("group"), true, "C value");
        options.addOption("d", options.getOptionGroup("group"), true, "D value");
        String[] arguments = {"--a", "--b=3", "--group-c", "--group-d"};
        CommandLine commandLine = parser.parse(options, arguments);
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals(3, commandLine.getOptionValue("b").intValue());
        assertTrue(commandLine.getOptionValue("c") != null);
        assertTrue(commandLine.getOptionValue("d") != null);
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = Arrays.asList("a", "b");
        options.setRequiredOptions(requiredOptions);
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");
        String[] arguments = {"-a"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A value");
        String[] arguments = {"--x"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A value");
        String[] arguments = {"--z"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", 2, "B value");
        Properties properties = new Properties();
        properties.put("a", "A1");
        properties.put("b", "3");
        CommandLine commandLine = parser.parse(options, new String[0], properties);
        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertEquals("A1", commandLine.getOptionValue("a"));
        assertEquals(3, commandLine.getOptionValue("b").intValue());
        assertArrayEquals(new String[0], commandLine.getArgs());
    }
}