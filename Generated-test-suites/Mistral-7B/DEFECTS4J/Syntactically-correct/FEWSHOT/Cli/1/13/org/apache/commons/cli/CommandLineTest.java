package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCreateCommandLine() {
        CommandLine cmdLine = new CommandLine();
        assertNull(cmdLine.getOptionValue("opt"));
        assertFalse(cmdLine.hasOption("opt"));
        assertNull(cmdLine.getOptionObject("opt"));
        assertNull(cmdLine.getOptionObject('o'));
        assertNull(cmdLine.getOptionValue("opt", "defaultValue"));
        assertNull(cmdLine.getOptionValue('o', "defaultValue"));
        assertNull(cmdLine.getArgs());
        assertNull(cmdLine.getArgList());
    }

    @Test
    public void testHasOption() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "option", false));
        assertTrue(cmdLine.hasOption("opt"));
        assertTrue(cmdLine.hasOption('o'));
        assertFalse(cmdLine.hasOption("anotherOpt"));
        assertFalse(cmdLine.hasOption('A'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "option", true, "value"));
        assertEquals("value", cmdLine.getOptionValue("opt"));
        assertEquals("value", cmdLine.getOptionValue('o'));
        assertNull(cmdLine.getOptionValue("anotherOpt"));
        assertNull(cmdLine.getOptionValue('A'));

        cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "option", true, "value1"));
        cmdLine.addOption(new Option("opt", "option", true, "value2"));
        assertArrayEquals(new String[]{"value1", "value2"}, cmdLine.getOptionValues("opt"));
        assertArrayEquals(new String[]{"value1", "value2"}, cmdLine.getOptionValues('o'));
        assertNull(cmdLine.getOptionValues("anotherOpt"));
        assertNull(cmdLine.getOptionValues('A'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "option", true, "value1,value2"));
        assertArrayEquals(new String[]{"value1", "value2"}, cmdLine.getOptionValues("opt"));
        assertArrayEquals(new String[]{"value1", "value2"}, cmdLine.getOptionValues('o'));
        assertNull(cmdLine.getOptionValues("anotherOpt"));
        assertNull(cmdLine.getOptionValues('A'));

        cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "option", true, "value1"));
        cmdLine.addOption(new Option("opt", "option", true, "value2"));
        cmdLine.addOption(new Option("opt", "option", true, "value3"));
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, cmdLine.getOptionValues("opt"));
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, cmdLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "option", true, Integer.TYPE, "1"));
        assertEquals(1, cmdLine.getOptionObject("opt"));
        assertEquals(1, cmdLine.getOptionObject('o'));
        assertNull(cmdLine.getOptionObject("anotherOpt"));
        assertNull(cmdLine.getOptionObject('A'));

        cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "option", true, Integer.TYPE, "1"));
        cmdLine.addOption(new Option("opt", "option", true, Integer.TYPE, "2"));
        cmdLine.addOption(new Option("opt", "option", true, Integer.TYPE, "3"));
        assertArrayEquals(new Integer[]{1, 2, 3}, cmdLine.getOptionObjects("opt"));
        assertArrayEquals(new Integer[]{1, 2, 3}, cmdLine.getOptionObjects('o'));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        cmdLine.addArg("arg3");
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, cmdLine.getArgs());
        assertArrayEquals(Arrays.asList("arg1", "arg2", "arg3"), cmdLine.getArgList());

        cmdLine = new CommandLine();
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        cmdLine.addArg("arg3");
        cmdLine.addArg("arg4");
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3", "arg4"}, cmdLine.getArgs());
        assertArrayEquals(Arrays.asList("arg1", "arg2", "arg3", "arg4"), cmdLine.getArgList());
    }

    @Test
    public void testAddArg() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        cmdLine.addArg("arg3");
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, cmdLine.getArgs());
        assertArrayEquals(Arrays.asList("arg1", "arg2", "arg3"), cmdLine.getArgList());

        cmdLine = new CommandLine();
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        cmdLine.addArg("arg3");
        cmdLine.addArg("arg4");
        cmdLine.addArg("arg5");
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3", "arg4", "arg5"}, cmdLine.getArgs());
        assertArrayEquals(Arrays.asList("arg1", "arg2", "arg3", "arg4", "arg5"), cmdLine.getArgList());
    }

    @Test
    public void testAddOption() {
        CommandLine cmdLine = new CommandLine();
        Option option = new Option("opt", "option", false);
        cmdLine.addOption(option);
        assertSame(option, cmdLine.getOptions()[0]);
        assertEquals("option", cmdLine.getOptions()[0].getLongOpt());
        assertFalse(cmdLine.hasOption("anotherOpt"));
        assertFalse(cmdLine.hasOption('A'));
        assertNull(cmdLine.getOptionValue("opt"));
        assertNull(cmdLine.getOptionValue('o'));
        assertFalse(cmdLine.hasOption("opt", true));
        assertFalse(cmdLine.hasOption("opt", false));
        assertFalse(cmdLine.hasOption('o', true));
        assertFalse(cmdLine.hasOption('o', false));
    }

    @Test
    public void testToString() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt", "option", false));
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        String expected = "[ CommandLine: [ options: [Option{name='opt', optional=false, shortName='o', values=[]}] ] [ args: [arg1, arg2] ] ]";
        assertEquals(expected, cmdLine.toString());
    }

    @Test
    public void testIterator() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt1", "option1", false));
        cmdLine.addOption(new Option("opt2", "option2", true, "value"));
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");

        Iterator it = cmdLine.iterator();
        List<Object> list = new LinkedList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }

        List<Object> expected = Arrays.asList(new Option[] { cmdLine.getOptions()[0], cmdLine.getOptions()[1] }, "arg1", "arg2");
        assertEquals(expected, list);
    }

    @Test
    public void testGetOptions() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("opt1", "option1", false));
        cmdLine.addOption(new Option("opt2", "option2", true, "value"));
        Option[] options = cmdLine.getOptions();
        assertEquals(2, options.length);
        assertEquals("option1", options[0].getLongOpt());
        assertEquals(false, options[0].isRequired());
        assertEquals("option2", options[1].getLongOpt());
        assertEquals(true, options[1].isRequired());
    }

    @Test
    public void testGetOptionsEmpty() {
        CommandLine cmdLine = new CommandLine();
        Option[] options = cmdLine.getOptions();
        assertEquals(0, options.length);
    }
}