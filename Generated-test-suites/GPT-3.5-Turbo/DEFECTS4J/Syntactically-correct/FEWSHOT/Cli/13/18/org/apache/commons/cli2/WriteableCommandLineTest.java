package org.apache.commons.cli2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    
    private WriteableCommandLine commandLine;
    
    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }
    
    @After
    public void tearDown() {
        commandLine = null;
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption("test"));
        assertFalse(commandLine.hasOption("foo"));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("test");
        commandLine.addOption(option);
        
        commandLine.addValue(option, "value");
        
        assertTrue(commandLine.hasValue(option));
        assertFalse(commandLine.hasValue("foo"));
    }
    
    @Test
    public void testGetArgumentValues() {
        Option option = new Option("test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        List<String> values = commandLine.getArgumentValues(option);
        
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        assertFalse(values.contains("value3"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option1, defaultValues);
        
        assertEquals(defaultValues, commandLine.getDefaultValues(option1));
        assertNull(commandLine.getDefaultValues(option2));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
        
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("test1");
        
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getDefaultSwitch(option1));
        
        commandLine.setDefaultSwitch(option1, false);
        assertFalse(commandLine.getDefaultSwitch(option1));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        
        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals("value2", commandLine.getProperty("property2"));
        assertNull(commandLine.getProperty("property3"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption("123"));
    }
}