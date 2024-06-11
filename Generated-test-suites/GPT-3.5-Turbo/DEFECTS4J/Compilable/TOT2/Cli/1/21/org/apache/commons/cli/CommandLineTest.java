package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "option", false, "Option for testing"));
        assertTrue(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("x"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "option", true, "Option for testing"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "option", true, "Option for testing"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        String[] values = cmd.getOptionValues("o");
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("o", "option", false, "Option for testing");
        cmd.addOption(opt);
        assertTrue(cmd.hasOption("o"));
        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        assertEquals(opt, options[0]);
    }
}