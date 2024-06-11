import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new OptionBuilder().withPreferredName("root").create();
        List arguments = new ArrayList();

        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    // Test adding an option to the command line
    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    // Test adding a value to the command line
    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String value = "value1";
        commandLine.addValue(option, value);

        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    // Test adding a switch to the command line
    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    // Test getting an option from the command line
    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option1");
        assertEquals(option, retrievedOption);
    }

    // Test getting values from the command line
    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    // Test getting undefaulted values from the command line
    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    // Test getting a switch from the command line
    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        Boolean defaultValue = false;
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        assertTrue(switchValue);
    }

    // Test getting a property from the command line
    @Test
    public void testGetProperty() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String property = "property1";
        String value = "value1";
        commandLine.addProperty(option, property, value);

        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    // Test adding a property to the command line
    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String property = "property1";
        String value = "value1";
        commandLine.addProperty(option, property, value);

        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    // Test getting properties from the command line
    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);

        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }

    // Test looking like an option
    @Test
    public void testLooksLikeOption() {
        String trigger1 = "-t";
        String trigger2 = "--test";
        String trigger3 = "t";

        assertTrue(commandLine.looksLikeOption(trigger1));
        assertTrue(commandLine.looksLikeOption(trigger2));
        assertFalse(commandLine.looksLikeOption(trigger3));
    }

    // Test converting the command line to a string
    @Test
    public void testToString() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String argument1 = "arg1";
        String argument2 = "arg2";
        commandLine.addValue(option, argument1);
        commandLine.addValue(option, argument2);

        String expected = argument1 + " " + argument2;
        String result = commandLine.toString();
        assertEquals(expected, result);
    }

    // Test getting the options from the command line
    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    // Test getting the option triggers from the command line
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    // Test setting default values for an option
    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String defaultValue = "default";
        List<String> defaults = new ArrayList<>();
        defaults.add(defaultValue);
        commandLine.setDefaultValues(option, defaults);

        List<String> retrievedDefaults = commandLine.getValues(option, null);
        assertTrue(retrievedDefaults.contains(defaultValue));
    }

    // Test setting a default switch for an option
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);

        Boolean retrievedSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultValue, retrievedSwitch);
    }

    // Test getting the normalized values from the command line
    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String argument1 = "arg1";
        String argument2 = "arg2";
        commandLine.addValue(option, argument1);
        commandLine.addValue(option, argument2);

        List<String> normalised = commandLine.getNormalised();
        assertTrue(normalised.contains(argument1));
        assertTrue(normalised.contains(argument2));
    }
}