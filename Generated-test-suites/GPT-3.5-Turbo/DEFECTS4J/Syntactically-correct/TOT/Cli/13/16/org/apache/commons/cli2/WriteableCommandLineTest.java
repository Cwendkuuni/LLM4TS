import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    
    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl();
    }
    
    // Test case for addOption() method
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        
        List<Option> options = commandLine.getOptions();
        
        assertTrue(options.contains(option));
    }
    
    // Test cases for addValue() method
    
    @Test
    public void testAddValue() {
        Object value = "value";
        
        commandLine.addValue(option, value);
        
        List<Object> values = commandLine.getValues(option);
        
        assertTrue(values.contains(value));
    }
    
    // Test case for addValue() method with null argument
    
    @Test(expected = NullPointerException.class)
    public void testAddValueWithNullArgument() {
        commandLine.addValue(null, "value");
    }
    
    // Test cases for getValues() method
    
    @Test
    public void testGetValues() {
        option.addArgument("arg1");
        option.addArgument("arg2");
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        List<Object> values = commandLine.getValues(option);
        
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }
    
    // Test case for setDefaultValues() method
    
    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        commandLine.setDefaultValues(option, defaultValues);
        
        List<Object> retrievedDefaultValues = commandLine.getDefaultValues(option);
        
        assertEquals(2, retrievedDefaultValues.size());
        assertTrue(retrievedDefaultValues.contains("default1"));
        assertTrue(retrievedDefaultValues.contains("default2"));
    }
    
    // Test case for addSwitch() method
    
    @Test
    public void testAddSwitch() {
        boolean switchValue = true;
        
        commandLine.addSwitch(option, switchValue);
        
        Boolean retrievedSwitchValue = commandLine.getSwitchValue(option);
        
        assertEquals(switchValue, retrievedSwitchValue);
    }
    
    // Test case for addSwitch() method with IllegalStateException
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithIllegalStateException() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
    
    // Test case for setDefaultSwitch() method
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        Boolean retrievedDefaultSwitch = commandLine.getDefaultSwitch(option);
        
        assertEquals(defaultSwitch, retrievedDefaultSwitch);
    }
    
    // Test case for addProperty() method
    
    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";
        
        commandLine.addProperty(property, value);
        
        String retrievedValue = commandLine.getProperty(property);
        
        assertEquals(value, retrievedValue);
    }
    
    // Test case for looksLikeOption() method
    
    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        
        boolean result = commandLine.looksLikeOption(argument);
        
        assertTrue(result);
    }
    
    // Test case for looksLikeOption() method with null argument
    
    @Test
    public void testLooksLikeOptionWithNullArgument() {
        boolean result = commandLine.looksLikeOption(null);
        
        assertFalse(result);
    }
}