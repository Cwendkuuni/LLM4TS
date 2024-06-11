import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option rootOption;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        rootOption = new Option("root", "Root Option", true);
        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option", false);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getOption("test") == option);
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch", "Switch Option", false);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test", "Test Option", false);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("test2", "Test Option 2", false)));
    }


    @Test
    public void testGetValuesWithDefaultValues() {
        Option option = new Argument();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option, null);
        assertEquals(defaultValues, values);
    }

    @Test
    public void testGetValuesWithEmptyDefaultValues() {
        Option option = new Argument();
        List<String> defaultValues = new ArrayList<>();
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetValuesWithNoDefaultValues() {
        Option option = new Argument();

        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }


    @Test
    public void testGetSwitchWithDefault() {
        Option option = new Option("switch", "Switch Option", false);
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitchWithDefaultValue() {
        Option option = new Option("switch", "Switch Option", false);

        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetSwitchWithNoDefault() {
        Option option = new Option("switch", "Switch Option", false);

        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("property", "Property Option", false);
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("property", "Property Option", false);
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);

        assertEquals(2, commandLine.getProperties(option).size());
        assertTrue(commandLine.getProperties(option).contains(property1));
        assertTrue(commandLine.getProperties(option).contains(property2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1", "Test Option 1", false);
        Option option2 = new Option("test2", "Test Option 2", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("test1"));
        assertTrue(commandLine.getOptionTriggers().contains("test2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option1 = new Option("test1", "Test Option 1", false);
        Option option2 = new Option("test2", "Test Option 2", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.looksLikeOption("test1"));
        assertFalse(commandLine.looksLikeOption("test3"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(rootOption, "value");

        assertEquals("value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1", "Test Option 1", false);
        Option option2 = new Option("test2", "Test Option 2", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetNormalised() {
        String argument1 = "argument1";
        String argument2 = "argument2";
        List<String> arguments = Arrays.asList(argument1, argument2);
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(arguments, commandLine.getNormalised());
    }
}