package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    // Test cases for constructor Option(String opt, String description)
    @Test
    public void testOptionConstructor1() {
        Option option = new Option("a", "description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    // Test cases for constructor Option(String opt, boolean hasArg, String description)
    @Test
    public void testOptionConstructor2() {
        Option option = new Option("a", true, "description");
        assertEquals("a", option.getOpt());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    // Test cases for constructor Option(String opt, String longOpt, boolean hasArg, String description)
    @Test
    public void testOptionConstructor3() {
        Option option = new Option("a", "longOpt", true, "description");
        assertEquals("a", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }


    // Test cases for setArgName(String argName)
    @Test
    public void testSetArgName() {
        Option option = new Option("a", "description");
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    // Test cases for getArgName()
    @Test
    public void testGetArgName() {
        Option option = new Option("a", "description");
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    // Test cases for setArgs(int num)
    @Test
    public void testSetArgs() {
        Option option = new Option("a", "description");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    // Test cases for getValueSeparator()
    @Test
    public void testGetValueSeparator() {
        Option option = new Option("a", "description");
        option.setValueSeparator(':');
        assertEquals(':', option.getValueSeparator());
    }

    // Test cases for hasValueSeparator()
    @Test
    public void testHasValueSeparator() {
        Option option1 = new Option("a", "description");
        assertFalse(option1.hasValueSeparator());

        Option option2 = new Option("a", "description");
        option2.setValueSeparator(':');
        assertTrue(option2.hasValueSeparator());
    }

    // Test cases for addValueForProcessing(String value)
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_Exception() {
        Option option = new Option("a", "description");
        option.addValueForProcessing("value");
    }

    // Test cases for addValue(String value)
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue_Exception() {
        Option option = new Option("a", "description");
        option.addValue("value");
    }

    // Test cases for getValue()
    @Test
    public void testGetValue() {
        Option option = new Option("a", "description");
        option.addValue("value1");
        assertEquals("value1", option.getValue());
    }

    // More test cases for getValue()
    @Test
    public void testGetValue_MultipleValues() {
        Option option = new Option("a", "description");
        option.addValue("value1");
        option.addValue("value2");
        assertEquals("value1", option.getValue());
    }

    // Test cases for getValue(int index)
    @Test
    public void testGetValue_Index() {
        Option option = new Option("a", "description");
        option.addValue("value1");
        option.addValue("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    // More test cases for getValue(int index)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_Index_Exception() {
        Option option = new Option("a", "description");
        option.addValue("value1");
        option.getValue(1);
    }

    // Test cases for getValue(String defaultValue)
    @Test
    public void testGetValue_DefaultValue() {
        Option option = new Option("a", "description");
        assertEquals("defaultValue", option.getValue("defaultValue"));
    }

    // More test cases for getValue(String defaultValue)
    @Test
    public void testGetValue_DefaultValueWithValue() {
        Option option = new Option("a", "description");
        option.addValue("value");
        assertEquals("value", option.getValue("defaultValue"));
    }

    // Test cases for getValues()
    @Test
    public void testGetValues() {
        Option option = new Option("a", "description");
        option.addValue("value1");
        option.addValue("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    // More test cases for getValues()
    @Test
    public void testGetValues_NoValues() {
        Option option = new Option("a", "description");
        assertNull(option.getValues());
    }

    // Test cases for getValuesList()
    @Test
    public void testGetValuesList() {
        Option option = new Option("a", "description");
        option.addValue("value1");
        option.addValue("value2");
        assertEquals(2, option.getValuesList().size());
        assertTrue(option.getValuesList().contains("value1"));
        assertTrue(option.getValuesList().contains("value2"));
    }

    // Test cases for hasNoValues()
    @Test
    public void testHasNoValues() {
        Option option1 = new Option("a", "description");
        assertTrue(option1.hasNoValues());

        Option option2 = new Option("a", "description");
        option2.addValue("value");
        assertFalse(option2.hasNoValues());
    }

    // Test cases for equals(Object o)
    @Test
    public void testEquals() {
        Option option1 = new Option("a", "description");
        Option option2 = new Option("a", "description");
        assertTrue(option1.equals(option2));
    }

    // More test cases for equals(Object o)
    @Test
    public void testEquals_DifferentOpts() {
        Option option1 = new Option("a", "description");
        Option option2 = new Option("b", "description");
        assertFalse(option1.equals(option2));
    }

    // More test cases for equals(Object o)
    @Test
    public void testEquals_DifferentLongOpts() {
        Option option1 = new Option("a", "longOpt", true, "description");
        Option option2 = new Option("a", "differentLongOpt", true, "description");
        assertFalse(option1.equals(option2));
    }

    // Test cases for hashCode()
    @Test
    public void testHashCode() {
        Option option1 = new Option("a", "description");
        Option option2 = new Option("a", "description");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    // More test cases for hashCode()
    @Test
    public void testHashCode_DifferentOpts() {
        Option option1 = new Option("a", "description");
        Option option2 = new Option("b", "description");
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    // More test cases for hashCode()
    @Test
    public void testHashCode_DifferentLongOpts() {
        Option option1 = new Option("a", "longOpt", true, "description");
        Option option2 = new Option("a", "differentLongOpt", true, "description");
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    // Test cases for clone()
    @Test
    public void testClone() {
        Option option1 = new Option("a", "description");
        option1.addValue("value");
        Option option2 = (Option) option1.clone();
        assertEquals(option1.getOpt(), option2.getOpt());
        assertArrayEquals(option1.getValues(), option2.getValues());
    }

    // Test cases for addValueForProcessing(String value)
    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("a", true, "description");
        option.addValueForProcessing("value");
        assertArrayEquals(new String[]{"value"}, option.getValues());
    }
}