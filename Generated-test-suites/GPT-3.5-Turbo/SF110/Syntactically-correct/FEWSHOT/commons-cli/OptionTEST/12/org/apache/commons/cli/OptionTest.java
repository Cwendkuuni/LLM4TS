import org.junit.Test;

import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructor1() {
        Option option = new Option("a", "description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(0, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals("[ option: a  :: description ]", option.toString());
    }

    @Test
    public void testOptionConstructor2() {
        Option option = new Option("a", true, "description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals("[ option: a [ARG] :: description ]", option.toString());
    }

    @Test
    public void testOptionConstructor3() {
        Option option = new Option("a", "long", true, "description");
        assertEquals("a", option.getOpt());
        assertEquals("long", option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertEquals("[ option: a long [ARG] :: description ]", option.toString());
    }

    @Test
    public void testOptionConstructorExceptions() {
        try {
            Option option = new Option(null, "description");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("opt", e.getMessage());
        }

        try {
            Option option = new Option("a", null, true, "description");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("option must have a long name", e.getMessage());
        }
    }

    @Test
    public void testGetId() {
        Option option = new Option("a", "description");
        assertEquals(97, option.getId());

        option = new Option("-a", "description");
        assertEquals(45, option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("a", "description");
        assertEquals("a", option.getKey());

        option = new Option("-a", "description");
        assertEquals("a", option.getKey());

        option = new Option(null, "long", true, "description");
        assertEquals("long", option.getKey());
    }

    @Test
    public void testGetType() {
        Option option = new Option("a", "description");
        assertNull(option.getType());

        Object obj = new Object();
        option.setType(obj);
        assertEquals(obj, option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("a", "description");
        
        Object obj = new Object();
        option.setType(obj);
        assertEquals(obj, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("a", "description");
        assertNull(option.getLongOpt());

        option.setLongOpt("long");
        assertEquals("long", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("a", "description");

        option.setLongOpt("long");
        assertEquals("long", option.getLongOpt());
    }
    
    // Additional test cases for the remaining methods...
}