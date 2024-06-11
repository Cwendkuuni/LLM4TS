package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("opt", "description");
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
        option.setType(new Object());
        assertNotNull(option.getType());
    }

    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("longOpt");
        assertNotNull(option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
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
        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetValueSeparator() {
        assertEquals('\0', option.getValueSeparator());
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
        option.setArgs(4);
        assertEquals(4, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing() {
        option.addValueForProcessing("value");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test(expected = RuntimeException.class)
    public void testAddValue() {
        option.addValue("value");
        option.addValue("value2");
        option.addValue("value3");
    }
}