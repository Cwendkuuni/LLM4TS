import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Option
        Option option = new Option.Builder("test").build();
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Check if the option is added successfully
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Argument
        Argument argument = new Argument.Builder("test").build();
        
        // Add a value to the argument
        commandLine.addValue(argument, "value");
        
        // Check if the value is added successfully
        assertTrue(commandLine.getValues(argument, null).contains("value"));
    }
    
    @Test
    public void testAddSwitch() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Option
        Option option = new Option.Builder("test").build();
        
        // Add a switch to the option
        commandLine.addSwitch(option, true);
        
        // Check if the switch is added successfully
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Option
        Option option = new Option.Builder("test").build();
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Check if the command line has the option
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Option
        Option option = new Option.Builder("test").build();
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Get the option by trigger
        Option retrievedOption = commandLine.getOption("test");
        
        // Check if the retrieved option is the same as the added option
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetValues() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Argument
        Argument argument = new Argument.Builder("test").build();
        
        // Add values to the argument
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        
        // Get the values of the argument
        List<String> values = commandLine.getValues(argument, null);
        
        // Check if the values are retrieved correctly
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Argument
        Argument argument = new Argument.Builder("test").build();
        
        // Add values to the argument
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        
        // Get the undefaulted values of the argument
        List<String> values = commandLine.getUndefaultedValues(argument);
        
        // Check if the values are retrieved correctly
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }
    
    @Test
    public void testGetSwitch() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Option
        Option option = new Option.Builder("test").build();
        
        // Add a switch to the option
        commandLine.addSwitch(option, true);
        
        // Get the switch value of the option
        Boolean switchValue = commandLine.getSwitch(option, null);
        
        // Check if the switch value is retrieved correctly
        assertTrue(switchValue);
    }
    
    @Test
    public void testGetProperty() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Get a property
        String property = commandLine.getProperty("property");
        
        // Check if the property is null
        assertNull(property);
    }
    
    @Test
    public void testAddProperty() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Add a property
        commandLine.addProperty("property", "value");
        
        // Get the property value
        String property = commandLine.getProperty("property");
        
        // Check if the property value is retrieved correctly
        assertEquals("value", property);
    }
    
    @Test
    public void testGetPropertyWithDefaultValue() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Get a property with a default value
        String property = commandLine.getProperty("property", "default");
        
        // Check if the property is retrieved correctly with the default value
        assertEquals("default", property);
    }
    
    @Test
    public void testGetProperties() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Get the properties
        Set<String> properties = commandLine.getProperties();
        
        // Check if the properties are empty
        assertTrue(properties.isEmpty());
    }
    
    @Test
    public void testLooksLikeOption() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Check if a string looks like an option
        boolean looksLikeOption = commandLine.looksLikeOption("-test");
        
        // Check if the string is recognized as an option
        assertTrue(looksLikeOption);
    }
    
    @Test
    public void testToString() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Convert the command line to a string
        String commandLineString = commandLine.toString();
        
        // Check if the command line string is not null
        assertNotNull(commandLineString);
    }
    
    @Test
    public void testGetOptions() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Get the options
        List<Option> options = commandLine.getOptions();
        
        // Check if the options list is not null
        assertNotNull(options);
    }
    
    @Test
    public void testGetOptionTriggers() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Get the option triggers
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        
        // Check if the option triggers set is not null
        assertNotNull(optionTriggers);
    }
    
    @Test
    public void testSetDefaultValues() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Option
        Option option = new Option.Builder("test").build();
        
        // Set default values for the option
        commandLine.setDefaultValues(option, Arrays.asList("value1", "value2"));
        
        // Get the default values of the option
        List<String> defaultValues = commandLine.getValues(option, null);
        
        // Check if the default values are set correctly
        assertEquals(Arrays.asList("value1", "value2"), defaultValues);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Create a new Option
        Option option = new Option.Builder("test").build();
        
        // Set a default switch for the option
        commandLine.setDefaultSwitch(option, true);
        
        // Get the default switch value of the option
        Boolean defaultSwitch = commandLine.getSwitch(option, null);
        
        // Check if the default switch value is set correctly
        assertTrue(defaultSwitch);
    }
    
    @Test
    public void testGetNormalised() {
        // Create a new WriteableCommandLineImpl instance
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        
        // Get the normalised arguments
        List<String> normalisedArguments = commandLine.getNormalised();
        
        // Check if the normalised arguments list is not null
        assertNotNull(normalisedArguments);
    }
}