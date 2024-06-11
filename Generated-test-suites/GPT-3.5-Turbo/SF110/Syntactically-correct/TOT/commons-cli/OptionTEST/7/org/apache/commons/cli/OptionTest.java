package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    Option option;

    @Before
    public void setUp() {
        option = new Option("opt", "optDescription");
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
        option.setType("type");
        assertEquals("type", option.getType());
    }

    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
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
        assertEquals("optDescription", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
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
        assertNull(option.getArgName());
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
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_NoArgsAllowed() {
        option.setArgs(Option.UNINITIALIZED);
        option.addValueForProcessing("value");
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_ListFull() {
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }

    @Test
    public void testAddValueForProcessing_ValueSeparator() {
        option.setArgs(2);
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testAddValue() {
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("The addValue method is not intended for client use. Subclasses should use the addValueForProcessing method instead.");
        option.addValue("value");
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Option option3 = new Option("opt1", "description");
        Option option4 = new Option("opt", "description1");
        Option option5 = new Option("opt", "description");
        option5.setLongOpt("longOpt");

        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
        assertFalse(option1.equals(option5));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option optionClone = (Option) option.clone();
        assertFalse(option.equals(optionClone));
        assertEquals(option.getOpt(), optionClone.getOpt());
        assertEquals(option.getDescription(), optionClone.getDescription());
        assertEquals(option.getType(), optionClone.getType());
        assertEquals(option.getValuesList(), optionClone.getValuesList());
    }
}