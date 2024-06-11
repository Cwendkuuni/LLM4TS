import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineTest {

    // Test case for addOption method
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption", "Test Option", true, "Test");
        commandLine.addOption(option);
        
        // Verify that the option is added to the CommandLine
        Assert.assertTrue(commandLine.hasOption("testOption"));
    }

    // Test case for addValue method
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption", "Test Option", true, "Test");
        commandLine.addOption(option);

        // Add a value to the option
        commandLine.addValue(option, "value1");

        // Verify that the value is added to the option
        List<String> optionValues = commandLine.getValues(option);
        Assert.assertEquals(1, optionValues.size());
        Assert.assertEquals("value1", optionValues.get(0));
    }

    // Test case for getUndefaultedValues method
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption", "Test Option", true, "Test");
        commandLine.addOption(option);

        // Add values to the option
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        // Verify that the correct values are returned
        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, undefaultedValues.size());
        Assert.assertEquals("value1", undefaultedValues.get(0));
        Assert.assertEquals("value2", undefaultedValues.get(1));
    }

    // Test case for setDefaultValues method
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption", "Test Option", true, "Test");

        // Set default values for the option
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        // Verify that the default values are set
        List<String> optionValues = commandLine.getValues(option);
        Assert.assertEquals(2, optionValues.size());
        Assert.assertEquals("default1", optionValues.get(0));
        Assert.assertEquals("default2", optionValues.get(1));
    }

    // Test case for addSwitch method
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption", "Test Option", true, "Test");
        commandLine.addOption(option);

        // Add a switch value to the option
        commandLine.addSwitch(option, true);

        // Verify that the switch value is added
        Boolean switchValue = commandLine.getSwitchValue(option);
        Assert.assertTrue(switchValue);
    }

    // Test case for setDefaultSwitch method
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption", "Test Option", true, "Test");

        // Set the default switch value for the option
        commandLine.setDefaultSwitch(option, true);

        // Verify that the default switch value is set
        Boolean switchValue = commandLine.getSwitchValue(option);
        Assert.assertTrue(switchValue);
    }

    // Test case for addProperty method
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption", "Test Option", true, "Test");
        commandLine.addOption(option);

        // Add a property value to the option
        commandLine.addProperty(option, "property1", "value1");

        // Verify that the property value is added
        String propertyValue = commandLine.getProperty(option, "property1");
        Assert.assertEquals("value1", propertyValue);
    }

    // Test case for addProperty method (default property set)
    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Add a property value to the default property set
        commandLine.addProperty("property1", "value1");

        // Verify that the property value is added to the default property set
        String propertyValue = commandLine.getProperty("property1");
        Assert.assertEquals("value1", propertyValue);
    }

    // Test case for looksLikeOption method
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Verify that the argument looks like an option trigger
        Assert.assertTrue(commandLine.looksLikeOption("-option"));
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
    }

    // Test case for setCurrentOption method
    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption", "Test Option", true, "Test");

        // Set the current option
        commandLine.setCurrentOption(option);

        // Verify that the current option is set correctly
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option, currentOption);
    }
}