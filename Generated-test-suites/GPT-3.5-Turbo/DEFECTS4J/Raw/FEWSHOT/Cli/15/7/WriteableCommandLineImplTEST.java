
package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option option;
    private List<String> arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = new Argument();
        arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        List<String> defaultValues = new ArrayList<>();
        commandLine.setDefaultValues(option, defaultValues);
        assertTrue(commandLine.getValues(option, null).isEmpty());
    }

    @Test
    public void testGetUndefaultedValues() {
        assertTrue(commandLine.getUndefaultedValues(option).isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = false;
        assertNull(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String defaultValue = "default";
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property));
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetPropertyDefaultOption() {
        String property = "property";
        String defaultValue = "default";
        assertEquals(defaultValue, commandLine.getProperty(option, property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        assertTrue(commandLine.getProperties(option).isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-f";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String argument = "argument";
        arguments.add(argument);
        assertEquals(argument, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().isEmpty());
    }

    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertTrue(commandLine.getNormalised().isEmpty());
    }
}
