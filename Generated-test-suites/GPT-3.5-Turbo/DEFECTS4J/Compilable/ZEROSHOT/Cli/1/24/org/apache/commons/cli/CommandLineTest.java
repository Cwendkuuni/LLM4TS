package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, "Option A"));
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, "Option A"));
        assertTrue(commandLine.hasOption('a'));
        assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        commandLine.addOption(new Option("b", "banana", true, "Option B"));
        assertEquals("apple", commandLine.getOptionObject("a"));
        assertEquals("banana", commandLine.getOptionObject("b"));
        assertNull(commandLine.getOptionObject("c"));
    }

    @Test
    public void testGetOptionObject_char() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        commandLine.addOption(new Option("b", "banana", true, "Option B"));
        assertEquals("apple", commandLine.getOptionObject('a'));
        assertEquals("banana", commandLine.getOptionObject('b'));
        assertNull(commandLine.getOptionObject('c'));
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        commandLine.addOption(new Option("b", "banana", true, "Option B"));
        assertNull(commandLine.getOptionValue("a"));
        assertEquals("banana", commandLine.getOptionValue("b"));
        assertNull(commandLine.getOptionValue("c"));
    }

    @Test
    public void testGetOptionValue_char() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        commandLine.addOption(new Option("b", "banana", true, "Option B"));
        assertNull(commandLine.getOptionValue('a'));
        assertEquals("banana", commandLine.getOptionValue('b'));
        assertNull(commandLine.getOptionValue('c'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        commandLine.addOption(new Option("b", "banana", true, "Option B"));
        assertNull(commandLine.getOptionValues("a"));
        assertArrayEquals(new String[] { "banana" }, commandLine.getOptionValues("b"));
        assertNull(commandLine.getOptionValues("c"));
    }

    @Test
    public void testGetOptionValues_char() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        commandLine.addOption(new Option("b", "banana", true, "Option B"));
        assertNull(commandLine.getOptionValues('a'));
        assertArrayEquals(new String[] { "banana" }, commandLine.getOptionValues('b'));
        assertNull(commandLine.getOptionValues('c'));
    }

    @Test
    public void testGetOptionValue_String_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        commandLine.addOption(new Option("b", "banana", true, "Option B"));
        assertNull(commandLine.getOptionValue("a", "default"));
        assertEquals("banana", commandLine.getOptionValue("b", "default"));
        assertEquals("default", commandLine.getOptionValue("c", "default"));
    }

    @Test
    public void testGetOptionValue_char_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        commandLine.addOption(new Option("b", "banana", true, "Option B"));
        assertNull(commandLine.getOptionValue('a', "default"));
        assertEquals("banana", commandLine.getOptionValue('b', "default"));
        assertEquals("default", commandLine.getOptionValue('c', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[] { "arg1", "arg2" }, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertEquals(2, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().contains("arg1"));
        assertTrue(commandLine.getArgList().contains("arg2"));
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.getArgs().length);
        commandLine.addArg("arg1");
        assertEquals(1, commandLine.getArgs().length);
        assertEquals("arg1", commandLine.getArgs()[0]);
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.getOptions().length);
        commandLine.addOption(new Option("a", "apple", false, "Option A"));
        assertEquals(1, commandLine.getOptions().length);
        assertEquals("a", commandLine.getOptions()[0].getOpt());
    }
}