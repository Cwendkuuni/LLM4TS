package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        Option option1 = new Option("--option1", "Option 1");
        Option option2 = new Option("-o2", "Option 2");
        
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("--option1"));
        
        commandLine.addOption(option2);
        assertEquals(option2, commandLine.getOption("-o2"));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("-o", "Option");
        
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValue(option));
        
        commandLine.addValue(option, "value2");
        assertEquals("value2", commandLine.getValue(option));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("-o", "Option");
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("-o", "Option");
        
        List defaultValues = Arrays.asList("default1", "default2");
        
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("-o", "Option");
        
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
        
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitchValue(option));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("-o", "Option");
        
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitchValue(option));
        
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getDefaultSwitchValue(option));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("-o", "Option");

        commandLine.addProperty(option, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option, "property1"));
        
        commandLine.addProperty(option, "property2", "value2");
        assertEquals("value2", commandLine.getProperty(option, "property2"));
    }
    
    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));
        
        commandLine.addProperty("property2", "value2");
        assertEquals("value2", commandLine.getProperty("property2"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--option"));
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private Option currentOption;
        
        @Override
        public void addOption(Option option) {
            // TODO: implement this method
        }

        @Override
        public void addValue(Option option, Object value) {
            // TODO: implement this method
        }

        @Override
        public List getUndefaultedValues(Option option) {
            // TODO: implement this method
            return null;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            // TODO: implement this method
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            // TODO: implement this method
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            // TODO: implement this method
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            // TODO: implement this method
        }

        @Override
        public void addProperty(String property, String value) {
            // TODO: implement this method
        }

        @Override
        public boolean looksLikeOption(String argument) {
            // TODO: implement this method
            return false;
        }

        @Override
        public Option getCurrentOption() {
            // TODO: implement this method
            return currentOption;
        }

        @Override
        public void setCurrentOption(Option currentOption) {
            // TODO: implement this method
            this.currentOption = currentOption;
        }
    }

}