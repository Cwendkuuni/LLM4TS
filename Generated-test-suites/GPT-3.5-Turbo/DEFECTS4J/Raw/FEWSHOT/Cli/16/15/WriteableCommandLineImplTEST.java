package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import java.util.List;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option argument = new Option("arg", "Argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, null);
        commandLine.addValue(argument, "value");
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("switch", "Switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("test", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("test", "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertNull(commandLine.getOption("test"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("test"));
    }
    
    @Test
    public void testGetValues() {
        Option argument = new Option("arg", "Argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, null);
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(0, values.size());
        commandLine.addValue(argument, "value");
        values = commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
        values = commandLine.getValues(argument, List.of("default1", "default2"));
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option argument = new Option("arg", "Argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, null);
        List<String> values = commandLine.getUndefaultedValues(argument);
        assertEquals(0, values.size());
        commandLine.addValue(argument, "value");
        values = commandLine.getUndefaultedValues(argument);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("switch", "Switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        assertNull(commandLine.getSwitch(option, null));
        assertFalse(commandLine.getSwitch(option, false));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals("", commandLine.getProperty("test"));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("test", "value");
        assertEquals("value", commandLine.getProperty("test"));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals("default", commandLine.getProperty("test", "default"));
    }
    
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(0, commandLine.getProperties().size());
        commandLine.addProperty("test", "value");
        assertEquals(1, commandLine.getProperties().size());
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertFalse(commandLine.looksLikeOption("value"));
        commandLine.addOption(new Option("test", "Test Option"));
        assertTrue(commandLine.looksLikeOption("--test"));
    }
    
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, List.of("arg1", "arg2", "arg3"));
        assertEquals("arg1 arg2 arg3", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(0, commandLine.getOptions().size());
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        assertEquals(0, commandLine.getOptionTriggers().size());
        Option option = new Option("test", "Test Option");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option argument = new Option("arg", "Argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argument, null);
        List<String> defaults = List.of("value1", "value2");
        commandLine.setDefaultValues(argument, defaults);
        assertEquals(defaults, commandLine.getValues(argument, null));
        commandLine.setDefaultValues(argument, null);
        assertEquals(0, commandLine.getValues(argument, null).size());
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("switch", "Switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        commandLine.setDefaultSwitch(option, null);
        assertFalse(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, List.of("arg1", "arg2", "arg3"));
        assertEquals(List.of("arg1", "arg2", "arg3"), commandLine.getNormalised());
    }
}