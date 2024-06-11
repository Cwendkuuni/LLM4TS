import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        Option optionFromMap = commandLine.getOption("opt");
        assertEquals(option, optionFromMap);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1"), values);

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", null, false, "Switch test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        Option optionFromMap = commandLine.getOption("opt");
        assertEquals(option, optionFromMap);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1"), values);

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);

        commandLine.addValue(option, "value3");
        values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2", "value3"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(Collections.singletonList("value1"), values);

        commandLine.addValue(option, "value2");
        values = commandLine.getUndefaultedValues(option);
        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", null, false, "Switch test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "prop", false, "Property test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "property");
        assertEquals("value", propertyValue);

        commandLine.addProperty(option, "property2", "value2");
        propertyValue = commandLine.getProperty(option, "property2");
        assertEquals("value2", propertyValue);

        commandLine.addProperty(option, "property", null);
        propertyValue = commandLine.getProperty(option, "property");
        assertNull(propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "prop", false, "Property test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(Collections.emptyMap(), properties);

        commandLine.addProperty(option, "property1", "value1");
        properties = commandLine.getProperties(option);
        assertEquals(Collections.singletonMap("property1", "value1"), properties);

        commandLine.addProperty(option, "property2", "value2");
        properties = commandLine.getProperties(option);
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property1", "value1");
        expectedProperties.put("property2", "value2");
        assertEquals(expectedProperties, properties);
    }

    @Test
    public void testToString() {
        String[] args = {"arg1", "arg2", "arg3"};
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "opt", false, "Option test"), Arrays.asList(args));

        String expectedString = String.join(" ", Arrays.asList("arg1", "arg2", "arg3"));
        String commandLineString = commandLine.toString();
        assertEquals(expectedString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "opt1", false, "Option test1");
        Option option2 = new Option("o2", "opt2", false, "Option test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(option2));
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        List<Option> expectedOptions = Arrays.asList(option1, option2);
        assertEquals(expectedOptions, options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "opt1", false, "Option test1");
        Option option2 = new Option("o2", "opt2", false, "Option test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(option2));
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Set<String> expectedOptionTriggers = Arrays.asList("o1", "o2").stream().collect(Collectors.toSet());
        assertEquals(expectedOptionTriggers, optionTriggers);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDefaultValuesAndSwitchTwice() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.setDefaultValues(option, Arrays.asList("value1"));
        commandLine.setDefaultValues(option, Arrays.asList("value2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDefaultSwitchAndValuesTwice() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        commandLine.setDefaultSwitch(option, false);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        List<String> defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option, null);
        assertEquals(defaultValues, values);

        commandLine.addValue(option, "value3");
        values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2", "value3"), values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        String[] args = {"arg1", "arg2", "arg3"};
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "opt", false, "Option test"), Arrays.asList(args));

        List<String> normalised = commandLine.getNormalised();
        List<String> expectedNormalised = Arrays.asList("arg1", "arg2", "arg3");
        assertEquals(expectedNormalised, normalised);
    }
}