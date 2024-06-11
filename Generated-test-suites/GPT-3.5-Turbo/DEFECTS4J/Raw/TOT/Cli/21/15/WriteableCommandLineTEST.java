import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");

        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");

        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        Assert.assertEquals("value", commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");

        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);

        Assert.assertEquals("value", undefaultedValues.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");

        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);

        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");

        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        Assert.assertEquals(true, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("o", "option", true, "Option");

        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");

        Assert.assertEquals("value", commandLine.getProperty(option, "property"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property", "value");

        Assert.assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        boolean result = commandLine.looksLikeOption("-o");

        Assert.assertTrue(result);
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options = new ArrayList<>();
        private List<Object> values = new ArrayList<>();
        private List<Object> defaultValues = new ArrayList<>();
        private List<Boolean> switches = new ArrayList<>();
        private Boolean defaultSwitch;
        private List<String> properties = new ArrayList<>();
        private List<String> propertyValues = new ArrayList<>();
        private List<String> defaultProperties = new ArrayList<>();
        private List<String> defaultPropertyValues = new ArrayList<>();

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            values.add(value);
        }

        @Override
        public List<Object> getUndefaultedValues(Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            switches.add(value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            this.defaultSwitch = defaultSwitch;
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            properties.add(property);
            propertyValues.add(value);
        }

        @Override
        public void addProperty(String property, String value) {
            defaultProperties.add(property);
            defaultPropertyValues.add(value);
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
        public List<Object> getValues(Option option) {
            return values;
        }

        @Override
        public List<Object> getDefaultValues(Option option) {
            return defaultValues;
        }

        @Override
        public List<Boolean> getSwitches(Option option) {
            return switches;
        }

        @Override
        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitch;
        }

        @Override
        public String getProperty(Option option, String property) {
            int index = properties.indexOf(property);
            return propertyValues.get(index);
        }

        @Override
        public String getProperty(String property) {
            int index = defaultProperties.indexOf(property);
            return defaultPropertyValues.get(index);
        }
    }
}