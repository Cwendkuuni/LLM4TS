import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Create a root option for the command line
        Option rootOption = new OptionBuilder().withPreferredName("root").create();

        // Create an argument for the command line
        Argument argument = new ArgumentBuilder().create();

        // Create a list of arguments
        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument);

        // Create a writeable command line implementation
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        // Create a new option
        Option option = new OptionBuilder().withPreferredName("option").create();

        // Add the option to the command line
        commandLine.addOption(option);

        // Verify that the option is added successfully
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Create an option and add it to the command line
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);

        // Add a value to the option
        commandLine.addValue(option, "value");

        // Verify that the value is added successfully
        List<String> values = commandLine.getValues(option, null);
        assertNotNull(values);
        assertTrue(values.contains("value"));
    }

    @Test
    public void testAddSwitch() {
        // Create an option and add it to the command line
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);

        // Add a switch to the option
        commandLine.addSwitch(option, true);

        // Verify that the switch is added successfully
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetOption() {
        // Create an option and add it to the command line
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);

        // Retrieve the option from the command line using the trigger
        Option retrievedOption = commandLine.getOption("option");

        // Verify that the retrieved option is the same as the added option
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        // Create an option and add it to the command line
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);

        // Add values to the option
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        // Retrieve the values for the option
        List<String> values = commandLine.getValues(option, null);

        // Verify that the retrieved values contain the added values
        assertNotNull(values);
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        // Create an option and add it to the command line
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);

        // Add a switch to the option
        commandLine.addSwitch(option, true);

        // Retrieve the switch for the option
        Boolean switchValue = commandLine.getSwitch(option, null);

        // Verify that the retrieved switch value is the same as the added switch value
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testAddProperty() {
        // Add a property to the command line
        commandLine.addProperty("property", "value");

        // Retrieve the value of the property
        String propertyValue = commandLine.getProperty("property", null);

        // Verify that the retrieved property value is the same as the added property value
        assertEquals("value", propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        // Verify that a trigger with a prefix looks like an option
        assertTrue(commandLine.looksLikeOption("-trigger"));

        // Verify that a trigger without a prefix does not look like an option
        assertFalse(commandLine.looksLikeOption("trigger"));
    }

    @Test
    public void testToString() {
        // Set up the command line with some arguments
        commandLine.addOption(new OptionBuilder().withPreferredName("option1").create());
        commandLine.addOption(new OptionBuilder().withPreferredName("option2").create());
        commandLine.addValue(commandLine.getOption("option1"), "value1");
        commandLine.addValue(commandLine.getOption("option2"), "value2");

        // Convert the command line to a string
        String commandLineString = commandLine.toString();

        // Verify that the generated string is correct
        assertEquals("option1 value1 option2 value2", commandLineString);
    }

    @Test
    public void testGetOptions() {
        // Add some options to the command line
        commandLine.addOption(new OptionBuilder().withPreferredName("option1").create());
        commandLine.addOption(new OptionBuilder().withPreferredName("option2").create());

        // Retrieve the options from the command line
        List<Option> options = commandLine.getOptions();

        // Verify that the retrieved options are the same as the added options
        assertNotNull(options);
        assertEquals(2, options.size());
        assertEquals("option1", options.get(0).getPreferredName());
        assertEquals("option2", options.get(1).getPreferredName());
    }

    @Test
    public void testGetOptionTriggers() {
        // Add some options to the command line
        commandLine.addOption(new OptionBuilder().withPreferredName("option1").create());
        commandLine.addOption(new OptionBuilder().withPreferredName("option2").create());

        // Retrieve the option triggers from the command line
        Set<String> optionTriggers = commandLine.getOptionTriggers();

        // Verify that the retrieved option triggers are the same as the added option triggers
        assertNotNull(optionTriggers);
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        // Create an option and add it to the command line
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);

        // Create a list of default values
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        // Set the default values for the option
        commandLine.setDefaultValues(option, defaults);

        // Retrieve the default values for the option
        List<String> retrievedDefaults = commandLine.getValues(option, null);

        // Verify that the retrieved default values are the same as the set default values
        assertNotNull(retrievedDefaults);
        assertEquals(defaults, retrievedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        // Create an option and add it to the command line
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);

        // Set the default switch for the option
        commandLine.setDefaultSwitch(option, true);

        // Retrieve the default switch for the option
        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);

        // Verify that the retrieved default switch is the same as the set default switch
        assertEquals(Boolean.TRUE, retrievedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        // Create some arguments and add them to the command line
        Argument argument1 = new ArgumentBuilder().create();
        Argument argument2 = new ArgumentBuilder().create();
        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument1);
        arguments.add(argument2);
        commandLine = new WriteableCommandLineImpl(commandLine.getRootOption(), arguments);

        // Retrieve the normalised arguments from the command line
        List<Argument> normalisedArguments = commandLine.getNormalised();

        // Verify that the retrieved normalised arguments are the same as the added arguments
        assertNotNull(normalisedArguments);
        assertEquals(arguments, normalisedArguments);
    }
}