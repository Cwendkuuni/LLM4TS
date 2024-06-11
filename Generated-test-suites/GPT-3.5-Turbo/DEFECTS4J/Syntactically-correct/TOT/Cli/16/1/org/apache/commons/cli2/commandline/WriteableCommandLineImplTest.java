import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.apache.commons.cli2.option.OptionImpl;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Create a root option
        OptionImpl rootOption = new OptionImpl("root", "Root Option");
        
        // Create some arguments
        ArgumentImpl arg1 = new ArgumentImpl();
        ArgumentImpl arg2 = new ArgumentImpl();
        
        // Create a list of arguments
        List arguments = new ArrayList();
        arguments.add(arg1);
        arguments.add(arg2);
        
        // Create the command line object
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Verify that the option was added successfully
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Add a value to the option
        commandLine.addValue(option, "value");
        
        // Get the values for the option
        List values = commandLine.getValues(option, null);
        
        // Verify that the value was added successfully
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Add a switch to the option
        commandLine.addSwitch(option, true);
        
        // Get the switch for the option
        Boolean bool = commandLine.getSwitch(option, null);
        
        // Verify that the switch was added successfully
        assertNotNull(bool);
        assertTrue(bool);
    }
    
    @Test
    public void testHasOption() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Verify that the command line has the option
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Get the option from the command line
        OptionImpl retrievedOption = (OptionImpl) commandLine.getOption("option");
        
        // Verify that the retrieved option is the same as the added option
        assertEquals(option, retrievedOption);
    }
    
    @Test
    public void testGetValues() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Create a list of default values
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        
        // Set the default values for the option
        commandLine.setDefaultValues(option, defaults);
        
        // Get the values for the option
        List values = commandLine.getValues(option, null);
        
        // Verify that the values are the same as the default values
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Add a value to the option
        commandLine.addValue(option, "value");
        
        // Get the undefaulted values for the option
        List values = commandLine.getUndefaultedValues(option);
        
        // Verify that the undefaulted values are the same as the added value
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Set the default switch for the option
        commandLine.setDefaultSwitch(option, false);
        
        // Get the switch for the option
        Boolean bool = commandLine.getSwitch(option, null);
        
        // Verify that the switch is the same as the default switch
        assertNotNull(bool);
        assertFalse(bool);
    }
    
    @Test
    public void testGetProperty() {
        // Set a property for the option
        commandLine.addProperty("option", "property", "value");
        
        // Get the property for the option
        String property = commandLine.getProperty("option", "property", null);
        
        // Verify that the property is the same as the added property
        assertEquals("value", property);
    }
    
    @Test
    public void testAddProperty() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Add a property to the option
        commandLine.addProperty(option, "property", "value");
        
        // Get the properties for the option
        Properties properties = (Properties) commandLine.optionToProperties.get(option);
        
        // Verify that the property was added successfully
        assertNotNull(properties);
        assertEquals("value", properties.getProperty("property"));
    }
    
    @Test
    public void testGetProperties() {
        // Set a property for the option
        commandLine.addProperty("option", "property", "value");
        
        // Get the properties for the option
        Set properties = commandLine.getProperties(new OptionImpl("option", "Option"));
        
        // Verify that the properties are not empty
        assertFalse(properties.isEmpty());
        assertTrue(properties.contains("property"));
    }
    
    @Test
    public void testLooksLikeOption() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Set the prefixes for the option
        option.setPrefixes("-");
        
        // Verify that the trigger looks like an option
        assertTrue(commandLine.looksLikeOption("-option"));
    }
    
    @Test
    public void testToString() {
        // Add some arguments to the command line
        commandLine.addOption(new ArgumentImpl("arg1", "Argument 1"));
        commandLine.addOption(new ArgumentImpl("arg2", "Argument 2"));
        
        // Convert the command line to a string
        String string = commandLine.toString();
        
        // Verify that the string is the same as the expected string
        assertEquals("arg1 arg2", string);
    }
    
    @Test
    public void testGetOptions() {
        // Create a list of options
        List options = new ArrayList();
        options.add(new OptionImpl("option1", "Option 1"));
        options.add(new OptionImpl("option2", "Option 2"));
        
        // Add the options to the command line
        for (int i = 0; i < options.size(); i++) {
            commandLine.addOption((OptionImpl) options.get(i));
        }
        
        // Get the options from the command line
        List retrievedOptions = commandLine.getOptions();
        
        // Verify that the retrieved options are the same as the added options
        assertEquals(options.size(), retrievedOptions.size());
        for (int i = 0; i < retrievedOptions.size(); i++) {
            assertEquals(options.get(i), retrievedOptions.get(i));
        }
    }
    
    @Test
    public void testGetOptionTriggers() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Get the option triggers from the command line
        Set triggers = commandLine.getOptionTriggers();
        
        // Verify that the triggers contain the added option
        assertTrue(triggers.contains("option"));
    }
    
    @Test
    public void testSetDefaultValues() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Create a list of default values
        List defaults = new ArrayList();
        defaults.add("default");
        
        // Set the default values for the option
        commandLine.setDefaultValues(option, defaults);
        
        // Get the values for the option
        List values = commandLine.getValues(option, null);
        
        // Verify that the values are the same as the default values
        assertEquals(1, values.size());
        assertEquals("default", values.get(0));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        // Create a new option
        OptionImpl option = new OptionImpl("option", "Option");
        
        // Set the default switch for the option
        commandLine.setDefaultSwitch(option, true);
        
        // Get the switch for the option
        Boolean bool = commandLine.getSwitch(option, null);
        
        // Verify that the switch is the same as the default switch
        assertNotNull(bool);
        assertTrue(bool);
    }
    
    @Test
    public void testGetNormalised() {
        // Create a list of arguments
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        
        // Get the normalised arguments from the command line
        List normalised = commandLine.getNormalised();
        
        // Verify that the normalised arguments are the same as the added arguments
        assertEquals(arguments.size(), normalised.size());
        for (int i = 0; i < normalised.size(); i++) {
            assertEquals(arguments.get(i), normalised.get(i));
        }
    }
}