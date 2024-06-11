
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testConstructorWithDescription() {
        Option option = new Option("option", "This is a test option");
        assertEquals("option", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertEquals("This is a test option", option.getDescription());
    }
    
    @Test
    public void testConstructorWithHasArg() {
        Option option = new Option("option", true, "This is a test option");
        assertEquals("option", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(1, option.getArgs());
        assertEquals("This is a test option", option.getDescription());
    }
    
    @Test
    public void testConstructorWithLongOpt() {
        Option option = new Option("option", "longOption", false, "This is a test option");
        assertEquals("option", option.getOpt());
        assertEquals("longOption", option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertEquals("This is a test option", option.getDescription());
    }
    
    @Test
    public void testConstructorWithOptionalArg() {
        Option option = new Option("option", true, "This is a test option");
        option.setOptionalArg(true);
        assertEquals("option", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(1, option.getArgs());
        assertEquals("This is a test option", option.getDescription());
    }
    
    @Test
    public void testSetType() {
        Option option = new Option("option", "This is a test option");
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }
    
    @Test
    public void testSetLongOpt() {
        Option option = new Option("option", "This is a test option");
        option.setLongOpt("longOption");
        assertEquals("longOption", option.getLongOpt());
    }
    
    @Test
    public void testSetRequired() {
        Option option = new Option("option", "This is a test option");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetArgName() {
        Option option = new Option("option", "This is a test option");
        option.setArgName("argument");
        assertEquals("argument", option.getArgName());
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testSetArgs() {
        Option option = new Option("option", "This is a test option");
        option.setArgs(2);
        assertTrue(option.hasArgs());
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        Option option = new Option("option", "This is a test option");
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("option", true, "This is a test option");
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
        assertEquals(1, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue());
        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
    }
    
    @Test
    public void testClearValues() {
        Option option = new Option("option", true, "This is a test option");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        assertNull(option.getValue());
        assertEquals(0, option.getValues().length);
        assertTrue(option.hasNoValues());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("option", true, "This is a test option");
        option.addValue("value");
    }
    
    @Test
    public void testEquals() {
        Option option1 = new Option("option1", "This is a test option");
        Option option2 = new Option("option1", "This is a test option");
        Option option3 = new Option("option2", "This is a test option");
        Option option4 = new Option("option1", "This is another test option");
        
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("option1", "This is a test option");
        Option option2 = new Option("option1", "This is a test option");
        Option option3 = new Option("option2", "This is a test option");
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }
    
    @Test
    public void testClone() {
        Option option1 = new Option("option1", "This is a test option");
        option1.addValueForProcessing("value1");
        option1.addValueForProcessing("value2");
        
        Option option2 = (Option) option1.clone();
        
        assertTrue(option1.equals(option2));
        assertNotSame(option1, option2);
        assertArrayEquals(option1.getValues(), option2.getValues());
    }

    @Test
    public void testToString() {
        Option option = new Option("option", "This is a test option");
        assertEquals("[ option: option  [ARG] :: This is a test option ]", option.toString());
        
        option = new Option("option", "longOption", true, "This is a test option");
        option.setArgs(2);
        assertEquals("[ option: option longOption [ARG...] :: This is a test option ]", option.toString());
    }

}
