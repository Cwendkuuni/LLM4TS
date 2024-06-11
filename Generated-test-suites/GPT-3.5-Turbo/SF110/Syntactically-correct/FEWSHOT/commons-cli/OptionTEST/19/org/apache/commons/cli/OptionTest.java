
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    
    @Test
    public void testOptionConstructor1() {
        Option option = new Option("a", "desc");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("desc", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor2() {
        Option option = new Option("a", true, "desc");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("desc", option.getDescription());
    }
    
    @Test
    public void testOptionConstructor3() {
        Option option = new Option("a", "b", true, "desc");
        assertEquals("a", option.getOpt());
        assertEquals("b", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("desc", option.getDescription());
    }
    
    @Test
    public void testOptionGetId() {
        Option option = new Option("a", "desc");
        assertEquals('a', option.getId());
    }
    
    @Test
    public void testOptionGetKey() {
        Option option = new Option("a", "desc");
        assertEquals("a", option.getKey());
        
        option = new Option(null, "desc");
        assertEquals("desc", option.getKey());
    }
    
    @Test
    public void testOptionGetType() {
        Option option = new Option("a", "desc");
        assertNull(option.getType());
        
        option.setType("type");
        assertEquals("type", option.getType());
    }
    
    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("a", "desc");
        assertNull(option.getLongOpt());
        
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testOptionSetOptionalArg() {
        Option option = new Option("a", "desc");
        assertFalse(option.hasOptionalArg());
        
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("a", "desc");
        assertTrue(option.hasLongOpt());
        
        option = new Option("a", null, "desc");
        assertFalse(option.hasLongOpt());
    }
    
    @Test
    public void testOptionHasArg() {
        Option option = new Option("a", "desc");
        assertFalse(option.hasArg());
        
        option = new Option("a", true, "desc");
        assertTrue(option.hasArg());
        
        option = new Option("a", 2, "desc");
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testOptionGetDescription() {
        Option option = new Option("a", "desc");
        assertEquals("desc", option.getDescription());
        
        option.setDescription("newDesc");
        assertEquals("newDesc", option.getDescription());
    }
    
    @Test
    public void testOptionIsRequired() {
        Option option = new Option("a", "desc");
        assertFalse(option.isRequired());
        
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testOptionSetArgName() {
        Option option = new Option("a", "desc");
        assertFalse(option.hasArgName());
        
        option.setArgName("argName");
        assertTrue(option.hasArgName());
        assertEquals("argName", option.getArgName());
    }
    
    @Test
    public void testOptionHasArgs() {
        Option option = new Option("a", "desc");
        assertFalse(option.hasArgs());
        
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        Option option = new Option("a", "desc");
        assertFalse(option.hasValueSeparator());
        
        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testGetArgs() {
        Option option = new Option("a", "desc");
        assertEquals(-1, option.getArgs());
        
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingNoArgsAllowed() {
        Option option = new Option("a", "desc");
        option.addValueForProcessing("value");
    }
    
    @Test
    public void testProcessValue() {
        Option option = new Option("a", 2, "desc");
        
        option.addValueForProcessing("value1:value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testAdd() {
        Option option = new Option("a", 2, "desc");
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testGetValue() {
        Option option = new Option("a", "desc");
        
        assertNull(option.getValue());
        
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }
    
    @Test
    public void testGetValueIndexOutOfBounds() {
        Option option = new Option("a", 2, "desc");
        
        assertNull(option.getValue(0));
        
        option.addValueForProcessing("value");
        assertNull(option.getValue(1));
    }
    
    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("a", "desc");
        
        assertNull(option.getValue("defaultValue"));
        
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("defaultValue"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("a", 2, "desc");
        
        assertNull(option.getValues());
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }
    
    @Test
    public void testGetValuesList() {
        Option option = new Option("a", 2, "desc");
        
        assertTrue(option.getValuesList().isEmpty());
        
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }
    
    @Test
    public void testToString() {
        Option option = new Option("a", "desc");
        assertEquals("[ option: a :: desc ]", option.toString());
        
        option = new Option("a", "b", true, "desc");
        assertEquals("[ option: a b [ARG] :: desc ]", option.toString());
    }
    
    @Test
    public void testHasNoValues() {
        Option option = new Option("a", "desc");
        assertTrue(option.hasNoValues());
        
        option.addValueForProcessing("value");
        assertFalse(option.hasNoValues());
    }
    
    @Test
    public void testEquals() {
        Option option1 = new Option("a", "desc");
        Option option2 = new Option("a", "desc");
        Option option3 = new Option("b", "desc");
        
        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(null));
        assertFalse(option1.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        Option option1 = new Option("a", "desc");
        Option option2 = new Option("a", "desc");
        
        assertEquals(option1.hashCode(), option2.hashCode());
    }
    
    @Test
    public void testClone() {
        Option option1 = new Option("a", "desc");
        Option option2 = (Option)option1.clone();
        
        assertEquals(option1.getOpt(), option2.getOpt());
        assertEquals(option1.getLongOpt(), option2.getLongOpt());
        assertEquals(option1.getDescription(), option2.getDescription());
        assertEquals(option1.getType(), option2.getType());
        assertEquals(option1.getNumberOfArgs(), option2.getNumberOfArgs());
        assertEquals(option1.getValues(), option2.getValues());
    }
    
    @Test
    public void testClearValues() {
        Option option = new Option("a", "desc");
        
        option.addValueForProcessing("value");
        
        assertFalse(option.hasNoValues());
        
        option.clearValues();
        
        assertTrue(option.hasNoValues());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("a", "desc");
        
        option.addValue("value");
    }
}
