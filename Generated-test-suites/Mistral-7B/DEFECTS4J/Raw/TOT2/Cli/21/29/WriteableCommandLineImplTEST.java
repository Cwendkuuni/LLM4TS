package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setUp() {
        option = new Option("o", "output", true, "Output file path");
        commandLine = new WriteableCommandLineImpl(new Option("cmd", "command"), new ArrayList());
        commandLine.addOption(option);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals("output", option.getLongOpt());
        assertEquals("o", option.getOpt());
        assertEquals(true, option.isRequired());
        assertEquals("Output file path", option.getDescription());
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        assertEquals("test.txt", values.get(0));
        assertEquals(1, values.size());
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWhenAlreadySet() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Option resultOption = commandLine.getOption("o");
        assertEquals(option, resultOption);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option, "test.txt");
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("test.txt", values.get(0));
        assertEquals("output.txt", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property1", "value1");
        Properties properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        Map<Object, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("property1", "value1");
        assertEquals(propertiesMap, properties);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option, "property1", "value1");
        Properties properties = commandLine.getProperties(option);
        assertEquals("property1", properties.stringPropertyNames().next());
        assertEquals("value1", properties.getProperty("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("o--output"));
        assertTrue(commandLine.looksLikeOption("--output"));
        assertFalse(commandLine.looksLikeOption("--unknown"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option, "test.txt");
        commandLine.addValue(option, "output.txt");
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "test.txt output.txt";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertEquals("o", optionTriggers.iterator().next());
    }
}