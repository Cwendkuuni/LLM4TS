package org.apache.commons.cli2;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, undefaultedValues.size());
        Assert.assertEquals("value1", undefaultedValues.get(0));
        Assert.assertEquals("value2", undefaultedValues.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_Exception() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");

        String value = commandLine.getProperty(option, "property");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testAddProperty_DefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addProperty("property", "value");

        String value = commandLine.getDefaultProperty("property");
        Assert.assertEquals("value", value);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        boolean result = commandLine.looksLikeOption("-f");
        Assert.assertTrue(result);
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options = new ArrayList<>();
        private List<Object> values = new ArrayList<>();
        private List<Object> undefaultedValues = new ArrayList<>();
        private List<List<Object>> defaultValues = new ArrayList<>();
        private List<Boolean> switches = new ArrayList<>();
        private List<Boolean> defaultSwitches = new ArrayList<>();
        private List<String> properties = new ArrayList<>();
        private List<String> valuesMap = new ArrayList<>();
        private List<String> defaultProperties = new ArrayList<>();
        private List<String> defaultValuesMap = new ArrayList<>();

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
        public List<Object> getUndefaultedValues(final Option option) {
            return undefaultedValues;
        }

        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            this.defaultValues.add(defaultValues);
        }

        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            switches.add(value);
        }

        @Override
        public boolean getSwitch(final Option option) {
            return switches.get(0);
        }

        @Override
        public boolean getDefaultSwitch(final Option option) {
            return defaultSwitches.get(0);
        }

        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
        }

        @Override
        public void addProperty(final Option option, final String property, final String value) {
            properties.add(property);
            valuesMap.add(value);
        }

        @Override
        public void addProperty(final String property, final String value) {
            defaultProperties.add(property);
            defaultValuesMap.add(value);
        }

        @Override
        public String getProperty(final Option option, final String property) {
            int index = properties.indexOf(property);
            return valuesMap.get(index);
        }

        @Override
        public String getDefaultProperty(final String property) {
            int index = defaultProperties.indexOf(property);
            return defaultValuesMap.get(index);
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
}