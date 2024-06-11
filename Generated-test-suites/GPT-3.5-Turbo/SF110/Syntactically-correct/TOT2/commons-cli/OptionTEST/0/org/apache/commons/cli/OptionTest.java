package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    
    @Test
    public void testOptionConstructor1() {
        Option option = new Option("o1", "Description");
        assertEquals("o1", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Description", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor2() {
        Option option = new Option("o2", true, "Description");
        assertEquals("o2", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Description", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor3() {
        Option option = new Option("o3", "longOpt", true, "Description");
        assertEquals("o3", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Description", option.getDescription());
    }
    
    @Test
    public void testGetId() {
        Option option = new Option("o4", "Description");
        assertEquals(111, option.getId());
    }
    
    // Add more test cases for other methods
    
    @Test
    public void testClone() {
        Option option1 = new Option("o5", "Description");
        Option option2 = (Option)option1.clone();
        assertNotSame(option1, option2);
        assertEquals(option1.getOpt(), option2.getOpt());
        assertEquals(option1.getDescription(), option2.getDescription());
    }
}