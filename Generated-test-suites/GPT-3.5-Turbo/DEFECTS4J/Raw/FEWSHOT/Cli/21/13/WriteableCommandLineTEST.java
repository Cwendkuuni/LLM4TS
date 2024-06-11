
package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    
    private WriteableCommandLine commandLine;
    
    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testAddOption() {
        Option option = new OptionImpl("opt");
        commandLine.addOption(option);
        assertTrue(option.getWriteableCommandLines().contains(commandLine));
    }
    
    @Test
    public void testAddValue() {
        Option option = new OptionImpl("opt");
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option).get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("opt");
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.getUndefaultedValues(option).contains(value1));
        assertTrue(commandLine.getUndefaultedValues(option).contains(value2));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("opt");
        Object defaultValue1 = "default1";
        Object defaultValue2 = "default2";
        commandLine.setDefaultValues(option, defaultValue1, defaultValue2);
        assertEquals(2, commandLine.getDefaultValues(option).size());
        assertTrue(commandLine.getDefaultValues(option).contains(defaultValue1));
        assertTrue(commandLine.getDefaultValues(option).contains(defaultValue2));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("opt");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new OptionImpl("opt");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("opt");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("opt");
        String property = "prop";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getPropertyValue(option, property));
    }
    
    @Test
    public void testAddPropertyToDefault() {
        String property = "prop";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getDefaultPropertyValue(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-opt"));
        assertTrue(commandLine.looksLikeOption("--opt"));
    }
}

