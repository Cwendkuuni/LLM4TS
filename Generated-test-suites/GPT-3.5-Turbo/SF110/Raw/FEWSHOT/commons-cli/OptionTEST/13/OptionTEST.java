package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidOpt() {
        new Option(null, "description");
    }
    
    @Test
    public void testConstructorWithOptAndDescription() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test
    public void testConstructorWithOptHasArgAndDescription() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getType());
        assertEquals(1, option.getArgs());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test
    public void testConstructorWithOptLongOptHasArgAndDescription() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getType());
        assertEquals(1, option.getArgs());
        assertNull(option.getValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test
    public void testSetTypeAndGettersAndSetters() {
        Option option = new Option("opt", "description");
        option.setType("type");
        assertEquals("type", option.getType());
        
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
        
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
        
        option.setRequired(true);
        assertTrue(option.isRequired());
        
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
        
        option.setArgs(2);
        assertEquals(2, option.getArgs());
        
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
    }
    
    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("opt", true, "description");
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
        assertNull(option.getValue(1));
        assertNull(option.getValue("default"));
        assertEquals("value1", option.getValue("default"));
        assertEquals("value1", option.getValues()[0]);
        assertTrue(option.getValuesList().contains("value1"));
        
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue());
        assertEquals("value2", option.getValue(1));
        assertNull(option.getValue(2));
        assertNull(option.getValue("default"));
        assertEquals("value2", option.getValue("default"));
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
        assertTrue(option.getValuesList().contains("value1"));
        assertTrue(option.getValuesList().contains("value2"));
    }
    
    @Test
    public void testToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt  :: description ]", option.toString());
        
        option = new Option("opt", true, "description");
        assertEquals("[ option: opt [ARG] :: description ]", option.toString());
        
        option = new Option("opt", "longOpt", true, "description");
        assertEquals("[ option: opt longOpt [ARG] :: description ]", option.toString());
    }
    
    @Test
    public void testEqualsAndHashcode() {
        Option option1 = new Option("opt", "description1");
        Option option2 = new Option("opt", "description2");
        assertTrue(option1.equals(option2));
        assertTrue(option1.hashCode() == option2.hashCode());
        
        option1 = new Option("opt1", "description");
        option2 = new Option("opt2", "description");
        assertFalse(option1.equals(option2));
        assertFalse(option1.hashCode() == option2.hashCode());
        
        option1 = new Option("opt", "description");
        option2 = new Option("opt", "description");
        option2.setLongOpt("longOpt");
        assertFalse(option1.equals(option2));
        assertFalse(option1.hashCode() == option2.hashCode());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}