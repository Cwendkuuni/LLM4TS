import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    
    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("t", "test", false, "Test option");
        
        assertEquals("t", option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Test option", option.getDescription());
    }
    
    @Test
    public void testOptionConstructorWithHasArg() {
        Option option = new Option("t", true, "Test option");
        
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Test option", option.getDescription());
    }
    
    @Test
    public void testOptionConstructorWithHasArgAndLongOpt() {
        Option option = new Option("t", "test", true, "Test option");
        
        assertEquals("t", option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Test option", option.getDescription());
    }
    
    @Test
    public void testGetId() {
        Option option = new Option("t", "test", true, "Test option");
        
        assertEquals(116, option.getId());
    }
    
    @Test
    public void testHasKey() {
        Option option = new Option("t", "test", true, "Test option");
        
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        Option option = new Option("t", "test", true, "Test option");
        
        assertEquals("Test option", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        Option option = new Option("t", "test", true, "Test option");
        option.setDescription("New description");
        
        assertEquals("New description", option.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        Option option = new Option("t", "test", true, "Test option");
        
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testSetRequired() {
        Option option = new Option("t", "test", true, "Test option");
        option.setRequired(true);
        
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetArgName() {
        Option option = new Option("t", "test", true, "Test option");
        option.setArgName("arg");
        
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        Option option = new Option("t", "test", true, "Test option");
        option.setArgName("arg");
        
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        Option option = new Option("t", "test", true, "Test option");
        option.setArgs(2);
        
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        Option option = new Option("t", "test", true, "Test option");
        option.setArgs(2);
        
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        Option option = new Option("t", "test", true, "Test option");
        option.setValueSeparator(',');
        
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        Option option = new Option("t", "test", true, "Test option");
        option.setValueSeparator(',');
        
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("t", "test", true, "Test option");
        option.add("value1");
        option.add("value2");
        
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals(2, option.getValues().length);
    }
    
    @Test
    public void testGetValue() {
        Option option = new Option("t", "test", true, "Test option");
        option.add("value1");
        option.add("value2");
        
        assertEquals("value1", option.getValue());
    }
    
    @Test
    public void testGetValueAtIndex() {
        Option option = new Option("t", "test", true, "Test option");
        option.add("value1");
        option.add("value2");
        
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("t", "test", true, "Test option");
        
        assertEquals("default", option.getValue("default"));
    }
    
    @Test
    public void testGetValuesList() {
        Option option = new Option("t", "test", true, "Test option");
        option.add("value1");
        option.add("value2");
        
        assertEquals(2, option.getValuesList().size());
    }
    
    @Test
    public void testToString() {
        Option option = new Option("t", "test", true, "Test option");
        
        assertEquals("[ option: t test [ARG] :: Test option ]", option.toString());
    }
    
    @Test
    public void testEquals() {
        Option option1 = new Option("t", "test", true, "Test option");
        Option option2 = new Option("t", "test", true, "Test option");
        
        assertTrue(option1.equals(option2));
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("t", "test", true, "Test option");
        Option option2 = new Option("t", "test", true, "Test option");
        
        assertEquals(option1.hashCode(), option2.hashCode());
    }
    
    @Test
    public void testClone() {
        Option option1 = new Option("t", "test", true, "Test option");
        Option option2 = (Option) option1.clone();
        
        assertEquals(option1, option2);
    }
    
    @Test
    public void testClearValues() {
        Option option = new Option("t", "test", true, "Test option");
        option.add("value1");
        option.add("value2");
        option.clearValues();
        
        assertTrue(option.getValuesList().isEmpty());
    }
    
}