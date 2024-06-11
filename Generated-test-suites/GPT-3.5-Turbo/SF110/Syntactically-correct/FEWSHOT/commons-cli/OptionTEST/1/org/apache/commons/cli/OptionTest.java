
package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class OptionTest {

    private Option option;

    @Before
    public void setup() {
        option = new Option("t", "testing option", true, "This is a testing option");
    }

    @Test
    public void testConstructor1() {
        assertNotNull(option);
        assertEquals("t", option.getOpt());
        assertEquals("testing option", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("This is a testing option", option.getDescription());
    }

    @Test
    public void testConstructor2() {
        Option option = new Option("t", false, "This is another testing option");
        assertNotNull(option);
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("This is another testing option", option.getDescription());
    }

    @Test
    public void testConstructor3() {
        Option option = new Option("t", "test", true, "This is a third testing option");
        assertNotNull(option);
        assertEquals("t", option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("This is a third testing option", option.getDescription());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_invalidOption() {
        Option option = new Option(null, "testing option", true, "This is an invalid testing option");
    }

    @Test
    public void testSetType() {
        option.setType("String");
        assertEquals("String", option.getType());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("test");
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
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
        assertEquals("This is a testing option", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("This is an updated testing option");
        assertEquals("This is an updated testing option", option.getDescription());
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
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertTrue(option.hasArgs());
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
    }

    @Test
    public void testAddValueForProcessing() {
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
        option.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test(expected=RuntimeException.class)
    public void testAddValueForProcessing_noArgsAllowed() {
        Option option = new Option("t", "testing option", false, "This option does not allow args");
        option.addValueForProcessing("value1");
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValue_index() {
        assertNull(option.getValue(0));
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValue_defaultValue() {
        assertNull(option.getValue("default"));
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertEquals(0, option.getValuesList().size());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }

    @Test
    public void testToString() {
        assertEquals("[ option: t test [ARG] :: This is a testing option ]", option.toString());
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}
