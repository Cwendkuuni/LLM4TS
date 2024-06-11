import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        List<String> arguments = new ArrayList<>();
        Option rootOption = new Option("root");
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option");
        Object value = "value";
        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList(value), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option");
        boolean value = true;
        commandLine.addSwitch(option, value);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option");
        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option");
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = new ArrayList<>();
        values.add("value");
        commandLine.addValue(option, values.get(0));

        List<Object> retrievedValues = commandLine.getValues(option, null);
        assertEquals(values, retrievedValues);

        List<Object> retrievedValuesWithDefault = commandLine.getValues(option, values);
        assertEquals(values, retrievedValuesWithDefault);

        List<Object> retrievedValuesWithDefaultEmpty = commandLine.getValues(option, Collections.emptyList());
        assertEquals(values, retrievedValuesWithDefaultEmpty);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option");

        List<Object> values = new ArrayList<>();
        values.add("value");
        commandLine.addValue(option, values.get(0));

        List<Object> retrievedValues = commandLine.getUndefaultedValues(option);
        assertEquals(values, retrievedValues);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option");
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option");
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-trigger";
        assertTrue(commandLine.looksLikeOption(trigger));

        trigger = "--trigger";
        assertTrue(commandLine.looksLikeOption(trigger));

        trigger = "/trigger";
        assertFalse(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        Option option = new Option("option");
        commandLine.addValue(option, "value");

        String expectedToString = "value";
        String toString = commandLine.toString();
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("option");
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertEquals(Collections.singletonList(option), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("option");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptionTriggers().contains("option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> retrievedDefaultValues = commandLine.getValues(option, null);
        assertEquals(defaultValues, retrievedDefaultValues);

        commandLine.setDefaultValues(option, null);
        retrievedDefaultValues = commandLine.getValues(option, null);
        assertEquals(Collections.emptyList(), retrievedDefaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, retrievedDefaultSwitch);

        commandLine.setDefaultSwitch(option, null);
        retrievedDefaultSwitch = commandLine.getSwitch(option, null);
        assertNull(retrievedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("option");
        List<String> normalised = new ArrayList<>();
        normalised.add("arg1");
        normalised.add("arg2");
        commandLine = new WriteableCommandLineImpl(option, normalised);

        List<String> retrievedNormalised = commandLine.getNormalised();
        assertEquals(normalised, retrievedNormalised);
    }
}