package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Test
    public void testAddOption() {
        commandLine = new WriteableCommandLineImpl();

        Option option = new OptionImpl();

        commandLine.addOption(option);

        assertNotNull(commandLine.getOptions());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        commandLine = new WriteableCommandLineImpl();

        Option option = new OptionImpl();

        String value = "value";

        commandLine.addValue(option, value);

        assertNotNull(commandLine.getValues());
        assertTrue(commandLine.getValues().containsKey(option));
        assertEquals(value, commandLine.getValues().get(option));
    }

    @Test
    public void testGetArgumentValues() {
        commandLine = new WriteableCommandLineImpl();

        Option option = new OptionImpl();

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.setArgumentValues(option, values);

        assertNotNull(commandLine.getArgumentValues(option));
        assertEquals(values, commandLine.getArgumentValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine = new WriteableCommandLineImpl();

        Option option = new OptionImpl();

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        assertNotNull(commandLine.getDefaultValues());
        assertTrue(commandLine.getDefaultValues().containsKey(option));
        assertEquals(defaultValues, commandLine.getDefaultValues().get(option));
    }

    @Test
    public void testAddSwitch() {
        commandLine = new WriteableCommandLineImpl();

        Option option = new OptionImpl();

        commandLine.addSwitch(option, true);

        assertNotNull(commandLine.getSwitches());
        assertTrue(commandLine.getSwitches().containsKey(option));
        assertTrue(commandLine.getSwitches().get(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_alreadyAdded() {
        commandLine = new WriteableCommandLineImpl();

        Option option = new OptionImpl();

        commandLine.addSwitch(option, true);

        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine = new WriteableCommandLineImpl();

        Option option = new OptionImpl();

        commandLine.setDefaultSwitch(option, true);

        assertNotNull(commandLine.getDefaultSwitches());
        assertTrue(commandLine.getDefaultSwitches().containsKey(option));
        assertTrue(commandLine.getDefaultSwitches().get(option));
    }

    @Test
    public void testAddProperty() {
        commandLine = new WriteableCommandLineImpl();

        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertNotNull(commandLine.getProperties());
        assertTrue(commandLine.getProperties().containsKey(property));
        assertEquals(value, commandLine.getProperties().get(property));
    }

    @Test
    public void testLooksLikeOption() {
        commandLine = new WriteableCommandLineImpl();

        String argument = "-option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOption_notOption() {
        commandLine = new WriteableCommandLineImpl();

        String argument = "value";

        assertFalse(commandLine.looksLikeOption(argument));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {

    private List<Option> options = new ArrayList<Option>();
    private List<Option> switches = new ArrayList<Option>();
    private List<Option> defaultSwitches = new ArrayList<Option>();
    private List<Option> properties = new ArrayList<Option>();
    private List<String> argumentValues = new ArrayList<>();
    private List<String> defaultValues = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private List<String> defaultProperties = new ArrayList<>();

    @Override
    public void addOption(final Option option) {
        options.add(option);
    }

    @Override
    public void addValue(final Option option, final Object value) {
        values.add(value.toString());
    }

    @Override
    public List<String> getArgumentValues(Option option) {
        return argumentValues;
    }

    @Override
    public void setDefaultValues(final Option option, final List defaultValues) {
        this.defaultValues.addAll(defaultValues);
    }

    @Override
    public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
        if (switches.contains(option)) {
            throw new IllegalStateException("Option already added as a switch");
        }
        switches.add(option);
    }

    @Override
    public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
        defaultSwitches.add(option);
    }

    @Override
    public void addProperty(final String property, final String value) {
        properties.add(property);
        properties.add(value);
    }

    @Override
    public boolean looksLikeOption(final String argument) {
        return argument.startsWith("-");
    }

    // getters and setters

    public List<Option> getOptions() {
        return options;
    }

    public List<Option> getSwitches() {
        return switches;
    }

    public List<Option> getDefaultSwitches() {
        return defaultSwitches;
    }

    public List<Option> getProperties() {
        return properties;
    }
}