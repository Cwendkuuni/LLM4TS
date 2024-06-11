import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option optionWithValue;
    private Option optionWithSwitch;
    private Option optionWithProperty;

    @Before
    public void setUp() {
        Option rootOption = new Option("t", "test", false, "Test flag");
        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        optionWithValue = new Option("v", "value", true, "Value option");
        optionWithValue.setDefaultValue("default value");
        commandLine.addOption(optionWithValue);

        optionWithSwitch = new Option("s", "switch", false, "Switch option");
        commandLine.addSwitch(optionWithSwitch, false);

        optionWithProperty = new Option("p", "property", true, "Property option");
        commandLine.addProperty(optionWithProperty, "propertyName", "propertyValue");
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(optionWithValue, "testValue");
        List values = commandLine.getValues(optionWithValue, null);
        assertEquals("testValue", values.get(0));
    }

    @Test
    public void testAddValueWithDefault() {
        List defaults = new ArrayList<>();
        defaults.add("default value");
        commandLine.setDefaultValues(optionWithValue, defaults);
        commandLine.addValue(optionWithValue, null);
        List values = commandLine.getValues(optionWithValue, null);
        assertEquals("default value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(optionWithSwitch, true);
        Boolean value = commandLine.getSwitch(optionWithSwitch, false);
        assertTrue(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(optionWithSwitch, true);
        commandLine.addSwitch(optionWithSwitch, false);
    }

    @Test
    public void testGetProperty() {
        String value = commandLine.getProperty(optionWithProperty, "defaultPropertyValue");
        assertEquals("propertyValue", value);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String value = commandLine.getProperty(optionWithProperty, null);
        assertEquals("propertyValue", value);
    }

    @Test
    public void testGetProperties() {
        Set properties = commandLine.getProperties(optionWithProperty);
        assertTrue(properties.contains("propertyName"));
    }

    @Test
    public void testGetPropertiesWithEmptyMap() {
        WriteableCommandLineImpl commandLineWithEmptyMap = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Set properties = commandLineWithEmptyMap.getProperties();
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testToString() {
        assertEquals("--test --value testValue", commandLine.toString());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("--value"));
    }
}