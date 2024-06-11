package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testConstructor1() {
        Option option = new Option("f", "description");
        assertEquals("f", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testConstructor2() {
        Option option = new Option("f", true, "description");
        assertEquals("f", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testConstructor3() {
        Option option = new Option("f", "foo", true, "description");
        assertEquals("f", option.getOpt());
        assertEquals("foo", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new Option("f", "description");
        assertEquals(102, option.getId());
        option = new Option("foo", "description");
        assertEquals(102, option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("f", "description");
        assertEquals("f", option.getKey());
        option = new Option(null, "foo", true, "description");
        assertEquals("foo", option.getKey());
    }

    @Test
    public void testGetType() {
        Option option = new Option("f", "description");
        assertNull(option.getType());
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("f", "description");
        assertNull(option.getLongOpt());
        option.setLongOpt("foo");
        assertEquals("foo", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("f", "description");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("f", "description");
        assertFalse(option.hasLongOpt());
        option = new Option("f", true, "description");
        assertFalse(option.hasLongOpt());
        option = new Option("f", "foo", true, "description");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("f", "description");
        assertFalse(option.hasArg());
        option = new Option("f", true, "description");
        assertTrue(option.hasArg());
        option = new Option("f", "foo", true, "description");
        assertTrue(option.hasArg());
    }

    @Test
    public void testGetArgName() {
        Option option = new Option("f", "description");
        assertNull(option.getArgName());
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("f", "description");
        assertFalse(option.hasArgName());
        option.setArgName("arg");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("f", "description");
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
        option = new Option("f", true, "description");
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testGetValueSeparator() {
        Option option = new Option("f", "description");
        assertEquals('\0', option.getValueSeparator());
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option = new Option("f", "description");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("f", "description");
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgs() {
        Option option = new Option("f", "description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("f", true, "description");
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("f", "description");
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            option.addValue("value");
        });
        assertEquals("The addValue method is not intended for client use. Subclasses should use the addValueForProcessing method instead. ", exception.getMessage());
    }

    @Test
    public void testGetValue() {
        Option option = new Option("f", true, "description");
        assertNull(option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        Option option = new Option("f", true, "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            option.getValue(2);
        });
        assertEquals("Index: 2, Size: 2", exception.getMessage());
    }

    @Test
    public void testGetValueWithDefault() {
        Option option = new Option("f", true, "description");
        assertEquals("default", option.getValue("default"));
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("f", true, "description");
        assertNull(option.getValues());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] values = option.getValues();
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("f", true, "description");
        assertEquals(0, option.getValuesList().size());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("f", "description");
        assertEquals("[ option: f  :: description ]", option.toString());
        option = new Option("f", "foo", true, "description");
        assertEquals("[ option: f foo [ARG] :: description ]", option.toString());
        option.setArgs(2);
        assertEquals("[ option: f foo [ARG...] :: description ]", option.toString());
        option.setType(String.class);
        assertEquals("[ option: f foo [ARG...] :: description :: class java.lang.String ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("f", "description");
        assertTrue(option1.equals(option1));
        assertFalse(option1.equals(null));
        assertFalse(option1.equals(new Object()));

        Option option2 = new Option("f", "description");
        assertTrue(option1.equals(option2));
        assertTrue(option2.equals(option1));

        option1.setLongOpt("foo");
        assertFalse(option1.equals(option2));
        assertFalse(option2.equals(option1));

        option2.setLongOpt("foo");
        assertTrue(option1.equals(option2));
        assertTrue(option2.equals(option1));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("f", "description");
        Option option2 = new Option("f", "description");
        assertEquals(option1.hashCode(), option2.hashCode());

        option1.setLongOpt("foo");
        assertNotEquals(option1.hashCode(), option2.hashCode());

        option2.setLongOpt("foo");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("f", "description");
        Option option2 = (Option)option1.clone();
        assertEquals(option1, option2);
        assertNotSame(option1, option2);
        assertEquals(option1.getValuesList(), option2.getValuesList());
        assertNotSame(option1.getValuesList(), option2.getValuesList());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("f", true, "description");
        option.addValueForProcessing("value");
        option.clearValues();
        assertEquals(0, option.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueThrowsUnsupportedOperationException() {
        Option option = new Option("f", true, "description");
        option.addValue("value");
    }

}