package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        // Test if option is set
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("test", "Test Option"));
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testGetOptionValue() {
        // Test getting option value
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("test", "Test Option"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValues() {
        // Test getting option values
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("test", "Test Option"));
        cmd.addArg("value1");
        cmd.addArg("value2");
        String[] values = cmd.getOptionValues("test");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    public void testGetArgs() {
        // Test getting leftover arguments
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    @Test
    public void testAddOption() {
        // Test adding an option
        CommandLine cmd = new CommandLine();
        Option option = new Option("test", "Test Option");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testGetOptions() {
        // Test getting all processed options
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("test1", "Test Option 1");
        Option option2 = new Option("test2", "Test Option 2");
        cmd.addOption(option1);
        cmd.addOption(option2);
        Option[] options = cmd.getOptions();
        assertEquals(2, options.length);
    }
}