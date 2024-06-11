package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void optionConstructor1() {
        Option option = new Option("o", "Description");
        assertEquals("o", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertFalse(option.hasArg());
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void optionConstructor2() {
        Option option = new Option("o", true, "Description");
        assertEquals("o", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
    }

    // Add more test cases for all other methods

    @Test
    public void addValueForProcessingThrowsException() {
        Option option = new Option("o", "Description");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            option.addValueForProcessing("value");
        });
        assertEquals("NO_ARGS_ALLOWED", exception.getMessage());
    }

    @Test
    public void cloneOption() {
        Option option = new Option("o", "Description");
        Option clonedOption = (Option) option.clone();
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
    }

}