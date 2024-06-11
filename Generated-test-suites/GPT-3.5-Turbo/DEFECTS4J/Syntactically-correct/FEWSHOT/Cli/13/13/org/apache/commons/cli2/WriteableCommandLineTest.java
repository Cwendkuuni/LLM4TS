
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
        Option option = new OptionImpl("test", "t");
        commandLine.addOption(option);
        
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
        
        Option addedOption = commandLine.getOption("test");
        assertEquals(option, addedOption);
    }
    
    @Test
    public void testAddValue() {
        Option option = new OptionImpl("test", "t");
        commandLine.addOption(option);
        
        Integer value = 10;
        commandLine.addValue(option, value);
        
        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
        
        List<Object> allValues = commandLine.getAllValues();
        assertEquals(1, allValues.size());
        assertEquals(value, allValues.get(0));
    }
    
    @Test
    public void testGetValues() {
        Option option = new OptionImpl("test", "t");
        commandLine.addOption(option);
        
        Integer value1 = 10;
        commandLine.addValue(option, value1);
        
        Integer value2 = 20;
        commandLine.addValue(option, value2);
        
        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("test", "t");
        commandLine.setDefaultValues(option, new ArrayList<>());
        
        List<Object> defaultValues = commandLine.getDefaultValues(option);
        assertEquals(0, defaultValues.size());
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("test", "t");
        commandLine.addOption(option);
        
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.hasSwitch(option));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("test", "t");
        commandLine.setDefaultSwitch(option, true);
        
        Boolean defaultSwitch = commandLine.getDefaultSwitch(option);
        assertEquals(true, defaultSwitch);
    }
    
    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);
        
        String retrievedValue = commandLine.getProperty(property);
        assertEquals(value, retrievedValue);
    }
    
    @Test
    public void testLooksLikeOption() {
        String option = "-t";
        assertTrue(commandLine.looksLikeOption(option));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    
    private List<Option> options = new ArrayList<>();
    private List<Object> values = new ArrayList<>();
    private List<Object> allValues = new ArrayList<>();
    private List<Object> defaultValues = new ArrayList<>();
    private List<Option> switches = new ArrayList<>();
    private List<Boolean> switchValues = new ArrayList<>();
    private List<String> properties = new ArrayList<>();
    private List<String> propertyValues = new ArrayList<>();
    
    @Override
    public void addOption(Option option) {
        options.add(option);
    }
    
    @Override
    public void addValue(Option option, Object value) {
        values.add(value);
        allValues.add(value);
    }
    
    @Override
    public List<Object> getValues(Option option) {
        return values;
    }
    
    @Override
    public void setDefaultValues(Option option, List<Object> defaultValues) {
        this.defaultValues.addAll(defaultValues);
    }
    
    @Override
    public List<Object> getDefaultValues(Option option) {
        return defaultValues;
    }
    
    @Override
    public void addSwitch(Option option, boolean value) {
        switches.add(option);
        switchValues.add(value);
    }
    
    @Override
    public boolean hasSwitch(Option option) {
        int index = switches.indexOf(option);
        return index != -1 && switchValues.get(index);
    }
    
    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        switches.add(option);
        switchValues.add(defaultSwitch);
    }
    
    @Override
    public Boolean getDefaultSwitch(Option option) {
        int index = switches.indexOf(option);
        if (index != -1) {
            return switchValues.get(index);
        }
        return null;
    }
    
    @Override
    public void addProperty(String property, String value) {
        properties.add(property);
        propertyValues.add(value);
    }
    
    @Override
    public String getProperty(String property) {
        int index = properties.indexOf(property);
        if (index != -1) {
            return propertyValues.get(index);
        }
        return null;
    }
    
    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }
    
    @Override
    public List<Option> getOptions() {
        return options;
    }
    
    @Override
    public List<Object> getAllValues() {
        return allValues;
    }
}
