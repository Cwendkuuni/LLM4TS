package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("test", "This is a test option");
        assertNotNull(option);
        assertEquals("test", option.getOpt());
        assertEquals("This is a test option", option.getDescription());
    }

    @Test
    public void testOptionConstructorWithHasArgAndDescription() {
        Option option = new Option("test", true, "This is a test option with argument");
        assertNotNull(option);
        assertEquals("test", option.getOpt());
        assertTrue(option.hasArg());
        assertEquals("This is a test option with argument", option.getDescription());
    }

    @Test
    public void testOptionConstructorWithLongOptAndDescription() {
        Option option = new Option("t", "test", false, "This is a test option with long option");
        assertNotNull(option);
        assertEquals("t", option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("This is a test option with long option", option.getDescription());
    }
    
    // Additional test cases for other methods can be added here

}