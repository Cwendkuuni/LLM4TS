import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Values;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {
    private WriteableCommandLine commandLine;
    private Option option;
    private List<String> arguments;

    @Before
    public void setUp() {
        option = new Option("o", "option", true, "Option");
        Argument argument = new Argument("argument");
        arguments = Arrays.asList("arg1", "arg2", "arg3");
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Object value = "value";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList(value), values);
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.valueOf(value), switchValue);
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Object value1 = "value1";
        Object value2 = "value2";
        List<Object> defaultValues = Arrays.asList(value1, value2);
        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList(value1, value2), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertEquals(Arrays.asList(value1, value2), values);
    }

    @Test
    public void testGetSwitch() {
        boolean defaultValue = true;
        Boolean retrievedValue = commandLine.getSwitch(option, defaultValue);
        assertEquals(Boolean.valueOf(true), retrievedValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "property";
        String defaultValue = "default";
        String retrievedValue = commandLine.getProperty(option, property, defaultValue);
        assertEquals(defaultValue, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        List<String> properties = commandLine.getProperties(option);
        assertEquals(Arrays.asList(property1, property2), properties);
    }

    @Test
    public void testGetPropertiesAllOptions() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        List<String> properties = commandLine.getProperties();
        assertEquals(Arrays.asList(property1, property2), properties);
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "option";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String expectedString = "arg1 arg2 arg3";
        String commandLineString = commandLine.toString();
        assertEquals(expectedString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertEquals(Arrays.asList(option), options);
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(Arrays.asList("o", "option"), optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = Arrays.asList("value");
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> retrievedDefaultValues = commandLine.getValues(option, null);
        assertEquals(defaultValues, retrievedDefaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);
        assertEquals(Boolean.valueOf(defaultSwitch), retrievedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(arguments, normalised);
    }
}