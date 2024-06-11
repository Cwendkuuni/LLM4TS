import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;
import java.util.*;

public class OptionTest {
    
    @Test
    public void testConstructor1() {
        Option option = new Option("opt", "description");
        
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
    }
    
    @Test
    public void testConstructor2() {
        Option option = new Option("opt", true, "description");
        
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
    }
    
    @Test
    public void testConstructor3() {
        Option option = new Option("opt", "longOpt", true, "description");
        
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_invalidOption() {
        Option option = new Option("opt=", "description");
    }
    
    @Test
    public void testGetId() {
        Option option = new Option("o", "description");
        
        assertEquals('o', option.getId());
    }
    
    @Test
    public void testGetKey() {
        Option option = new Option("opt", "description");
        
        assertEquals("opt", option.getKey());
        
        option = new Option(null, "longOpt", true, "description");
        
        assertEquals("longOpt", option.getKey());
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
    
    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testSetLongOpt() {
        Option option = new Option("opt", "description");
        
        option.setLongOpt("longOpt");
        
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", "description");
        
        assertFalse(option.hasOptionalArg());
        
        option.setOptionalArg(true);
        
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt", "description");
        
        assertFalse(option.hasLongOpt());
        
        option.setLongOpt("longOpt");
        
        assertTrue(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt2", true, "description");
        Option option3 = new Option("opt3", "longOpt", true, "description");
        
        assertFalse(option1.hasArg());
        assertTrue(option2.hasArg());
        assertTrue(option3.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        Option option = new Option("opt", "description");
        
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        Option option = new Option("opt", "description");
        
        option.setDescription("newDescription");
        
        assertEquals("newDescription", option.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        Option option = new Option("opt", "description");
        
        assertFalse(option.isRequired());
        
        option.setRequired(true);
        
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetArgName() {
        Option option = new Option("opt", "description");
        
        option.setArgName("argName");
        
        assertEquals("argName", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        Option option = new Option("opt", "description");
        
        assertFalse(option.hasArgName());
        
        option.setArgName("argName");
        
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt2", false, "description");
        Option option3 = new Option("opt3", "longOpt", false, "description");
        
        assertFalse(option1.hasArgs());
        assertFalse(option2.hasArgs());
        assertFalse(option3.hasArgs());
        
        option1.setArgs(3);
        option2.setArgs(2);
        option3.setArgs(4);
        
        assertTrue(option1.hasArgs());
        assertTrue(option2.hasArgs());
        assertTrue(option3.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        Option option = new Option("opt", "description");
        
        option.setArgs(3);
        
        assertEquals(3, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        Option option = new Option("opt", "description");
        
        option.setValueSeparator(';');
        
        assertEquals(';', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        Option option = new Option("opt", "description");
        
        assertFalse(option.hasValueSeparator());
        
        option.setValueSeparator(';');
        
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testGetArgs() {
        Option option = new Option("opt", "description");
        
        assertEquals(-1, option.getArgs());
        
        option.setArgs(3);
        
        assertEquals(3, option.getArgs());
    }
    
    @Test(expected=RuntimeException.class)
    public void testAddValueForProcessing_invalidNumberOfArgs() {
        Option option = new Option("opt", "description");
        
        option.addValueForProcessing("value");
    }
    
    @Test
    public void testProcessValue_noValueSeparator() {
        Option option = new Option("opt", "description");
        
        option.setArgs(2);
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testProcessValue_withValueSeparator() {
        Option option = new Option("opt", "description");
        
        option.setArgs(3);
        option.setValueSeparator(';');
        
        option.addValueForProcessing("value1;value2;value3");
        
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }
    
    @Test(expected=RuntimeException.class)
    public void testProcessValue_listFull() {
        Option option = new Option("opt", "description");
        
        option.setArgs(2);
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("opt", "description");
        
        try {
            option.addValue("value");
        } catch (UnsupportedOperationException e) {
            return;
        }
        
        fail("Expected UnsupportedOperationException is not thrown");
    }
    
}