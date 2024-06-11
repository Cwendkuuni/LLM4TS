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
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("test"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("test"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("argument");
        assertEquals(1, commandLine.getArgs().length);
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", false, "test option");
        commandLine.addOption(option);
        assertNotNull(commandLine.getOptionObject("t"));
    }
}