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
        Option option = new Option("f", "file", false, "input file");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("file"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("f", "file", false, "input file");
        commandLine.addOption(option);

        commandLine.addValue(option, "test.txt");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("test.txt");

        assertEquals(expectedValues, commandLine.getValues(option));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("f", "file", false, "input file");

        assertNull(commandLine.getValues(option));

        commandLine.addValue(option, "test.txt");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("test.txt");

        assertEquals(expectedValues, commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("f", "file", false, "input file");

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default.txt");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", true, "verbose output");

        assertFalse(commandLine.getSwitch(option));

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", true, "verbose output");

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";

        assertNull(commandLine.getProperty(property));

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-f"));
        assertTrue(commandLine.looksLikeOption("--file"));
        assertFalse(commandLine.looksLikeOption("test.txt"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {

    private List<Option> options;
    private List<Object> values;
    private List<List<String>> defaultValues;
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
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        values.add(value);
    }

    @Override
    public List<String> getValues(Option option) {
        return values;
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        this.defaultValues.add(defaultValues);
    }

    @Override
    public List<String> getDefaultValues(Option option) {
        return defaultValues;
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        switches.add(value);
    }

    @Override
    public boolean getSwitch(Option option) {
        return switches.get(0);
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        defaultSwitches.add(defaultSwitch);
    }

    @Override
    public Boolean getDefaultSwitch(Option option) {
        return defaultSwitches.get(0);
    }

    @Override
    public void addProperty(String property, String value) {
        properties.add(value);
    }

    @Override
    public String getProperty(String property) {
        return properties.get(0);
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }
}