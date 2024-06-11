package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testNewCommandLine() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("foo"));
        assertNull(cmd.getOptionValue("foo"));
        assertNull(cmd.getOptionValues("foo"));
        assertArrayEquals(new String[0], cmd.getArgs());
        assertArrayEquals(Collections.emptyList().toArray(new List[0]), cmd.getArgList().toArray());
        assertEquals(0, cmd.getOptions().length);
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "foo", false, ""));
        cmd.addOption(new Option("h", "help", true, "Display this help message, or the help for the given subcommand"));

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("g"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        Option optBoolean = new Option("f", "foo", false, "");
        Option optValue = new Option("i", "input", true, "input file");
        cmd.addOption(optBoolean);
        cmd.addOption(optValue);

        assertNull(cmd.getOptionObject("g"));
        assertNull(cmd.getOptionObject("f"));
        assertEquals(Boolean.FALSE, cmd.getOptionObject("f"));
        assertEquals("input file", cmd.getOptionObject("i"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        Option optString = new Option("i", "input", true, "input file");
        cmd.addOption(optString);

        assertNull(cmd.getOptionValue("g"));
        assertNull(cmd.getOptionValue("f"));
        assertNull(cmd.getOptionValue("i"));

        cmd.addArg("input.txt");

        assertEquals("input.txt", cmd.getOptionValue("i"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        Option optString = new Option("i", "input", true, "input file");
        cmd.addOption(optString);

        cmd.addArg("input1.txt");
        cmd.addArg("input2.txt");

        String[] expected = {"input1.txt", "input2.txt"};
        assertArrayEquals(expected, cmd.getOptionValues("i"));
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, cmd.getArgs());
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List<String> expected = Arrays.asList("arg1", "arg2");
        assertArrayEquals(expected.toArray(new String[0]), cmd.getArgList().toArray());
    }

    @Test
    public void testToString() {
        // TODO: Implement test for toString()
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option optBoolean = new Option("f", "foo", false, "");
        Option optValue = new Option("i", "input", true, "input file");
        cmd.addOption(optBoolean);
        cmd.addOption(optValue);

        cmd.addArg("arg1");
        cmd.addArg("arg2");

        Iterator<Option> it = cmd.iterator();

        assertEquals(optBoolean, it.next());
        assertEquals(optValue, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option optBoolean = new Option("f", "foo", false, "");
        Option optValue = new Option("i", "input", true, "input file");
        cmd.addOption(optBoolean);
        cmd.addOption(optValue);

        Option[] expected = {optBoolean, optValue};
        assertArrayEquals(expected, cmd.getOptions());
    }
}