package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    @Test
    public void testCreateInstance() {
        Option rootOption = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        assertEquals(rootOption, commandLine.getOption(rootOption.getPreferredName()));
        assertTrue(commandLine.getOptions().contains(rootOption));
        assertTrue(commandLine.getOptionTriggers().contains(rootOption.getPreferredName()));
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("option", "opt", false, "Option description.");
        Option subOption = new Option("sub", null, true, "Suboption description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
        commandLine.addOption(subOption);
        Option addedOption = commandLine.getOption("sub");
        assertSame(subOption, addedOption);
        assertTrue(commandLine.getOptions().contains(subOption));
        assertTrue(commandLine.getOptionTriggers().contains("sub"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Option result = commandLine.getOption("opt");
        assertSame(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetValuesEmpty() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List values = commandLine.getValues(option, null);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
        switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchDefaultValue() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertFalse(switchValue);
        switchValue = commandLine.getSwitch(option, null);
        assertFalse(switchValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", "value1");
        Map optionProperties = (Map) commandLine.getOptionToProperties().get(option);
        assertEquals(1, optionProperties.size());
        Map propertiesMap = (Map) optionProperties.get("properties");
        assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", "value1");
        String result = commandLine.getProperty("property1");
        assertEquals("value1", result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", "value1");
        String result = commandLine.getProperty("property2");
        assertNull(result);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", "value1");
        Set result = commandLine.getProperties(option);
        assertEquals(1, result.size());
        assertTrue(result.contains("property1"));
    }

    @Test
    public void testGetPropertiesEmpty() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Set result = commandLine.getProperties(option);
        assertEquals(0, result.size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), new ArrayList<>());
        for (String trigger : new String[]{"--property", "-p", "--prop", "-p="}) {
            assertTrue(commandLine.looksLikeOption(trigger));
        }
        for (String trigger : new String[]{"--foo", "-f"}) {
            assertFalse(commandLine.looksLikeOption(trigger));
        }
    }

    @Test
    public void testToString() {
        Option option = new Option("option", "opt", false, "Option description.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals("option value1 value2", commandLine.toString());
    }
}