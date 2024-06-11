package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    
    private WriteableCommandLine commandLine;
    
    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testAddOption() {
        Option option = new OptionImpl();
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new OptionImpl();
        Object value = new Object();
        
        commandLine.addValue(option, value);
        
        List<Object> values = commandLine.getValues(option);
        assertEquals(values.get(0), value);
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl();
        Object value1 = new Object();
        Object value2 = new Object();
        List<Object> expected = new ArrayList<Object>();
        expected.add(value1);
        expected.add(value2);
        
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        
        List<Object> actual = commandLine.getUndefaultedValues(option);
        assertEquals(actual, expected);
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl();
        List<Object> defaultValues = new ArrayList<Object>();
        Object defaultValue1 = new Object();
        Object defaultValue2 = new Object();
        defaultValues.add(defaultValue1);
        defaultValues.add(defaultValue2);
        
        commandLine.setDefaultValues(option, defaultValues);
        
        List<Object> actual = commandLine.getDefaultValues(option);
        assertEquals(actual, defaultValues);
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl();
        boolean value = true;
        
        commandLine.addSwitch(option, value);
        
        assertTrue(commandLine.getSwitches().contains(option));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl();
        Boolean defaultSwitch = Boolean.TRUE;
        
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        assertEquals(commandLine.getDefaultSwitch(option), defaultSwitch);
    }
    
    @Test
    public void testAddProperty() {
        Option option = new OptionImpl();
        String property = "key";
        String value = "value";
        
        commandLine.addProperty(option, property, value);
        
        assertEquals(commandLine.getProperty(option, property), value);
    }
    
    @Test
    public void testAddPropertyToDefaultSet() {
        String property = "key";
        String value = "value";
        
        commandLine.addProperty(property, value);
        
        assertEquals(commandLine.getProperty(property), value);
    }
    
    @Test
    public void testLooksLikeOption() {
        String argument = "-f";
        
        assertTrue(commandLine.looksLikeOption(argument));
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        
        private List<Option> options;
        private List<Object> values;
        private List<String> properties;
        private List<Boolean> switches;
        private List<Object> defaultValues;
        private List<String> defaultProperties;
        private List<Boolean> defaultSwitches;
        
        public WriteableCommandLineImpl() {
            options = new ArrayList<Option>();
            values = new ArrayList<Object>();
            properties = new ArrayList<String>();
            switches = new ArrayList<Boolean>();
            defaultValues = new ArrayList<Object>();
            defaultProperties = new ArrayList<String>();
            defaultSwitches = new ArrayList<Boolean>();
        }
        
        @Override
        public void addOption(Option option) {
            options.add(option);
        }
        
        @Override
        public void addValue(Option option, Object value) {
            values.add(value);
        }
        
        @Override
        public List<Object> getValues(Option option) {
            return values;
        }
        
        @Override
        public List<Object> getUndefaultedValues(Option option) {
            return values;
        }
        
        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }
        
        @Override
        public List<Object> getDefaultValues(Option option) {
            return defaultValues;
        }
        
        @Override
        public void addSwitch(Option option, Boolean value) {
            switches.add(value);
        }
        
        @Override
        public List<Boolean> getSwitches() {
            return switches;
        }
        
        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
        }
        
        @Override
        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitches.get(0);
        }
        
        @Override
        public void addProperty(Option option, String property, String value) {
            properties.add(value);
        }
        
        @Override
        public List<String> getProperties(Option option) {
            return properties;
        }
        
        @Override
        public void addProperty(String property, String value) {
            defaultProperties.add(value);
        }
        
        @Override
        public String getProperty(String property) {
            return defaultProperties.get(0);
        }
        
        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }
    }
}