package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;


public class WriteableCommandLineTest {
    private WriteableCommandLine writeableCommandLine;
    private Option option;
    
    @Before
    public void setup() {
        writeableCommandLine = new WriteableCommandLineImpl();
        option = new OptionImpl();
    }
    
    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, writeableCommandLine.getValues(option));
    }
    
    @Test
    public void testGetValues() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, writeableCommandLine.getValues(option));
    }
    
    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }
    
    @Test
    public void testAddSwitch() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option));
    }
    
    @Test(expected=IllegalStateException.class)
    public void testAddSwitch_ThrowsIllegalStateException() {
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty("key1", "value1");
        assertEquals("value1", writeableCommandLine.getProperty("key1"));
    }
    
    @Test
    public void testAddProperty_OverwriteExistingValue() {
        writeableCommandLine.addProperty("key1", "value1");
        writeableCommandLine.addProperty("key1", "value2");
        assertEquals("value2", writeableCommandLine.getProperty("key1"));
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("--option"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private Option option;
        private List<Object> values;
        private List<Object> defaultValues;
        private Boolean switchValue;
        private Boolean defaultSwitch;
        private String property;
        
        public WriteableCommandLineImpl() {
            values = new ArrayList<>();
            defaultValues = new ArrayList<>();
        }
        
        @Override
        public void addOption(final Option option) {
            this.option = option;
        }
        
        @Override
        public void addValue(final Option option, final Object value) {
            values.add(value);
        }
        
        @Override
        public List<Object> getValues(final Option option) {
            return values;
        }
        
        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            this.defaultValues = defaultValues;
        }
        
        @Override
        public List<Object> getDefaultValues(final Option option) {
            return defaultValues;
        }
        
        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            if(switchValue != null) {
                throw new IllegalStateException("Switch has already been added");
            }
            switchValue = value;
        }
        
        @Override
        public boolean getSwitch(final Option option) {
            return switchValue;
        }
        
        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            this.defaultSwitch = defaultSwitch;
        }
        
        @Override
        public Boolean getDefaultSwitch(final Option option) {
            return defaultSwitch;
        }
        
        @Override
        public void addProperty(final String property, final String value) {
            this.property = value;
        }
        
        @Override
        public String getProperty(final String property) {
            return this.property;
        }
        
        @Override
        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("--");
        }
        
        @Override
        public boolean hasOption(final Option option) {
            return this.option == option;
        }
    }
}