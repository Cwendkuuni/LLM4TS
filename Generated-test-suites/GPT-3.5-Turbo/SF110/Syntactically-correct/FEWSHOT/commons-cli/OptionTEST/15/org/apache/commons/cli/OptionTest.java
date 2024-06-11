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
    public void setUp() {
        option = new Option("test", "description");
    }

    @Test
    public void testConstructorWithOptAndDescription() {
        assertEquals("test", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertEquals(-1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertEquals(0, option.getValuesList().size());
    }

    @Test
    public void testConstructorWithOptHasArgAndDescription() {
        option = new Option("test", true, "description");

        assertEquals("test", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertEquals(1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertEquals(0, option.getValuesList().size());
    }

    @Test
    public void testConstructorWithOptLongOptHasArgAndDescription() {
        option = new Option("test", "testLong", true, "description");

        assertEquals("test", option.getOpt());
        assertEquals("testLong", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertEquals(1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertEquals(0, option.getValuesList().size());
    }

    @Test
    public void testGetId() {
        assertEquals('t', option.getId());
    }

    @Test
    public void testGetKey() {
        assertEquals("test", option.getKey());
    }

    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("testLong");

        assertEquals("testLong", option.getLongOpt());
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
    public void testSetDescription() {
        option.setDescription("newDescription");

        assertEquals("newDescription", option.getDescription());
    }

    @Test
    public void testSetRequired() {
        option.setRequired(true);

        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        option.setArgName("newArgName");

        assertTrue(option.hasArgName());
        assertEquals("newArgName", option.getArgName());
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
        option.setValueSeparator(',');

        assertTrue(option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingThrowsExceptionWhenNoArgsAllowed() {
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testAddValue() {
        assertFalse(option.addValue("value"));
    }

    @Test
    public void testToString() {
        assertEquals("[ option: test :: description ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option otherOption = new Option("test", "description");
        assertTrue(option.equals(otherOption));
    }

    @Test
    public void testHashCode() {
        Option otherOption = new Option("test", "description");
        assertEquals(option.hashCode(), otherOption.hashCode());
    }

    @Test
    public void testClone() {
        Option cloneOption = (Option) option.clone();

        assertEquals(option.getOpt(), cloneOption.getOpt());
        assertEquals(option.getLongOpt(), cloneOption.getLongOpt());
        assertEquals(option.getDescription(), cloneOption.getDescription());
        assertEquals(option.getId(), cloneOption.getId());
        assertEquals(option.getType(), cloneOption.getType());
        assertEquals(option.getArgName(), cloneOption.getArgName());
        assertEquals(option.getArgs(), cloneOption.getArgs());
        assertEquals(option.getValueSeparator(), cloneOption.getValueSeparator());
        assertEquals(option.hasArg(), cloneOption.hasArg());
        assertEquals(option.hasOptionalArg(), cloneOption.hasOptionalArg());
        assertEquals(option.hasLongOpt(), cloneOption.hasLongOpt());
        assertEquals(option.hasArgName(), cloneOption.hasArgName());
        assertEquals(option.hasArgs(), cloneOption.hasArgs());
        assertEquals(option.hasValueSeparator(), cloneOption.hasValueSeparator());
        assertEquals(option.isRequired(), cloneOption.isRequired());
        assertEquals(option.getValuesList().size(), cloneOption.getValuesList().size());
        assertEquals(option.toString(), cloneOption.toString());
    }

    @Test
    public void testClearValues() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();

        assertEquals(0, option.getValuesList().size());
    }
}