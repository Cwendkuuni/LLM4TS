import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Create a root option and arguments
        Option rootOption = new OptionBuilder().withPreferredName("root").create();
        List<String> arguments = Arrays.asList("argument1", "argument2");

        // Create a new WriteableCommandLineImpl instance
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();

        // Add the option and check if it is present
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        Object value = "value1";

        // Add a value to the option and check if it is present
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        boolean value = true;

        // Add a switch to the option and check if it is present
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();

        // Add the option and check if it is present
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();

        // Add the option and check if it is returned correctly
        commandLine.addOption(option);
        Option returnedOption = commandLine.getOption("option1");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        Object value = "value1";

        // Add a value to the option and check if it is returned correctly
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        Object value = "value1";

        // Add a value to the option and check if it is returned correctly
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        boolean value = true;

        // Add a switch to the option and check if it is returned correctly
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property1";
        String value = "value1";

        // Add a property to the option and check if it is returned correctly
        commandLine.addProperty(property, value);
        String returnValue = commandLine.getProperty(property);
        assertEquals(value, returnValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String property = "property1";
        String value = "value1";

        // Add a property to the option and check if it is present
        commandLine.addProperty(option, property, value);
        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String property = "property1";
        String defaultValue = "default";

        // Add a property to the option and check if it is returned correctly
        commandLine.addProperty(option, property, null);
        String returnValue = commandLine.getProperty(option, property, defaultValue);
        assertEquals(defaultValue, returnValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        String property1 = "property1";
        String property2 = "property2";

        // Add two properties to the option and check if they are present
        commandLine.addProperty(option, property1, "value1");
        commandLine.addProperty(option, property2, "value2");
        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        String optionString = "--option1";

        // Check if the option string looks like an option
        boolean looksLikeOption = commandLine.looksLikeOption(optionString);
        assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        String argumentString = "argument1 argument2";

        // Check if the arguments are returned correctly as a string
        String toString = commandLine.toString();
        assertEquals(argumentString, toString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        // Add two options and check if they are present
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        // Add two options and check if their triggers are returned correctly
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        List<String> defaults = Arrays.asList("default1", "default2");

        // Set default values for the option and check if they are returned correctly
        commandLine.setDefaultValues(option, defaults);
        List<String> returnedDefaults = commandLine.getValues(option, null);
        assertEquals(defaults, returnedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        Boolean defaultSwitch = Boolean.TRUE;

        // Set a default switch for the option and check if it is returned correctly
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean returnedDefaultSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, returnedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        String argument1 = "argument1";
        String argument2 = "argument2";

        // Check if the arguments are returned correctly
        List<String> normalised = commandLine.getNormalised();
        assertEquals(argument1, normalised.get(0));
        assertEquals(argument2, normalised.get(1));
    }
}

