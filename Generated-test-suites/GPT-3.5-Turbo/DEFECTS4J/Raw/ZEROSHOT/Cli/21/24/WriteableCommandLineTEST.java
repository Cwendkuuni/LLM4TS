import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        Object value = new Object();
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        Object value1 = new Object();
        Object value2 = new Object();
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(new Object());
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getValues(option);
        Assert.assertEquals(defaultValues, values);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        String value1 = commandLine.getProperty(option, "property1");
        String value2 = commandLine.getProperty(option, "property2");
        Assert.assertEquals("value1", value1);
        Assert.assertEquals("value2", value2);
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        String value1 = commandLine.getProperty(null, "property1");
        String value2 = commandLine.getProperty(null, "property2");
        Assert.assertEquals("value1", value1);
        Assert.assertEquals("value2", value2);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        boolean isOption = commandLine.looksLikeOption("-test");
        Assert.assertTrue(isOption);
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.setCurrentOption(option);
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option, currentOption);
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.setCurrentOption(option);
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option, currentOption);
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;
        private List<Object> values;
        private List<Object> undefaultedValues;
        private List<List<Object>> defaultValues;
        private List<Boolean> switches;
        private List<Boolean> defaultSwitches;
        private List<Option> properties;
        private List<String> propertyNames;
        private List<List<String>> propertyValues;
        private List<String> defaultPropertyNames;
        private List<List<String>> defaultPropertyValues;
        private List<String> currentOptions;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            undefaultedValues = new ArrayList<>();
            defaultValues = new ArrayList<>();
            switches = new ArrayList<>();
            defaultSwitches = new ArrayList<>();
            properties = new ArrayList<>();
            propertyNames = new ArrayList<>();
            propertyValues = new ArrayList<>();
            defaultPropertyNames = new ArrayList<>();
            defaultPropertyValues = new ArrayList<>();
            currentOptions = new ArrayList<>();
        }

        public void addOption(final Option option) {
            options.add(option);
        }

        public void addValue(final Option option, final Object value) {
            values.add(value);
        }

        public List<Object> getValues(final Option option) {
            return values;
        }

        public List getUndefaultedValues(final Option option) {
            return undefaultedValues;
        }

        public void setDefaultValues(final Option option, final List defaultValues) {
            this.defaultValues.add(defaultValues);
        }

        public List<Option> getOptions() {
            return options;
        }

        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            switches.add(value);
        }

        public Boolean getSwitch(final Option option) {
            return switches.get(0);
        }

        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
        }

        public void addProperty(final Option option, final String property, final String value) {
            properties.add(option);
            propertyNames.add(property);
            propertyValues.add(new ArrayList<>());
            propertyValues.get(propertyValues.size() - 1).add(value);
        }

        public void addProperty(final String property, final String value) {
            propertyNames.add(property);
            propertyValues.add(new ArrayList<>());
            propertyValues.get(propertyValues.size() - 1).add(value);
        }

        public String getProperty(final Option option, final String property) {
            for (int i = 0; i < properties.size(); i++) {
                if (properties.get(i) == option && propertyNames.get(i).equals(property)) {
                    return propertyValues.get(i).get(0);
                }
            }
            return null;
        }

        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }

        public Option getCurrentOption() {
            return currentOptions.get(0);
        }

        public void setCurrentOption(final Option currentOption) {
            currentOptions.add(currentOption);
        }
    }
}