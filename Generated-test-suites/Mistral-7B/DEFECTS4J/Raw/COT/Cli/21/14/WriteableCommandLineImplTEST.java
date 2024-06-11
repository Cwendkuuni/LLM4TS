package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resources.ResourceConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setup() {
        option = new Option("option", "anOption", true, "Option value");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    

    // Test addOption method
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    // Test hasOption method
    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testHasOptionFalse() {
        assertFalse(commandLine.hasOption(new Option("anotherOption", "anotherOption")));
    }

    // Test getOption method
    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionNull() {
        commandLine.getOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionUnknown() {
        commandLine.getOption("anotherOption");
    }

    // Test getValues method
    @Test
    public void testGetValuesEmpty() {
        List values = commandLine.getValues(option, null);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetValuesSingleValue() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetValuesMultipleValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValuesUnmodifiable() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        values.add("value3");
    }

    @Test
    public void testGetValuesDefaultValue() {
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }

    // Test getUndefaultedValues method
    @Test
    public void testGetUndefaultedValuesEmpty() {
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetUndefaultedValuesSingleValue() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValuesMultipleValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    // Test getSwitch method
    @Test
    public void testGetSwitchFalse() {
        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitchTrue() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSwitchWithDefault() {
        commandLine.addSwitch(option, false);
        commandLine.addSwitch(option, true);
        assertFalse(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    // Test getProperty method
    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        commandLine.addProperty(option, "property", properties);
        String property = commandLine.getProperty(option, "defaultValue");
        assertEquals("value", property);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Properties properties = new Properties();
        commandLine.addProperty(option, "property", properties);
        String property = commandLine.getProperty(option, "defaultValue");
        assertEquals("defaultValue", property);
    }

    // Test getProperties method
    @Test
    public void testGetPropertiesEmpty() {
        Properties properties = commandLine.getProperties(option);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testGetPropertiesSingleProperty() {
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        commandLine.addProperty(option, "property", properties);
        Properties result = commandLine.getProperties(option);
        assertEquals(1, result.size());
        assertEquals("value", result.getProperty("property"));
    }

    @Test
    public void testGetPropertiesMultipleProperties() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        commandLine.addProperty(option, "property1", properties);
        Properties result = commandLine.getProperties(option);
        assertEquals(2, result.size());
        assertEquals("value1", result.getProperty("property1"));
        assertEquals("value2", result.getProperty("property2"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("option"));
        assertFalse(commandLine.looksLikeOption("anotherOption"));
    }

    // Test setDefaultValues method
    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertEquals(0, normalised.size());
    }

    @Test
    public void testGetNormalisedWithValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("value1 value2", commandLine.toString());
    }

    
}