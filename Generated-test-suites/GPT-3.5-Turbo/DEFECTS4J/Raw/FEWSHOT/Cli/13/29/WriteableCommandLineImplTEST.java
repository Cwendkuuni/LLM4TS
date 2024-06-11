package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private Option option;
    private Option argument;
    private List<String> arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = new Option("o", "option", false, "An option");
        argument = new Argument("arg", true, "An argument");
        arguments = new ArrayList<>();
        arguments.add("argument1");
        arguments.add("argument2");
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(argument, "value");
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddValueWithMultipleValues() {
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(2, values.size());
        assertEquals("argument1", values.get(0));
        assertEquals("argument2", values.get(1));
    }

    @Test
    public void testGetValuesWithDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        List<String> values = commandLine.getValues(argument, defaultValues);
        assertEquals(2, values.size());
        assertEquals("argument1", values.get(0));
        assertEquals("argument2", values.get(1));
    }

    @Test
    public void testGetValuesWithOptionDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(argument, defaultValues);
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testGetValuesWithNoValues() {
        List<String> values = commandLine.getValues(option, null);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetSwitchWithDefaultValue() {
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetSwitchWithOptionDefaultValue() {
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property", null));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        assertEquals("default", commandLine.getProperty("property", "default"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals(2, commandLine.getProperties().size());
        assertTrue(commandLine.getProperties().contains("property1"));
        assertTrue(commandLine.getProperties().contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("argument1"));
    }

    @Test
    public void testToString() {
        assertEquals("argument1 argument2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("o"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultValuesWithNull() {
        List<String> defaults = new ArrayList<>();
        defaults.add("default");
        commandLine.setDefaultValues(option, defaults);
        commandLine.setDefaultValues(option, null);
        List<String> values = commandLine.getValues(option, null);
        assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testSetDefaultSwitchWithNull() {
        commandLine.setDefaultSwitch(option, true);
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(2, commandLine.getNormalised().size());
        assertTrue(commandLine.getNormalised().contains("argument1"));
        assertTrue(commandLine.getNormalised().contains("argument2"));
    }
}