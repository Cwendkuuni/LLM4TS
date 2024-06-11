package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("opt", "description");
    }

    @Test
    public void testConstructor() {
        assertNotNull(option);
    }

    @Test
    public void testGetId() {
        assertEquals('o', option.getId());
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
    public void testHasArgs() {
        assertFalse(option.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertTrue(option.hasArgs());
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
    }
    
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing() {
        option.addValueForProcessing("value");
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        assertNull(option.getValue(0));
    }

    @Test
    public void testGetValueWithDefault() {
        assertEquals("defaultValue", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertNotNull(option.getValuesList());
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test
    public void testToString() {
        assertEquals("[ option: opt  :: description ]", option.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(option.equals(option));

        Option option2 = new Option("opt2", "description");
        assertFalse(option.equals(option2));
    }

    @Test
    public void testHashCode() {
        assertEquals(111, option.hashCode());
    }

    @Test
    public void testClone() {
        Option clone = (Option) option.clone();
        assertNotNull(clone);
        assertEquals(option.getOpt(), clone.getOpt());
        assertEquals(option.getType(), clone.getType());
        assertEquals(option.getLongOpt(), clone.getLongOpt());
        assertEquals(option.hasOptionalArg(), clone.hasOptionalArg());
        assertEquals(option.hasLongOpt(), clone.hasLongOpt());
        assertEquals(option.hasArg(), clone.hasArg());
        assertEquals(option.getDescription(), clone.getDescription());
        assertEquals(option.isRequired(), clone.isRequired());
        assertEquals(option.getArgName(), clone.getArgName());
        assertEquals(option.hasArgs(), clone.hasArgs());
        assertEquals(option.getArgs(), clone.getArgs());
        assertEquals(option.hasValueSeparator(), clone.hasValueSeparator());
        assertEquals(option.getValueSeparator(), clone.getValueSeparator());
        assertEquals(option.getValues(), clone.getValues());
        assertEquals(option.getValuesList(), clone.getValuesList());
        assertEquals(option.toString(), clone.toString());
        assertEquals(option.equals(clone), clone.equals(option));
        assertEquals(option.hashCode(), clone.hashCode());
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}