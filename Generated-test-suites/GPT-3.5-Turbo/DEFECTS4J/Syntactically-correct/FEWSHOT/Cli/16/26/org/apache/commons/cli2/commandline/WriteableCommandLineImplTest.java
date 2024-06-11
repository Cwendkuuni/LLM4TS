package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;

    @Before
    public void setUp() {
        option = new Option("o", "option", false, "Option");
        argument = new Argument("arg", "Argument", false);

        List<String> arguments = new ArrayList<>(Arrays.asList("arg1", "arg2", "arg3"));

        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");

        List<String> values = commandLine.getValues(argument, null);

        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("option"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");

        List<String> values = commandLine.getValues(argument, null);

        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");

        List<String> values = commandLine.getUndefaultedValues(argument);

        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";

        assertNull(commandLine.getProperty(property));
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";

        Properties properties = new Properties();
        properties.setProperty(property, value);

        commandLine.addProperty(option, property, value);
        commandLine.addProperty(option, property, value);

        assertEquals(properties, commandLine.getProperties(option));
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "property";
        String value = "value";
        String defaultValue = "defaultValue";

        assertNull(commandLine.getProperty(option, property, null));
        assertEquals(defaultValue, commandLine.getProperty(option, property, defaultValue));
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        assertEquals(properties.keySet(), commandLine.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1 arg2 arg3", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = new ArrayList<>();
        options.add(option);

        assertEquals(options, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        List<String> triggers = new ArrayList<>();
        triggers.add("o");
        triggers.add("option");

        assertEquals(triggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        arguments.add("arg3");

        assertEquals(arguments, commandLine.getNormalised());
    }
}