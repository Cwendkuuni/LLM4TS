import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {
    
    private Option option;
    
    @Before
    public void setup() {
        option = new Option("test", "description");
    }
    
    @Test
    public void testGetId() {
        assertEquals('t', option.getId());
    }
    
    @Test
    public void testGetOpt() {
        assertEquals("test", option.getOpt());
    }
    
    @Test
    public void testGetType() {
        assertNull(option.getType());
    }
    
    @Test
    public void testSetType() {
        option.setType("type");
        assertEquals("type", option.getType());
    }
    
    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }
    
    @Test
    public void testSetLongOpt() {
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        assertFalse(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        assertFalse(option.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testSetRequired() {
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testSetArgName() {
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        assertFalse(option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(':');
        assertEquals(':', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
    }
    
    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
    }
    
    @Test(expected=RuntimeException.class)
    public void testAddValueForProcessingNoArgsAllowed() {
        option.addValueForProcessing("value");
    }
    
    @Test
    public void testAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        List<String> values = option.getValuesList();
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }
    
    @Test(expected=RuntimeException.class)
    public void testAddValueListFull() {
        option.setArgs(1);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
    }
    
    @Test
    public void testGetValue() {
        assertNull(option.getValue());
    }
    
    @Test
    public void testGetValueAtIndex() {
        assertNull(option.getValue(0));
    }
    
    @Test
    public void testGetValueWithDefaultValue() {
        assertEquals("defaultValue", option.getValue("defaultValue"));
    }
    
    @Test
    public void testGetValues() {
        assertNull(option.getValues());
    }
    
    @Test
    public void testGetValuesList() {
        List<String> values = option.getValuesList();
        assertNotNull(values);
        assertTrue(values.isEmpty());
    }
    
    @Test
    public void testToString() {
        assertEquals("[ option: test :: description ]", option.toString());
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(option.equals(option));
    }
    
    @Test
    public void testEqualsNull() {
        assertFalse(option.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(option.equals("test"));
    }
    
    @Test
    public void testEqualsDifferentOpt() {
        Option other = new Option("test2", "description");
        assertFalse(option.equals(other));
    }
    
    @Test
    public void testEqualsDifferentLongOpt() {
        Option other = new Option("test", "longOpt", false, "description");
        assertFalse(option.equals(other));
    }
    
    @Test
    public void testEqualsSameOptAndLongOpt() {
        Option other = new Option("test", "description2");
        assertTrue(option.equals(other));
    }
    
    @Test
    public void testHashCode() {
        Option other = new Option("test", "description");
        assertEquals(option.hashCode(), other.hashCode());
    }
    
    @Test
    public void testClone() {
        Option clone = (Option)option.clone();
        assertEquals(option.getOpt(), clone.getOpt());
        assertEquals(option.getDescription(), clone.getDescription());
        assertEquals(option.getType(), clone.getType());
        assertEquals(option.getLongOpt(), clone.getLongOpt());
        assertEquals(option.hasArgs(), clone.hasArgs());
        assertEquals(option.getArgs(), clone.getArgs());
    }
    
    @Test
    public void testClearValues() {
        option.addValueForProcessing("value");
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}