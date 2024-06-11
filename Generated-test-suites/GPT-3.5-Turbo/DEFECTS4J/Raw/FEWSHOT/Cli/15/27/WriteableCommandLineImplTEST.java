package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option option;
    
    @Before
    public void setUp() {
        option = new Option("o", "option", true, "sample option");
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3");
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }
    
    @Test
    public void testAddOption() {
        assertTrue(commandLine.getOptions().isEmpty());
        
        commandLine.addOption(option);
        
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        assertNull(commandLine.getValues(option, null));
        
        Integer value1 = 1;
        Integer value2 = 2;
        
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        
        List<Object> expectedValues = Arrays.asList(value1, value2);
        List<Object> actualValues = commandLine.getValues(option, null);
        
        assertEquals(expectedValues, actualValues);
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
        assertNull(commandLine.getOption("o"));
        
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption("o"));
    }
    
    @Test
    public void testGetValues() {
        assertNull(commandLine.getValues(option, null));
        
        Integer value1 = 1;
        Integer value2 = 2;
        
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        
        List<Object> expectedValues = Arrays.asList(value1, value2);
        List<Object> actualValues = commandLine.getValues(option, null);
        
        assertEquals(expectedValues, actualValues);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        assertNull(commandLine.getUndefaultedValues(option));
        
        Integer value1 = 1;
        Integer value2 = 2;
        
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        
        List<Object> expectedValues = Arrays.asList(value1, value2);
        List<Object> actualValues = commandLine.getUndefaultedValues(option);
        
        assertEquals(expectedValues, actualValues);
    }
    
    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property"));
        
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        
        commandLine.addProperty(option, "property", "value");
        
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testAddProperty() {
        assertFalse(commandLine.getProperties().contains("property"));
        
        commandLine.addProperty("property", "value");
        
        assertTrue(commandLine.getProperties().contains("property"));
    }
    
    @Test
    public void testGetProperties() {
        assertFalse(commandLine.getProperties(option).contains("property"));
        
        commandLine.addProperty(option, "property", "value");
        
        assertTrue(commandLine.getProperties(option).contains("property"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("arg1"));
        
        assertTrue(commandLine.looksLikeOption("o1"));
    }
    
    @Test
    public void testToString() {
        String expectedString = "arg1 arg2 arg3";
        String actualString = commandLine.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertFalse(commandLine.getOptionTriggers().contains("o"));
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.getOptionTriggers().contains("o"));
    }
    
    @Test
    public void testSetDefaultValues() {
        assertNull(commandLine.getValues(option, null));
        
        Integer default1 = 1;
        Integer default2 = 2;
        
        commandLine.setDefaultValues(option, Arrays.asList(default1, default2));
        
        List<Object> expectedValues = Arrays.asList(default1, default2);
        List<Object> actualValues = commandLine.getValues(option, null);
        
        assertEquals(expectedValues, actualValues);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        assertFalse(commandLine.getSwitch(option, null));
        
        commandLine.setDefaultSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        List<String> expectedArguments = Arrays.asList("arg1", "arg2", "arg3");
        List<String> actualArguments = commandLine.getNormalised();
        
        assertEquals(expectedArguments, actualArguments);
    }
}