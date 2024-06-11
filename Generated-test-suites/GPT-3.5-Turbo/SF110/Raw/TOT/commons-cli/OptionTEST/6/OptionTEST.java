import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructor1() {
        Option option = new Option("a", "description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasOptionalArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(UNINITIALIZED, option.getArgs());
    }
    
    @Test
    public void testConstructor2() {
        Option option = new Option("a", true, "description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasOptionalArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testConstructor3() {
        Option option = new Option("a", "b", true, "description");
        assertEquals("a", option.getOpt());
        assertEquals("b", option.getLongOpt());
        assertTrue(option.hasArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasOptionalArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(1, option.getArgs());
    }
    
    @Test
    public void testSetType() {
        Option option = new Option("a", "description");
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
    }
    
    @Test
    public void testSetLongOpt() {
        Option option = new Option("a", "description");
        option.setLongOpt("b");
        assertEquals("b", option.getLongOpt());
        assertTrue(option.hasLongOpt());
    }
    
    @Test
    public void testSetOptionalArg() {
        Option option = new Option("a", "description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasOptionalArg() {
        Option option = new Option("a", "description");
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        Option option = new Option("a", "description");
        assertFalse(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = new Option("a", "description");
        assertFalse(option.hasArg());
        
        option.setArgName("arg");
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        Option option = new Option("a", "description");
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        Option option = new Option("a", "description");
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        Option option = new Option("a", "description");
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testSetRequired() {
        Option option = new Option("a", "description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetArgName() {
        Option option = new Option("a", "description");
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testHasArgName() {
        Option option = new Option("a", "description");
        assertFalse(option.hasArgName());
        
        option.setArgName("arg");
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        Option option = new Option("a", "description");
        assertFalse(option.hasArgs());
        
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        Option option = new Option("a", "description");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        Option option = new Option("a", "description");
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        Option option = new Option("a", "description");
        assertFalse(option.hasValueSeparator());
        
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testGetArgs() {
        Option option = new Option("a", "description");
        assertEquals(UNINITIALIZED, option.getArgs());
        
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingNoArgsAllowed() {
        Option option = new Option("a", "description");
        option.addValueForProcessing("value");
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingListFull() {
        Option option = new Option("a", "description");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }
    
    @Test
    public void testProcessValue() {
        Option option = new Option("a", "description");
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("a", "description");
        option.addValue("value1");
        assertEquals("value1", option.getValue());
        
        option.setArgs(2);
        option.addValue("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testGetValue() {
        Option option = new Option("a", "description");
        assertNull(option.getValue());
        
        option.addValue("value");
        assertEquals("value", option.getValue());
    }
    
    @Test
    public void testGetValueIndex() {
        Option option = new Option("a", "description");
        assertNull(option.getValue(0));
        
        option.addValue("value");
        assertEquals("value", option.getValue(0));
    }
    
    @Test
    public void testGetValueOrDefault() {
        Option option = new Option("a", "description");
        assertNull(option.getValue("default"));
        
        option.addValue("value");
        assertEquals("value", option.getValue("default"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("a", "description");
        assertNull(option.getValues());
        
        option.setArgs(2);
        option.addValue("value1");
        option.addValue("value2");
        
        String[] expected = {"value1", "value2"};
        assertArrayEquals(expected, option.getValues());
    }
    
    @Test
    public void testGetValuesList() {
        Option option = new Option("a", "description");
        assertNotNull(option.getValuesList());
        assertTrue(option.getValuesList().isEmpty());
        
        option.addValue("value1");
        option.addValue("value2");
        
        assertEquals(2, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }
    
    @Test
    public void testEquals() {
        Option option1 = new Option("a", "description");
        Option option2 = new Option("a", "description");
        assertTrue(option1.equals(option2));
        
        Option option3 = new Option("b", "description");
        assertFalse(option1.equals(option3));
        
        Option option4 = new Option("a", "description1");
        assertFalse(option1.equals(option4));
        
        option1.setLongOpt("lopt");
        assertFalse(option1.equals(option2));
        
        option2.setLongOpt("lopt");
        assertTrue(option1.equals(option2));
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("a", "description");
        Option option2 = new Option("a", "description");
        assertEquals(option1.hashCode(), option2.hashCode());
    }
    
    @Test
    public void testClone() {
        Option option1 = new Option("a", "description");
        option1.setArgs(2);
        option1.addValue("value1");
        
        Option option2 = (Option) option1.clone();
        assertEquals(option1.getOpt(), option2.getOpt());
        assertEquals(option1.getLongOpt(), option2.getLongOpt());
        assertEquals(option1.hasArg(), option2.hasArg());
        assertEquals(option1.hasLongOpt(), option2.hasLongOpt());
        assertEquals(option1.hasOptionalArg(), option2.hasOptionalArg());
        assertEquals(option1.getDescription(), option2.getDescription());
        assertEquals(option1.isRequired(), option2.isRequired());
        assertEquals(option1.getArgName(), option2.getArgName());
        assertEquals(option1.hasArgName(), option2.hasArgName());
        assertEquals(option1.hasArgs(), option2.hasArgs());
        assertEquals(option1.hasValueSeparator(), option2.hasValueSeparator());
        assertEquals(option1.getArgs(), option2.getArgs());
        assertArrayEquals(option1.getValues(), option2.getValues());
    }
    
    @Test
    public void testClearValues() {
        Option option = new Option("a", "description");
        option.setArgs(2);
        option.addValue("value1");
        option.addValue("value2");
        
        option.clearValues();
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValues());
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("a", "description");
        option.addValue("value");
    }
}