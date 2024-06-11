package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {
    @Test
    public void testConstructorWithDescription() {
        Option option = new Option("a", "description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(-1, option.getArgs());
        assertEquals((int)'a', option.getId());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test
    public void testConstructorWithHasArg() {
        Option option = new Option("a", true, "description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(1, option.getArgs());
        assertEquals((int)'a', option.getId());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("a", "long", true, "description");
        assertEquals("a", option.getOpt());
        assertEquals("long", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(1, option.getArgs());
        assertEquals((int)'a', option.getId());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test
    public void testSetType() {
        Option option = new Option("a", "description");
        assertNull(option.getType());
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("a", "description");
        assertNull(option.getLongOpt());
        option.setLongOpt("long");
        assertEquals("long", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("a", "description");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("a", "description");
        assertFalse(option.hasLongOpt());
        option.setLongOpt("long");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("a", "description");
        assertFalse(option.hasArg());
        option.setArgs(1);
        assertTrue(option.hasArg());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("a", "description");
        assertEquals("description", option.getDescription());
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("a", "description");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("a", "description");
        assertNull(option.getArgName());
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("a", "description");
        assertFalse(option.hasArgName());
        option.setArgName("arg");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("a", "description");
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("a", "description");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("a", "description");
        assertEquals(-1, option.getArgs());
        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("a", "description");
        option.setArgs(2);
        option.addValueForProcessing("1");
        option.addValueForProcessing("2");
        assertEquals("1", option.getValue(0));
        assertEquals("2", option.getValue(1));
        assertNull(option.getValue(2));
        assertNull(option.getValue());
        assertNull(option.getValue("default"));
        assertNull(option.getValues());
        assertEquals(2, option.getValuesList().size());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("a", "description");
        try {
            option.addValue("value");
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("The addValue method is not intended for client use. Subclasses should use the addValueForProcessing method instead. ", e.getMessage());
        }
    }
}