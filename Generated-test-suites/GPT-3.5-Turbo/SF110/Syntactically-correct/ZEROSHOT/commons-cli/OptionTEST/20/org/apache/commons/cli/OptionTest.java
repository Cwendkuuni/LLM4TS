package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() throws IllegalArgumentException {
        option = new Option("opt", "description");
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
        Object type = new Object();
        option.setType(type);
        assertSame(type, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        String longOpt = "longOpt";
        option.setLongOpt(longOpt);
        assertEquals(longOpt, option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        assertFalse(option.hasLongOpt());
        option.setLongOpt("longOpt");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        assertFalse(option.hasArg());
        option.setArgs(1);
        assertTrue(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        String description = "newDescription";
        option.setDescription(description);
        assertEquals(description, option.getDescription());
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        String argName = "newArgName";
        option.setArgName(argName);
        assertEquals(argName, option.getArgName());
    }

    @Test
    public void testHasArgName() {
        assertFalse(option.hasArgName());
        option.setArgName("argName");
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
        int num = 2;
        option.setArgs(num);
        assertEquals(num, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        char sep = ',';
        option.setValueSeparator(sep);
        assertEquals(sep, option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
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
    public void testAddValueForProcessingWithMultipleValues() {
        option.setArgs(3);
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2,value3");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithListFull() {
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        assertNull(option.getValue(0));
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        String defaultValue = "defaultValue";
        assertEquals(defaultValue, option.getValue(defaultValue));
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue(defaultValue));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] expected = {"value1", "value2"};
        assertArrayEquals(expected, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        List<String> values = option.getValuesList();
        assertNotNull(values);
        assertTrue(values.isEmpty());
        option.addValueForProcessing("value1");
        values = option.getValuesList();
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testToString() {
        String expected = "[ option: opt description ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "newDescription");
        Option option3 = new Option("newOpt", "description");
        assertFalse(option.equals(null));
        assertFalse(option.equals(new Object()));
        assertTrue(option.equals(option));
        assertTrue(option.equals(option1));
        assertFalse(option.equals(option2));
        assertFalse(option.equals(option3));
    }

    @Test
    public void testHashCode() {
        option.setLongOpt("longOpt");
        int expected = option.getOpt().hashCode() + option.getLongOpt().hashCode();
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testClone() {
        option.addValueForProcessing("value");
        Option clonedOption = (Option) option.clone();
        assertNotSame(option, clonedOption);
        assertSame(option.getValuesList(), clonedOption.getValuesList());
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        assertFalse(option.getValuesList().isEmpty());
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}