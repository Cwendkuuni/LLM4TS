package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testEmptyCommandLine() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));
        assertEquals(Collections.emptyList(), cmd.getArgList());
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testSimpleOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "opt", false, "an option"));
        assertTrue(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption("opt2"));
        assertNull(cmd.getOptionValue("opt"));
        assertNull(cmd.getOptionValue("opt2"));
        assertEquals(Collections.emptyList(), cmd.getArgList());
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testOptionWithValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "opt", true, "an option with value"));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("opt"));
        assertEquals("value", cmd.getOptionValue("opt"));
        assertEquals("value", cmd.getOptionValue("opt"));
        assertEquals(Collections.emptyList(), cmd.getArgList());
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testMultipleOptions() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "opt1", false, "option 1"));
        cmd.addOption(new Option("o", "opt2", false, "option 2"));
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertFalse(cmd.hasOption("opt3"));
        assertNull(cmd.getOptionValue("opt1"));
        assertNull(cmd.getOptionValue("opt2"));
        assertNull(cmd.getOptionValue("opt3"));
        assertEquals(Collections.emptyList(), cmd.getArgList());
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testOptionWithValueAndArg() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "opt", true, "an option with value")
                .requiredArg(true)
                .argName("arg"));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption("opt"));
        assertEquals("arg1", cmd.getOptionValue("opt"));
        assertEquals("arg1", cmd.getOptionValue("opt"));
        List argList = cmd.getArgList();
        assertEquals(1, argList.size());
        assertEquals("arg1", argList.get(0));
        assertArrayEquals(new String[] {"arg1"}, cmd.getArgs());
    }

    @Test
    public void testLeftOverArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "opt", false, "an option"));
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertTrue(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption("opt2"));
        assertNull(cmd.getOptionValue("opt"));
        assertNull(cmd.getOptionValue("opt2"));
        List argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertArrayEquals(new String[] {"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "opt1", false, "option 1"));
        cmd.addOption(new Option("o", "opt2", false, "option 2"));
        Option[] options = cmd.getOptions();
        assertEquals(2, options.length);
        assertSame(cmd.getOption("opt1"), options[0]);
        assertSame(cmd.getOption("opt2"), options[1]);
    }
}