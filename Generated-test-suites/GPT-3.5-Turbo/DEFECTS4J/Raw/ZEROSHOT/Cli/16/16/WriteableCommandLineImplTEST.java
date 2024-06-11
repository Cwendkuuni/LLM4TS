import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create an instance of Option
        Option option = new Option();
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Assert that the option is added to the options list
        Assert.assertTrue(commandLine.getOptions().contains(option));
        
        // Assert that the option is added to the nameToOption map
        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
        
        // Assert that the option triggers are added to the nameToOption map
        for (Iterator i = option.getTriggers().iterator(); i.hasNext();) {
            Assert.assertEquals(option, commandLine.getOption(i.next()));
        }
    }
    
    @Test
    public void testAddValue() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create an instance of Option
        Option option = new Option();
        
        // Create a value to add
        String value = "value";
        
        // Add the value to the command line
        commandLine.addValue(option, value);
        
        // Assert that the value is added to the values map
        Assert.assertTrue(commandLine.getValues(option, null).contains(value));
    }
    
    @Test
    public void testAddSwitch() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create an instance of Option
        Option option = new Option();
        
        // Create a switch value to add
        boolean switchValue = true;
        
        // Add the switch value to the command line
        commandLine.addSwitch(option, switchValue);
        
        // Assert that the switch value is added to the switches map
        Assert.assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create an instance of Option
        Option option = new Option();
        
        // Add the option to the command line
        commandLine.addOption(option);
        
        // Assert that the command line has the option
        Assert.assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetProperty() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create a property name and value
        String property = "property";
        String value = "value";
        
        // Add the property to the command line
        commandLine.addProperty(property, value);
        
        // Assert that the property can be retrieved from the command line
        Assert.assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testAddProperty() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create an instance of Option
        Option option = new Option();
        
        // Create a property name and value
        String property = "property";
        String value = "value";
        
        // Add the property to the command line
        commandLine.addProperty(option, property, value);
        
        // Assert that the property is added to the optionToProperties map
        Assert.assertEquals(value, commandLine.getProperty(option, property, null));
    }
    
    @Test
    public void testLooksLikeOption() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Set the prefixes
        commandLine.prefixes = Collections.singleton("-");
        
        // Create a trigger that looks like an option
        String trigger1 = "-option";
        
        // Create a trigger that does not look like an option
        String trigger2 = "value";
        
        // Assert that the first trigger looks like an option
        Assert.assertTrue(commandLine.looksLikeOption(trigger1));
        
        // Assert that the second trigger does not look like an option
        Assert.assertFalse(commandLine.looksLikeOption(trigger2));
    }
    
    @Test
    public void testToString() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create a list of arguments
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        
        // Set the normalised list
        commandLine.normalised = arguments;
        
        // Assert that the toString method returns the concatenated list of arguments
        Assert.assertEquals("arg1 arg2", commandLine.toString());
    }
    
    @Test
    public void testSetDefaultValues() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create an instance of Option
        Option option = new Option();
        
        // Create a list of default values
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        // Set the default values for the option
        commandLine.setDefaultValues(option, defaultValues);
        
        // Assert that the default values can be retrieved from the command line
        Assert.assertEquals(defaultValues, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Create an instance of Option
        Option option = new Option();
        
        // Create a default switch value
        Boolean defaultSwitch = true;
        
        // Set the default switch value for the option
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        // Assert that the default switch value can be retrieved from the command line
        Assert.assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        // Create an instance of WriteableCommandLineImpl
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        
        // Set the normalised list
        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        commandLine.normalised = normalised;
        
        // Assert that the normalised list can be retrieved
        Assert.assertEquals(normalised, commandLine.getNormalised());
    }
}