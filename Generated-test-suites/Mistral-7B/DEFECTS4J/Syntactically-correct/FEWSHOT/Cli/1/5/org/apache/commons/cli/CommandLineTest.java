package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCommandLine() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("foo"));
        assertFalse(cmd.hasOption("foo"));
        assertNull(cmd.getOptionValue("foo"));
        assertNull(cmd.getOptionValues("foo"));
        assertEquals(0, cmd.args.size());
    }

    @Test
    public void testBasicOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", true, false, "default");
        cmd.addOption(opt);
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertTrue(cmd.hasOption("option"));
        assertEquals("default", cmd.getOptionValue("option"));
        assertNull(cmd.getOptionValue("nonexistent"));
        assertArrayEquals(new String[] { "arg1", "arg2" }, cmd.getArgs());
    }

    @Test
    public void testMultipleOptions() {
        CommandLine cmd = new CommandLine();

        Option opt1 = new Option("option1", true, false, "default1");
        Option opt2 = new Option("option2", true, false, "default2");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertTrue(cmd.hasOption("option1"));
        assertEquals("default1", cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("default2", cmd.getOptionValue("option2"));
        assertNull(cmd.getOptionValue("nonexistent"));
        assertArrayEquals(new String[] { "arg1", "arg2" }, cmd.getArgs());
    }

    @Test
    public void testMultipleValuesOption() {
        CommandLine cmd = new CommandLine();

        Option opt = new Option("option", true, true, "default");
        cmd.addOption(opt);
        cmd.addArg("value1");
        cmd.addArg("value2");

        assertEquals(Arrays.asList(new String[] { "value1", "value2" }), Arrays.asList(cmd.getOptionValues("option")));
        assertEquals(Arrays.asList(new String[] { "value1", "value2" }), Arrays.asList(cmd.getOptionValues("option")));
    }

    @Test
    public void testMultipleValuesOptionEmpty() {
        CommandLine cmd = new CommandLine();

        Option opt = new Option("option", true, true, "default");
        cmd.addOption(opt);

        assertEquals(Collections.emptyList(), Arrays.asList(cmd.getOptionValues("option")));
    }

    @Test
    public void testListOption() {
        CommandLine cmd = new CommandLine();

        Option opt = new Option("option", true, false, null, 1);
        cmd.addOption(opt);
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List<String> values = Arrays.asList(cmd.getOptionValues("option"));
        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", true, false, "default");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("option"));
    }

    @Test
    public void testHasOptionFalse() {
        CommandLine cmd = new CommandLine();

        assertFalse(cmd.hasOption("nonexistent"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", true, false, "default");
        cmd.addOption(opt);
        cmd.addArg("bar");

        assertEquals("default", cmd.getOptionObject("option"));
        assertNull(cmd.getOptionObject("nonexistent"));
    }

    @Test
    public void testGetOptionObjectNotSet() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionObject("nonexistent"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", true, false, "default");
        cmd.addOption(opt);
        cmd.addArg("bar");

        assertEquals("bar", cmd.getOptionValue("option"));
        assertNull(cmd.getOptionValue("nonexistent"));
    }

    @Test
    public void testGetOptionValueEmpty() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", true, false, "default");
        cmd.addOption(opt);

        assertNull(cmd.getOptionValue("option"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", true, true, "default");
        cmd.addOption(opt);
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[] { "arg1", "arg2" }, cmd.getOptionValues("option"));
        assertArrayEquals(new String[] { "arg1", "arg2" }, cmd.getOptionValues("option"));
    }

    @Test
    public void testGetOptionValuesEmpty() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("option", true, true, "default");
        cmd.addOption(opt);

        assertEquals(Collections.emptyList().toArray(new String[0]), cmd.getOptionValues("option"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[] { "arg1", "arg2" }, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List<String> args = cmd.getArgList();
        assertEquals(Arrays.asList("arg1", "arg2"), args);
    }
}