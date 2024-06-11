package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl(new Option("a", "arg", true, "An argument"), new ArrayList());
        option = new Option("b", "bool", false, "A boolean option");
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        commandLine.addValue(option, null);
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        assertFalse(switchValue);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
        Option emptyOption = new Option("empty", "", 0, "");
        assertFalse(commandLine.hasOption(emptyOption));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("b");
        assertSame(option, retrievedOption);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptionNull() {
        commandLine.getOption(null);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("defaultValue1", values.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValuesNullOption() {
        commandLine.getValues(null, null);
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.setDefaultValues(option, new ArrayList());
        values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUndefaultedValuesNullOption() {
        commandLine.getUndefaultedValues(null);
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        assertFalse(switchValue);

        commandLine.setDefaultSwitch(option, true);
        switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSwitchNullOption() {
        commandLine.getSwitch(null, null);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value1");
        Map properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("value1", properties.get("property"));

        commandLine.addProperty(option, "property", "value2");
        properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("value2", properties.get("property"));
    }

    @Test
    public void testAddPropertyNullValue() {
        commandLine.addProperty(option, "property", null);
        Map properties = commandLine.getProperties(option);
        assertEquals(0, properties.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyNullOption() {
        commandLine.addProperty(null, "property", "value1");
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option, "property", "value1");
        String propertyValue = commandLine.getProperty("property");
        assertEquals("value1", propertyValue);

        commandLine.addProperty(option, "property", "value2");
        propertyValue = commandLine.getProperty("property");
        assertEquals("value2", propertyValue);

        commandLine.addProperty(option, "property", null);
        propertyValue = commandLine.getProperty("property");
        assertNull(propertyValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyNullOption() {
        commandLine.getProperty(null);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Map properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));

        Map allProperties = commandLine.getProperties();
        assertEquals(1, allProperties.size());
        assertTrue(allProperties.containsKey(option));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addProperty(option, "property", "value3");

        String commandLineString = commandLine.toString();
        String expected = "value1 value2 ";
        String expectedWithProperty = expected + "-p property=value3";

        if (commandLineString.endsWith(" ")) {
            commandLineString = commandLineString.substring(0, commandLineString.length() - 1);
        }

        assertEquals(expectedWithProperty, commandLineString);
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option1 = new Option("o", "option1", false, "Option 1"));
        commandLine.addOption(option2 = new Option("o", "option2", false, "Option 2"));
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option1 = new Option("o", "option1", false, "Option 1"));
        commandLine.addOption(option2 = new Option("o", "option2", false, "Option 2"));
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option, new ArrayList());
        List values = commandLine.getValues(option, null);
        assertEquals(0, values.size());

        commandLine.addValue(option, "value1");
        commandLine.setDefaultValues(option, new ArrayList());
        values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("defaultValue1", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, null);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertFalse(switchValue);

        commandLine.setDefaultSwitch(option, true);
        switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("value1", normalised.get(0));
        assertEquals("value2", normalised.get(1));
    }
}