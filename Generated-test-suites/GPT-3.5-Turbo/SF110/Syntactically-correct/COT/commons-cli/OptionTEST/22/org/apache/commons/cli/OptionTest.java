
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructor1() {
        String opt = "o";
        String description = "description";
        Option option = new Option(opt, description);
        assertEquals(opt, option.getOpt());
        assertEquals(description, option.getDescription());
    }
    
    @Test
    public void testOptionConstructor2() {
        String opt = "o";
        boolean hasArg = true;
        String description = "description";
        Option option = new Option(opt, hasArg, description);
        assertEquals(opt, option.getOpt());
        assertEquals(hasArg, option.hasArg());
        assertEquals(description, option.getDescription());
    }
    
    @Test
    public void testOptionConstructor3() {
        String opt = "o";
        String longOpt = "option";
        boolean hasArg = true;
        String description = "description";
        Option option = new Option(opt, longOpt, hasArg, description);
        assertEquals(opt, option.getOpt());
        assertEquals(longOpt, option.getLongOpt());
        assertEquals(hasArg, option.hasArg());
        assertEquals(description, option.getDescription());
    }
    
    // Test for getId() method
    @Test
    public void testGetId() {
        Option option = new Option("o", "description");
        assertEquals('o', option.getId());
        
        option = new Option(null, "option", true, "description");
        assertEquals('o', option.getId());
    }
    
    // Test for getKey() method
    @Test
    public void testGetKey() {
        Option option = new Option("o", "description");
        assertEquals("o", option.getKey());
        
        option = new Option(null, "option", true, "description");
        assertEquals("option", option.getKey());
    }
    
    // Test for getType() and setType() methods
    @Test
    public void testType() {
        Option option = new Option("o", "description");
        assertNull(option.getType());
        
        Object type = new Object();
        option.setType(type);
        assertEquals(type, option.getType());
    }
    
    // Test for getLongOpt() and setLongOpt() methods
    @Test
    public void testLongOpt() {
        Option option = new Option("o", "description");
        assertNull(option.getLongOpt());
        
        String longOpt = "option";
        option.setLongOpt(longOpt);
        assertEquals(longOpt, option.getLongOpt());
    }
    
    // Test for hasOptionalArg() method
    @Test
    public void testHasOptionalArg() {
        Option option = new Option("o", "description");
        assertFalse(option.hasOptionalArg());
        
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    // Test for hasLongOpt() method
    @Test
    public void testHasLongOpt() {
        Option option = new Option("o", "description");
        assertFalse(option.hasLongOpt());
        
        option.setLongOpt("option");
        assertTrue(option.hasLongOpt());
    }
    
    // Test for hasArg() method
    @Test
    public void testHasArg() {
        Option option = new Option("o", "description");
        assertFalse(option.hasArg());
        
        option.setArgs(1);
        assertTrue(option.hasArg());
        
        option.setArgs(Option.UNLIMITED_VALUES);
        assertTrue(option.hasArg());
    }
    
    // Test for getDescription() and setDescription() methods
    @Test
    public void testDescription() {
        Option option = new Option("o", "description");
        assertEquals("description", option.getDescription());
        
        String description = "new description";
        option.setDescription(description);
        assertEquals(description, option.getDescription());
    }
    
    // Test for isRequired() and setRequired() methods
    @Test
    public void testRequired() {
        Option option = new Option("o", "description");
        assertFalse(option.isRequired());
        
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    // Test for getArgName() and setArgName() methods
    @Test
    public void testArgName() {
        Option option = new Option("o", "description");
        assertFalse(option.hasArgName());
        
        String argName = "arg";
        option.setArgName(argName);
        assertTrue(option.hasArgName());
        assertEquals(argName, option.getArgName());
    }
    
    // Test for hasArgs() method
    @Test
    public void testHasArgs() {
        Option option = new Option("o", "description");
        assertFalse(option.hasArgs());
        
        option.setArgs(2);
        assertTrue(option.hasArgs());
        
        option.setArgs(Option.UNLIMITED_VALUES);
        assertTrue(option.hasArgs());
    }
    
    // Test for getArgs() and setArgs() methods
    @Test
    public void testArgs() {
        Option option = new Option("o", "description");
        assertEquals(-1, option.getArgs());
        
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    // Test for getValueSeparator() and setValueSeparator() methods
    @Test
    public void testValueSeparator() {
        Option option = new Option("o", "description");
        assertEquals('\0', option.getValueSeparator());
        
        char sep = ':';
        option.setValueSeparator(sep);
        assertEquals(sep, option.getValueSeparator());
    }
    
    // TODO - Test for additional methods...
}

