import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Create a new Option
        Option option = new OptionImpl("opt1");

        // Add the Option to the command line
        commandLine.addOption(option);

        // Check if the Option was added successfully
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Create a new Option
        Option option = new OptionImpl("opt1");

        // Add a value to the Option
        commandLine.addValue(option, "value1");

        // Check if the value was added successfully
        assertTrue(commandLine.getValues(option).contains("value1"));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Create a new Option
        Option option = new OptionImpl("opt1");

        // Add a value to the Option
        commandLine.addValue(option, "value1");

        // Get the undefaulted values for the Option
        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);

        // Check if the undefaulted values are correct
        assertEquals(1, undefaultedValues.size());
        assertEquals("value1", undefaultedValues.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Create a new Option
        Option option = new OptionImpl("opt1");

        // Create a list of default values
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        // Set the default values for the Option
        commandLine.setDefaultValues(option, defaultValues);

        // Check if the default values were set correctly
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Create a new Option
        Option option = new OptionImpl("opt1");

        // Add a switch value to the Option
        commandLine.addSwitch(option, true);

        // Check if the switch value was added successfully
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Create a new Option
        Option option = new OptionImpl("opt1");

        // Set the default switch value for the Option
        commandLine.setDefaultSwitch(option, true);

        // Check if the default switch value was set correctly
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Create a new Option
        Option option = new OptionImpl("opt1");

        // Add a property value to the Option
        commandLine.addProperty(option, "property1", "value1");

        // Check if the property value was added successfully
        assertEquals("value1", commandLine.getProperty(option, "property1"));
    }

    @Test
    public void testAddDefaultProperty() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Add a property value to the default property set
        commandLine.addProperty("property1", "value1");

        // Check if the property value was added successfully
        assertEquals("value1", commandLine.getDefaultProperty("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Check if the argument looks like an Option trigger
        assertTrue(commandLine.looksLikeOption("-opt1"));
    }

    @Test
    public void testGetCurrentOption() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Set the current option
        Option option = new OptionImpl("opt1");
        commandLine.setCurrentOption(option);

        // Check if the current option is correct
        assertEquals(option, commandLine.getCurrentOption());
    }
}