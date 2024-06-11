package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {
	
    private Option option;
    
    @Before
    public void setUp() {
        option = new Option("a", "option a description");
        option.setLongOpt("longOpt");
        option.setArgName("arg");
        option.setDescription("option description");
        option.setRequired(true);
        option.setOptionalArg(true);
        option.setArgs(2);
        option.setType(String.class);
        option.setValueSeparator(';');
    }
    
    @Test
    public void testGetOpt() {
        assertEquals("a", option.getOpt());
    }
    
    @Test
    public void testGetType() {
        assertEquals(String.class, option.getType());
    }
    
    @Test
    public void testSetType() {
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
    }
    
    @Test
    public void testGetLongOpt() {
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testSetLongOpt() {
        option.setLongOpt("newLongOpt");
        assertEquals("newLongOpt", option.getLongOpt());
    }
    
    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(false);
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        assertTrue(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("option description", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetRequired() {
        option.setRequired(false);
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testSetArgName() {
        option.setArgName("newArg");
        assertEquals("newArg", option.getArgName());
    }
    
    @Test
    public void testGetArgName() {
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testGetArgs() {
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertEquals("value1", option.getValue());
    }
    
    @Test
    public void testGetValueIndex() {
        assertNull(option.getValue(0));
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testGetValueDefault() {
        assertEquals("defaultValue", option.getValue("defaultValue"));
        
        option.addValueForProcessing("value1");
        
        assertEquals("value1", option.getValue("defaultValue"));
    }
    
    @Test
    public void testGetValues() {
        assertNull(option.getValues());
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertEquals(Arrays.asList("value1", "value2"), Arrays.asList(option.getValues()));
    }
    
    @Test
    public void testGetValuesList() {
        assertEquals(new ArrayList<String>(), option.getValuesList());
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }
    
    @Test
    public void testToString() {
        assertEquals("[ option: a longOpt [ARG...] :: option description :: class java.lang.String ]", option.toString());
    }
    
    @Test
    public void testEqualsTrue() {
        Option option2 = new Option("a", "option a description");
        option2.setLongOpt("longOpt");
        
        assertTrue(option.equals(option2));
    }
    
    @Test
    public void testEqualsFalse() {
        Option option2 = new Option("b", "option b description");
        option2.setLongOpt("longOpt");
        
        assertFalse(option.equals(option2));
    }
    
    @Test
    public void testHashCode() {
        Option option2 = new Option("a", "option a description");
        option2.setLongOpt("longOpt");
        
        assertEquals(option.hashCode(), option2.hashCode());
    }
    
    @Test
    public void testClone() {
        Option option2 = (Option) option.clone();
        
        assertEquals(option, option2);
    }
    
    @Test
    public void testClearValues() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        option.clearValues();
        
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
    
}