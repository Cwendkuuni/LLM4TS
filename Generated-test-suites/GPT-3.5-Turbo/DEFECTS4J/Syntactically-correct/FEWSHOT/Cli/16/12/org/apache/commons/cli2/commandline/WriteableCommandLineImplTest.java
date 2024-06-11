package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option();
        List<String> arguments = new ArrayList<String>();
        
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option();

        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Argument();

        assertNull(commandLine.getValues(option, null));
        
        Object value = new Object();
        commandLine.addValue(option, value);
        
        List<Object> expectedValues = new ArrayList<Object>();
        expectedValues.add(value);
        
        assertEquals(expectedValues, commandLine.getValues(option, null));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option();

        assertFalse(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option();

        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option();
        option.setPreferredName("option");

        assertNull(commandLine.getOption("option"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Argument();
        
        assertNull(commandLine.getValues(option, null));

        List<Object> values = new ArrayList<Object>();
        values.add(new Object());
        commandLine.addValue(option, values.get(0));
        
        assertEquals(values, commandLine.getValues(option, null));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option();
        
        assertNull(commandLine.getUndefaultedValues(option));

        List<Object> values = new ArrayList<Object>();
        values.add(new Object());
        commandLine.addValue(option, values.get(0));
        
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option();
        
        assertNull(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    
    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property"));
        
        Option option = new Option();
        commandLine.addProperty(option, "property", "value");
        
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option();

        assertTrue(commandLine.getProperties().isEmpty());
        
        commandLine.addProperty(option, "property", "value");
        
        assertFalse(commandLine.getProperties().isEmpty());
    }
    
    @Test
    public void testGetPropertyWithDefaultValue() {
        Option option = new Option();

        assertEquals("default", commandLine.getProperty(option, "property", "default"));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option();

        assertTrue(commandLine.getProperties(option).isEmpty());
        
        commandLine.addProperty(option, "property", "value");
        
        assertFalse(commandLine.getProperties(option).isEmpty());
    }
    
    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("arg"));
        assertTrue(commandLine.looksLikeOption("-arg"));
        assertTrue(commandLine.looksLikeOption("--arg"));
    }
    
    @Test
    public void testToString() {
        Option option = new Option();
        commandLine.addOption(option);
        
        List<String> arguments = new ArrayList<String>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals("arg1 arg2", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().isEmpty());
        
        Option option = new Option();
        commandLine.addOption(option);
        
        assertFalse(commandLine.getOptions().isEmpty());
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        
        Option option = new Option();
        option.setPreferredName("option");
        commandLine.addOption(option);
        
        assertFalse(commandLine.getOptionTriggers().isEmpty());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option();
        List<Object> defaults = new ArrayList<Object>();
        defaults.add(new Object());
        
        assertNull(commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, defaults);
        
        assertEquals(defaults, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option();
        assertFalse(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        assertTrue(commandLine.getNormalised().isEmpty());
        
        Option option = new Option();
        List<String> arguments = new ArrayList<String>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(arguments, commandLine.getNormalised());
    }
}