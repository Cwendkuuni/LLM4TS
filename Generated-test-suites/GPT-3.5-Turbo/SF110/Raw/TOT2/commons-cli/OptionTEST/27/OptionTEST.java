import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructorWithDescription() {
        Option option = new Option("test", "Test description");
        
        assertEquals("test", option.getOpt());
        assertEquals("Test description", option.getDescription());
        assertFalse(option.hasLongOpt());
    }
    
    @Test
    public void testGetId() {
        Option option = new Option("t", "Test description");
        
        assertEquals('t', option.getId());
    }
    
    @Test
    public void testGetType() {
        Option option = new Option("test", "Test description");
        option.setType(Integer.class);
        
        assertEquals(Integer.class, option.getType());
    }
    
    // Add more test cases for the remaining methods
    
    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("test", "Test description");
        option.setArgs(2);
        
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingNoArgsAllowed() {
        Option option = new Option("test", "Test description");
        
        option.addValueForProcessing("value");
    }
    
    // Add more edge cases and exception handling scenarios test cases
    
}