import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option.Builder("root").create();
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option.Builder("test").create();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument.Builder("arg").create();
        Object value1 = new Object();
        Object value2 = new Object();

        commandLine.addValue(argument, value1);
        commandLine.addValue(argument, value2);

        List<Object> expectedValues = new ArrayList<>(Arrays.asList(value1, value2));
        List<Object> actualValues = commandLine.getValues(argument, null);

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = new Option.Builder("switch").create();
        commandLine.addSwitch(switchOption, true);
        assertTrue(commandLine.getSwitch(switchOption, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option.Builder("test").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option.Builder("test").create();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValuesWithCommandLineValues() {
        Argument argument = new Argument.Builder("arg").create();
        Object value1 = new Object();
        Object value2 = new Object();

        commandLine.addValue(argument, value1);
        commandLine.addValue(argument, value2);

        List<Object> expectedValues = new ArrayList<>(Arrays.asList(value1, value2));
        List<Object> actualValues = commandLine.getValues(argument, null);

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetValuesWithDefaultValues() {
        Argument argument = new Argument.Builder("arg").create();
        Object value1 = new Object();
        Object value2 = new Object();
        List<Object> defaultValues = new ArrayList<>(Arrays.asList(value1, value2));

        List<Object> actualValues = commandLine.getValues(argument, defaultValues);

        assertEquals(defaultValues, actualValues);
    }

    @Test
    public void testGetValuesWithOptionDefaultValues() {
        Argument argument = new Argument.Builder("arg").create();
        Object value1 = new Object();
        Object value2 = new Object();
        List<Object> optionDefaultValues = new ArrayList<>(Arrays.asList(value1, value2));

        Option option = new Option.Builder("test").withDefaultValues(optionDefaultValues).create();
        commandLine.addOption(option);

        List<Object> expectedValues = new ArrayList<>(Arrays.asList(value1, value2));
        List<Object> actualValues = commandLine.getValues(argument, null);

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetValuesWithEmptyList() {
        Argument argument = new Argument.Builder("arg").create();
        List<Object> expectedValues = new ArrayList<>();

        List<Object> actualValues = commandLine.getValues(argument, null);

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetSwitchWithCommandLineSwitch() {
        Option switchOption = new Option.Builder("switch").create();
        commandLine.addSwitch(switchOption, true);

        assertTrue(commandLine.getSwitch(switchOption, false));
    }

    @Test
    public void testGetSwitchWithDefaultSwitch() {
        Option switchOption = new Option.Builder("switch").create();
        Boolean defaultSwitch = Boolean.TRUE;

        assertTrue(commandLine.getSwitch(switchOption, defaultSwitch));
    }

    @Test
    public void testGetPropertyWithDefaultPropertyValue() {
        String property = "test";
        String defaultValue = "default";

        String actualValue = commandLine.getProperty(property, defaultValue);

        assertEquals(defaultValue, actualValue);
    }

    @Test
    public void testGetPropertyWithPropertyValue() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);

        String actualValue = commandLine.getProperty(property, null);

        assertEquals(value, actualValue);
    }

    @Test
    public void testGetPropertyWithDefaultPropertyValue() {
        String property = "test";
        String defaultValue = null;

        String actualValue = commandLine.getProperty(property, defaultValue);

        assertNull(actualValue);
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "--test";

        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        List<String> arguments = new ArrayList<>(Arrays.asList("arg1", "arg2"));
        commandLine = new WriteableCommandLineImpl(null, arguments);

        String expectedString = "arg1 arg2";
        String actualString = commandLine.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option.Builder("test1").create();
        Option option2 = new Option.Builder("test2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> expectedOptions = new ArrayList<>(Arrays.asList(option1, option2));
        List<Option> actualOptions = commandLine.getOptions();

        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option.Builder("test1").create();
        Option option2 = new Option.Builder("test2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<String> expectedTriggers = new ArrayList<>(Arrays.asList("test1", "test2"));
        List<String> actualTriggers = new ArrayList<>(commandLine.getOptionTriggers());

        assertEquals(expectedTriggers, actualTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Argument argument = new Argument.Builder("arg").create();
        Object value1 = new Object();
        Object value2 = new Object();
        List<Object> defaultValues = new ArrayList<>(Arrays.asList(value1, value2));

        Option option = new Option.Builder("test").create();
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> expectedValues = new ArrayList<>(Arrays.asList(value1, value2));
        List<Object> actualValues = commandLine.getValues(argument, null);

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testSetDefaultValuesWithNull() {
        Argument argument = new Argument.Builder("arg").create();
        Object value = new Object();

        Option option = new Option.Builder("test").create();
        commandLine.addOption(option);
        commandLine.addValue(argument, value);
        commandLine.setDefaultValues(option, null);

        List<Object> expectedValues = new ArrayList<>(Arrays.asList(value));
        List<Object> actualValues = commandLine.getValues(argument, null);

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option switchOption = new Option.Builder("switch").create();

        Option option = new Option.Builder("test").create();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(switchOption, true);

        assertTrue(commandLine.getSwitch(switchOption, false));
    }

    @Test
    public void testSetDefaultSwitchWithNull() {
        Option switchOption = new Option.Builder("switch").create();

        Option option = new Option.Builder("test").create();
        commandLine.addOption(option);
        commandLine.addSwitch(switchOption, true);
        commandLine.setDefaultSwitch(switchOption, null);

        assertTrue(commandLine.getSwitch(switchOption, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>(Arrays.asList("arg1", "arg2"));
        commandLine = new WriteableCommandLineImpl(null, arguments);

        List<String> expectedArguments = new ArrayList<>(Arrays.asList("arg1", "arg2"));
        List<String> actualArguments = commandLine.getNormalised();

        assertEquals(expectedArguments, actualArguments);
    }
}