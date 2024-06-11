package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCreateCommandLine() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("opt"));
        assertFalse(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));
        assertNull(cmd.getOptionValues("opt"));
        assertEquals(Collections.emptyList(), cmd.getArgList());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("opt", true, "o"));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.hasOption('o'));
    }

    @Test
    public void testHasOptionFalse() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("opt"));
        assertFalse(cmd.hasOption('x'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("opt", true, "o", new Boolean(true)));
        assertTrue(cmd.hasOption("opt"));
        assertTrue(cmd.getOptionObject("opt") instanceof Boolean);
        assertTrue((Boolean) cmd.getOptionObject("opt"));

        cmd.addOption(new Option("opt2", false, "o2"));
        assertNull(cmd.getOptionObject("opt2"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("opt", true, "o", "value"));
        assertTrue(cmd.hasOption("opt"));
        assertEquals("value", cmd.getOptionValue("opt"));

        cmd.addOption(new Option("opt2", true, "o2", "value2"));
        assertEquals("value2", cmd.getOptionValue("opt2"));

        cmd.addOption(new Option("opt3", false, "o3"));
        assertNull(cmd.getOptionValue("opt3"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("opt", true, "o", "value1,value2"));
        assertTrue(cmd.hasOption("opt"));
        String[] expected = new String[]{"value1", "value2"};
        assertArrayEquals(expected, cmd.getOptionValues("opt"));

        cmd.addOption(new Option("opt2", false, "o2"));
        assertNull(cmd.getOptionValues("opt2"));

        cmd.addOption(new Option("opt3", true, "o3", "value3"));
        String[] expected3 = new String[]{"value3"};
        assertArrayEquals(expected3, cmd.getOptionValues("opt3"));
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("opt", true, "o", "value"));
        Option[] expected = new Option[] { cmd.getOptions()[0] };
        assertArrayEquals(expected, cmd.getOptions());
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        assertEquals("arg1", cmd.getArgList().get(0));

        cmd.addArg("arg2");
        assertEquals(2, cmd.getArgList().size());

        cmd.addArg("arg3");
        assertEquals("arg3", cmd.getArgList().get(1));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("opt1", false, "o1"));
        cmd.addOption(new Option("opt2", false, "o2"));
        cmd.addOption(new Option("opt3", true, "o3", "arg3"));
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        List<Option> options = new LinkedList<>();
        for (Object o : cmd.iterator()) {
            options.add((Option) o);
        }

        assertEquals(4, options.size());
        assertEquals("opt3", options.get(2).getKey());
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] expected = new String[]{"arg1", "arg2"};
        assertArrayEquals(expected, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        List<String> expected = Arrays.asList("arg1", "arg2");
        assertEquals(expected, cmd.getArgList());
    }

    @Test
    public void testToString() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addOption(new Option("opt", false, "o", "optValue"));

        String expected = "[ CommandLine: [ options: {o={Option@2894921326 value=[], longOpt=opt, shortOpt=o, args=[], type=class java.lang.Boolean, optional=true, argsName=}, o2={Option@105837611 value=[], longOpt=o2, shortOpt=o2, args=[], type=class java.lang.String, optional=false}} ] [ args: [arg1, arg2] ] ]";
        assertEquals(expected, cmd.toString());
    }
}