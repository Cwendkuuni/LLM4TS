import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        // Create an option
        Option option = new OptionImpl("testOption");

        // Add the option to the command line
        commandLine.addOption(option);

        // Assert that the option has been added
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Create an option
        Option option = new OptionImpl("testOption");

        // Add a value to the option
        commandLine.addValue(option, "testValue");

        // Retrieve the values for the option
        List<String> values = commandLine.getValues(option);

        // Assert that the value has been added
        assertTrue(values.contains("testValue"));
    }

    @Test
    public void testSetDefaultValues() {
        // Create an option
        Option option = new OptionImpl("testOption");

        // Set default values for the option
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);

        // Retrieve the default values for the option
        List<String> retrievedDefaultValues = commandLine.getDefaultValues(option);

        // Assert that the default values have been set
        assertEquals(defaultValues, retrievedDefaultValues);
    }

    @Test
    public void testAddSwitch() {
        // Create a switch option
        Option option = new OptionImpl("testSwitch", OptionType.SWITCH);

        // Add the switch to the command line
        commandLine.addSwitch(option, true);

        // Retrieve the switch value
        boolean switchValue = commandLine.getSwitch(option);

        // Assert that the switch value has been set
        assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultSwitch() {
        // Create a switch option
        Option option = new OptionImpl("testSwitch", OptionType.SWITCH);

        // Set the default value for the switch
        commandLine.setDefaultSwitch(option, true);

        // Retrieve the default switch value
        Boolean defaultSwitchValue = commandLine.getDefaultSwitch(option);

        // Assert that the default switch value has been set
        assertEquals(true, defaultSwitchValue);
    }

    @Test
    public void testAddProperty() {
        // Add a property to the command line
        commandLine.addProperty("testProperty", "testValue");

        // Retrieve the value for the property
        String propertyValue = commandLine.getProperty("testProperty");

        // Assert that the property value has been set
        assertEquals("testValue", propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        // Test an argument that looks like an option trigger
        boolean looksLikeOption = commandLine.looksLikeOption("-testOption");
        assertTrue(looksLikeOption);

        // Test an argument that does not look like an option trigger
        looksLikeOption = commandLine.looksLikeOption("testValue");
        assertFalse(looksLikeOption);
    }
}