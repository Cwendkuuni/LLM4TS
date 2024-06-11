package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {
    
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption("test"));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "desc");
        
        commandLine.addValue(option, "value1");
        
        List<String> expected = new ArrayList<>();
        expected.add("value1");
        
        assertEquals(expected, commandLine.getValues(option));
    }
    
    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        
        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        
        commandLine.setValues(option, expected);
        
        assertEquals(expected, commandLine.getValues(option));
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        
        commandLine.setDefaultValues(option, defaultValues);
        
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        
        commandLine.addSwitch(option, true);
        
        assertTrue(commandLine.getSwitch(option));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        
        commandLine.setDefaultSwitch(option, true);
        
        assertEquals(true, commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        commandLine.addProperty("key", "value");
        
        assertEquals("value", commandLine.getProperty("key"));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("option"));
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        
        private Option option;
        private List<String> values;
        private List<String> defaultValues;
        private boolean switchValue;
        private Boolean defaultSwitch;
        private String propertyKey;
        private String propertyValue;
        
        public WriteableCommandLineImpl() {
            this.option = null;
            this.values = new ArrayList<>();
            this.defaultValues = new ArrayList<>();
            this.switchValue = false;
            this.defaultSwitch = null;
            this.propertyKey = null;
            this.propertyValue = null;
        }
        
        @Override
        public void addOption(Option option) {
            this.option = option;
        }
        
        @Override
        public void addValue(Option option, Object value) {
            values.add(value.toString());
        }
        
        @Override
        public List<String> getValues(Option option) {
            return values;
        }
        
        @Override
        public void setValues(Option option, List values) {
            this.values = values;
        }
        
        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }
        
        @Override
        public List<String> getDefaultValues(Option option) {
            return defaultValues;
        }
        
        @Override
        public void addSwitch(Option option, boolean value) {
            if (switchValue) {
                throw new IllegalStateException("Switch already added");
            }
            switchValue = value;
        }
        
        @Override
        public boolean getSwitch(Option option) {
            return switchValue;
        }
        
        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            this.defaultSwitch = defaultSwitch;
        }
        
        @Override
        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitch;
        }
        
        @Override
        public void addProperty(String property, String value) {
            this.propertyKey = property;
            this.propertyValue = value;
        }
        
        @Override
        public String getProperty(String property) {
            if (property.equals(propertyKey)) {
                return propertyValue;
            }
            return null;
        }
        
        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }
    }
}