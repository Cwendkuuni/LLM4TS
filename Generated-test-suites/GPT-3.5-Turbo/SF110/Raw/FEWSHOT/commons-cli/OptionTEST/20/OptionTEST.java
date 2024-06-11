package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionStringDescription() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertEquals(-1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals(-1, option.getArgs());
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionStringHasArgDescription() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionStringLongOptHasArgDescription() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testGetId() {
        Option option = new Option("opt", "description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option1 = new Option("opt", "description");
        assertEquals("opt", option1.getKey());
        Option option2 = new Option(null, "longOpt", true, "description");
        assertEquals("longOpt", option2.getKey());
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
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option1 = new Option("opt", "description");
        assertFalse(option1.hasLongOpt());
        Option option2 = new Option(null, "longOpt", true, "description");
        assertTrue(option2.hasLongOpt());
    }

    @Test
    public void testHasArgs() {
        Option option1 = new Option("opt", "description");
        assertFalse(option1.hasArgs());
        Option option2 = new Option("opt", true, "description");
        assertFalse(option2.hasArgs());
        Option option3 = new Option("opt", "longOpt", true, "description");
        assertFalse(option3.hasArgs());
        Option option4 = new Option("opt", "description");
        option4.setArgs(2);
        assertTrue(option4.hasArgs());
    }

    @Test
    public void testHasValueSeparator() {
        Option option1 = new Option("opt", "description");
        assertFalse(option1.hasValueSeparator());
        Option option2 = new Option("opt", "description");
        option2.setValueSeparator(',');
        assertTrue(option2.hasValueSeparator());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        assertEquals(Arrays.asList("value1"), option.getValuesList());
        option.addValueForProcessing("value2");
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }

    @Test
    public void testGetValue() {
        Option option1 = new Option("opt", "description");
        assertNull(option1.getValue());
        Option option2 = new Option("opt", "description");
        option2.addValueForProcessing("value1");
        assertEquals("value1", option2.getValue());
        Option option3 = new Option("opt", "description");
        option3.addValueForProcessing("value1");
        option3.addValueForProcessing("value2");
        assertEquals("value1", option3.getValue());
    }

    @Test
    public void testGetValueIndex() {
        Option option1 = new Option("opt", "description");
        assertNull(option1.getValue(0));
        Option option2 = new Option("opt", "description");
        option2.addValueForProcessing("value1");
        assertEquals("value1", option2.getValue(0));
        Option option3 = new Option("opt", "description");
        option3.addValueForProcessing("value1");
        option3.addValueForProcessing("value2");
        assertEquals("value1", option3.getValue(0));
        assertEquals("value2", option3.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        Option option1 = new Option("opt", "description");
        assertNull(option1.getValue("default"));
        Option option2 = new Option("opt", "description");
        option2.addValueForProcessing("value1");
        assertEquals("value1", option2.getValue("default"));
        Option option3 = new Option("opt", "description");
        option3.addValueForProcessing("value1");
        option3.addValueForProcessing("value2");
        assertEquals("value1", option3.getValue("default"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("opt", "description");
        assertNull(option1.getValues());
        Option option2 = new Option("opt", "description");
        option2.addValueForProcessing("value1");
        assertArrayEquals(new String[] { "value1" }, option2.getValues());
        Option option3 = new Option("opt", "description");
        option3.addValueForProcessing("value1");
        option3.addValueForProcessing("value2");
        assertArrayEquals(new String[] { "value1", "value2" }, option3.getValues());
    }

    @Test
    public void testToSTring() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("[ option: opt [ARG...] :: description ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt2", "description2");
        Option option3 = new Option("opt1", "description1");
        Option option4 = new Option("opt1", "description2");
        Option option5 = new Option("opt2", "description1");
        assertTrue(option1.equals(option1));
        assertFalse(option1.equals(null));
        assertFalse(option1.equals(""));
        assertFalse(option1.equals(option2));
        assertTrue(option1.equals(option3));
        assertFalse(option1.equals(option4));
        assertFalse(option1.equals(option5));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", null, true, "description");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("opt", "description");
        option1.addValueForProcessing("value1");
        Option option2 = (Option)option1.clone();
        assertEquals("opt", option2.getOpt());
        assertEquals("description", option2.getDescription());
        assertArrayEquals(new String[] { "value1" }, option2.getValues());
        assertNotSame(option1, option2);
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value1");
        option.clearValues();
        assertTrue(option.hasNoValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}