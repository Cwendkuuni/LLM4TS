package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("testOption"));
        assertFalse(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("testOption"));
        assertNull(commandLine.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("testOption"));
        assertNull(commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("testOption"));
        assertNull(commandLine.getOptionValues('t'));
    }

    @Test
    public void testGetOptionValueWithDefaultValue() {
        CommandLine commandLine = new CommandLine();
        assertEquals("defaultValue", commandLine.getOptionValue("testOption", "defaultValue"));
        assertEquals("defaultValue", commandLine.getOptionValue('t', "defaultValue"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("testArg");
        assertArrayEquals(new String[]{"testArg"}, commandLine.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("testOption", "Test option"));
        assertTrue(commandLine.hasOption("testOption"));
        assertEquals("testOption", commandLine.getOptionValue("testOption"));
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.iterator().hasNext());
        commandLine.addOption(new Option("testOption", "Test option"));
        assertTrue(commandLine.iterator().hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.getOptions().length);
        commandLine.addOption(new Option("testOption", "Test option"));
        assertEquals(1, commandLine.getOptions().length);
    }
}