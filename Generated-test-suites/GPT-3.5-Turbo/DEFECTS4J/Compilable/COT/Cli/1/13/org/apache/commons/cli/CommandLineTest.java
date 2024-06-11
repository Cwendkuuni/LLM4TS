package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("test"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("test"));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("test"));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues('t'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue("test", "default"));
    }

    @Test
    public void testGetOptionValueCharWithDefault() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue('t', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[]{}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        assertTrue(commandLine.getArgList().isEmpty());
    }
}
