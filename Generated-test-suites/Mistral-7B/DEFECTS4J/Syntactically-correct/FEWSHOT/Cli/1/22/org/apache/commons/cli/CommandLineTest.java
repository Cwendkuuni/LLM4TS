package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandLineTest {

    @Test
    public void testCommandLine() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getArgs());
        assertNull(cmd.getArgList());
        assertTrue(cmd.getOptions().length == 0);
        assertTrue(cmd.iterator().hasNext());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        // default case
        assertFalse(cmd.hasOption("test"));
        // add option
        Option opt = new Option("t", "test", false, "test option");
        cmd.addOption(opt);
        assertTrue(cmd.hasOption("t"));
        assertTrue(cmd.hasOption('t'));
        assertTrue(cmd.hasOption("TEST"));
        assertFalse(cmd.hasOption("foo"));
        // remove option
        cmd.removeOption(opt);
        assertFalse(cmd.hasOption("t"));
        assertFalse(cmd.hasOption("test"));
        assertFalse(cmd.hasOption('t'));
        assertFalse(cmd.hasOption("TEST"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        // default case
        assertNull(cmd.getOptionValue("test"));
        // add option with no value
        Option opt = new Option("t", "test", false, "test option");
        cmd.addOption(opt);
        assertNull(cmd.getOptionValue("t"));
        assertNull(cmd.getOptionValue("test"));
        assertNull(cmd.getOptionValue('t'));
        // add option with value
        opt = new Option("t", "test", true, "test option");
        cmd.addOption(opt);
        cmd.addArg("foo");
        cmd.addArg("bar");
        cmd.addArg("baz");
        assertEquals("foo", cmd.getOptionValue("t"));
        assertEquals("foo", cmd.getOptionValue("test"));
        assertEquals("foo", cmd.getOptionValue('t'));
        assertEquals("bar", cmd.getOptionValue("t", "bar"));
        assertEquals("bar", cmd.getOptionValue("test", "bar"));
        assertEquals("bar", cmd.getOptionValue('t', "bar"));
        // add multiple values
        cmd.clear(); // clear previous values
        cmd.addArg("foo");
        cmd.addArg("bar");
        cmd.addArg("baz");
        opt.setArgName("arg");
        opt.setArgsRequired(true);
        cmd.addOption(opt);
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, cmd.getOptionValues("t"));
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, cmd.getOptionValues("test"));
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, cmd.getOptionValues('t'));
        assertArrayEquals(new String[]{"arg", "arg", "arg"}, cmd.getOptionValues("arg", new String[]{"arg", "arg", "arg"}));
        assertArrayEquals(new String[]{"arg", "arg", "arg"}, cmd.getOptionValues("ARG", new String[]{"arg", "arg", "arg"}));
        assertArrayEquals(new String[]{"arg", "arg", "arg"}, cmd.getOptionValues('A', new String[]{"arg", "arg", "arg"}));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        // default case
        assertNull(cmd.getOptionValues("test"));
        // add option with no value
        Option opt = new Option("t", "test", false, "test option");
        cmd.addOption(opt);
        assertNull(cmd.getOptionValues("t"));
        assertNull(cmd.getOptionValues("test"));
        assertNull(cmd.getOptionValues('t'));
        // add option with value
        opt = new Option("t", "test", true, "test option");
        cmd.addOption(opt);
        cmd.addArg("foo");
        cmd.addArg("bar");
        cmd.addArg("baz");
        assertEquals(new String[0], cmd.getOptionValues("t"));
        assertEquals(new String[0], cmd.getOptionValues("test"));
        assertEquals(new String[0], cmd.getOptionValues('t'));
        assertEquals(new String[]{"foo"}, cmd.getOptionValues("t", new String[0]));
        assertEquals(new String[]{"foo"}, cmd.getOptionValues("test", new String[0]));
        assertEquals(new String[]{"foo"}, cmd.getOptionValues('t', new String[0]));
        // add multiple values
        cmd.clear(); // clear previous values
        cmd.addArg("foo");
        cmd.addArg("bar");
        cmd.addArg("baz");
        opt.setArgName("arg");
        opt.setArgsRequired(true);
        cmd.addOption(opt);
        assertNull(cmd.getOptionValues("t"));
        assertNull(cmd.getOptionValues("test"));
        assertNull(cmd.getOptionValues('t'));
        assertArrayEquals(new String[]{}, cmd.getOptionValues("arg"));
        assertArrayEquals(new String[]{}, cmd.getOptionValues("ARG"));
        assertArrayEquals(new String[]{}, cmd.getOptionValues('A'));
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, cmd.getOptionValues("arg", new String[0]));
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, cmd.getOptionValues("ARG", new String[0]));
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, cmd.getOptionValues('A', new String[0]));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        // default case
        assertNull(cmd.getArgs());
        // add arguments
        cmd.addArg("foo");
        cmd.addArg("bar");
        cmd.addArg("baz");
        String[] args = cmd.getArgs();
        assertEquals(3, args.length);
        assertEquals("foo", args[0]);
        assertEquals("bar", args[1]);
        assertEquals("baz", args[2]);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        // default case
        assertNull(cmd.getArgList());
        // add arguments
        cmd.addArg("foo");
        cmd.addArg("bar");
        cmd.addArg("baz");
        List argList = cmd.getArgList();
        assertEquals(3, argList.size());
        assertEquals("foo", argList.get(0));
        assertEquals("bar", argList.get(1));
        assertEquals("baz", argList.get(2));
    }

    @Test
    public void testToString() {
        CommandLine cmd = new CommandLine();
        // default case
        assertEquals("", cmd.toString());
        // add options
        Option opt = new Option("t", "test", false, "test option");
        cmd.addOption(opt);
        Option opt2 = new Option("T", "test2", false, "test option 2");
        cmd.addOption(opt2);
        // add arguments
        cmd.addArg("foo");
        cmd.addArg("bar");
        cmd.addArg("baz");
        assertEquals("[ CommandLine: [ options: {test=null, test2=null} ] [ args: [foo, bar, baz] ] ]", cmd.toString());
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        // add arguments
        cmd.addArg("foo");
        cmd.addArg("bar");
        cmd.addArg("baz");
        // check size and get arguments
        assertEquals(3, cmd.getArgs().length);
        assertEquals(3, cmd.getArgList().size());
        assertEquals("foo", cmd.getArgs()[0]);
        assertEquals("bar", cmd.getArgs()[1]);
        assertEquals("baz", cmd.getArgs()[2]);
        assertEquals("foo", cmd.getArgList().get(0));
        assertEquals("bar", cmd.getArgList().get(1));
        assertEquals("baz", cmd.getArgList().get(2));
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        // add options
        Option opt = new Option("t", "test", false, "test option");
        cmd.addOption(opt);
        Option opt2 = new Option("T", "test2", false, "test option 2");
        cmd.addOption(opt2);
        // check size and get options
        assertEquals(2, cmd.getOptions().length);
        assertEquals(2, cmd.iterator().size());
        Option optInList = cmd.iterator().next();
        assertEquals(opt, optInList);
        assertEquals("test", optInList.getLongOpt());
        assertEquals("t", optInList.getKey());
        optInList = cmd.iterator().next();
        assertEquals(opt2, optInList);
        assertEquals("test2", optInList.getLongOpt());
        assertEquals("T", optInList.getKey());
    }
}