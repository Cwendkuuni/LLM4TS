import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    
    @Test
    public void testOptionConstructor1() {
        Option option = new Option("opt", "description");
        
        assertEquals("opt", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor2() {
        Option option = new Option("opt", true, "description");
        
        assertEquals("opt", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor3() {
        Option option = new Option("opt", "longOpt", true, "description");
        
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }

    // Add more test cases for other methods to cover edge cases and exception handling scenarios
    
}