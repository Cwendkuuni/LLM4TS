package org.apache.commons.cli2.commandline;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineImplTest {

    private Option option;
    private List<Object> arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = mock(Option.class);
        arguments = new ArrayList<Object>();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        String trigger = "trigger";
        when(option.getPreferredName()).thenReturn(trigger);
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(trigger));
    }

    @Test
    public void testGetValues() {
        List<Object> defaultValue = Arrays.asList(new Object());
        when(option.getDefaultValues()).thenReturn(defaultValue);
        List<Object> values = Arrays.asList(new Object());
        commandLine.addValue(option, values.get(0));
        assertEquals(values, commandLine.getValues(option, null));
        assertEquals(values, commandLine.getValues(option, defaultValue));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value = new Object();
        commandLine.addValue(option, value);
        assertEquals(Arrays.asList(value), commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = true;
        when(option.getDefaultSwitch()).thenReturn(defaultValue);
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
        assertFalse(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String defaultValue = "defaultValue";
        commandLine.addProperty(option, property, defaultValue);
        assertEquals(defaultValue, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        arguments.add(arg1);
        arguments.add(arg2);
        assertEquals(arg1 + " " + arg2, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(Arrays.asList(option), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        String trigger = "trigger";
        when(option.getPreferredName()).thenReturn(trigger);
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains(trigger));
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaults = Arrays.asList(new Object());
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = false;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        String arg = "arg";
        arguments.add(arg);
        assertEquals(Arrays.asList(arg), commandLine.getNormalised());
    }

}