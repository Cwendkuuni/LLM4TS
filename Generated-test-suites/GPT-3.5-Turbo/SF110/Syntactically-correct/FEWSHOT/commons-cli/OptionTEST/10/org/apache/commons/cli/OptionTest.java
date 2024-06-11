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
        option = new Option("t", "test", true, "test option");
    }

    @Test
    public void testGetOpt() {
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testGetType() {
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("test-option");
        assertEquals("test-option", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        assertTrue(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        assertEquals("test option", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("new test option");
        assertEquals("new test option", option.getDescription());
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
    public void testSetArgName() {
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator('=');
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator('=');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingNoArgsAllowed() {
        option.setArgs(Option.UNINITIALIZED);
        option.addValueForProcessing("value");
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingListFull() {
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }

    @Test
    public void testAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue(0));
        option.addValueForProcessing("value2");
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueAtIndex() {
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
        String[] values = option.getValues();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testGetValuesList() {
        assertTrue(option.getValuesList().isEmpty());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }

    @Test
    public void testToString() {
        assertEquals("[ option: t test [ARG] :: test option ]", option.toString());
    }

    @Test
    public void testHasNoValues() {
        assertTrue(option.hasNoValues());
        option.addValueForProcessing("value");
        assertFalse(option.hasNoValues());
    }

    @Test
    public void testEquals() {
        Option option2 = new Option("t", "test", true, "test option");
        assertTrue(option.equals(option2));
        assertTrue(option2.equals(option));
    }

    @Test
    public void testHashCode() {
        Option option2 = new Option("t", "test", true, "test option");
        assertEquals(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option2 = (Option) option.clone();
        assertTrue(option.equals(option2));
        assertFalse(option == option2);
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        assertFalse(option.hasNoValues());
        option.clearValues();
        assertTrue(option.hasNoValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}