import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option rootOption;
    private List arguments;

    @Before
    public void setUp() {
        rootOption = new Option("root");
        arguments = new ArrayList(Arrays.asList("arg1", "arg2"));
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        Object value = "value";
        assertFalse(commandLine.hasOption(option));
        commandLine.addValue(option, value);
        assertTrue(commandLine.hasOption(option));
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        boolean value = true;
        assertFalse(commandLine.hasOption(option));
        commandLine.addSwitch(option, value);
        assertTrue(commandLine.hasOption(option));
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        assertNull(commandLine.getOption(option.getPreferredName()));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        List values = Arrays.asList("value1", "value2");
        assertNull(commandLine.getValues(option, null));
        commandLine.addValue(option, values.get(0));
        assertEquals(values, commandLine.getValues(option, null));
        commandLine.addValue(option, values.get(1));
        assertEquals(values, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        List values = Arrays.asList("value1", "value2");
        assertNull(commandLine.getUndefaultedValues(option));
        commandLine.addValue(option, values.get(0));
        assertEquals(values, commandLine.getUndefaultedValues(option));
        commandLine.addValue(option, values.get(1));
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        Boolean defaultValue = Boolean.TRUE;
        assertNull(commandLine.getSwitch(option, defaultValue));
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, defaultValue));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        assertEquals("", commandLine.getProperty(property));
        String optionName = "testOption";
        Option option = new Option(optionName);
        String propertyValue = "testValue";
        commandLine.addProperty(option, property, propertyValue);
        assertEquals(propertyValue, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        String optionName = "testOption";
        Option option = new Option(optionName);
        assertFalse(commandLine.getProperties(option).contains(property));
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "testProperty";
        String defaultValue = "default";
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
        String optionName = "testOption";
        Option option = new Option(optionName);
        assertFalse(commandLine.getProperties(option).contains(property));
        assertEquals(defaultValue, commandLine.getProperty(option, property, defaultValue));
        String propertyValue = "testValue";
        commandLine.addProperty(option, property, propertyValue);
        assertEquals(propertyValue, commandLine.getProperty(option, property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        String property1 = "testProperty1";
        String property2 = "testProperty2";
        String optionName = "testOption";
        Option option = new Option(optionName);
        assertTrue(commandLine.getProperties().isEmpty());
        commandLine.addProperty(option, property1, "value");
        assertTrue(commandLine.getProperties().contains(property1));
        commandLine.addProperty(option, property2, "value");
        assertTrue(commandLine.getProperties().contains(property1));
        assertTrue(commandLine.getProperties().contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-t";
        assertTrue(commandLine.looksLikeOption(trigger));
        trigger = "--test";
        assertTrue(commandLine.looksLikeOption(trigger));
        trigger = "test";
        assertFalse(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String expected = "arg1 arg2";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option1, defaults);
        assertEquals(defaults, commandLine.getValues(option1, null));
        commandLine.addOption(option2);
        assertNull(commandLine.getValues(option2, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);
        assertEquals(defaultValue, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(arguments, commandLine.getNormalised());
    }
}