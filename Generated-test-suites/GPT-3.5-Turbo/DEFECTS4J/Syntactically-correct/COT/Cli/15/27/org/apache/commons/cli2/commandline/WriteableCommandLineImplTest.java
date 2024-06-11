import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        
        // Create an Option object
        Option option = new OptionBuilder().withLongName("testOption").create();
        
        // Add the Option to the command line
        commandLine.addOption(option);
        
        // Check if the option is present in the options list
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Create an Argument object
        Argument argument = new ArgumentBuilder().withName("testArgument").create();

        // Add a value to the Argument in the command line
        commandLine.addValue(argument, "testValue");

        // Check if the value is present in the values list
        Assert.assertTrue(commandLine.getValues(argument).contains("testValue"));
    }
    
    @Test
    public void testAddSwitch() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Create a Switch object
        Switch switchObj = new SwitchBuilder().withLongName("testSwitch").create();

        // Add a switch to the command line
        commandLine.addSwitch(switchObj, true);

        // Check if the switch is present in the switches map
        Assert.assertTrue(commandLine.getSwitch(switchObj, false));
    }

    @Test
    public void testHasOption() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Create an Option object
        Option option = new OptionBuilder().withLongName("testOption").create();

        // Add the Option to the command line
        commandLine.addOption(option);

        // Check if the command line has the option
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Create an Option object
        Option option = new OptionBuilder().withLongName("testOption").create();

        // Add the Option to the command line
        commandLine.addOption(option);

        // Get the Option using its trigger
        Option retrievedOption = commandLine.getOption("testOption");

        // Check if the retrieved Option is the same as the original Option
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Create an Argument object
        Argument argument = new ArgumentBuilder().withName("testArgument").create();

        // Add a value to the Argument in the command line
        commandLine.addValue(argument, "testValue");

        // Get the values for the Argument
        List<String> values = commandLine.getValues(argument);

        // Check if the retrieved values list contains the added value
        Assert.assertTrue(values.contains("testValue"));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Create an Argument object
        Argument argument = new ArgumentBuilder().withName("testArgument").create();

        // Add a value to the Argument in the command line
        commandLine.addValue(argument, "testValue");

        // Get the undefaulted values for the Argument
        List<String> values = commandLine.getUndefaultedValues(argument);

        // Check if the retrieved values list contains the added value
        Assert.assertTrue(values.contains("testValue"));
    }

    @Test
    public void testGetSwitch() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Create a Switch object
        Switch switchObj = new SwitchBuilder().withLongName("testSwitch").create();

        // Add a switch to the command line
        commandLine.addSwitch(switchObj, true);

        // Get the switch value for the Switch
        boolean switchValue = commandLine.getSwitch(switchObj, false);

        // Check if the retrieved switch value is true
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Set a property for the command line
        commandLine.addProperty("testProperty", "testValue");

        // Get the value of the property
        String propertyValue = commandLine.getProperty("testProperty");

        // Check if the retrieved property value is the same as the set value
        Assert.assertEquals("testValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Add a property to the command line
        commandLine.addProperty("testProperty", "testValue");

        // Get the properties for the command line
        Set<String> properties = commandLine.getProperties();

        // Check if the properties set contain the added property
        Assert.assertTrue(properties.contains("testProperty"));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        // Create a WriteableCommandLineImpl object
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // Get a property with a default value
        String propertyValue = commandLine.getProperty("testProperty", "defaultValue");

        // Check if the retrieved property value is the same as the default value
        Assert.assertEquals("defaultValue", propertyValue);
    }

}