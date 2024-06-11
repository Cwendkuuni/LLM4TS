package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;
    private List arguments;
    private String trigger;
    private List valueList;
    private List defaultValues;
    private Boolean bool;
    private Boolean defaultValue;
    private String property;
    private String value;
    private List options;
    private Set prefixes;

    @Before
    public void setUp() {
        option = new Option();
        trigger = "-t";
        option.setPreferredName(trigger);
        argument = new Argument();
        option.getTriggers().add(trigger);
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
        valueList = new ArrayList();
        defaultValues = new ArrayList();
        bool = Boolean.TRUE;
        defaultValue = Boolean.FALSE;
        property = "property";
        value = "value";
        options = new ArrayList();
        prefixes = new HashSet<>();
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }
    
    @Test
    public void testAddValue() {
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, defaultValues).contains(value));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, bool);
        assertEquals(bool, commandLine.getSwitch(option, defaultValue));
    }
    
    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option()));
    }
    
    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(trigger));
        assertNull(commandLine.getOption(""));
    }
    
    @Test
    public void testGetValues() {
        commandLine.addValue(option, value);
        assertEquals(valueList, commandLine.getValues(option, defaultValues));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, value);
        assertEquals(valueList, commandLine.getUndefaultedValues(option));
    }
    
    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, bool);
        assertEquals(bool, commandLine.getSwitch(option, defaultValue));
        assertNull(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, ""));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property));
    }
    
    @Test
    public void testGetPropertyDefault() {
        assertEquals("", commandLine.getProperty(property));
    }
    
    @Test
    public void testGetPropertyOptionDefault() {
        assertEquals("", commandLine.getProperty(option, property, ""));
    }
    
    @Test
    public void testGetProperties() {
        commandLine.addProperty(option, property, value);
        assertEquals(Collections.singleton(property), commandLine.getProperties(option));
    }
    
    @Test
    public void testGetPropertiesEmpty() {
        assertEquals(Collections.EMPTY_SET, commandLine.getProperties(option));
    }
    
    @Test
    public void testLooksLikeOption() {
        prefixes.add("-");
        option.setPrefixes(prefixes);
        assertTrue(commandLine.looksLikeOption(trigger));
        assertFalse(commandLine.looksLikeOption(""));
    }
    
    @Test
    public void testToString() {
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        assertEquals(options, commandLine.getOptions());
    }
    
    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        assertEquals(Collections.singleton(trigger), commandLine.getOptionTriggers());
    }
    
    @Test
    public void testSetDefaultValuesNull() {
        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.getValues(option, defaultValues));
    }
    
    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, defaultValues));
    }
    
    @Test
    public void testSetDefaultSwitchNull() {
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, defaultValue));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, defaultValue);
        assertEquals(defaultValue, commandLine.getSwitch(option, defaultValue));
    }
    
    @Test
    public void testGetNormalised() {
        assertEquals(arguments, commandLine.getNormalised());
    }
}