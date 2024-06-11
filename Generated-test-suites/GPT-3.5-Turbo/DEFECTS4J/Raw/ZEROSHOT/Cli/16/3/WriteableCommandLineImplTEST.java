import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Option("test");
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testSwitch");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        Option result = commandLine.getOption("testOption");
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testSwitch");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        String result = commandLine.getProperty(property);
        assertEquals(value, result);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        Option option = new PropertyOption();
        commandLine.addProperty(option, property, value);
        String result = commandLine.getProperty(option, property, null);
        assertEquals(value, result);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "testProperty";
        String defaultValue = "defaultValue";
        String result = commandLine.getProperty(property, defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testGetProperties() {
        String property1 = "testProperty1";
        String value1 = "testValue1";
        String property2 = "testProperty2";
        String value2 = "testValue2";
        commandLine.addProperty(property1, value1);
        commandLine.addProperty(property2, value2);
        assertEquals(2, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains(property1));
        assertTrue(commandLine.getProperties().contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-t"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        String result = commandLine.toString();
        assertEquals("-testOption value", result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("testOption1"));
        assertTrue(commandLine.getOptionTriggers().contains("testOption2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testSwitch");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("testOption");
        List<String> arguments = new ArrayList<>();
        arguments.add("testArgument");
        commandLine = new WriteableCommandLineImpl(option, arguments);
        List<String> normalised = commandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals("testArgument", normalised.get(0));
    }
}