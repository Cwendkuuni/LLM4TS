package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("t", "Test description");
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
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testAddValueForProcessingWithNoArgsAllowed() {
        try {
            option.setValueSeparator(';');
            option.addValueForProcessing("value1;value2;value3");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("NO_ARGS_ALLOWED", e.getMessage());
        }
    }

    @Test
    public void testAddValueForProcessingWithArgs() {
        option.setArgs(3);
        option.addValueForProcessing("value1;value2;value3");
        assertEquals(3, option.getValues().length);
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testGetValueAtIndexOutOfBounds() {
        option.addValueForProcessing("value1");
        try {
            option.getValue(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 1, Size: 1", e.getMessage());
        }
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertNotSame(option, clonedOption);
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value1");
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueUnsupportedOperation() {
        option.addValue("value");
    }

}