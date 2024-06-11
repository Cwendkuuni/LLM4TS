package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {
    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(option, "value");

        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addSwitch(option, true);

        assertEquals(true, commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.setDefaultSwitch(option, true);

        assertEquals(true, commandLine.getSwitch(option));
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

        assertEquals(true, commandLine.looksLikeOption("-option"));
        assertEquals(false, commandLine.looksLikeOption("value"));
    }

    class WriteableCommandLineImpl implements WriteableCommandLine {
        List<Option> options = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        Boolean switchValue;
        String property;

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
            values.addAll(defaultValues);
        }

        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            switchValue = value;
        }

        @Override
        public Boolean getSwitch(final Option option) {
            return switchValue;
        }

        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            switchValue = defaultSwitch;
        }

        @Override
        public void addProperty(final String property, final String value) {
            this.property = value;
        }

        @Override
        public String getProperty(final String property) {
            return this.property;
        }

        @Override
        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }
    }
}