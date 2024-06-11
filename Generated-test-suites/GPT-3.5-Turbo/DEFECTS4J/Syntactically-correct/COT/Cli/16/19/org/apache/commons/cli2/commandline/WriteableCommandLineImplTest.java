import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        // Add option
        commandLine.addOption(option);

        // Assert that option is added
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String value = "value";

        // Add value
        commandLine.addValue(option, value);

        // Assert that value is added
        Assert.assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        Boolean value = true;

        // Add switch
        commandLine.addSwitch(option, value);

        // Assert that switch is added
        Assert.assertTrue(commandLine.getSwitch(option, null).equals(value));
    }

    @Test
    public void testHasOption() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        // Add option
        commandLine.addOption(option);

        // Assert that option is present
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        // Add option
        commandLine.addOption(option);

        // Get option
        Option retrievedOption = commandLine.getOption("test");

        // Assert that retrieved option is correct
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String value = "value";

        // Add value
        commandLine.addValue(option, value);

        // Get values
        List<String> values = commandLine.getValues(option, null);

        // Assert that values contain the added value
        Assert.assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String value = "value";

        // Add value
        commandLine.addValue(option, value);

        // Get undefaulted values
        List<String> values = commandLine.getUndefaultedValues(option);

        // Assert that values contain the added value
        Assert.assertTrue(values.contains(value));
    }

    @Test
    public void testGetSwitch() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        Boolean value = true;

        // Add switch
        commandLine.addSwitch(option, value);

        // Get switch
        Boolean retrievedSwitch = commandLine.getSwitch(option, null);

        // Assert that retrieved switch is correct
        Assert.assertEquals(value, retrievedSwitch);
    }

    @Test
    public void testGetProperty() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String property = "property";
        String value = "value";

        // Add property
        commandLine.addProperty(option, property, value);

        // Get property
        String retrievedProperty = commandLine.getProperty(option, property, null);

        // Assert that retrieved property is correct
        Assert.assertEquals(value, retrievedProperty);
    }

    @Test
    public void testAddProperty() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String property = "property";
        String value = "value";

        // Add property
        commandLine.addProperty(option, property, value);

        // Get properties
        Set<String> properties = commandLine.getProperties(option);

        // Assert that properties contain the added property
        Assert.assertTrue(properties.contains(property));
    }

    @Test
    public void testGetProperties() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String property = "property";
        String value = "value";

        // Add property
        commandLine.addProperty(option, property, value);

        // Get properties
        Set<String> properties = commandLine.getProperties();

        // Assert that properties contain the added property
        Assert.assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        String trigger = "-t";

        // Assert that trigger looks like an option
        Assert.assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        // Add value
        commandLine.addValue(option, "value");

        // Convert command line to string
        String output = commandLine.toString();

        // Assert that output is correct
        Assert.assertEquals("value", output);
    }

    @Test
    public void testGetOptions() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        // Add option
        commandLine.addOption(option);

        // Get options
        List<Option> options = commandLine.getOptions();

        // Assert that options contain the added option
        Assert.assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        // Add option
        commandLine.addOption(option);

        // Get option triggers
        Set<String> triggers = commandLine.getOptionTriggers();

        // Assert that triggers contain the added option trigger
        Assert.assertTrue(triggers.contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        List<String> defaults = new ArrayList<>();
        defaults.add("default");

        // Set default values
        commandLine.setDefaultValues(option, defaults);

        // Get values
        List<String> values = commandLine.getValues(option, null);

        // Assert that values contain the default values
        Assert.assertTrue(values.containsAll(defaults));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        Boolean defaultValue = true;

        // Set default switch
        commandLine.setDefaultSwitch(option, defaultValue);

        // Get switch
        Boolean retrievedSwitch = commandLine.getSwitch(option, null);

        // Assert that retrieved switch is correct
        Assert.assertEquals(defaultValue, retrievedSwitch);
    }

    @Test
    public void testGetNormalised() {
        // Create objects for testing
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        List<String> normalised = new ArrayList<>();
        normalised.add("arg");

        // Set normalised arguments
        // Use reflection to access private field
        Field normalisedField = commandLine.getClass().getDeclaredField("normalised");
        normalisedField.setAccessible(true);
        normalisedField.set(commandLine, normalised);

        // Get normalised arguments
        List<String> retrievedNormalised = commandLine.getNormalised();

        // Assert that retrieved normalised arguments are correct
        Assert.assertEquals(normalised, retrievedNormalised);
    }
}