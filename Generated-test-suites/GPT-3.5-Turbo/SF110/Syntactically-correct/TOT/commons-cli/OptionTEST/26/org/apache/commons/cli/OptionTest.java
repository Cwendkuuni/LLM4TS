import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("o", "option", true, "This is an option");
    }

    @Test
    public void testGetOpt() {
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testGetLongOpt() {
        assertEquals("option", option.getLongOpt());
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
        assertEquals("This is an option", option.getDescription());
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
        assertFalse(option.hasArgName());
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        assertNull(option.getValue(0));
    }

    @Test
    public void testGetValueWithDefault() {
        assertEquals("default", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertNotNull(option.getValuesList());
    }

    @Test
    public void testToString() {
        String expected = "[ option: o option [ARG] :: This is an option ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testHasNoValues() {
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testEquals() {
        Option sameOption = new Option("o", "option", true, "This is an option");
        assertTrue(option.equals(sameOption));
    }

    @Test
    public void testHashCode() {
        Option sameOption = new Option("o", "option", true, "This is an option");
        assertEquals(option.hashCode(), sameOption.hashCode());
    }
}