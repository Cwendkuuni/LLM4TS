import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private Option option;
    private List arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Setup common objects
        arguments = new ArrayList();
        option = new OptionImpl("test", true);
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue("Option should be added", commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        
        assertEquals("Value should be added", value, values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        
        assertNotNull("Switch should have a value", switchValue);
        assertTrue("Switch value should be true", switchValue);
    }
    
    @Test
    public void testHasOption() {
        assertFalse("Option should not be present", commandLine.hasOption(option));
        
        commandLine.addOption(option);
        assertTrue("Option should be present", commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option returnedOption = commandLine.getOption("test");
        assertNull("Option should not be present", returnedOption);
        
        commandLine.addOption(option);
        returnedOption = commandLine.getOption("test");
        assertEquals("Option should be returned", option, returnedOption);
    }
    
    @Test
    public void testGetValues() {
        List defaultValues = new ArrayList();
        defaultValues.add("default");
        
        List returnedValues = commandLine.getValues(option, defaultValues);
        assertEquals("Default values should be returned", defaultValues, returnedValues);
        
        Object value = "value";
        commandLine.addValue(option, value);
        returnedValues = commandLine.getValues(option, defaultValues);
        assertEquals("Added value should be returned", value, returnedValues.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Boolean defaultValue = true;
        Boolean returnedSwitch = commandLine.getSwitch(option, defaultValue);
        assertEquals("Default value should be returned", defaultValue, returnedSwitch);
        
        commandLine.addSwitch(option, true);
        returnedSwitch = commandLine.getSwitch(option, defaultValue);
        assertNotNull("Switch should have a value", returnedSwitch);
        assertTrue("Added switch value should be true", returnedSwitch);
    }
    
    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        
        commandLine.addProperty(property, value);
        String returnedValue = commandLine.getProperty(property, null);
        
        assertEquals("Property value should be added", value, returnedValue);
    }
    
    @Test
    public void testGetProperty() {
        String property = "property";
        String defaultValue = "default";
        
        String returnedValue = commandLine.getProperty(property, defaultValue);
        assertEquals("Default value should be returned", defaultValue, returnedValue);
        
        String value = "value";
        commandLine.addProperty(property, value);
        returnedValue = commandLine.getProperty(property, defaultValue);
        assertEquals("Added property value should be returned", value, returnedValue);
    }
    
    @Test
    public void testLooksLikeOption() {
        assertFalse("Trigger should not look like an option", commandLine.looksLikeOption("test"));
        
        option.addTrigger("test");
        assertTrue("Trigger should look like an option", commandLine.looksLikeOption("test"));
    }
    
    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        arguments.add(arg1);
        arguments.add(arg2);
        
        String expectedString = arg1 + " " + arg2;
        String returnedString = commandLine.toString();
        
        assertEquals("String representation should match", expectedString, returnedString);
    }
    
    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertTrue("Options list should be empty", options.isEmpty());
        
        commandLine.addOption(option);
        options = commandLine.getOptions();
        assertEquals("Options list should contain added option", option, options.get(0));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();
        assertTrue("Option triggers set should be empty", triggers.isEmpty());
        
        commandLine.addOption(option);
        triggers = commandLine.getOptionTriggers();
        assertTrue("Option triggers set should contain added trigger", triggers.contains("test"));
    }
    
    @Test
    public void testSetDefaultValues() {
        List defaults = new ArrayList();
        defaults.add("default");
        
        commandLine.setDefaultValues(option, defaults);
        List returnedDefaults = commandLine.getValues(option, null);
        assertEquals("Default values should be set", defaults, returnedDefaults);
        
        commandLine.setDefaultValues(option, null);
        returnedDefaults = commandLine.getValues(option, null);
        assertTrue("Default values should be reset", returnedDefaults.isEmpty());
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean returnedSwitch = commandLine.getSwitch(option, null);
        assertEquals("Default switch should be set", defaultSwitch, returnedSwitch);
        
        commandLine.setDefaultSwitch(option, null);
        returnedSwitch = commandLine.getSwitch(option, null);
        assertNull("Default switch should be null", returnedSwitch);
    }
    
    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertTrue("Normalised list should be empty", normalised.isEmpty());
        
        String arg1 = "arg1";
        String arg2 = "arg2";
        arguments.add(arg1);
        arguments.add(arg2);
        
        normalised = commandLine.getNormalised();
        assertEquals("Normalised list should contain added arguments", arguments, normalised);
    }
}