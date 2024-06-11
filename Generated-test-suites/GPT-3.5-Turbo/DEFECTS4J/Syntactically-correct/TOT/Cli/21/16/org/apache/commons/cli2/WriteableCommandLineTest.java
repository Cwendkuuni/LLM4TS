import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.DefaultOption;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;
    private Option option;
    
    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new DefaultOption("test");
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        Object value = "value";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }
    
    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getValues(option);
        assertTrue(values.containsAll(defaultValues));
    }
    
    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        boolean switchValue = commandLine.getSwitch(option);
        assertTrue(switchValue == value);
        
        try {
            commandLine.addSwitch(option, !value); // adding switch twice should throw exception
            fail("Expected exception not thrown");
        } catch (IllegalStateException e) {
            // expected exception
        }
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        boolean switchValue = commandLine.getSwitch(option);
        assertTrue(switchValue == defaultSwitch);
    }
    
    @Test
    public void testAddProperty() {
        String property = "name";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String propertyValue = commandLine.getProperty(option, property);
        assertTrue(propertyValue.equals(value));
    }
    
    @Test
    public void testAddPropertyToDefaultSet() {
        String property = "name";
        String value = "value";
        commandLine.addProperty(property, value);
        String propertyValue = commandLine.getProperty(property);
        assertTrue(propertyValue.equals(value));
    }
    
    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        assertTrue(commandLine.looksLikeOption(argument));
    }
    
    private class WriteableCommandLineImpl implements WriteableCommandLine {
        List<Option> options;
        List<Object> values;
        List<Object> undefaultedValues;
        List<Object> defaultValues;
        List<Boolean> switches;
        List<Boolean> defaultSwitches;
        List<String> properties;
        List<String> propertyValues;
        
        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            undefaultedValues = new ArrayList<>();
            defaultValues = new ArrayList<>();
            switches = new ArrayList<>();
            defaultSwitches = new ArrayList<>();
            properties = new ArrayList<>();
            propertyValues = new ArrayList<>();
        }
        
        public void addOption(final Option option) {
            options.add(option);
        }
        
        public void addValue(final Option option, final Object value) {
            values.add(value);
            undefaultedValues.add(value);
        }
        
        public List<Object> getUndefaultedValues(final Option option) {
            return undefaultedValues;
        }
        
        public void setDefaultValues(final Option option, final List defaultValues) {
            this.defaultValues.addAll(defaultValues);
            values.addAll(defaultValues);
        }
        
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            if (switches.contains(value)) {
                throw new IllegalStateException("Switch already added");
            }
            switches.add(value);
        }
        
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
            switches.add(defaultSwitch);
        }
        
        public void addProperty(final Option option, final String property, final String value) {
            properties.add(property);
            propertyValues.add(value);
        }
        
        public void addProperty(final String property, final String value) {
            properties.add(property);
            propertyValues.add(value);
        }
        
        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }
        
        public List<Option> getOptions() {
            return options;
        }
        
        public List<Object> getValues(final Option option) {
            return values;
        }
        
        public boolean getSwitch(Option option) {
            return switches.get(switches.size() - 1);
        }
        
        public String getProperty(final Option option, final String property) {
            int index = properties.indexOf(property);
            return propertyValues.get(index);
        }
        
        public String getProperty(final String property) {
            int index = properties.indexOf(property);
            return propertyValues.get(index);
        }
        
        public void setCurrentOption(Option currentOption) {
            // TODO: Implement this method
        }
    }
}