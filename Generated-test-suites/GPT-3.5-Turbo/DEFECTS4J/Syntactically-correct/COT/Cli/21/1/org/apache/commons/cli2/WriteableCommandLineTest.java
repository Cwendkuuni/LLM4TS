import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    
    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testAddOption() {
        Option option = new OptionImpl("test", "test option");
        
        commandLine.addOption(option);
        
        List<Option> options = commandLine.getOptions();
        
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new OptionImpl("test", "test option");
        Object value = "test value";
        
        commandLine.addValue(option, value);
        
        List<Object> values = commandLine.getValues(option);
        
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("test", "test option");
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        
        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
        
        assertTrue(undefaultedValues.contains(value1));
        assertTrue(undefaultedValues.contains(value2));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("test", "test option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default value");
        
        commandLine.setDefaultValues(option, defaultValues);
        
        List<Object> values = commandLine.getValues(option);
        
        assertTrue(values.containsAll(defaultValues));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new OptionImpl("test", "test option");
        
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("test", "test option");
        Boolean defaultSwitch = true;
        
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        Boolean switchValue = commandLine.getSwitch(option);
        
        assertEquals(defaultSwitch, switchValue);
    }
    
    @Test
    public void testAddPropertyOption() {
        Option option = new OptionImpl("test", "test option");
        String property = "property";
        String value = "value";
        
        commandLine.addProperty(option, property, value);
        
        String propertyValue = commandLine.getProperty(option, property);
        
        assertEquals(value, propertyValue);
    }
    
    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        
        commandLine.addProperty(property, value);
        
        String propertyValue = commandLine.getProperty(property);
        
        assertEquals(value, propertyValue);
    }
    
    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        
        boolean looksLikeOption = commandLine.looksLikeOption(argument);
        
        assertTrue(looksLikeOption);
    }
    
    @Test
    public void testSetCurrentOption() {
        Option option = new OptionImpl("test", "test option");
        
        commandLine.setCurrentOption(option);
        
        Option currentOption = commandLine.getCurrentOption();
        
        assertEquals(option, currentOption);
    }
}