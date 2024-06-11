package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionTest {
    @Test
    public void testConstructorWithStringDescription() {
        Option option = new Option("f", "description");
        assertEquals("f", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getArgName());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(false, option.hasLongOpt());
        assertEquals(false, option.hasArg());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasArgName());
        assertEquals(false, option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertEquals(false, option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertNull(option.getValuesList());
    }

    @Test
    public void testConstructorWithArg() {
        Option option = new Option("f", true, "description");
        assertEquals("f", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getArgName());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(false, option.hasLongOpt());
        assertEquals(true, option.hasArg());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasArgName());
        assertEquals(false, option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals(false, option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertNull(option.getValuesList());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("f", "foo", false, "description");
        assertEquals("f", option.getOpt());
        assertEquals("foo", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getArgName());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(true, option.hasLongOpt());
        assertEquals(false, option.hasArg());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasArgName());
        assertEquals(false, option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertEquals(false, option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertNull(option.getValuesList());
    }

    @Test
    public void testSetAndGetLongOpt() {
        Option option = new Option("f", "foo", false, "description");
        option.setLongOpt("bar");
        assertEquals("bar", option.getLongOpt());
    }

    @Test
    public void testSetAndIsOptionalArg() {
        Option option = new Option("f", "description");
        option.setOptionalArg(true);
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("f", "description");
        option.setRequired(true);
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testSetAndGetArgName() {
        Option option = new Option("f", "description");
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
        assertEquals(true, option.hasArgName());
    }

    @Test
    public void testSetAndGetArgs() {
        Option option = new Option("f", "description");
        option.setArgs(2);
        assertEquals(true, option.hasArgs());
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("f", "description");
        option.setValueSeparator(',');
        assertEquals(true, option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testClearValues() {
        Option option = new Option("f", "description");
        option.addValueForProcessing("value");
        option.clearValues();
        assertEquals(true, option.hasNoValues());
        assertEquals(null, option.getValue());
        assertEquals(null, option.getValue(0));
        assertEquals(null, option.getValue("default"));
        assertEquals(null, option.getValues());
        assertEquals(0, option.getValuesList().size());
    }
}