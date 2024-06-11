package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), new String[0]);
        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "--b", "3"});

        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());

        Option optA = optionsList.get(0);
        assertEquals("a", optA.getLongOpt());
        assertTrue(optA.isSet());
        assertFalse(optA.hasArg());

        Option optB = optionsList.get(1);
        assertEquals("b", optB.getLongOpt());
        assertEquals(3, optB.getValues()[0].length());
        assertFalse(optB.isSet());
        assertFalse(optB.hasArg());

        Option optC = optionsList.get(2);
        assertEquals("c", optC.getLongOpt());
        assertEquals(3, optC.getValue().length());
        assertTrue(optC.isSet());
        assertFalse(optC.hasArg());

        assertEquals(Collections.emptyList(), cmd.getArgs());
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});

        List<Option> optionsList = cmd.getOptions();
        assertEquals(0, optionsList.size());

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs().toArray(new String[0]));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1", "--b", "3"});

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option optA = optionsList.get(0);
        assertEquals("a", optA.getLongOpt());
        assertTrue(optA.isSet());
        assertFalse(optA.hasArg());

        Option optB = optionsList.get(1);
        assertEquals("b", optB.getLongOpt());
        assertEquals(3, optB.getValues()[0].length());
        assertTrue(optB.isSet());
        assertFalse(optB.hasArg());

        assertEquals(new String[]{"arg1"}, cmd.getArgs().toArray(new String[0]));
    }

    @Test
    public void testParseWithExtraArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1", "arg2"});

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());

        Option optA = optionsList.get(0);
        assertEquals("a", optA.getLongOpt());
        assertTrue(optA.isSet());
        assertFalse(optA.hasArg());

        assertEquals(new String[]{"arg1"}, cmd.getArgs().toArray(new String[0]));

        assertThrows(ParseException.class, () -> parser.parse(options, new String[]{"-a", "arg1", "arg2", "arg3"}));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"--x"}));
    }

    @Test
    public void testParseWithRequiredOptionMissing() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A", "Required option");

        Parser parser = new DefaultParser();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("b", "valueB");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());

        Option optA = optionsList.get(0);
        assertEquals("a", optA.getLongOpt());
        assertTrue(optA.isSet());
        assertFalse(optA.hasArg());
        assertEquals("valueA", optA.getValue());

        Option optB = optionsList.get(1);
        assertEquals("b", optB.getLongOpt());
        assertTrue(optB.isSet());
        assertFalse(optB.hasArg());
        assertEquals("valueB", optB.getValue());
    }

    @Test
    public void testParseWithPropertiesAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Properties properties = new Properties();
        properties.put("a", "valueA");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"}, properties);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());

        Option optA = optionsList.get(0);
        assertEquals("a", optA.getLongOpt());
        assertTrue(optA.isSet());
        assertFalse(optA.hasArg());
        assertEquals("valueA", optA.getValue());

        assertEquals(new String[]{"arg1", "arg2"}, cmd.getArgs().toArray(new String[0]));
    }
}