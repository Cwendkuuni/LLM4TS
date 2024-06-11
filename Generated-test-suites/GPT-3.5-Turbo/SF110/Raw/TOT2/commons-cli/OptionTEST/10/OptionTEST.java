package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("o", "Option description");
        assertEquals("o", option.getOpt());
        assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testOptionConstructorWithHasArg() {
        Option option = new Option("o", true, "Option description");
        assertTrue(option.hasArg());
    }

    @Test
    public void testOptionConstructorWithOptions() {
        Option option = new Option("o", "option", true, "Option description");
        assertEquals("o", option.getOpt());
        assertEquals("option", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option description", option.getDescription());
    }

    // Add more test cases for other methods and scenarios

}