package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParseWithArgs() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] args = {"arg1", "arg2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("long-option-a", true, "Long Option A");

        String[] args = {"--long-option-a"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("long-option-a"));
        assertFalse(cmd.hasOption("short-option-a"));
    }

    @Test
    public void testParseWithValueOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] args = {"--a", "value1", "--b", "value2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("value1"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getOptionValue("b").equals("value2"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        List<Option> requiredOptions = options.getRequiredOptions();

        String[] args = {"arg1"};
        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            List<String> missingOptions = Arrays.asList(e.getOptionNames().toArray(new String[0]));
            assertTrue(requiredOptions.containsAll(missingOptions));
        }
    }

    @Test
    public void testParseWithIllegalArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--b"};
        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            fail("UnrecognizedOptionException should be thrown");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: b", e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group Options");
        group.setRequired(true);
        group.addOption("a", true, "Option A");
        group.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        String[] args = {"--group"};
        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            List<String> missingOptions = Arrays.asList(e.getOptionNames().toArray(new String[0]));
            assertTrue(group.getOptions().containsAll(missingOptions));
        }
    }

    @Test
    public void testParseWithOptionGroupAndArgs() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group Options");
        group.addOption("a", true, "Option A");
        group.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        String[] args = {"--group", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertTrue(group.isSelected());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParseWithOptionGroupAndNoArgs() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group Options");
        group.addOption("a", true, "Option A");
        group.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        String[] args = {"--group"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertFalse(group.isSelected());
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertArrayEquals(Collections.emptyList().toArray(new String[0]), cmd.getArgs());
    }
}