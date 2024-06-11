package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {
    
    @Test
    public void testGetOpt() {
        Option option = new Option("o", "Option description");
        assertEquals("o", option.getOpt());
    }
    
    @Test
    public void testGetLongOpt() {
        Option option = new Option("o", "long", true, "Option description");
        assertEquals("long", option.getLongOpt());
    }
    
    @Test
    public void testRequired() {
        Option option = new Option("o", "Option description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testValueSeparator() {
        Option option = new Option("o", "Option description");
        option.setValueSeparator(';');
        assertEquals(';', option.getValueSeparator());
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("o", "Option description");
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }
    
    @Test
    public void testAddMultipleValues() {
        Option option = new Option("o", "Option description");
        option.setArgs(3);
        option.addValueForProcessing("value1;value2;value3");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }
    
    // Add more test methods for other public methods
    
    @Test
    public void testClone() {
        Option option = new Option("o", "Option description");
        Option clonedOption = (Option) option.clone();
        assertNotSame(option, clonedOption);
        assertEquals(option.getOpt(), clonedOption.getOpt());
    }
    
}