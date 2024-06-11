package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
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
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        List<Option> options = commandLine.getOptions();
        
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option");
        
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        
        List<Object> values = commandLine.getValues(option);
        
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");
        
        List<Object> values1 = commandLine.getValues(option1);
        List<Object> values2 = commandLine.getValues(option2);
        List<Object> values3 = commandLine.getValues(new Option("option3"));
        
        assertEquals(2, values1.size());
        assertEquals("value1", values1.get(0));
        assertEquals("value2", values1.get(1));
        
        assertEquals(1, values2.size());
        assertEquals("value3", values2.get(0));
        
        assertEquals(0, values3.size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        List<Object> defaultValues = new ArrayList<>();
        
        defaultValues.add("value1");
        defaultValues.add("value2");
        
        commandLine.setDefaultValues(option, defaultValues);
        
        List<Object> values = commandLine.getValues(option);
        
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option");
        
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
        
        Boolean switchValue = commandLine.getSwitchValue(option);
        
        assertEquals(false, switchValue);
    }

    @Test
    public void testGetSwitchValue() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);
        
        Boolean switchValue1 = commandLine.getSwitchValue(option1);
        Boolean switchValue2 = commandLine.getSwitchValue(option2);
        Boolean switchValue3 = commandLine.getSwitchValue(new Option("option3"));
        
        assertEquals(true, switchValue1);
        assertEquals(false, switchValue2);
        assertNull(switchValue3);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        
        commandLine.setDefaultSwitch(option, false);
        
        Boolean switchValue = commandLine.getSwitchValue(option);
        
        assertEquals(false, switchValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        
        String value1 = commandLine.getProperty("property1");
        String value2 = commandLine.getProperty("property2");
        String value3 = commandLine.getProperty("property3");
        
        assertEquals("value1", value1);
        assertEquals("value2", value2);
        assertNull(value3);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {

    private List<Option> options;
    private List<String> values;
    private List<List<Object>> defaultValues;
    private List<Boolean> switches;
    private List<Boolean> defaultSwitches;
    private List<String> properties;

    public WriteableCommandLineImpl() {
        options = new ArrayList<>();
        values = new ArrayList<>();
        defaultValues = new ArrayList<>();
        switches = new ArrayList<>();
        defaultSwitches = new ArrayList<>();
        properties = new ArrayList<>();
    }
    
    public void addOption(Option option) {
        options.add(option);
    }

    public void addValue(Option option, Object value) {
        values.add((String) value);
    }

    public List<Object> getValues(Option option) {
        List<Object> optionValues = new ArrayList<>();
        for (String value : values) {
            optionValues.add(value);
        }
        return optionValues;
    }

    public void setDefaultValues(Option option, List defaultValues) {
        this.defaultValues.add(defaultValues);
    }

    public void addSwitch(Option option, boolean value) {
        switches.add(value);
    }

    public Boolean getSwitchValue(Option option) {
        Boolean switchValue = null;
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(option)) {
                switchValue = switches.get(i);
                break;
            }
        }
        return switchValue;
    }

    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        defaultSwitches.add(defaultSwitch);
    }

    public void addProperty(String property, String value) {
        properties.add(value);
    }

    public String getProperty(String property) {
        String propertyValue = null;
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).equals(property)) {
                propertyValue = properties.get(i);
                break;
            }
        }
        return propertyValue;
    }

    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    public List<Option> getOptions() {
        return options;
    }

    public List<String> getValues() {
        return values;
    }

    public List<List<Object>> getDefaultValues() {
        return defaultValues;
    }

    public List<Boolean> getSwitches() {
        return switches;
    }

    public List<Boolean> getDefaultSwitches() {
        return defaultSwitches;
    }

    public List<String> getProperties() {
        return properties;
    }
}