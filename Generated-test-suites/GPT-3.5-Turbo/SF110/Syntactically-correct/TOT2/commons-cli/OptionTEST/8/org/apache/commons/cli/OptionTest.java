package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructor1() {
        Option option = new Option("-a", "Description");
        assertEquals("-a", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testOptionConstructor2() {
        Option option = new Option("-a", true, "Description");
        assertEquals("-a", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testOptionConstructor3() {
        Option option = new Option("-a", "--arg", true, "Description");
        assertEquals("-a", option.getOpt());
        assertEquals("--arg", option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }
    
    // Add more test cases for other methods

}