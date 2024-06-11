package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructorWithDescription() {
        Option option = new Option("-o", "Description");
        assertEquals("-o", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testConstructorWithArg() {
        Option option = new Option("-o", true, "Description");
        assertEquals("-o", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("-o", "--option", true, "Description");
        assertEquals("-o", option.getOpt());
        assertEquals("--option", option.getLongOpt());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("-o", "Description");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    // Add more test cases for other methods as needed

}