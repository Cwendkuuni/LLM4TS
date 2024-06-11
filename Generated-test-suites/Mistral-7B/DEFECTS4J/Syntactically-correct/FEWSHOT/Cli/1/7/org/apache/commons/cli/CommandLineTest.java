package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    CommandLine cmd;

    @Before
    public void setUp() {
        cmd = new CommandLine();
    }

    @Test
    public void testCreateCommandLine() {
        // nothing to do
    }

    @Test
    public void testHasOptionWithNoOptions() {
        assertFalse(cmd.hasOption("test"));
    }

    @Test
    public void testHasOptionWithDefinedOption() {
        cmd.addOption(new Option("t", "test", false, "test option"));
        assertTrue(cmd.hasOption("t"));
    }

    @Test
    public void testHasOptionWithOptionCharacter() {
        cmd.addOption(new Option('t', "test", false, "test option"));
        assertTrue(cmd.hasOption('t'));
    }

    @Test
    public void testGetOptionObjectWithDefinedOptionNoValue() {
        cmd.addOption(new Option("t", "test", false, "test option"));
        assertNull(cmd.getOptionObject("t"));
    }

    @Test
    public void testGetOptionObjectWithDefinedOptionValue() {
        cmd.addOption(new Option("t", "test", true, "test option"));
        String value = "testValue";
        cmd.addArg(value);
        assertEquals(value, cmd.getOptionObject("t"));
    }

    @Test
    public void testGetOptionValueWithDefinedOptionNoValue() {
        cmd.addOption(new Option("t", "test", false, "test option"));
        assertNull(cmd.getOptionValue("t"));
    }

    @Test
    public void testGetOptionValueWithDefinedOptionValue() {
        cmd.addOption(new Option("t", "test", true, "test option"));
        String value = "testValue";
        cmd.addArg(value);
        assertEquals(value, cmd.getOptionValue("t"));
    }

    @Test
    public void testGetOptionValuesWithDefinedOptionNoValue() {
        cmd.addOption(new Option("t", "test", false, "test option"));
        assertNull(cmd.getOptionValues("t"));
    }

    @Test
    public void testGetOptionValuesWithDefinedOptionValue() {
        cmd.addOption(new Option("t", "test", true, "test option"));
        String value1 = "testValue1";
        String value2 = "testValue2";
        cmd.addArg(value1);
        cmd.addArg(value2);
        String[] values = cmd.getOptionValues("t");
        assertArrayEquals(new String[] { value1, value2 }, values);
    }

    @Test
    public void testGetOptionsWithDefinedOption() {
        cmd.addOption(new Option("t", "test", false, "test option"));
        Option[] options = cmd.getOptions();
        assertNotNull(options);
        assertSame(cmd.getOptionObject("t").getClass(), options[0].getClass());
        assertEquals("test option", options[0].getLongOpt());
        assertEquals("test", options[0].getOpt());
        assertFalse(options[0].hasArg());
    }

    @Test
    public void testGetArgsWithNoArgs() {
        String[] args = cmd.getArgs();
        assertArrayEquals(new String[0], args);
    }

    @Test
    public void testGetArgListWithNoArgs() {
        List argList = cmd.getArgList();
        assertTrue(argList.isEmpty());
    }

    @Test
    public void testGetArgsWithDefinedArgs() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        cmd.addArg(arg1);
        cmd.addArg(arg2);
        String[] args = cmd.getArgs();
        assertArrayEquals(new String[] { arg1, arg2 }, args);
    }

    @Test
    public void testGetArgListWithDefinedArgsOrder() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        cmd.addArg(arg2);
        cmd.addArg(arg1);
        List argList = cmd.getArgList();
        List expectedArgList = new LinkedList(Arrays.asList(arg1, arg2));
        Collections.reverse(expectedArgList);
        assertArrayEquals(expectedArgList.toArray(), argList.toArray());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testGetOptionValueWithUndefinedOption() {
        cmd.getOptionValue("undefinedOption");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testGetOptionValueWithUndefinedOptionCharacter() {
        cmd.getOptionValue('u');
    }
}