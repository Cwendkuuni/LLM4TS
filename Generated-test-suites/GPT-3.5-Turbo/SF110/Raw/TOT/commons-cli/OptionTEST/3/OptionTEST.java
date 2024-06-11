import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructor1() {
        try {
            Option option = new Option("opt", "description");
            assertEquals("opt", option.getOpt());
            assertNull(option.getLongOpt());
            assertFalse(option.hasArg());
            assertFalse(option.hasOptionalArg());
            assertFalse(option.hasLongOpt());
            assertFalse(option.hasArgName());
            assertEquals("description", option.getDescription());
            assertFalse(option.isRequired());
            assertNull(option.getArgName());
            assertFalse(option.hasArgs());
            assertEquals(Option.UNINITIALIZED, option.getArgs());
            assertEquals(0, option.getValuesList().size());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown");
        }
    }
    
    @Test
    public void testConstructor2() {
        try {
            Option option = new Option("opt", true, "description");
            assertEquals("opt", option.getOpt());
            assertNull(option.getLongOpt());
            assertTrue(option.hasArg());
            assertFalse(option.hasOptionalArg());
            assertFalse(option.hasLongOpt());
            assertFalse(option.hasArgName());
            assertEquals("description", option.getDescription());
            assertFalse(option.isRequired());
            assertNull(option.getArgName());
            assertFalse(option.hasArgs());
            assertEquals(Option.UNINITIALIZED, option.getArgs());
            assertEquals(0, option.getValuesList().size());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown");
        }
    }
    
    @Test
    public void testConstructor3() {
        try {
            Option option = new Option("opt", "longOpt", true, "description");
            assertEquals("opt", option.getOpt());
            assertEquals("longOpt", option.getLongOpt());
            assertTrue(option.hasArg());
            assertFalse(option.hasOptionalArg());
            assertTrue(option.hasLongOpt());
            assertFalse(option.hasArgName());
            assertEquals("description", option.getDescription());
            assertFalse(option.isRequired());
            assertNull(option.getArgName());
            assertFalse(option.hasArgs());
            assertEquals(Option.UNINITIALIZED, option.getArgs());
            assertEquals(0, option.getValuesList().size());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown");
        }
    }
    
    @Test
    public void testGetId() {
        Option option = new Option("o");
        assertEquals('o', option.getId());
    }
    
    @Test
    public void testGetKey() {
        Option option = new Option("o");
        assertEquals("o", option.getKey());
        
        option = new Option("o", "longOpt", true, "description");
        assertEquals("o", option.getKey());
        
        option = new Option(null, "longOpt", true, "description");
        assertEquals("longOpt", option.getKey());
    }
    
    // Continue with tests for all the other methods
    
}