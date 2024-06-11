package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("t", "test", true, "testing option");
    }

    @Test
    public void testGetOpt() {
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testGetType() {
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        option.setType("String");
        assertNotNull(option.getType());
    }

    @Test
    public void testGetLongOpt() {
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("testing");
        assertEquals("testing", option.getLongOpt());
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
    }

    @Test
    public void testGetDescription() {
        assertEquals("testing option", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("new testing option");
        assertEquals("new testing option", option.getDescription());
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
        option.setArgName("argument");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testGetArgName() {
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetValueSeparator() {
        assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_ThrowsRuntimeException() {
        option.setArgs(1);
        option.addValueForProcessing("value1,value2");
    }

    @Test
    public void testGetValue() {
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testGetValue_IndexOutOfBoundsException() {
        assertNull(option.getValue(1));
    }

    @Test
    public void testGetValue_DefaultValue() {
        assertNull(option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        option.addValueForProcessing("value1,value2");
        String[] values = {"value1", "value2"};
        assertArrayEquals(values, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        option.addValueForProcessing("value1");
        List<String> valuesList = new ArrayList<>();
        valuesList.add("value1");
        assertEquals(valuesList, option.getValuesList());
    }

    @Test
    public void testToString() {
        assertEquals("[ option: t test [ARG] :: testing option ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option2 = new Option("t", "test", true, "testing option");
        assertTrue(option.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option2 = new Option("t", "test", true, "testing option");
        assertEquals(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        assertNotSame(option, clonedOption);
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value1");
        option.clearValues();
        assertEquals(0, option.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue_ThrowsUnsupportedOperationException() {
        option.addValue("value");
    }
}