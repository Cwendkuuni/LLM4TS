import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithoutArg() {
        Option option = new Option("a", "Description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Description", option.getDescription());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testOptionConstructorWithArg() {
        Option option = new Option("a", true, "Description");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Description", option.getDescription());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testOptionConstructorWithLongOpt() {
        Option option = new Option("a", "longOpt", true, "Description");
        assertEquals("a", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Description", option.getDescription());
        assertFalse(option.hasOptionalArg());
    }
    
    @Test
    public void testOptionConstructorWithLongOptAndOptionalArg() {
        Option option = new Option("a", "longOpt", true, "Description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testGetKeyWithOpt() {
        Option option = new Option("a", "Description");
        assertEquals("a", option.getKey());
    }
    
    @Test
    public void testGetKeyWithLongOpt() {
        Option option = new Option("a", "longOpt", true, "Description");
        assertEquals("longOpt", option.getKey());
    }
    
    @Test
    public void testGetType() {
        Option option = new Option("a", "Description");
        assertNull(option.getType());
    }
    
    @Test
    public void testSetType() {
        Option option = new Option("a", "Description");
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }
    
    @Test
    public void testHasLongOpt() {
        Option option = new Option("a", "Description");
        assertTrue(option.hasLongOpt());
    }
    
    @Test
    public void testHasNoLongOpt() {
        Option option = new Option("a", "Description");
        option.setLongOpt(null);
        assertFalse(option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasArg());
    }
    
    @Test
    public void testHasArgWithArgs() {
        Option option = new Option("a", true, "Description");
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testHasArgWithUnlimitedValues() {
        Option option = new Option("a", "Description");
        option.setArgs(Option.UNLIMITED_VALUES);
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testGetDescription() {
        Option option = new Option("a", "Description");
        assertEquals("Description", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        Option option = new Option("a", "Description");
        option.setDescription("New Description");
        assertEquals("New Description", option.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        Option option = new Option("a", "Description");
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testSetRequired() {
        Option option = new Option("a", "Description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testGetArgName() {
        Option option = new Option("a", "Description");
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testSetArgName() {
        Option option = new Option("a", "Description");
        option.setArgName("newArg");
        assertEquals("newArg", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasArgName());
    }
    
    @Test
    public void testHasArgNameWithValue() {
        Option option = new Option("a", "Description");
        option.setArgName("arg");
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasArgs());
    }
    
    @Test
    public void testHasArgsWithMultipleArgs() {
        Option option = new Option("a", "Description");
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testHasArgsWithUnlimitedValues() {
        Option option = new Option("a", "Description");
        option.setArgs(Option.UNLIMITED_VALUES);
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testHasValueSeparator() {
        Option option = new Option("a", "Description");
        assertFalse(option.hasValueSeparator());
    }
    
    @Test
    public void testHasValueSeparatorWithValue() {
        Option option = new Option("a", "Description");
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testGetArgs() {
        Option option = new Option("a", "Description");
        assertEquals(-1, option.getArgs());
    }
    
    @Test
    public void testGetArgsWithArgsSet() {
        Option option = new Option("a", "Description");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("a", "Description");
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithInvalidArgCount() {
        Option option = new Option("a", "Description");
        option.setArgs(1);
        option.setArgName("arg");
        option.addValueForProcessing("value1,value2");
    }
    
    @Test
    public void testGetValue() {
        Option option = new Option("a", "Description");
        assertNull(option.getValue());
    }
    
    @Test
    public void testGetValueWithValues() {
        Option option = new Option("a", "Description");
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }
    
    @Test
    public void testGetValueAtIndex() {
        Option option = new Option("a", "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value2", option.getValue(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAtIndexOutOfBounds() {
        Option option = new Option("a", "Description");
        option.getValue(0);
    }
    
    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("a", "Description");
        assertNull(option.getValue("default"));
    }
    
    @Test
    public void testGetValueWithValuesAndDefaultValue() {
        Option option = new Option("a", "Description");
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("default"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("a", "Description");
        assertNull(option.getValues());
    }
    
    @Test
    public void testGetValuesWithValues() {
        Option option = new Option("a", "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }
    
    @Test
    public void testGetValuesList() {
        Option option = new Option("a", "Description");
        assertNotNull(option.getValuesList());
        assertEquals(0, option.getValuesList().size());
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("a", "Description");
        assertFalse(option.addValue("value"));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueUnsupportedOperationException() {
        Option option = new Option("a", "Description");
        option.addValue("value");
    }
}