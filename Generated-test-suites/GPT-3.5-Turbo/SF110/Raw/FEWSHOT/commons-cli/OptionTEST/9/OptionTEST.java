package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;

    @Before
    public void setup() {
        option = new Option("opt", "description");
    }

    @Test
    public void testConstructorNoArgs() {
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasLongOpt());
    }

    @Test
    public void testConstructorWithArgs() {
        option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasLongOpt());
    }

    @Test
    public void testConstructorWithLongOpt() {
        option = new Option("opt", "longOpt", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertEquals("longOpt", option.getLongOpt());
        assertFalse(option.hasArg());
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testGetKeyWithOpt() {
        assertEquals("opt", option.getKey());
    }

    @Test
    public void testGetKeyWithLongOpt() {
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getKey());
    }

    @Test
    public void testSetType() {
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
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
    public void testHasOptionalArg() {
        assertFalse(option.hasOptionalArg());
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
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
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
    public void testAddValueForProcessingNoArgs() {
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessingWithArgs() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueAtIndex() {
        assertNull(option.getValue(0));
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        assertEquals("defaultValue", option.getValue("defaultValue"));
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
        option.setArgs(3);
        option.addValueForProcessing("value1,value2,value3");
        String[] expected = {"value1", "value2", "value3"};
        assertArrayEquals(expected, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertTrue(option.getValuesList().isEmpty());
        option.setArgs(3);
        option.addValueForProcessing("value1,value2,value3");
        List<String> expected = Arrays.asList("value1", "value2", "value3");
        assertEquals(expected, option.getValuesList());
    }

    @Test
    public void testToString() {
        String expected = "[ option: opt :: description ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testHasNoValues() {
        assertTrue(option.hasNoValues());
        option.addValueForProcessing("value");
        assertFalse(option.hasNoValues());
    }

    @Test
    public void testEqualsWithSameOption() {
        assertTrue(option.equals(option));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(option.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentOpt() {
        Option other = new Option("other", "description");
        assertFalse(option.equals(other));
    }

    @Test
    public void testEqualsWithDifferentLongOpt() {
        option = new Option("opt", "longOpt", true, "description");
        Option other = new Option("opt", "otherLongOpt", true, "description");
        assertFalse(option.equals(other));
    }

    @Test
    public void testEqualsWithEqualOptions() {
        option = new Option("opt", "longOpt", true, "description");
        Option other = new Option("opt", "longOpt", true, "description");
        assertTrue(option.equals(other));
    }

    @Test
    public void testHashCode() {
        assertEquals(3656, option.hashCode());
    }

    @Test(expected = RuntimeException.class)
    public void testCloneNotSupported() {
        option.clone();
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        assertFalse(option.getValuesList().isEmpty());
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueNotSupported() {
        option.addValue("value");
    }
}