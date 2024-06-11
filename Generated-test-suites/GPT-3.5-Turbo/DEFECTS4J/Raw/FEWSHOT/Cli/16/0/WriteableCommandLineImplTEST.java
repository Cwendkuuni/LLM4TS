package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;

    @Before
    public void setUp() {
        option = new Option("a", "option A");
        argument = new Argument();
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
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
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, commandLine.getValues(argument, null));
    }
    
    @Test
    public void testAddSwitch() {
        assertFalse(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("a"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("a"));
    }
    
    @Test
    public void testGetValuesWithDefaultValues() {
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        assertEquals(defaultValues, commandLine.getValues(argument, defaultValues));
    }
    
    @Test
    public void testGetValuesWithoutDefaultValues() {
        List expectedValues = new ArrayList();
        expectedValues.add("arg1");
        expectedValues.add("arg2");
        assertEquals(expectedValues, commandLine.getValues(argument, null));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        List expectedValues = new ArrayList();
        expectedValues.add("arg1");
        expectedValues.add("arg2");
        assertEquals(expectedValues, commandLine.getUndefaultedValues(argument));
    }
    
    @Test
    public void testGetSwitchWithDefaultValue() {
        assertFalse(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }
    
    @Test
    public void testGetSwitchWithoutDefaultValue() {
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property"));
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }
    
    @Test
    public void testAddProperty() {
        assertNull(commandLine.getProperty("property"));
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testGetPropertiesWithDefaultOption() {
        assertEquals(Collections.EMPTY_SET, commandLine.getProperties());
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals(2, commandLine.getProperties(option).size());
    }
    
    @Test
    public void testGetPropertiesWithoutDefaultOption() {
        assertEquals(Collections.EMPTY_SET, commandLine.getProperties());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals(2, commandLine.getProperties().size());
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertTrue(commandLine.looksLikeOption("--a"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }
    
    @Test
    public void testToString() {
        assertEquals("arg1 arg2", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        List expectedOptions = new ArrayList();
        expectedOptions.add(option);
        assertEquals(expectedOptions, commandLine.getOptions());
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertEquals(Collections.singleton("a"), commandLine.getOptionTriggers());
    }
    
    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(argument, null));
        commandLine.setDefaultValues(option, null);
        assertEquals(Collections.EMPTY_LIST, commandLine.getValues(argument, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        List expectedValues = new ArrayList();
        expectedValues.add("arg1");
        expectedValues.add("arg2");
        assertEquals(expectedValues, commandLine.getNormalised());
    }
    
}