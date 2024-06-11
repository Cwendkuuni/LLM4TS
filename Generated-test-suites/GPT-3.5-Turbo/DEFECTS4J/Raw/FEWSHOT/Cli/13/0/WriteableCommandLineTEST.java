package org.apache.commons.cli;

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
        Option option = new Option("f", "file");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("f", "file");
        String value = "test.txt";
        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("f", "file");
        List<String> values = new ArrayList<>();
        values.add("test1.txt");
        values.add("test2.txt");
        commandLine.addValues(option, values);

        assertEquals(values, commandLine.getArgumentValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("f", "file");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1.txt");
        defaultValues.add("default2.txt");
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose");
        boolean value = true;
        commandLine.addSwitch(option, value);

        assertEquals(value, commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose");
        Boolean defaultSwitch = false;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-f";
        assertTrue(commandLine.looksLikeOption(argument));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<Option> options = new ArrayList<>();
    private List<String> argumentValues = new ArrayList<>();
    private List<Option> switchOptions = new ArrayList<>();
    private List<Boolean> switchValues = new ArrayList<>();
    private List<String> properties = new ArrayList<>();

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        argumentValues.add(value.toString());
    }

    @Override
    public List<String> getArgumentValues(Option option) {
        return argumentValues;
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        // not implemented
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        switchOptions.add(option);
        switchValues.add(value);
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        // not implemented
    }

    @Override
    public void addProperty(String property, String value) {
        properties.add(value);
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
    public String getValue(Option option) {
        // not implemented
        return null;
    }

    @Override
    public List<String> getDefaultValues(Option option) {
        // not implemented
        return null;
    }

    @Override
    public boolean getSwitchValue(Option option) {
        // not implemented
        return false;
    }

    @Override
    public Boolean getDefaultSwitch(Option option) {
        // not implemented
        return null;
    }

    @Override
    public String getProperty(String property) {
        // not implemented
        return null;
    }
}