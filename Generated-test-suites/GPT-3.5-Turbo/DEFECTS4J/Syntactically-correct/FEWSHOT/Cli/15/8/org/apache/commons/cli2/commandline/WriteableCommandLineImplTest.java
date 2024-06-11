package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value");
        assertTrue(commandLine.getValues(option, null).contains("value"));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("testOption"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getUndefaultedValues(option));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals(2, commandLine.getProperties(option).size());
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption");
        option.setPrefixes(Arrays.asList("-"));
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        assertTrue(commandLine.looksLikeOption("-option"));
    }
    
    @Test
    public void testToString() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        assertEquals("arg1 arg2", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(new HashSet(Arrays.asList("testOption1", "testOption2")), commandLine.getOptionTriggers());
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        List defaults = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }
}