package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionTest {
    @Test
    public void testConstructor1() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(false, option.hasLongOpt());
        assertEquals(false, option.hasArg());
        assertEquals("description", option.getDescription());
        assertEquals(false, option.isRequired());
        assertNull(option.getArgName());
        assertEquals(false, option.hasArgName());
        assertEquals(false, option.hasArgs());
        assertEquals(UNINITIALIZED, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals(null, option.getValue());
        assertEquals(null, option.getValue(0));
        assertEquals(null, option.getValue("defaultValue"));
        assertEquals(null, option.getValues());
        assertEquals(0, option.getValuesList().size());
    }

    @Test
    public void testConstructor2() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(false, option.hasLongOpt());
        assertEquals(true, option.hasArg());
        assertEquals("description", option.getDescription());
        assertEquals(false, option.isRequired());
        assertNull(option.getArgName());
        assertEquals(false, option.hasArgName());
        assertEquals(false, option.hasArgs());
        assertEquals(UNINITIALIZED, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals(null, option.getValue());
        assertEquals(null, option.getValue(0));
        assertEquals(null, option.getValue("defaultValue"));
        assertEquals(null, option.getValues());
        assertEquals(0, option.getValuesList().size());
    }

    @Test
    public void testConstructor3() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(true, option.hasLongOpt());
        assertEquals(true, option.hasArg());
        assertEquals("description", option.getDescription());
        assertEquals(false, option.isRequired());
        assertNull(option.getArgName());
        assertEquals(false, option.hasArgName());
        assertEquals(false, option.hasArgs());
        assertEquals(UNINITIALIZED, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals(null, option.getValue());
        assertEquals(null, option.getValue(0));
        assertEquals(null, option.getValue("defaultValue"));
        assertEquals(null, option.getValues());
        assertEquals(0, option.getValuesList().size());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("opt", "description");
        option.setType("type");
        assertEquals("type", option.getType());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("opt", "description");
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", "description");
        option.setOptionalArg(true);
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt", "description");
        assertEquals(false, option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("opt", "description");
        assertEquals(false, option.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("opt", "description");
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("opt", "description");
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("opt", "description");
        option.setRequired(true);
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("opt", "description");
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("opt", "description");
        assertEquals(false, option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("opt", "description");
        assertEquals(false, option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        assertEquals(true, option.hasArgs());
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasValueSeparator() {
        Option option = new Option("opt", "description");
        assertEquals(false, option.hasValueSeparator());
    }

    @Test
    public void testGetValueSeparator() {
        Option option = new Option("opt", "description");
        assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("opt", "description");
        boolean thrown = false;
        try {
            option.addValue("value");
        } catch (UnsupportedOperationException e) {
            thrown = true;
        }
        assertEquals(true, thrown);
    }
}