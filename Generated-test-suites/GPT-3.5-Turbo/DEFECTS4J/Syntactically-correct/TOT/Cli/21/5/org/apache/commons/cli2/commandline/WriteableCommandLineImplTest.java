import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Initialize the root option and arguments
        Option rootOption = new OptionBuilder().create();
        List arguments = new ArrayList();

        // Create a new WriteableCommandLineImpl object for testing
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add the option to the command line
        commandLine.addOption(option);

        // Check if the option was added successfully
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add a value to the option
        commandLine.addValue(option, "testValue");

        // Check if the value was added successfully
        assertEquals("testValue", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add a switch to the option
        commandLine.addSwitch(option, true);

        // Check if the switch was added successfully
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add the option to the command line
        commandLine.addOption(option);

        // Check if the command line has the option
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add the option to the command line
        commandLine.addOption(option);

        // Get the option from the command line
        Option result = commandLine.getOption("testOption");

        // Check if the correct option was returned
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add values to the option
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        // Get the values from the option
        List<String> values = commandLine.getValues(option, null);

        // Check if the correct values were returned
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add values to the option
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        // Get the undefaulted values from the option
        List<String> values = commandLine.getUndefaultedValues(option);

        // Check if the correct undefaulted values were returned
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add a switch to the option
        commandLine.addSwitch(option, true);

        // Get the switch from the option
        Boolean result = commandLine.getSwitch(option, null);

        // Check if the correct switch value was returned
        assertTrue(result);
    }

    @Test
    public void testGetProperty() {
        // Create a new property option
        Option option = new PropertyOption("testOption");

        // Add a property to the option
        commandLine.addProperty(option, "testProperty", "testValue");

        // Get the property from the option
        String result = commandLine.getProperty(option, "testProperty", null);

        // Check if the correct property value was returned
        assertEquals("testValue", result);
    }

    @Test
    public void testLooksLikeOption() {
        // Test a trigger that looks like an option
        assertTrue(commandLine.looksLikeOption("-t"));

        // Test a trigger that does not look like an option
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add the option to the command line
        commandLine.addOption(option);

        // Convert the command line to string
        String result = commandLine.toString();

        // Check if the correct string representation was returned
        assertEquals("testOption", result);
    }

    @Test
    public void testGetOptions() {
        // Create two new options
        Option option1 = new OptionBuilder().withPreferredName("testOption1").create();
        Option option2 = new OptionBuilder().withPreferredName("testOption2").create();

        // Add the options to the command line
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // Get the options from the command line
        List<Option> options = commandLine.getOptions();

        // Check if the correct options were returned
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        // Create two new options
        Option option1 = new OptionBuilder().withPreferredName("testOption1").create();
        Option option2 = new OptionBuilder().withPreferredName("testOption2").create();

        // Add the options to the command line
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // Get the option triggers from the command line
        Set<String> triggers = commandLine.getOptionTriggers();

        // Check if the correct option triggers were returned
        assertTrue(triggers.contains("testOption1"));
        assertTrue(triggers.contains("testOption2"));
    }

    @Test
    public void testSetDefaultValues() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Set default values for the option
        commandLine.setDefaultValues(option, new ArrayList<String>(){{add("defaultValue1"); add("defaultValue2");}});

        // Get the default values from the option
        List<String> defaultValues = commandLine.getValues(option, null);

        // Check if the correct default values were set
        assertEquals("defaultValue1", defaultValues.get(0));
        assertEquals("defaultValue2", defaultValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Set a default switch value for the option
        commandLine.setDefaultSwitch(option, true);

        // Get the default switch value from the option
        Boolean defaultSwitch = commandLine.getSwitch(option, null);

        // Check if the correct default switch value was set
        assertTrue(defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("testOption").create();

        // Add the option to the command line
        commandLine.addOption(option);

        // Get the normalised list from the command line
        List<String> normalised = commandLine.getNormalised();

        // Check if the normalised list contains the correct option
        assertTrue(normalised.contains("testOption"));
    }
}