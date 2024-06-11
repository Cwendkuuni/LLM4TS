package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    
    @Test
    public void testOptionConstructor1() {
        Option option = new Option("o", "Description");
        assertEquals("o", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertFalse(option.hasArg());
    }
    
    @Test
    public void testOptionConstructor2() {
        Option option = new Option("o", true, "Description");
        assertEquals("o", option.getOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals("Description", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor3() {
        Option option = new Option("o", "longo", true, "Description");
        assertEquals("o", option.getOpt());
        assertEquals("longo", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals("Description", option.getDescription());
    }
    
    // Add more test cases for other methods
    
}