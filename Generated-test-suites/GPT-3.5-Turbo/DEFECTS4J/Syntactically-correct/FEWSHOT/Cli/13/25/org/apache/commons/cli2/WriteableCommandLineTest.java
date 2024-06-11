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
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("option"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("option");
        commandLine.addValue(option, "value");
        assertTrue(commandLine.hasOption("option"));
        assertEquals("value", commandLine.getValue("option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("option");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);
        assertTrue(commandLine.hasOption("option"));
        assertEquals(defaultValues, commandLine.getValues("option"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption("option"));
        assertTrue(commandLine.getSwitch("option"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsException() {
        Option option = new OptionImpl("option");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasOption("option"));
        assertTrue(commandLine.getDefaultSwitch("option"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertTrue(commandLine.hasProperty("property"));
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<String> options;
    private List<String> values;
    private List<Option> defaultOptions;
    private List<List<String>> defaultValues;
    private List<Option> switches;
    private List<Boolean> defaultSwitches;
    private List<String> properties;
    private List<String> propertyValues;

    public WriteableCommandLineImpl() {
        options = new ArrayList<>();
        values = new ArrayList<>();
        defaultOptions = new ArrayList<>();
        defaultValues = new ArrayList<>();
        switches = new ArrayList<>();
        defaultSwitches = new ArrayList<>();
        properties = new ArrayList<>();
        propertyValues = new ArrayList<>();
    }

    @Override
    public void addOption(Option option) {
        options.add(option.getName());
    }

    @Override
    public void addValue(Option option, Object value) {
        values.add(value.toString());
    }

    @Override
    public List<String> getValues(Option option) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(option.getName())) {
                result.add(values.get(i));
            }
        }
        return result;
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        defaultOptions.add(option);
        defaultValues.add(defaultValues);
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        switches.add(option);
        defaultSwitches.add(value);
    }

    @Override
    public boolean getSwitch(Option option) {
        for (int i = 0; i < switches.size(); i++) {
            if (switches.get(i).equals(option)) {
                return defaultSwitches.get(i);
            }
        }
        return false;
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        switches.add(option);
        defaultSwitches.add(defaultSwitch);
    }

    @Override
    public void addProperty(String property, String value) {
        properties.add(property);
        propertyValues.add(value);
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    @Override
    public boolean hasOption(String option) {
        return options.contains(option);
    }

    @Override
    public String getValue(String option) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(option)) {
                return values.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean hasProperty(String property) {
        return properties.contains(property);
    }

    @Override
    public String getProperty(String property) {
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).equals(property)) {
                return propertyValues.get(i);
            }
        }
        return null;
    }

    @Override
    public Boolean getDefaultSwitch(String option) {
        for (int i = 0; i < switches.size(); i++) {
            if (switches.get(i).getName().equals(option)) {
                return defaultSwitches.get(i);
            }
        }
        return null;
    }

    @Override
    public boolean hasValues(Option option) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(option.getName())) {
                return true;
            }
        }
        return false;
    }
}