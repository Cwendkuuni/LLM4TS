package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "opt", false, "An option");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o"});
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("opt"));
    }

    @Test
    public void testParseSingleValueOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "opt", true, "An option with a value");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "value"});
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("opt"));
        assertArrayEquals(new String[]{"value"}, cmd.getOptionValues("opt"));
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o1", "opt1", false, "Option 1");
        options.addOption("o2", "opt2", true, "Option 2 with a value");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o1", "-o2", "value"});
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("opt1"));
        assertFalse(cmd.hasOption("opt2"));
        assertNull(cmd.getOptionValues("opt1"));
        assertArrayEquals(new String[]{"value"}, cmd.getOptionValues("opt2"));
    }

    @Test
    public void testParseMultipleValuesOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "opt", true, "An option with multiple values");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-o", "value1", "value2"});
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("opt"));
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("opt"));
    }

    @Test
    public void testParseSingleArg() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1"});
        assertNotNull(cmd);
        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
    }

    @Test
    public void testParseMultipleArgs() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
        assertNotNull(cmd);
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithHelpOption() throws ParseException {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--help"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"--invalid"}));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "opt", true, "An option");
        options.addRequiredOption("r", "req", true, "A required option");
        Parser parser = new DefaultParser();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
    }

    @Test
    public void testParseWithMissingRequiredOptionValue() throws ParseException {
        Options options = new Options();
        options.addOption("o", "opt", true, "An option");
        options.addRequiredOption("r", "req", true, "A required option");
        Parser parser = new DefaultParser();
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[]{"-r"}));
    }

    @Test
    public void testParseWithExtraArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2", "arg3"});
        assertNotNull(cmd);
        assertEquals(3, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
        assertEquals("arg3", cmd.getArgs()[2]);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("opt1", "value1");
        properties.setProperty("opt2", "value2");
        options.addOption("o1", "opt1", false, "Option 1 from properties");
        options.addOption("o2", "opt2", false, "Option 2 from properties");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertArrayEquals(new String[]{"value1"}, cmd.getOptionValues("opt1"));
        assertArrayEquals(new String[]{"value2"}, cmd.getOptionValues("opt2"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.addOption("g1", "opt1", false, "Option 1 in group");
        group.addOption("g2", "opt2", false, "Option 2 in group");
        options.addOption("o", "opt", false, "Option not in group");
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--group", "-g1"});
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("opt1"));
        assertFalse(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption("opt2"));
    }
}