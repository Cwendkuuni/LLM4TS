import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class OptionTest {
    
    @Test
    public void testGetOpt() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
    }
    
    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testHasOptionalArg() {
        Option option = new Option("opt", "description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt", "longOpt", false, "description");
        assertTrue(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", false, "description");
        Option option3 = new Option("opt", true, "description");
        assertTrue(!option1.hasArg());
        assertTrue(!option2.hasArg());
        assertTrue(option3.hasArg());
    }
    
    // Additional test cases for edge cases and error scenarios should be added here
    
}