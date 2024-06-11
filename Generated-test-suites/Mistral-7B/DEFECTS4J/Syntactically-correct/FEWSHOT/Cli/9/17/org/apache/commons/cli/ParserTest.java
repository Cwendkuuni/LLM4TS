package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 3, "Option C with an integer argument");
        options.addOption("d", null, "Option D with an optional string argument");
        options.addOption("e", null, "Option E with no arguments");
        options.addOptionGroup(new OptionGroup("group1")
                .required(true)
                .addOption("g1", true, "Option G1 in group1")
                .addOption("g2", true, "Option G2 in group1"));
        options.addOptionGroup(new OptionGroup("group2")
                .addOption("g3", true, "Option G3 in group2"));
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        CommandLine cmd = parser.parse(new String[0][], null);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"arg1", "arg2"}, null);
        List<Option> options = cmd.getOptions();
        assertTrue(options.size() == 0);
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs().toArray(new String[0]));
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-a", "-b", "valueB", "-c", "3", "-d", "valueD"}, null);
        List<Option> options = cmd.getOptions();
        assertArrayEquals(new String[]{"-a", "-b", "-c"}, cmd.getArgs().toArray(new String[0]));
        assertTrue(options.size() == 5);
        Option optionA = options.get(0);
        assertTrue(optionA.getValue().isPresent());
        assertEquals(true, optionA.getValue().get());
        Option optionB = options.get(1);
        assertTrue(optionB.getValue().isPresent());
        assertEquals("valueB", optionB.getValue().get());
        Option optionC = options.get(2);
        assertTrue(optionC.getValue().isPresent());
        assertEquals(3, optionC.getValue().get());
        Option optionD = options.get(3);
        assertNull(optionD.getValue());
        Option optionE = options.get(4);
        assertNull(optionE.getValue());
    }

    @Test
    public void testParseWithOptionsAndProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        CommandLine cmd = parser.parse(new String[0], properties);
        List<Option> options = cmd.getOptions();
        assertTrue(options.size() == 1);
        Option optionA = options.get(0);
        assertTrue(optionA.getValue().isPresent());
        assertEquals(true, optionA.getValue().get());
    }

    @Test
    public void testParseWithGroups() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-g1"}, null);
        List<Option> options = cmd.getOptions();
        assertTrue(options.size() == 2);
        Option optionG1 = options.get(0);
        assertTrue(optionG1.getValue().isPresent());
        assertEquals(true, optionG1.getValue().get());
        Option optionG2 = options.get(1);
        assertTrue(optionG2.getValue().isPresent());
        assertEquals(true, optionG2.getValue().get());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        CommandLine cmd = parser.parse(new String[0], null);
        assertThrows(MissingOptionException.class, () -> parser.parse(new String[0], null));
        cmd = parser.parse(new String[]{"-g1"}, null);
        assertFalse(cmd.hasOption("group1"));
        cmd = parser.parse(new String[]{"-g1", "-g2"}, null);
        assertTrue(cmd.hasOption("group1"));
        assertTrue(cmd.hasOption("group2"));
    }

    @Test
    public void testParseWithInvalidOptions() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"--invalid"}, null);
        List<Option> options = cmd.getOptions();
        assertTrue(options.isEmpty());
        assertArrayEquals(new String[]{"--invalid"}, cmd.getArgs().toArray(new String[0]));
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(new String[]{"--invalid"}, null));
    }

    @Test
    public void testParseWithMissingRequiredOptions() throws ParseException {
        CommandLine cmd = parser.parse(new String[0], null);
        assertThrows(MissingOptionException.class, () -> parser.parse(new String[0], null));
        cmd = parser.parse(new String[]{"-g1"}, null);
        assertFalse(cmd.hasOption("group1"));
        cmd = parser.parse(new String[]{"-g1", "-g2"}, null);
        assertTrue(cmd.hasOption("group1"));
        assertTrue(cmd.hasOption("group2"));
    }

    @Test
    public void testParseWithInvalidArgumentCount() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-c", "1", "2"}, null);
        List<Option> options = cmd.getOptions();
        assertTrue(options.isEmpty());
        assertArrayEquals(new String[]{"-c", "1", "2"}, cmd.getArgs().toArray(new String[0]));
        assertThrows(ParseException.class, () -> parser.parse(new String[]{"-c", "1", "2"}, null));
    }

    @Test
    public void testParseWithInvalidIntegerArgument() throws ParseException {
        CommandLine cmd = parser.parse(new String[]{"-c", "invalid"}, null);
        List<Option> options = cmd.getOptions();
        assertTrue(options.isEmpty());
        assertArrayEquals(new String[]{"-c", "invalid"}, cmd.getArgs().toArray(new String[0]));
        assertThrows(ParseException.class, () -> parser.parse(new String[]{"-c", "invalid"}, null));
    }
}