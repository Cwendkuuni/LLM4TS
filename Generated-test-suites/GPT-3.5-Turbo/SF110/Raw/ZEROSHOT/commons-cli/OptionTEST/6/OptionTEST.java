import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {
    Option option;
    
    @Before
    public void setUp() {
        option = new Option("opt", "description");
    }
    
    @Test
    public void testConstructor1() {
        assertEquals("opt", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(false, option.hasArg());
        assertEquals("description", option.getDescription());
        assertEquals(false, option.isRequired());
    }
    
    @Test
    public void testConstructor2() {
        option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(true, option.hasArg());
        assertEquals("description", option.getDescription());
        assertEquals(false, option.isRequired());
    }
    
    @Test
    public void testConstructor3() {
        option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(true, option.hasArg());
        assertEquals("description", option.getDescription());
        assertEquals(false, option.isRequired());
    }
    
    @Test
    public void testGetId() {
        assertEquals(111, option.getId()); // ASCII value of char 'o'
    }
    
    @Test
    public void testGetKey() {
        assertEquals("opt", option.getKey());
        
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getKey());
    }
    
    @Test
    public void testSetAndGetLongOpt() {
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }
    
    @Test
    public void testSetAndGetOptionalArg() {
        option.setOptionalArg(true);
        assertEquals(true, option.hasOptionalArg());
    }
    
    @Test
    public void testHasLongOpt() {
        assertEquals(false, option.hasLongOpt());
        
        option.setLongOpt("longOpt");
        assertEquals(true, option.hasLongOpt());
    }
    
    @Test
    public void testHasArg() {
        assertEquals(false, option.hasArg());
        
        option.setArgs(1);
        assertEquals(true, option.hasArg());
        
        option.setArgs(Option.UNLIMITED_VALUES);
        assertEquals(true, option.hasArg());
    }
    
    @Test
    public void testSetAndGetDescription() {
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }
    
    @Test
    public void testSetAndGetRequired() {
        option.setRequired(true);
        assertEquals(true, option.isRequired());
    }
    
    @Test
    public void testSetAndGetArgName() {
        option.setArgName("newArg");
        assertEquals("newArg", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        assertEquals(false, option.hasArgName());
        
        option.setArgName("newArg");
        assertEquals(true, option.hasArgName());
    }
    
    @Test
    public void testHasArgs() {
        assertEquals(false, option.hasArgs());
        
        option.setArgs(2);
        assertEquals(true, option.hasArgs());
        
        option.setArgs(Option.UNLIMITED_VALUES);
        assertEquals(true, option.hasArgs());
    }
    
    @Test
    public void testSetAndGetArgs() {
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testHasValueSeparator() {
        assertEquals(false, option.hasValueSeparator());
        
        option.setValueSeparator(',');
        assertEquals(true, option.hasValueSeparator());
    }
    
    @Test
    public void testAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testGetValue() {
        assertEquals(null, option.getValue());
        
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }
    
    @Test
    public void testGetValueWithIndex() {
        assertEquals(null, option.getValue(0));
        
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue(0));
    }
    
    @Test
    public void testGetValueWithDefaultValue() {
        assertEquals("default", option.getValue("default"));
        
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue("default"));
    }
    
    @Test
    public void testGetValues() {
        assertEquals(null, option.getValues());
        
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }
    
    @Test
    public void testGetValuesList() {
        assertEquals(new ArrayList<String>(), option.getValuesList());
        
        option.addValueForProcessing("value1");
        ArrayList<String> values = new ArrayList<>();
        values.add("value1");
        assertEquals(values, option.getValuesList());
    }
    
    @Test
    public void testToString() {
        option.setArgs(2);
        option.setArgName("ARG");
        option.setType("type");
        
        String expected = "[ option: opt [ARG...] :: description :: type ]";
        assertEquals(expected, option.toString());
    }
    
    @Test
    public void testClone() {
        option.setArgs(2);
        option.setValueSeparator(',');
        option.addValueForProcessing("value1,value2");
        
        Option clonedOption = (Option)option.clone();
        assertNotSame(option, clonedOption);
        assertArrayEquals(option.getValues(), clonedOption.getValues());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
    }
    
    @Test
    public void testClearValues() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        
        option.clearValues();
        assertEquals(new ArrayList<String>(), option.getValuesList());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}