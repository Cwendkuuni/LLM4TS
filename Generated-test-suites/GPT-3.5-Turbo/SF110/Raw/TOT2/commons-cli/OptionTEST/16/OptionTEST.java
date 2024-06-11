import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructor() {
        Option option = new Option("opt", "description");
        assertNotNull(option);
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testOptionHasArgConstructor() {
        Option option = new Option("opt", true, "description");
        assertNotNull(option);
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testOptionFullConstructor() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertNotNull(option);
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
    }
    
    @Test
    public void testSetType() {
        Option option = new Option("opt", "description");
        option.setType("type");
        assertEquals("type", option.getType());
    }
    
    // Add more test cases for the remaining methods
    
}