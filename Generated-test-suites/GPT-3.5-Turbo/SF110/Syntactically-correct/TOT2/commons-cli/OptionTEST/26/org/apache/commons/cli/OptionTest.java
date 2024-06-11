package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("-o", "Test option");
    }

    // Test getId method
    @Test
    public void testGetId() {
        assertEquals('o', option.getId());
    }

    // Test getOpt method
    @Test
    public void testGetOpt() {
        assertEquals("-o", option.getOpt());
    }

    // Test getType method
    @Test
    public void testGetType() {
        assertNull(option.getType());
    }

    // Test getLongOpt method
    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    // Test setLongOpt method
    @Test
    public void testSetLongOpt() {
        option.setLongOpt("--option");
        assertEquals("--option", option.getLongOpt());
    }

    // Test setDescription method
    @Test
    public void testSetDescription() {
        option.setDescription("Test description");
        assertEquals("Test description", option.getDescription());
    }

    // Test setArgs method
    @Test
    public void testSetArgs() {
        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }

    // Test setValueSeparator method
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    // Test addValueForProcessing method with invalid argument
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithInvalidArg() {
        option.addValueForProcessing("value1,value2,value3");
    }

    // Test getValue method with no values
    @Test
    public void testGetValueWithNoValues() {
        assertNull(option.getValue());
    }

    // Test getValue method with index out of bounds
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithIndexOutOfBounds() {
        option.getValue(1);
    }

    // Test addValue method
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}