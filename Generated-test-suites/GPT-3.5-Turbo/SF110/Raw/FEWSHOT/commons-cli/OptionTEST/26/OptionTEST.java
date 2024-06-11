package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class OptionTest {
    private Option option;

    @Before
    public void setUp() {
        option = new Option("opt", true, "description");
    }

    @Test
    public void testConstructor() {
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testConstructorWithLongOpt() {
        option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testGetKeyWithOpt() {
        assertEquals("opt", option.getKey());
    }

    @Test
    public void testGetKeyWithLongOpt() {
        option = new Option(null, "longOpt", true, "description");
        assertEquals("longOpt", option.getKey());
    }

    @Test
    public void testGetType() {
        assertNull(option.getType());
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
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        assertFalse(option.hasLongOpt());
        option = new Option("opt", "longOpt", true, "description");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        assertFalse(option.hasArg());
        option.setArgs(2);
        assertTrue(option.hasArg());
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
    public void testGetArgs() {
        assertEquals(1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testAddValueForProcessingWithSeparator() {
        option.setArgs(2);
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test (expected = RuntimeException.class)
    public void testAddValueForProcessingWithFullList() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2,value3");
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

        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        assertEquals("default", option.getValue("default"));

        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());

        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertEquals(new ArrayList<String>(), option.getValuesList());

        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertEquals(new ArrayList<String>(){{add("value1"); add("value2");}}, option.getValuesList());
    }

    @Test
    public void testToString() {
        assertEquals("[ option: opt [ARG] :: description ]", option.toString());

        option = new Option("opt", "longOpt", true, "description");
        assertEquals("[ option: opt longOpt [ARG] :: description ]", option.toString());
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(option.equals(null));
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(option.equals(option));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(option.equals(new String("option")));
    }

    @Test
    public void testEqualsWithDifferentOpt() {
        assertFalse(option.equals(new Option("opt2", true, "description")));
    }

    @Test
    public void testEqualsWithDifferentLongOpt() {
        assertFalse(option.equals(new Option("opt", "longOpt2", true, "description")));
    }

    @Test
    public void testEqualsWithEqualOptAndLongOpt() {
        assertTrue(option.equals(new Option("opt", true, "description")));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = option.getOpt() != null ? option.getOpt().hashCode() : 0;
        expectedHashCode = 31 * expectedHashCode + (option.getLongOpt() != null ? option.getLongOpt().hashCode() : 0);

        assertEquals(expectedHashCode, option.hashCode());
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option)option.clone();

        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.getType(), clonedOption.getType());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        option.clearValues();
        assertEquals(new ArrayList<String>(), option.getValuesList());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}