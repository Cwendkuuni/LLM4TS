import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;

public class OptionTest {

    @Test
    public void testConstructor1() throws IllegalArgumentException {
        Option option = new Option("a", "test option");
        
        // Check the values of the option
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("test option", option.getDescription());
        assertFalse(option.hasArg());
    }
    
    @Test
    public void testConstructor2() throws IllegalArgumentException {
        Option option = new Option("a", true, "test option");
        
        // Check the values of the option
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("test option", option.getDescription());
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testConstructor3() throws IllegalArgumentException {
        Option option = new Option("a", "alias", true, "test option");
        
        // Check the values of the option
        assertEquals("a", option.getOpt());
        assertEquals("alias", option.getLongOpt());
        assertEquals("test option", option.getDescription());
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testGetId() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        
        // Check the IDs of the options
        assertEquals('a', option1.getId());
        assertEquals('b', option2.getId());
    }
    
    @Test
    public void testGetKey() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option(null, "test option 2");
        
        // Check the keys of the options
        assertEquals("a", option1.getKey());
        assertEquals("test option 2", option2.getKey());
    }
    
    @Test
    public void testGetType() {
        Option option = new Option("a", "test option");
        assertNull(option.getType());
        
        // Set the type of the option
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
    }
    
    @Test
    public void testSetLongOpt() {
        Option option = new Option("a", "test option");
        assertNull(option.getLongOpt());
        
        // Set the long option of the option
        option.setLongOpt("alias");
        assertEquals("alias", option.getLongOpt());
    }
    
    @Test
    public void testSetOptionalArg() {
        Option option = new Option("a", "test option");
        assertFalse(option.hasOptionalArg());
        
        // Set that the option has an optional argument
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2", true, "description");
        
        assertFalse(option1.hasLongOpt());
        assertTrue(option2.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2", true, "description");
        
        assertFalse(option1.hasArg());
        assertTrue(option2.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        Option option = new Option("a", "test option");
        assertEquals("test option", option.getDescription());
        
        // Set the description of the option
        option.setDescription("updated description");
        assertEquals("updated description", option.getDescription());
    }
    
    @Test
    public void testGetArgName() {
        Option option = new Option("a", "test option");
        assertNull(option.getArgName());
        
        // Set the argument name of the option
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testHasArgs() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2", true, "description");
        
        assertFalse(option1.hasArgs());
        assertTrue(option2.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        Option option = new Option("a", "test option");
        assertEquals(-1, option.getArgs());
        
        // Set the number of arguments of the option
        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }
    
    @Test
    public void testGetValueSeparator() {
        Option option = new Option("a", "test option");
        assertEquals('\0', option.getValueSeparator());
        
        // Set the value separator of the option
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2", true, "description");
        
        assertFalse(option1.hasValueSeparator());
        assertFalse(option2.hasValueSeparator());
        
        // Set the value separator of option2
        option2.setValueSeparator(',');
        assertTrue(option2.hasValueSeparator());
    }
    
    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("a", "test option");
        option.setArgs(2);
        
        // Add values to the option
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        // Check the values of the option
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testGetValue() {
        Option option = new Option("a", "test option");
        assertNull(option.getValue());
        
        // Add a value to the option
        option.addValueForProcessing("value");
        
        // Check the value of the option
        assertEquals("value", option.getValue());
    }
    
    @Test
    public void testGetValueWithIndex() {
        Option option = new Option("a", "test option");
        assertNull(option.getValue(0));
        
        // Add values to the option
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        // Check the values of the option
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("a", "test option");
        assertNull(option.getValues());
        
        // Add values to the option
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        // Check the values of the option
        assertArrayEquals(new String[] {"value1", "value2"}, option.getValues());
    }
    
    @Test
    public void testGetValuesList() {
        Option option = new Option("a", "test option");
        assertTrue(option.getValuesList().isEmpty());
        
        // Add values to the option
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        // Check the values of the option
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }
    
    @Test
    public void testToString() {
        Option option = new Option("a", "test option");
        assertEquals("[ option: a test option ]", option.toString());
        
        // Set the long option and argument name of the option
        option.setLongOpt("alias");
        option.setArgName("arg");
        
        // Check the string representation of the option
        assertEquals("[ option: a alias [ARG] :: test option :: null ]", option.toString());
    }
    
    @Test
    public void testEquals() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        Option option3 = new Option("a", "test option 3");
        Option option4 = new Option("a", "test option 1");
        
        // Check the equality of the options
        assertFalse(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertTrue(option1.equals(option4));
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");
        Option option3 = new Option("a", "test option 3");
        Option option4 = new Option("a", "test option 1");
        
        // Check the hash codes of the options
        assertNotEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
        assertEquals(option1.hashCode(), option4.hashCode());
    }
    
    @Test
    public void testClone() {
        Option option = new Option("a", "test option");
        option.addValueForProcessing("value");
        
        // Clone the option
        Option clonedOption = (Option) option.clone();
        
        // Check the values of the cloned option
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.getType(), clonedOption.getType());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.hasArgs(), clonedOption.hasArgs());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("a", "test option");
        
        // Add a value to the option
        option.addValue("value");
    }
    
    @Test
    public void testClearValues() {
        Option option = new Option("a", "test option");
        option.addValueForProcessing("value");
        assertFalse(option.getValuesList().isEmpty());
        
        // Clear the values of the option
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }
}