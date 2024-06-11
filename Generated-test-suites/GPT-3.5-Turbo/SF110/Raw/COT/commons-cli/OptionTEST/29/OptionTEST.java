import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructor1() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testConstructor2() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testConstructor3() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
    }
    
    // Add more basic test cases for public methods
    
    @Test
    public void testGetValue() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue());
        
        option.addValue("value");
        assertEquals("value", option.getValue());
    }
    
    @Test
    public void testGetValueIndex() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue(0));
        
        option.addValue("value1");
        option.addValue("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("opt", "description");
        assertNull(option.getValues());
        
        option.addValue("value1");
        option.addValue("value2");
        String[] expectedValues = {"value1", "value2"};
        assertArrayEquals(expectedValues, option.getValues());
    }
    
    @Test
    public void testEquals() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Option option3 = new Option("opt1", "description");
        
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        Option option3 = new Option("opt1", "description");
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }
    
}