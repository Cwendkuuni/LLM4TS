package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructorWithDescription() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals(1, option.getArgs());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testConstructorWithHasArg() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals(1, option.getArgs());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(1, option.getArgs());
        assertEquals("description", option.getDescription());
    }

    // Additional test cases can be added for other methods

}