
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
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        Object value = new Object();
        commandLine.addValue(option, value);
        Assert.assertEquals(value, commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        Object value = new Object();
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(new Object());
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        boolean value = true;
        commandLine.addSwitch(option, value);
        Assert.assertEquals(value, commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Assert.assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        String property = "prop";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Assert.assertEquals(value, commandLine.getProperty(option, property));
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "prop";
        String value = "value";
        commandLine.addProperty(property, value);
        Assert.assertEquals(value, commandLine.getDefaultProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "--test=value";
        Assert.assertTrue(commandLine.looksLikeOption(argument));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options = new ArrayList<>();
        private List<Object> values = new ArrayList<>();
        private List<Object> undefaultedValues = new ArrayList<>();
        private List<List<Object>> defaultValues = new ArrayList<>();
        private List<Boolean> switchValues = new ArrayList<>();
        private List<Boolean> defaultSwitches = new ArrayList<>();
        private List<List<String>> properties = new ArrayList<>();
        private List<List<String>> defaultProperties = new ArrayList<>();

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
            return undefaultedValues;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues.add(defaultValues);
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            switchValues.add(value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            List<String> propValue = new ArrayList<>();
            propValue.add(property);
            propValue.add(value);
            properties.add(propValue);
        }

        @Override
        public void addProperty(String property, String value) {
            List<String> propValue = new ArrayList<>();
            propValue.add(property);
            propValue.add(value);
            defaultProperties.add(propValue);
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("--");
        }

        @Override
        public Option getCurrentOption() {
            return null;
        }

        @Override
        public void setCurrentOption(Option currentOption) {

        }

        public List<Option> getOptions() {
            return options;
        }

        public List<Object> getValues(Option option) {
            return values;
        }

        public List<Object> getDefaultValues(Option option) {
            return defaultValues.get(options.indexOf(option));
        }

        public boolean getSwitchValue(Option option) {
            return switchValues.get(options.indexOf(option));
        }

        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitches.get(options.indexOf(option));
        }

        public String getProperty(Option option, String property) {
            for(List<String> propValue : properties) {
                if(propValue.get(0).equals(property)) {
                    return propValue.get(1);
                }
            }
            return null;
        }

        public String getDefaultProperty(String property) {
            for(List<String> propValue : defaultProperties) {
                if(propValue.get(0).equals(property)) {
                    return propValue.get(1);
                }
            }
            return null;
        }
    }
}