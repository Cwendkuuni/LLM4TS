package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class WriteableCommandLineTest {
    
    @Test
    public void testAddOption() {
        // Add an option to the WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("opt", "option", false);
        cmdLine.addOption(option);
        
        // Check if the option was added successfully
        assertTrue(cmdLine.hasOption("opt"));
        assertEquals(option, cmdLine.getOption("opt"));
    }
    
    @Test
    public void testAddValue() {
        // Add an option and a value to the WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("opt", "option", false);
        Object value = "value";
        cmdLine.addOption(option);
        cmdLine.addValue(option, value);
        
        // Check if the value was added successfully
        assertTrue(cmdLine.hasOption("opt"));
        assertNotNull(cmdLine.getValues("opt"));
        assertEquals(1, cmdLine.getValues("opt").size());
        assertEquals(value, cmdLine.getValues("opt").get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        // Add an option and some values to the WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("opt", "option", false);
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        
        // Check if the undefaulted values are returned correctly
        assertEquals(2, cmdLine.getUndefaultedValues("opt").size());
        assertTrue(cmdLine.getUndefaultedValues("opt").contains("value1"));
        assertTrue(cmdLine.getUndefaultedValues("opt").contains("value2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        // Add an option and some default values to the WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("opt", "option", false);
        cmdLine.addOption(option);
        List defaultValues = Arrays.asList("default1", "default2");
        cmdLine.setDefaultValues(option, defaultValues);
        
        // Check if the default values are set correctly
        assertEquals(defaultValues, cmdLine.getDefaultValues("opt"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        // Add a switch to the WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("opt", "option", true);
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        
        // Check if the switch was added successfully
        assertTrue(cmdLine.hasOption("opt"));
        assertTrue(cmdLine.getSwitch("opt"));
        
        // Adding the same switch again should throw an IllegalStateException
        cmdLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        // Add an option and a default switch to the WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("opt", "option", true);
        cmdLine.addOption(option);
        Boolean defaultSwitch = true;
        cmdLine.setDefaultSwitch(option, defaultSwitch);
        
        // Check if the default switch is set correctly
        assertEquals(defaultSwitch, cmdLine.getDefaultSwitch("opt"));
    }
    
    @Test
    public void testAddProperty() {
        // Add an option and a property value to the WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("opt", "option", false);
        cmdLine.addOption(option);
        String property = "property";
        String value = "value";
        cmdLine.addProperty(option, property, value);
        
        // Check if the property value was added successfully
        assertTrue(cmdLine.hasOption("opt"));
        assertNotNull(cmdLine.getProperties("opt"));
        assertEquals(value, cmdLine.getProperties("opt").get(property));
    }
    
    @Test
    public void testAddDefaultProperty() {
        // Add a default property value to the WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        cmdLine.addProperty(property, value);
        
        // Check if the default property value was added successfully
        assertTrue(cmdLine.hasDefaultProperties());
        assertNotNull(cmdLine.getDefaultProperties());
        assertEquals(value, cmdLine.getDefaultProperties().get(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        // Create a WriteableCommandLine
        WriteableCommandLine cmdLine = new WriteableCommandLineImpl();
        
        // Test different argument cases
        assertTrue(cmdLine.looksLikeOption("-option"));
        assertTrue(cmdLine.looksLikeOption("--option"));
        assertTrue(cmdLine.looksLikeOption("-1"));
        assertTrue(cmdLine.looksLikeOption("--1"));
        assertFalse(cmdLine.looksLikeOption("option"));
        assertFalse(cmdLine.looksLikeOption("1"));
        assertFalse(cmdLine.looksLikeOption(""));
    }
    
    // Add more test methods for other methods in the WriteableCommandLine interface
    
}