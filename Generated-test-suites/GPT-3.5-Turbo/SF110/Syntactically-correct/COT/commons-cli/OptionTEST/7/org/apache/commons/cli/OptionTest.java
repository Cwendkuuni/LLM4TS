
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructorWithOptAndDescription() {
        Option option = new Option("a", "description");
        
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
    }

    @Test
    public void testConstructorWithOptHasArgAndDescription() {
        Option option = new Option("a", true, "description");
        
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
    }

    @Test
    public void testConstructorWithOptLongOptHasArgAndDescription() {
        Option option = new Option("a", "longOpt", true, "description");
        
        assertEquals("a", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
    }

    // Add more test cases for other methods in Option class

}

