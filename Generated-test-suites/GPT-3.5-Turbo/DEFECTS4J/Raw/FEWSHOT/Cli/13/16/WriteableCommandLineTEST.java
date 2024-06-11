package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = new Object();
        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");

        List<Object> values = commandLine.getValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertTrue(values.contains("default"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected=IllegalStateException.class)
    public void testAddSwitch_alreadyAdded() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, true);

        assertFalse(commandLine.hasSwitch(option));
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {

    private List<Option> options;
    private List<Object> values;
    private List<Object> defaultValues;
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

    @Override
    public void addOption(final Option option) {
        options.add(option);
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
    public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
        switches.add(value);
    }

    @Override
    public boolean hasSwitch(final Option option) {
        return switches.size() > 0;
    }

    @Override
    public Boolean getSwitch(final Option option) {
        return switches.get(0);
    }

    @Override
    public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
        defaultSwitches.add(defaultSwitch);
    }

    @Override
    public Boolean getDefaultSwitch(final Option option) {
        return defaultSwitches.size() > 0 ? defaultSwitches.get(0) : null;
    }

    @Override
    public void addProperty(final String property, final String value) {
        properties.add(value);
    }

    @Override
    public String getProperty(final String property) {
        return properties.get(0);
    }

    @Override
    public boolean looksLikeOption(final String argument) {
        return argument.startsWith("-");
    }

    @Override
    public List<Option> getOptions() {
        return options;
    }
}