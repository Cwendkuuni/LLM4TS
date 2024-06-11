package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() throws Exception {
        option = new Option("t", "test", true, "Test option");
    }

    @Test
    public void testGetOpt() {
        String opt = option.getOpt();
        assertEquals("t", opt);
    }

    @Test
    public void testGetLongOpt() {
        String longOpt = option.getLongOpt();
        assertEquals("test", longOpt);
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("newTest");
        String longOpt = option.getLongOpt();
        assertEquals("newTest", longOpt);
    }

    @Test
    public void testHasLongOpt() {
        boolean hasLongOpt = option.hasLongOpt();
        assertTrue(hasLongOpt);
    }

    @Test
    public void testHasArg() {
        boolean hasArg = option.hasArg();
        assertTrue(hasArg);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();
        assertEquals("Test option", description);
    }

    @Test
    public void testSetDescription() {
        option.setDescription("New test option");
        String description = option.getDescription();
        assertEquals("New test option", description);
    }

    @Test
    public void testIsRequired() {
        boolean isRequired = option.isRequired();
        assertFalse(isRequired);
    }

    @Test
    public void testSetRequired() {
        option.setRequired(true);
        boolean isRequired = option.isRequired();
        assertTrue(isRequired);
    }

    @Test
    public void testGetArgName() {
        String argName = option.getArgName();
        assertEquals("arg", argName);
    }

    @Test
    public void testHasArgName() {
        boolean hasArgName = option.hasArgName();
        assertTrue(hasArgName);
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        int numberOfArgs = option.getArgs();
        assertEquals(2, numberOfArgs);
    }

    @Test
    public void testGetValueSeparator() {
        char valueSeparator = option.getValueSeparator();
        assertEquals('\0', valueSeparator);
    }

    @Test
    public void testHasValueSeparator() {
        boolean hasValueSeparator = option.hasValueSeparator();
        assertFalse(hasValueSeparator);
    }

    @Test
    public void testAddValueForProcessing() {
        option.addValueForProcessing("value1");
        List<String> values = option.getValuesList();
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetValue() {
        String value = option.getValue();
        assertNull(value);
    }

    @Test
    public void testGetValueIndexOutOfBounds() {
        try {
            String value = option.getValue(0);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // success
        }
    }

    @Test
    public void testGetValueWithDefault() {
        String defaultValue = "defaultValue";
        String value = option.getValue(defaultValue);
        assertEquals(defaultValue, value);
    }

    @Test
    public void testGetValues() {
        String[] values = option.getValues();
        assertNull(values);
    }

    @Test
    public void testGetValuesList() {
        List<String> valuesList = option.getValuesList();
        assertNotNull(valuesList);
        assertEquals(0, valuesList.size());
    }

    @Test
    public void testToString() {
        String toString = option.toString();
        assertEquals("[ option: t test [ARG] :: Test option ]", toString);
    }

    @Test
    public void testEqualsSameObject() {
        boolean equals = option.equals(option);
        assertTrue(equals);
    }

    @Test
    public void testEqualsNullObject() {
        boolean equals = option.equals(null);
        assertFalse(equals);
    }

    @Test
    public void testEqualsDifferentClass() {
        boolean equals = option.equals("test");
        assertFalse(equals);
    }

    @Test
    public void testEqualsDifferentOpt() {
        Option otherOption = new Option("u", "test", true, "Test option");
        boolean equals = option.equals(otherOption);
        assertFalse(equals);
    }

    @Test
    public void testEqualsDifferentLongOpt() {
        Option otherOption = new Option("t", "newTest", true, "Test option");
        boolean equals = option.equals(otherOption);
        assertFalse(equals);
    }

    @Test
    public void testEqualsSameOptAndLongOpt() {
        Option otherOption = new Option("t", "test", true, "Test option");
        boolean equals = option.equals(otherOption);
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        int hashCode = option.hashCode();
        assertEquals(-2034688029, hashCode);
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.isRequired(), clonedOption.isRequired());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
    }
}