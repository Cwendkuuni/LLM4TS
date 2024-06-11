package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private List arguments;
    
    @Before
    public void setup() {
        option = new Option();
        arguments = new ArrayList();
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
        Object value = "value";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean bool = commandLine.getSwitch(option, null);
        assertEquals(value, bool.booleanValue());
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        String trigger = "trigger";
        Option option = commandLine.getOption(trigger);
        assertNull(option);
        Option newOption = new Option();
        newOption.addTrigger(trigger);
        commandLine.addOption(newOption);
        option = commandLine.getOption(trigger);
        assertNotNull(option);
        assertEquals(newOption, option);
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option();
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        List values = commandLine.getValues(option, defaultValues);
        assertEquals(defaultValues, values);
        Object value = "value";
        commandLine.addValue(option, value);
        values = commandLine.getValues(option, defaultValues);
        assertEquals(defaultValues.size() + 1, values.size());
        assertTrue(values.containsAll(defaultValues));
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option();
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option();
        Boolean defaultValue = Boolean.TRUE;
        Boolean bool = commandLine.getSwitch(option, defaultValue);
        assertEquals(defaultValue, bool);
        commandLine.addSwitch(option, false);
        bool = commandLine.getSwitch(option, null);
        assertFalse(bool.booleanValue());
    }
    
    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        String defaultValue = "defaultValue";
        String result = commandLine.getProperty(property, defaultValue);
        assertEquals(defaultValue, result);
        commandLine.addProperty(option, property, value);
        result = commandLine.getProperty(option, property, null);
        assertEquals(value, result);
    }
    
    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String result = commandLine.getProperty(property, null);
        assertEquals(value, result);
    }
    
    @Test
    public void testAddPropertyWithOption() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String result = commandLine.getProperty(option, property, null);
        assertEquals(value, result);
    }
    
    @Test
    public void testGetProperties() {
        Set properties = commandLine.getProperties(option);
        assertEquals(0, properties.size());
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertTrue(properties.contains(property));
    }
    
    @Test
    public void testGetAllProperties() {
        Set properties = commandLine.getProperties();
        assertEquals(0, properties.size());
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "trigger";
        assertFalse(commandLine.looksLikeOption(trigger));
        option.addTrigger(trigger);
        assertTrue(commandLine.looksLikeOption(trigger));
    }
    
    @Test
    public void testToString() {
        String argument1 = "argument1";
        String argument2 = "argument2";
        arguments.add(argument1);
        arguments.add(argument2);
        String result = commandLine.toString();
        assertEquals(argument1 + " " + argument2, result);
    }
    
    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertEquals(0, options.size());
    }
    
    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();
        assertEquals(0, triggers.size());
        String trigger = "trigger";
        option.addTrigger(trigger);
        triggers = commandLine.getOptionTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains(trigger));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option();
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(defaults, values);
        defaults.remove(0);
        commandLine.setDefaultValues(option, defaults);
        values = commandLine.getValues(option, null);
        assertEquals(defaults, values);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option();
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean bool = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, bool);
        defaultSwitch = Boolean.FALSE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        bool = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, bool);
    }
    
    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        assertEquals(arguments, normalised);
    }
}