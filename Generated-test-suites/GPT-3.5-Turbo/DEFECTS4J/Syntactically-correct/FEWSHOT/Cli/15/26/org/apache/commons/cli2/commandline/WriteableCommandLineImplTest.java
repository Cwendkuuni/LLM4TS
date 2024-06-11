package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLineImpl;
    private Option option;
    private Argument argument;

    @Before
    public void setUp() {
        option = new OptionImpl("op");
        argument = new ArgumentImpl("arg");

        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        
        commandLineImpl = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option newOption = new OptionImpl("newOption");
        commandLineImpl.addOption(newOption);
        
        assertTrue(commandLineImpl.hasOption(newOption));
        assertEquals(newOption, commandLineImpl.getOption("newOption"));
    }
    
    @Test
    public void testAddValue() {
        commandLineImpl.addValue(option, "value1");
        commandLineImpl.addValue(option, "value2");
        
        assertEquals(Arrays.asList("value1", "value2"), commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testAddValueWithArgumentOption() {
        commandLineImpl.addValue(argument, "value1");
        commandLineImpl.addValue(argument, "value2");
        
        assertEquals(Arrays.asList("value1", "value2"), commandLineImpl.getValues(argument, null));
    }
    
    @Test
    public void testAddSwitch() {
        commandLineImpl.addSwitch(option, true);
        
        assertTrue(commandLineImpl.hasOption(option));
        assertEquals(Boolean.TRUE, commandLineImpl.getSwitch(option, null));
    }
    
    @Test(expected=IllegalStateException.class)
    public void testAddSwitchWithExistingSwitch() {
        commandLineImpl.addSwitch(option, true);
        commandLineImpl.addSwitch(option, false);
    }
    
    @Test
    public void testHasOption() {
        assertTrue(commandLineImpl.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        assertEquals(option, commandLineImpl.getOption("op"));
    }
    
    @Test
    public void testGetValues() {
        commandLineImpl.addValue(option, "value1");
        commandLineImpl.addValue(option, "value2");
        
        assertEquals(Arrays.asList("value1", "value2"), commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testGetValuesWithDefaultValues() {
        commandLineImpl.setDefaultValues(option, Arrays.asList("default1", "default2"));
        
        assertEquals(Arrays.asList("default1", "default2"), commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testGetValuesWithEmptyDefaultValues() {
        commandLineImpl.setDefaultValues(option, new ArrayList<>());
        
        assertEquals(new ArrayList<>(), commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testGetValuesWithNullDefaultValues() {
        commandLineImpl.setDefaultValues(option, null);
        
        assertEquals(new ArrayList<>(), commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testGetValuesWithDefaultValuesOverride() {
        commandLineImpl.addValue(option, "value1");
        commandLineImpl.addValue(option, "value2");
        commandLineImpl.setDefaultValues(option, Arrays.asList("default1", "default2"));
        
        assertEquals(Arrays.asList("value1", "value2"), commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        commandLineImpl.addValue(option, "value1");
        commandLineImpl.addValue(option, "value2");
        
        assertEquals(Arrays.asList("value1", "value2"), commandLineImpl.getUndefaultedValues(option));
    }
    
    @Test
    public void testGetUndefaultedValuesWithOptionNotInValues() {
        Option newOption = new OptionImpl("newOption");
        
        assertEquals(new ArrayList<>(), commandLineImpl.getUndefaultedValues(newOption));
    }
    
    @Test
    public void testGetSwitch() {
        commandLineImpl.addSwitch(option, true);
        
        assertEquals(Boolean.TRUE, commandLineImpl.getSwitch(option, null));
    }
    
    @Test
    public void testGetSwitchWithDefaultSwitch() {
        commandLineImpl.setDefaultSwitch(option, true);
        
        assertEquals(Boolean.TRUE, commandLineImpl.getSwitch(option, null));
    }
    
    @Test
    public void testGetSwitchWithDefaultSwitchOverride() {
        commandLineImpl.addSwitch(option, false);
        commandLineImpl.setDefaultSwitch(option, true);
        
        assertEquals(Boolean.FALSE, commandLineImpl.getSwitch(option, null));
    }
    
    @Test
    public void testGetSwitchWithNoSwitchOrDefaultSwitch() {
        assertNull(commandLineImpl.getSwitch(option, null));
    }
    
    @Test
    public void testGetProperty() {
        String property = "prop";
        String value = "value";
        
        commandLineImpl.addProperty(option, property, value);
        
        assertEquals(value, commandLineImpl.getProperty(option, property, null));
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        String property = "prop";
        String value = "value";
        
        assertEquals(value, commandLineImpl.getProperty(option, property, value));
    }
    
    @Test
    public void testGetPropertyWithDefaultOverride() {
        String property = "prop";
        String value = "value";
        String defaultValue = "defaultValue";
        
        commandLineImpl.addProperty(option, property, value);
        
        assertEquals(defaultValue, commandLineImpl.getProperty(option, property, defaultValue));
    }
    
    @Test
    public void testGetProperties() {
        String property1 = "prop1";
        String value1 = "value1";
        String property2 = "prop2";
        String value2 = "value2";
        
        commandLineImpl.addProperty(option, property1, value1);
        commandLineImpl.addProperty(option, property2, value2);
        
        assertTrue(commandLineImpl.getProperties(option).contains(property1));
        assertTrue(commandLineImpl.getProperties(option).contains(property2));
    }
    
    @Test
    public void testGetPropertiesWithNoProperties() {
        assertTrue(commandLineImpl.getProperties().isEmpty());
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "-op";
        
        assertTrue(commandLineImpl.looksLikeOption(trigger));
    }
    
    @Test
    public void testLooksLikeOptionWithNoPrefixes() {
        String trigger = "op";
        
        assertFalse(commandLineImpl.looksLikeOption(trigger));
    }
    
    @Test
    public void testToString() {
        assertEquals("arg1 arg2", commandLineImpl.toString());
    }
    
    @Test
    public void testGetOptions() {
        commandLineImpl.addOption(option);
        
        assertTrue(commandLineImpl.getOptions().contains(option));
    }
    
    @Test
    public void testGetOptionTriggers() {
        commandLineImpl.addOption(option);
        
        assertTrue(commandLineImpl.getOptionTriggers().contains(option.getPreferredName()));
    }
    
    @Test
    public void testSetDefaultValues() {
        List defaults = Arrays.asList("default1", "default2");
        
        commandLineImpl.setDefaultValues(option, defaults);
        
        assertEquals(defaults, commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultValuesWithNull() {
        List defaults = null;
        
        commandLineImpl.setDefaultValues(option, defaults);
        
        assertEquals(new ArrayList<>(), commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultValuesWithEmptyList() {
        List defaults = new ArrayList<>();
        
        commandLineImpl.setDefaultValues(option, defaults);
        
        assertEquals(new ArrayList<>(), commandLineImpl.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        
        commandLineImpl.setDefaultSwitch(option, defaultSwitch);
        
        assertEquals(defaultSwitch, commandLineImpl.getSwitch(option, null));
    }
    
    @Test
    public void testSetDefaultSwitchWithNull() {
        Boolean defaultSwitch = null;
        
        commandLineImpl.setDefaultSwitch(option, defaultSwitch);
        
        assertNull(commandLineImpl.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        assertEquals(Arrays.asList("arg1", "arg2"), commandLineImpl.getNormalised());
    }
}