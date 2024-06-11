package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption("opt"));
        assertFalse(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        assertNull(commandLine.getOptionObject("opt"));
        assertNull(commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(commandLine.getOptionValue("opt"));
        assertNull(commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(commandLine.getOptionValues("opt"));
        assertNull(commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetArgs() {
        assertArrayEquals(new String[]{}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("arg");
        assertArrayEquals(new String[]{"arg"}, commandLine.getArgs());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option", false, "option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("opt"));
        assertFalse(commandLine.hasOption('o'));
        assertEquals(option, commandLine.getOptionObject("opt"));
        assertNull(commandLine.getOptionObject('o'));
        assertEquals("option", commandLine.getOptionValue("opt"));
        assertNull(commandLine.getOptionValue('o'));
        assertArrayEquals(new String[]{"option"}, commandLine.getOptionValues("opt"));
        assertNull(commandLine.getOptionValues('o'));
        assertEquals(1, commandLine.getOptions().length);
        assertEquals(option, commandLine.getOptions()[0]);
    }
}