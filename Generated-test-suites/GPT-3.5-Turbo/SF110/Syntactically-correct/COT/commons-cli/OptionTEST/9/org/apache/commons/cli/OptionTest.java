
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;
    
    @Before
    public void setUp() {
        option = new Option("opt", true, "description");
    }
    
    @Test
    public void testGetOpt() {
        assertEquals("opt", option.getOpt());
    }
    
    @Test
    public void testHasArg() {
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("description", option.getDescription());
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
    public void testSetArgs() {
        option.setArgs(2);
        assertTrue(option.hasArgs());
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());
    }
}
