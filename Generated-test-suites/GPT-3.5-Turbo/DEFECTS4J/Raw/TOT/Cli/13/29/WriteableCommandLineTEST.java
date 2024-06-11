import org.junit.Before;
import org.junit.Test;

// Test class for WriteableCommandLine
public class WriteableCommandLineTest {
  
    private WriteableCommandLine commandLine;
    private Option option;
  
    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl("testOption", "TO", false, "Test Option");
    }
  
    // Test case for addOption method
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        // Verify if the option is added successfully
        assert(commandLine.getOptions().contains(option));
    }
  
    // Test case for addValue method
    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        // Verify if the value is added successfully
        assert(commandLine.getValues(option).contains("value"));
    }
  
    // Test case for getValues method
    @Test
    public void testGetValues() {
        commandLine.addOption(option);
        // Add multiple values to the option
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        // Verify if the getValues method returns the expected values
        assert(commandLine.getValues(option).contains("value1"));
        assert(commandLine.getValues(option).contains("value2"));
        assert(commandLine.getValues(option).contains("value3"));
    }
  
    // Test case for setDefaultValues method
    @Test
    public void testSetDefaultValues() {
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        defaultValues.add("default3");
        commandLine.setDefaultValues(option, defaultValues);
        // Verify if the default values are set successfully
        assert(commandLine.getDefaultValues(option).containsAll(defaultValues));
    }
  
    // Test case for addSwitch method
    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        // Verify if the switch value is added successfully
        assert(commandLine.getSwitchValue(option));
    }
  
    // Test case for setDefaultSwitch method
    @Test
    public void testSetDefaultSwitch() {
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, false);
        // Verify if the default switch value is set successfully
        assert(!commandLine.getDefaultSwitch(option));
    }
  
    // Test case for addProperty method
    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        // Verify if the properties are added successfully
        assert(commandLine.getProperties().containsKey("property1"));
        assert(commandLine.getProperties().containsKey("property2"));
        assert(commandLine.getProperties().get("property1").equals("value1"));
        assert(commandLine.getProperties().get("property2").equals("value2"));
    }
  
    // Test case for looksLikeOption method
    @Test
    public void testLooksLikeOption() {
        // Test for argument starting with "-"
        assert(commandLine.looksLikeOption("-argument"));
        // Test for argument starting with "--"
        assert(commandLine.looksLikeOption("--argument"));
        // Test for argument without "-"
        assert(!commandLine.looksLikeOption("argument"));
    }
}