package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValue(option));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        List<String> values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");
        commandLine.setDefaultValues(option, values);
        List<String> result = commandLine.getValues(option);
        assertEquals(values, result);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        List<String> values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");
        commandLine.setDefaultValues(option, values);
        List<String> result = commandLine.getValues(option);
        assertEquals(values, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-test"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {

    private Option option;
    private List<String> values;
    private boolean switchValue;
    private String property;

    @Override
    public void addOption(Option option) {
        this.option = option;
    }

    @Override
    public void addValue(Option option, Object value) {
        values = new ArrayList<String>();
        values.add((String) value);
    }

    @Override
    public List<String> getValues(Option option) {
        return values;
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        values = defaultValues;
    }

    @Override
    public void addSwitch(Option option, boolean value) throws IllegalStateException {
        switchValue = value;
    }

    @Override
    public boolean getSwitch(Option option) {
        return switchValue;
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        switchValue = defaultSwitch;
    }

    @Override
    public void addProperty(String property, String value) {
        this.property = value;
    }

    @Override
    public String getProperty(String property) {
        return this.property;
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }
}