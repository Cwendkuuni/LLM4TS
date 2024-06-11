import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    
    private Option option;
    
    @Test
    public void testOptionConstructorWithDescription() {
        option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testOptionConstructorWithHasArg() {
        option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testOptionConstructorWithLongOpt() {
        option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertTrue(option.hasLongOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testGetId() {
        option = new Option("opt", "description");
        assertEquals(111, option.getId());
    }
    
    // Add more test cases for other methods
    
}