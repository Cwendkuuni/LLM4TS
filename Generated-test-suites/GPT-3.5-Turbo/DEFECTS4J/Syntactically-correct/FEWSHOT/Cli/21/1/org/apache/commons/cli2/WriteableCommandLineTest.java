
package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;
    private Option option;
    private List<String> defaultValues;
    
    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
        option = mock(Option.class);
        defaultValues = new ArrayList<>();
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }
    
    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        List<String> expectedValues = new ArrayList<>();
        when(option.getUndefaultedValues(option)).thenReturn(expectedValues);
        
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(expectedValues, values);
        verify(option).getUndefaultedValues(option);
    }
    
    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option, defaultValues);
        List<List<String>> defaultValuesList = commandLine.getDefaultValues();
        assertEquals(1, defaultValuesList.size());
        assertEquals(defaultValues, defaultValuesList.get(0));
    }
    
    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option);
        assertEquals(value, switchValue.booleanValue());
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = commandLine.getDefaultSwitch(option);
        assertEquals(defaultSwitch, switchValue);
    }
    
    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String propertyValue = commandLine.getProperty(option, property);
        assertEquals(value, propertyValue);
    }
    
    @Test
    public void testAddDefaultProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String propertyValue = commandLine.getDefaultProperty(property);
        assertEquals(value, propertyValue);
    }
    
    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        boolean result = commandLine.looksLikeOption(argument);
        assertEquals(true, result);
    }
    
    @Test
    public void testLooksLikeOption_notOption() {
        String argument = "value";
        boolean result = commandLine.looksLikeOption(argument);
        assertEquals(false, result);
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options;
        private List<List<String>> defaultValues;
        private List<Object> values;
        private List<Boolean> switches;
        private List<String> properties;
        private List<String> defaultProperties;
        
        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            defaultValues = new ArrayList<>();
            values = new ArrayList<>();
            switches = new ArrayList<>();
            properties = new ArrayList<>();
            defaultProperties = new ArrayList<>();
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
        public List<String> getUndefaultedValues(Option option) {
            return option.getUndefaultedValues(option);
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            defaultValues.add(defaultValues);
        }

        @Override
        public void addSwitch(Option option, boolean value) {
            switches.add(value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            switches.add(defaultSwitch);
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            properties.add(value);
        }

        @Override
        public void addProperty(String property, String value) {
            defaultProperties.add(value);
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        @Override
        public void setCurrentOption(Option currentOption) {}
        
        @Override
        public List<Option> getOptions() {
            return options;
        }

        @Override
        public List<List<String>> getDefaultValues() {
            return defaultValues;
        }

        @Override
        public List<Object> getValues(Option option) {
            return values;
        }

        @Override
        public Boolean getSwitch(Option option) {
            if (switches.size() > 0) {
                return switches.get(switches.size() - 1);
            }
            return null;
        }

        @Override
        public String getProperty(Option option, String property) {
            if (properties.size() > 0) {
                return properties.get(properties.size() - 1);
            }
            return null;
        }

        @Override
        public String getDefaultProperty(String property) {
            if (defaultProperties.size() > 0) {
                return defaultProperties.get(defaultProperties.size() - 1);
            }
            return null;
        }
    }
}
