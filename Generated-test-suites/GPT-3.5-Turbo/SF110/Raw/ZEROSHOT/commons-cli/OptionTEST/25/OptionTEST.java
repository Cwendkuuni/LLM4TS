import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class OptionTest {
    private Option option;
    
    @Before
    public void setUp() {
        option = new Option("opt", "description");
    }
    
    @Test
    public void testOptionConstructor() {
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("description", option.getDescription());
    }
    
    @Test
    public void testOptionConstructorWithHasArg() {
        Option optionWithArg = new Option("opt", true, "description");
        assertTrue(optionWithArg.hasArg());
    }
    
    @Test
    public void testOptionConstructorWithLongOpt() {
        Option optionWithLongOpt = new Option("opt", "longOpt", false, "description");
        assertTrue(optionWithLongOpt.hasLongOpt());
    }
    
    @Test
    public void testOptionHasOptionalArg() {
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testOptionHasArgName() {
        assertFalse(option.hasArgName());
        option.setArgName("argName");
        assertTrue(option.hasArgName());
    }
    
    @Test
    public void testOptionHasArgs() {
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }
    
    @Test
    public void testOptionGetValueSeparator() {
        assertEquals('\0', option.getValueSeparator());
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testOptionHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }
    
    @Test
    public void testOptionGetArgs() {
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingThrowsException() {
        option.addValueForProcessing("value");
    }
    
    @Test
    public void testOptionAddValueForProcessing() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueWithInvalidIndexThrowsException() {
        option.getValue(0);
    }
    
    @Test
    public void testOptionGetValueWithValidIndex() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }
    
    @Test
    public void testOptionGetValueWithDefaultValue() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue("default"));
        assertEquals("default", option.getValue(2, "default"));
    }
    
    @Test
    public void testOptionGetValuesList() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        ArrayList<String> valuesList = new ArrayList<String>();
        valuesList.add("value1");
        valuesList.add("value2");
        assertEquals(valuesList, option.getValuesList());
    }
    
    @Test
    public void testOptionToString() {
        String expectedToString = "[ option: opt  [ARG] :: description ]";
        assertEquals(expectedToString, option.toString());
    }
    
    @Test
    public void testOptionEquals() {
        Option equalOption = new Option("opt", "description");
        assertTrue(option.equals(option));
        assertTrue(option.equals(equalOption));
        assertFalse(option.equals(null));
        assertFalse(option.equals(new Object()));
        assertFalse(option.equals(new Option("different", "description")));
        assertFalse(option.equals(new Option("opt", "different")));
        option.setLongOpt("longOpt");
        assertFalse(option.equals(equalOption));
        equalOption.setLongOpt("longOpt");
        assertTrue(option.equals(equalOption));
    }
    
    @Test
    public void testOptionHashCode() {
        Option equalOption = new Option("opt", "description");
        assertEquals(option.hashCode(), equalOption.hashCode());
    }
    
    @Test
    public void testOptionClone() {
        Option cloneOption = (Option) option.clone();
        assertEquals(option.getOpt(), cloneOption.getOpt());
        assertEquals(option.getLongOpt(), cloneOption.getLongOpt());
        assertEquals(option.hasArg(), cloneOption.hasArg());
    }
    
    @Test
    public void testOptionClearValues() {
        option.setArgs(2);
        option.addValueForProcessing("value1,value2");
        assertFalse(option.hasNoValues());
        option.clearValues();
        assertTrue(option.hasNoValues());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueThrowsException() {
        option.addValue("value");
    }
}