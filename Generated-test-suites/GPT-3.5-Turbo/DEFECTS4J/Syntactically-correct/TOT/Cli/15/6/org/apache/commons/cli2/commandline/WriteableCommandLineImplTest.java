import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @BeforeEach
    void setUp() {
        // Create a root option and arguments
        Option rootOption = new Option("-r");
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        
        // Create a WriteableCommandLineImpl object
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    void addOption_shouldAddOptionToListAndMap() {
        // Create a new Option
        Option option = new Option("-o");
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Check if the option is added to the list of options
        assertTrue(commandLine.getOptions().contains(option));
        
        // Check if the option is added to the map of option triggers
        assertNotNull(commandLine.getOption("-o"));
    }

    @Test
    void addValue_shouldAddValueToListForGivenOption() {
        // Create a new Option
        Option option = new Option("-o");
        
        // Add a value to the option
        commandLine.addValue(option, "value1");
        
        // Check if the value is added to the list of values for the option
        assertTrue(commandLine.getValues(option, null).contains("value1"));
    }

    @Test
    void addSwitch_shouldAddSwitchToMapForGivenOption() {
        // Create a new Option
        Option option = new Option("-o");
        
        // Add a switch to the option
        commandLine.addSwitch(option, true);
        
        // Check if the switch is added to the map of switches for the option
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    void hasOption_shouldReturnTrueIfOptionIsPresent() {
        // Create a new Option
        Option option = new Option("-o");
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Check if the command line has the option
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    void getOption_shouldReturnOptionForGivenTrigger() {
        // Create a new Option
        Option option = new Option("-o");
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Get the option using its trigger
        Option retrievedOption = commandLine.getOption("-o");
        
        // Check if the retrieved option is the same as the added option
        assertEquals(option, retrievedOption);
    }
    
    @Test
    void getValues_shouldReturnValuesForGivenOption() {
        // Create a new Option
        Option option = new Option("-o");
        
        // Add some values to the option
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        // Get the values for the option
        List values = commandLine.getValues(option, null);
        
        // Check if the retrieved values are correct
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    void getUndefaultedValues_shouldReturnValuesForGivenOptionWithoutDefaultValues() {
        // Create a new Option
        Option option = new Option("-o");
        
        // Add some values to the option
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        // Get the undefaulted values for the option
        List values = commandLine.getUndefaultedValues(option);
        
        // Check if the retrieved undefaulted values are correct
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    void getSwitch_shouldReturnSwitchForGivenOptionWithDefaultValue() {
        // Create a new Option
        Option option = new Option("-o");
        
        // Add a switch to the option
        commandLine.addSwitch(option, true);
        
        // Get the switch for the option
        Boolean switchValue = commandLine.getSwitch(option, false);
        
        // Check if the retrieved switch value is correct
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    void getProperty_shouldReturnPropertyValueForGivenProperty() {
        // Add a property to the command line
        commandLine.addProperty("key1", "value1");
        
        // Get the property value for the given key
        String propertyValue = commandLine.getProperty("key1");
        
        // Check if the retrieved property value is correct
        assertEquals("value1", propertyValue);
    }

    @Test
    void addProperty_shouldAddPropertyToOptionProperties() {
        // Create a new PropertyOption and add it to the command line
        PropertyOption option = new PropertyOption("-p");
        commandLine.addOption(option);
        
        // Add a property to the option
        commandLine.addProperty(option, "key1", "value1");
        
        // Get the properties for the option
        Set properties = commandLine.getProperties(option);
        
        // Check if the property is added to the option's properties
        assertEquals(1, properties.size());
        assertTrue(properties.contains("key1"));
    }

    @Test
    void getProperty_shouldReturnPropertyValueForGivenOptionAndProperty() {
        // Create a new PropertyOption and add it to the command line
        PropertyOption option = new PropertyOption("-p");
        commandLine.addOption(option);
        
        // Add a property to the option
        commandLine.addProperty(option, "key1", "value1");
        
        // Get the property value for the given option and property
        String propertyValue = commandLine.getProperty(option, "key1", "defaultValue");
        
        // Check if the retrieved property value is correct
        assertEquals("value1", propertyValue);
    }

    @Test
    void looksLikeOption_shouldReturnTrueIfTriggerStartsWithPrefix() {
        // Set the prefixes for the root option
        Option rootOption = new Option("-r");
        rootOption.setPrefixes(Collections.singleton("-"));
        
        // Create a new WriteableCommandLineImpl object with the updated root option
        commandLine = new WriteableCommandLineImpl(rootOption, null);
        
        // Check if the trigger looks like an option
        assertTrue(commandLine.looksLikeOption("-r"));
    }

    @Test
    void toString_shouldReturnFormattedStringRepresentationOfCommandLine() {
        // Set the prefixes for the root option
        Option rootOption = new Option("-r");
        rootOption.setPrefixes(Collections.singleton("-"));
        
        // Create a new WriteableCommandLineImpl object with the updated root option
        commandLine = new WriteableCommandLineImpl(rootOption, null);
        
        // Add some arguments to the command line
        commandLine.getNormalised().add("arg1");
        commandLine.getNormalised().add("arg2");
        
        // Get the string representation of the command line
        String commandLineString = commandLine.toString();
        
        // Check if the formatted string representation is correct
        assertEquals("-r arg1 arg2", commandLineString);
    }

    @Test
    void getOptions_shouldReturnListOfAddedOptions() {
        // Create a new Option and add it to the command line
        Option option = new Option("-o");
        commandLine.addOption(option);
        
        // Get the list of options from the command line
        List options = commandLine.getOptions();
        
        // Check if the list of options is correct
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    void getOptionTriggers_shouldReturnSetOfOptionTriggers() {
        // Create a new Option and add it to the command line
        Option option = new Option("-o");
        commandLine.addOption(option);
        
        // Get the set of option triggers from the command line
        Set triggers = commandLine.getOptionTriggers();
        
        // Check if the set of option triggers is correct
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("-o"));
    }

    @Test
    void setDefaultValues_shouldSetDefaultValuesForGivenOption() {
        // Create a new Option and add it to the command line
        Option option = new Option("-o");
        commandLine.addOption(option);
        
        // Set default values for the option
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        
        // Get the default values for the option
        List retrievedDefaultValues = commandLine.getValues(option, null);
        
        // Check if the retrieved default values are correct
        assertEquals(2, retrievedDefaultValues.size());
        assertTrue(retrievedDefaultValues.contains("defaultValue1"));
        assertTrue(retrievedDefaultValues.contains("defaultValue2"));
    }

    @Test
    void setDefaultSwitch_shouldSetDefaultSwitchForGivenOption() {
        // Create a new Option and add it to the command line
        Option option = new Option("-o");
        commandLine.addOption(option);
        
        // Set a default switch for the option
        commandLine.setDefaultSwitch(option, true);
        
        // Get the default switch for the option
        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, false);
        
        // Check if the retrieved default switch value is correct
        assertEquals(Boolean.TRUE, retrievedDefaultSwitch);
    }

    @Test
    void getNormalised_shouldReturnNormalisedArguments() {
        // Set the prefixes for the root option
        Option rootOption = new Option("-r");
        rootOption.setPrefixes(Collections.singleton("-"));
        
        // Create a new WriteableCommandLineImpl object with the updated root option
        commandLine = new WriteableCommandLineImpl(rootOption, null);
        
        // Add some arguments to the command line
        commandLine.getNormalised().add("arg1");
        commandLine.getNormalised().add("arg2");
        
        // Get the normalised arguments from the command line
        List normalisedArguments = commandLine.getNormalised();
        
        // Check if the normalised arguments are correct
        assertEquals(2, normalisedArguments.size());
        assertTrue(normalisedArguments.contains("arg1"));
        assertTrue(normalisedArguments.contains("arg2"));
    }
}