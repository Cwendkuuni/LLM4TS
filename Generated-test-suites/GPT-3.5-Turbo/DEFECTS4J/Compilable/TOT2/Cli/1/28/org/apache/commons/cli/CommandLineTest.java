package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmdLine = new CommandLine();
        
        // Edge case: check if option is not set
        assertFalse(cmdLine.hasOption("test"));
        
        // Typical case: check if option is set
        cmdLine.addOption(new Option("test", "Test option"));
        assertTrue(cmdLine.hasOption("test"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmdLine = new CommandLine();
        
        // Edge case: option not set
        assertNull(cmdLine.getOptionObject("test"));
        
        // Typical case: option set
        cmdLine.addOption(new Option("test", "Test option"));
        assertNotNull(cmdLine.getOptionObject("test"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmdLine = new CommandLine();
        
        // Edge case: option not set
        assertNull(cmdLine.getOptionValue("test"));
        
        // Typical case: option set
        cmdLine.addOption(new Option("test", "Test option"));
        assertNotNull(cmdLine.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmdLine = new CommandLine();
        
        // Edge case: option not set
        assertNull(cmdLine.getOptionValues("test"));
        
        // Typical case: option set
        Option option = new Option("test", "Test option");
        option.addValue("value1");
        option.addValue("value2");
        cmdLine.addOption(option);
        assertArrayEquals(new String[]{"value1", "value2"}, cmdLine.getOptionValues("test"));
    }

    // Add more test cases for other methods

}