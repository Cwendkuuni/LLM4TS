
package org.apache.commons.cli;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("-t", "Test option");
    }

    @Test
    public void testGetId() {
        assertEquals('t', option.getId());
    }

    @Test
    public void testGetOpt() {
        assertEquals("-t", option.getOpt());
    }

    @Test
    public void testGetType() {
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Object type = new Object();
        option.setType(type);
        assertEquals(type, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());

        option.setLongOpt("test");
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("test");
        assertEquals("test", option.getLongOpt());
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

        option.setLongOpt("test");
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
        assertEquals("Test option", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("New description");
        assertEquals("New description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());

        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        assertNull(option.getArgName());

        option.setArgName("test");
        assertEquals("test", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        assertFalse(option.hasArgName());

        option.setArgName("test");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());

        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        assertEquals('\0', option.getValueSeparator());

        option.setValueSeparator(';');
        assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());

        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());

        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        option.setArgs(Option.UNINITIALIZED);
        option.addValueForProcessing("value");
    }

    @Test
    public void testProcessValue() {
        option.setArgs(2);
        option.setValueSeparator(',');

        option.addValueForProcessing("value1,value2");

        ArrayList<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        assertEquals(expected, option.getValuesList());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithListFull() {
        option.setArgs(2);

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test
    public void testGetValueWithNoValues() {
        assertNull(option.getValue());
    }

    @Test
    public void testGetValue() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        assertEquals("default", option.getValue("default"));

        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue("default"));
    }

    @Test
    public void testGetValuesWithNoValues() {
        assertNull(option.getValues());
    }

    @Test
    public void testGetValues() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        ArrayList<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        assertEquals(expected, option.getValuesList());
    }

    @Test
    public void testToString() {
        assertEquals("[ option: -t :: Test option ]", option.toString());

        option.setLongOpt("test");
        option.setArgs(2);
        option.setArgName("test");
        option.setOptionalArg(true);

        assertEquals("[ option: -t test [ARG...] :: Test option :: null ]", option.toString());
    }

    @Test
    public void testEqualsWithSameOption() {
        assertTrue(option.equals(option));
    }

    @Test
    public void testEqualsWithEqualOptions() {
        Option option2 = new Option("-t", "Test option");

        assertTrue(option.equals(option2));
    }

    @Test
    public void testEqualsWithDifferentOptions() {
        Option option2 = new Option("-t2", "Test option");

        assertFalse(option.equals(option2));
    }

    @Test
    public void testHashCodeWithEqualOptions() {
        Option option2 = new Option("-t", "Test option");

        assertEquals(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentOptions() {
        Option option2 = new Option("-t2", "Test option");

        assertNotEquals(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option clone = (Option) option.clone();

        assertEquals(option, clone);
        assertNotSame(option, clone);
    }

    @Test
    public void testClearValues() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        option.clearValues();

        assertTrue(option.hasNoValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}
