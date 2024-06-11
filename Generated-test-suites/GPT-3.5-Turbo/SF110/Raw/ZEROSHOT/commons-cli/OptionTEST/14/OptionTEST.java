import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {
    
    @Test
    public void testConstructor1() {
        String opt = "test";
        String description = "test option";
        Option option = new Option(opt, description);
        
        assertEquals(opt, option.getOpt());
        assertEquals(description, option.getDescription());
        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test
    public void testConstructor2() {
        String opt = "test";
        boolean hasArg = true;
        String description = "test option with arg";
        Option option = new Option(opt, hasArg, description);
        
        assertEquals(opt, option.getOpt());
        assertEquals(description, option.getDescription());
        assertNull(option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test
    public void testConstructor3() {
        String opt = "test";
        String longOpt = "testoption";
        boolean hasArg = true;
        String description = "test option with arg and long opt";
        Option option = new Option(opt, longOpt, hasArg, description);
        
        assertEquals(opt, option.getOpt());
        assertEquals(longOpt, option.getLongOpt());
        assertEquals(description, option.getDescription());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('\0', option.getValueSeparator());
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test
    public void testGetterSetterMethods() {
        Option option = new Option("test", "test option");
        
        // setType and getType methods
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
        
        // setLongOpt and getLongOpt methods
        option.setLongOpt("testoption");
        assertEquals("testoption", option.getLongOpt());
        
        // setOptionalArg and hasOptionalArg methods
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
        
        // hasLongOpt method
        assertTrue(option.hasLongOpt());
        
        // hasArg method
        assertFalse(option.hasArg());
        
        // setDescription and getDescription methods
        option.setDescription("option description");
        assertEquals("option description", option.getDescription());
        
        // setRequired and isRequired methods
        option.setRequired(true);
        assertTrue(option.isRequired());
        
        // setArgName and getArgName methods
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
        
        // hasArgName method
        assertTrue(option.hasArgName());
        
        // hasArgs method
        assertFalse(option.hasArgs());
        
        // setArgs and getArgs methods
        option.setArgs(2);
        assertTrue(option.hasArgs());
        assertEquals(2, option.getArgs());
        
        // setValueSeparator and getValueSeparator methods
        option.setValueSeparator('=');
        assertEquals('=', option.getValueSeparator());
        
        // hasValueSeparator method
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("test", "test option");
        
        try {
            option.addValueForProcessing("value1");
        } catch (RuntimeException ex) {
            assertEquals("NO_ARGS_ALLOWED", ex.getMessage());
        }
        
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }
    
    @Test
    public void testGetValue() {
        Option option = new Option("test", "test option");
        assertNull(option.getValue());
        
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertEquals("value1", option.getValue());
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        
        assertNull(option.getValue(2));
        assertNull(option.getValue(3));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("test", "test option");
        assertNull(option.getValues());
        
        option.setArgs(3);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertNull(option.getValues());
        
        option.addValueForProcessing("value3");
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, option.getValues());
    }
    
    @Test
    public void testToString() {
        Option option = new Option("test", "test option");
        assertEquals("[ option: test  :: test option ]", option.toString());
        
        option.setLongOpt("testoption");
        assertEquals("[ option: test testoption :: test option ]", option.toString());
        
        option.setArgs(2);
        assertEquals("[ option: test testoption [ARG...] :: test option ]", option.toString());
        
        option.setArgs(1);
        assertEquals("[ option: test testoption [ARG] :: test option ]", option.toString());
        
        option.setType(Integer.class);
        assertEquals("[ option: test testoption [ARG] :: test option :: java.lang.Integer ]", option.toString());
    }
    
    @Test
    public void testEquals() {
        Option option1 = new Option("test", "test option 1");
        Option option2 = new Option("test", "test option 2");
        Option option3 = new Option("test1", "test option 3");
        Option option4 = new Option("test", true, "test option 4");
        
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("test", "test option 1");
        Option option2 = new Option("test", "test option 2");
        Option option3 = new Option("test1", "test option 3");
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }
    
    @Test
    public void testClone() {
        Option option = new Option("test", "test option");
        option.setType(Integer.class);
        option.addValueForProcessing("value1");
        
        Option clonedOption = (Option)option.clone();
        
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.hasLongOpt(), clonedOption.hasLongOpt());
        assertEquals(option.hasArg(), clonedOption.hasArg());
        assertEquals(option.hasArgName(), clonedOption.hasArgName());
        assertEquals(option.hasArgs(), clonedOption.hasArgs());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
        
        assertNotSame(option, clonedOption);
    }
    
    @Test
    public void testClearValues() {
        Option option = new Option("test", "test option");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("test", "test option");
        option.addValue("value");
    }
}