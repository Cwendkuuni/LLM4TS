package org.apache.commons.cli;

import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    
    private Option option;
    
    @Before
    public void setUp() throws Exception {
        option = new Option("opt", "description");
    }
    
    @Test
    public void testGetOpt() {
        assertEquals("opt", option.getOpt());
    }
    
    @Test
    public void testGetType() {
        assertNull(option.getType());
    }
    
    @Test
    public void testSetType() {
        option.setType("type");
        assertEquals("type", option.getType());
    }
    
    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }
    
    @Test
    public void testSetLongOpt() {
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        assertFalse(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        assertFalse(option.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testSetRequired() {
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetArgName() {
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        assertFalse(option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(';');
        assertEquals(';', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
    }
    
    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_NoArgsAllowed() {
        option.setArgs(-1);
        option.addValueForProcessing("value");
    }
    
    @Test
    public void testProcessValue_NoSeparator() {
        option.setArgs(1);
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }
    
    @Test
    public void testProcessValue_WithSeparator() {
        option.setArgs(3);
        option.setValueSeparator(';');
        option.addValueForProcessing("value1;value2;value3");
        String[] expected = {"value1", "value2", "value3"};
        assertArrayEquals(expected, option.getValues());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValue_Overflow() {
        option.setArgs(2);
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
    }
    
    @Test
    public void testGetValue() {
        assertNull(option.getValue());
    }
    
    @Test
    public void testGetValue_IndexOutOfBounds() {
        assertNull(option.getValue(0));
    }
    
    @Test
    public void testGetValue_WithDefaultValue() {
        assertEquals("default", option.getValue("default"));
    }
    
    @Test
    public void testGetValues() {
        assertNull(option.getValues());
    }
    
    @Test
    public void testGetValuesList() {
        List<String> expected = new ArrayList<>();
        assertEquals(expected, option.getValuesList());
    }
    
    @Test
    public void testToString() {
        String expected = "[ option: opt :: description ]";
        assertEquals(expected, option.toString());
    }
    
    @Test
    public void testEquals_SameInstance() {
        assertTrue(option.equals(option));
    }
    
    @Test
    public void testEquals_NullArgument() {
        assertFalse(option.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        assertFalse(option.equals("string"));
    }
    
    @Test
    public void testEquals_SameOptAndLongOpt() {
        Option otherOption = new Option("opt", "description");
        assertTrue(option.equals(otherOption));
    }
    
    @Test
    public void testEquals_SameOptDifferentLongOpt() {
        Option otherOption = new Option("opt", "differentLongOpt");
        assertFalse(option.equals(otherOption));
    }
    
    @Test
    public void testEquals_DifferentOptSameLongOpt() {
        Option otherOption = new Option("differentOpt", "description");
        assertFalse(option.equals(otherOption));
    }
    
    @Test
    public void testHashCode() {
        int expected = 31 * "opt".hashCode() + "description".hashCode();
        assertEquals(expected, option.hashCode());
    }
    
    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        assertNotSame(option, clonedOption);
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.getType(), clonedOption.getType());
        assertEquals(option.getValues(), clonedOption.getValues());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
    }
    
    @Test
    public void testClearValues() {
        option.addValue("value");
        option.clearValues();
        assertNull(option.getValue());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}