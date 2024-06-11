import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class OptionTest {
    
    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("o", "description");
        assertNotNull(option);
        assertEquals("o", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testOptionConstructorWithHasArg() {
        Option option = new Option("o", true, "description");
        assertNotNull(option);
        assertEquals("o", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testOptionConstructorWithLongOpt() {
        Option option = new Option("o", "option", true, "description");
        assertNotNull(option);
        assertEquals("o", option.getOpt());
        assertEquals("option", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("o", "description");
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingNoArgsAllowed() {
        Option option = new Option("o", "description");
        option.setArgs(-1);
        option.addValueForProcessing("value1");
    }
    
    @Test
    public void testProcessValue() {
        Option option = new Option("o", "description");
        option.setArgs(2);
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        assertEquals(2, option.getValues().length);
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }
    
    @Test
    public void testClone() {
        Option option = new Option("o", "description");
        option.addValueForProcessing("value1");
        Option clone = (Option)option.clone();
        assertNotSame(option, clone);
        assertArrayEquals(option.getValues(), clone.getValues());
    }
}