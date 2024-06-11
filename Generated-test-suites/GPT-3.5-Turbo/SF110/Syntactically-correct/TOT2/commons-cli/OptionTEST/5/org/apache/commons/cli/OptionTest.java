package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructor() {
        Option option = new Option("o", "Description");
        assertEquals("o", option.getOpt());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
    }

    @Test
    public void testOptionWithArgs() {
        Option option = new Option("o", "longOpt", true, "Description with args");
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
    }

    @Test
    public void testGetType() {
        Option option = new Option("o", "Description");
        assertNull(option.getType());
        option.setType("Type");
        assertEquals("Type", option.getType());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("o", "Description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("o", "Description");
        assertThrows(RuntimeException.class, () -> option.addValueForProcessing("value"));
    }

    @Test
    public void testGetValue() {
        Option option = new Option("o", "Description");
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
        assertThrows(IndexOutOfBoundsException.class, () -> option.getValue(1));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] values = option.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("o", "Description");
        Option option2 = new Option("o", "Description");
        Option option3 = new Option("o", "Different description");
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("o", "Description");
        Option option2 = new Option("o", "Description");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("o", "Description");
        Option option2 = (Option) option1.clone();
        assertEquals(option1.getOpt(), option2.getOpt());
        assertNotSame(option1, option2);
    }
}