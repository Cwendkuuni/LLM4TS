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
    public void testNewInstance() {
        Option option = new Option("opt", "option", true, "Option description");
        List arguments = Arrays.asList("arg1", "arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(Option.builder("opt").required(false).build(), commandLine.getOption(0));
        assertEquals("arg1", commandLine.getValue(0, Option.VALUE_INDEX).get(0));
        assertEquals("arg2", commandLine.getValue(0, Option.VALUE_INDEX).get(1));
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getValues(0, null));
        assertFalse(commandLine.hasOption(new Option("other", "other", false, "Other option")));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.addOption(option);

        assertEquals(Option.builder("opt").required(false).build(), commandLine.getOption(0));
        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("other", "other", false, "Other option")));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");

        List valueList = commandLine.getValues(option, null);

        assertEquals(1, valueList.size());
        assertEquals("value1", valueList.get(0));
        assertFalse(commandLine.hasOption(new Option("other", "other", false, "Other option")));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
        assertFalse(commandLine.hasOption(new Option("other", "other", false, "Other option")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Option returnedOption = commandLine.getOption("opt");

        assertEquals(option, returnedOption);
        assertFalse(commandLine.hasOption(new Option("other", "other", false, "Other option")));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List valueList = commandLine.getValues(option, null);

        assertEquals(2, valueList.size());
        assertEquals("value1", valueList.get(0));
        assertEquals("value2", valueList.get(1));

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        List returnedValues = commandLine.getValues(option, defaultValues);

        assertEquals(2, returnedValues.size());
        assertEquals("value1", returnedValues.get(0));
        assertEquals("value2", returnedValues.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List returnedValues = commandLine.getUndefaultedValues(option);

        assertEquals(1, returnedValues.size());
        assertEquals("value1", returnedValues.get(0));
        assertNull(commandLine.getValues(option, null).get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
        Boolean defaultSwitchValue = commandLine.getSwitch(option, false);

        assertFalse(defaultSwitchValue);
    }

    @Test
    public void testgetProperty() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addProperty(option, "property", "value1");

        String propertyValue = commandLine.getProperty(option, "property", "defaultValue");

        assertEquals("value1", propertyValue);
        assertEquals("defaultValue", commandLine.getProperty(option, "otherProperty", "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Map optionProperties = commandLine.optionToProperties;

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addProperty(option, "property", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Map returnedProperties = commandLine.getProperties(option);

        assertEquals(2, returnedProperties.size());
        assertEquals("property1", returnedProperties.keySet().iterator().next());
        assertEquals("value1", returnedProperties.get("property1"));
        assertEquals("property2", returnedProperties.keySet().iterator().next());
        assertEquals("value2", returnedProperties.get("property2"));
        assertEquals(properties, commandLine.optionToProperties);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set propertyNames = commandLine.getProperties(option).keySet();

        assertEquals(2, propertyNames.size());
        assertTrue(propertyNames.contains("property1"));
        assertTrue(propertyNames.contains("property2"));

        Set allPropertyNames = commandLine.getProperties().keySet();

        assertEquals(2, allPropertyNames.size());
        assertTrue(allPropertyNames.contains("property1"));
        assertTrue(allPropertyNames.contains("property2"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("other", "other", false, "Other option")));
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "arg1");
        commandLine.addValue(option, "arg2");

        assertEquals("option arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("opt1", "opt1", true, "Option 1 description");
        Option option2 = new Option("opt2", "opt2", false, "Option 2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("opt1", "opt1", true, "Option 1 description");
        Option option2 = new Option("opt2", "opt2", false, "Option 2 description");
        Option option3 = new Option("opt3", "opt3", true, "Option 3 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        Set triggers = commandLine.getOptionTriggers();

        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("opt1"));
        assertTrue(triggers.contains("opt2"));
        assertTrue(triggers.contains("opt3"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List returnedValues = commandLine.getValues(option, null);

        assertEquals(defaultValues.size(), returnedValues.size());
        assertEquals("defaultValue1", returnedValues.get(0));
        assertEquals("defaultValue2", returnedValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);

        assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "arg1");
        commandLine.addValue(option, "arg2");

        List normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}