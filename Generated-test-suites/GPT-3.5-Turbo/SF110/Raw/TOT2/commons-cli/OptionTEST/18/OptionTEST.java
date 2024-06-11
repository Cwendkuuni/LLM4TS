import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("a", "description");
        assertEquals("a", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasArg());
    }

    @Test
    public void testOptionConstructorWithArgAndDescription() {
        Option option = new Option("b", true, "description");
        assertEquals("b", option.getOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testOptionConstructorWithLongOptAndArgAndDescription() {
        Option option = new Option("c", "long", true, "description");
        assertEquals("c", option.getOpt());
        assertEquals("long", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new Option("d", "description");
        assertEquals('d', option.getId());
    }

    // Add more test cases for other methods

}