import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    
    @Before
    public void setUp() {
        // Create a root option with prefixes
        Option rootOption = new Option("root", "Root Option");
        rootOption.addTrigger("-");
        rootOption.addTrigger("--");
        
        // Create arguments
        List<String> arguments = new ArrayList<>();
        arguments.add("argument1");
        arguments.add("argument2");
        
        // Initialize command line
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("test", "Test Option");
        Object value = "testValue";
        
        commandLine.addValue(option, value);
        
        List<Object> values = commandLine.getValues(option, null);
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "Test Option");
        boolean value = true;
        
        commandLine.addSwitch(option, value);
        
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(value, switchValue);
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);
        
        Option retrievedOption = commandLine.getOption("test");
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("test", "Test Option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        
        Object value1 = "value1";
        Object value2 = "value2";
        
        commandLine.addOption(option);
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        
        List<Object> values = commandLine.getValues(option, defaultValues);
        
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
        assertEquals(defaultValues.get(0), values.get(2));
        assertEquals(defaultValues.get(1), values.get(3));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test", "Test Option");
        Object value1 = "value1";
        Object value2 = "value2";
        
        commandLine.addOption(option);
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        
        List<Object> values = commandLine.getUndefaultedValues(option);
        
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "Test Option");
        Boolean defaultValue = true;
        Boolean switchValue = false;
        
        commandLine.addOption(option);
        commandLine.addSwitch(option, switchValue);
        
        Boolean retrievedSwitchValue = commandLine.getSwitch(option, defaultValue);
        
        assertEquals(switchValue, retrievedSwitchValue);
    }
    
    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        
        commandLine.addProperty(property, value);
        
        String retrievedValue = commandLine.getProperty(property);
        
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("test", "Test Option");
        String property = "testProperty";
        String value = "testValue";
        
        commandLine.addProperty(option, property, value);
        
        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        Option option = new Option("test", "Test Option");
        String property = "testProperty";
        String defaultValue = "defaultValue";
        
        String retrievedValue = commandLine.getProperty(option, property, defaultValue);
        
        assertEquals(defaultValue, retrievedValue);
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("test", "Test Option");
        String property1 = "testProperty1";
        String property2 = "testProperty2";
        
        commandLine.addProperty(option, property1, "value1");
        commandLine.addProperty(option, property2, "value2");
        
        Set<String> properties = commandLine.getProperties(option);
        
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }
    
    @Test
    public void testGetPropertiesWithoutOption() {
        String property = "testProperty";
        
        Set<String> properties = commandLine.getProperties();
        
        assertFalse(properties.contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        boolean looksLikeOption = commandLine.looksLikeOption("-");
        
        assertTrue(looksLikeOption);
    }
    
    @Test
    public void testToString() {
        String argument1 = "argument1";
        String argument2 = "argument2";
        
        String expected = argument1 + " " + argument2;
        
        String toString = commandLine.toString();
        
        assertEquals(expected, toString);
    }
    
    @Test
    public void testGetOptions() {
        Option option = new Option("test", "Test Option");
        
        commandLine.addOption(option);
        
        List<Option> options = commandLine.getOptions();
        
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("test", "Test Option");
        
        commandLine.addOption(option);
        
        Set<String> triggers = commandLine.getOptionTriggers();
        
        assertTrue(triggers.contains("test"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "Test Option");
        List<Object> defaults = new ArrayList<>();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        
        commandLine.setDefaultValues(option, defaults);
        List<Object> retrievedDefaults = commandLine.getValues(option, null);
        
        assertEquals(defaults, retrievedDefaults);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "Test Option");
        Boolean defaultSwitch = true;
        
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);
        
        assertEquals(defaultSwitch, retrievedDefaultSwitch);
    }
    
    @Test
    public void testGetNormalised() {
        List<String> expected = new ArrayList<>();
        expected.add("argument1");
        expected.add("argument2");
        
        List<String> normalised = commandLine.getNormalised();
        
        assertEquals(expected, normalised);
    }
}