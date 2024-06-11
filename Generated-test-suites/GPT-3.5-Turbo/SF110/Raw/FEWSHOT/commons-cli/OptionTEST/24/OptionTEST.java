package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {
    
    private Option option;
    
    @Before
    public void setUp() {
        option = new Option("f", "file", true, "File option");
    }
    
    @Test
    public void testConstructor() {
        assertEquals("f", option.getOpt());
        assertEquals("file", option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertEquals("File option", option.getDescription());
        assertFalse(option.isRequired());
        
        option = new Option("f", false, "File option");
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        
        option = new Option("f", "file", false, "File option");
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testGetId() {
        assertEquals('f', option.getId());
    }
    
    @Test
    public void testGetKey() {
        assertEquals("f", option.getKey());
        
        option = new Option(null, "file", true, "File option");
        assertEquals("file", option.getKey());
    }
    
    @Test
    public void testGetType() {
        assertNull(option.getType());
    }
    
    @Test
    public void testSetType() {
        option.setType(Object.class);
        assertEquals(Object.class, option.getType());
    }
    
    @Test
    public void testGetLongOpt() {
        assertEquals("file", option.getLongOpt());
    }
    
    @Test
    public void testSetLongOpt() {
        option.setLongOpt("filename");
        assertEquals("filename", option.getLongOpt());
    }
    
    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        assertTrue(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        
        option = new Option("f", false, "File option");
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        
        option = new Option("f", "file", false, "File option");
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("File option", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        option.setDescription("Description");
        assertEquals("Description", option.getDescription());
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
        option.setArgName("filename");
        assertEquals("filename", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        assertFalse(option.hasArgName());
        
        option.setArgName("filename");
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());
        
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
        
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testGetValues() {
        assertNull(option.getValues());
        
        option.addValueForProcessing("value1,value2,value3");
        assertEquals(3, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
        assertEquals("value3", option.getValues()[2]);
    }
    
    @Test
    public void testGetValuesList() {
        List<String> valuesList = option.getValuesList();
        assertTrue(valuesList.isEmpty());
        
        option.addValueForProcessing("value1,value2,value3");
        valuesList = option.getValuesList();
        assertEquals(3, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
        assertEquals("value3", valuesList.get(2));
    }
    
    @Test
    public void testToString() {
        assertEquals("[ option: f file [ARG...] :: File option ]", option.toString());
    }
    
    @Test
    public void testEquals() {
        Option option2 = new Option("f", "file", true, "File option");
        assertTrue(option.equals(option2));
        
        option2 = new Option("f", "filename", true, "File option");
        assertFalse(option.equals(option2));
        
        option2 = new Option("f", null, true, "File option");
        assertFalse(option.equals(option2));
        
        option2 = new Option(null, "file", true, "File option");
        assertFalse(option.equals(option2));
        
        option2 = new Option(null, null, true, "File option");
        assertEquals(option, option2);
        
        assertFalse(option.equals(null));
    }
    
    @Test
    public void testHashCode() {
        Option option2 = new Option("f", "file", true, "File option");
        assertEquals(option.hashCode(), option2.hashCode());
    }
    
    @Test
    public void testClone() {
        option.setArgName("filename");
        option.setArgs(2);
        option.setDescription("Description");
        option.setLongOpt("filename");
        option.setOptionalArg(true);
        option.setRequired(true);
        option.setType(Object.class);
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        
        Option clonedOption = (Option) option.clone();
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.isRequired(), clonedOption.isRequired());
        assertEquals(option.getType(), clonedOption.getType());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
        assertTrue(Arrays.equals(option.getValues(), clonedOption.getValues()));
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }
    
    @Test
    public void testAddValue() {
        // Unsupported operation
    }
    
    @Test(expected=RuntimeException.class)
    public void test_addValueForProcessing_with_invalidValue_shouldThrowException() {
        option.addValueForProcessing("value1,value2,value3");
        option.addValueForProcessing("value4");
    }
    
    @Test
    public void test_addValueForProcessing_with_validValue_shouldAddValue() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        
        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
    }
    
    @Test(expected=RuntimeException.class)
    public void test_addValueForProcessing_with_noArgsAllowed_shouldThrowException() {
        option = new Option("f", false, "File option");
        option.addValueForProcessing("value");
    }
    
    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }
    
    @Test
    public void testGetValue_with_defaultValue() {
        assertEquals("default", option.getValue("default"));
        
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("default"));
    }
    
    @Test
    public void testGetValue_with_index() {
        assertNull(option.getValue(0));
        
        option.addValueForProcessing("value1,value2,value3");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }
}