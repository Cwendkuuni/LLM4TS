package org.apache.commons.cli;

import java.io.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class OptionTest {

    Option option;

    @Before
    public void setUp() throws Exception {
        option = new Option("o","Option");
    }

    @Test
    public void testGetOpt() {
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("option");
        assertEquals("option", option.getLongOpt());
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
        assertEquals("Option", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("New Option");
        assertEquals("New Option", option.getDescription());
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
    public void testGetArgName() {
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testSetArgName() {
        option.setArgName("argument");
        assertEquals("argument", option.getArgName());
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
    public void testGetValueSeparator() {
        assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertEquals(new ArrayList<String>(), option.getValuesList());
    }

    @Test
    public void testEquals() {
        Option option2 = new Option("o","Option");
        assertTrue(option.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option2 = new Option("o","Option");
        assertEquals(option.hashCode(), option2.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }

    @After
    public void tearDown() throws Exception {
        option = null;
    }

}