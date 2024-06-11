import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private List<Object> values;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new Option("option", "Option", false);
        commandLine.addOption(option);
        values = new ArrayList<Object>();
        values.add("value1");
        values.add("value2");
    }

    @Test
    public void testAddOption() {
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<Object>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitches().containsKey(option));
        assertTrue(commandLine.getSwitches().get(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitches().containsKey(option));
        assertTrue(commandLine.getSwitches().get(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperties(option).get("property"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getDefaultProperties().get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;
        private List<Object> values;
        private List<Option> switches;
        private List<Boolean> switchValues;
        private List<Option> properties;
        private List<String> propertyNames;
        private List<String> propertyValues;
        private Option currentOption;

        public WriteableCommandLineImpl() {
            options = new ArrayList<Option>();
            values = new ArrayList<Object>();
            switches = new ArrayList<Option>();
            switchValues = new ArrayList<Boolean>();
            properties = new ArrayList<Option>();
            propertyNames = new ArrayList<String>();
            propertyValues = new ArrayList<String>();
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

        public List<Object> getUndefaultedValues(final Option option) {
            return values;
        }

        public void setDefaultValues(final Option option, final List defaultValues) {
            values.addAll(defaultValues);
        }

        public List<Option> getOptions() {
            return options;
        }

        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            switches.add(option);
            switchValues.add(value);
        }

        public Boolean getSwitch(final Option option) {
            int index = switches.indexOf(option);
            return index != -1 ? switchValues.get(index) : null;
        }

        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            switches.add(option);
            switchValues.add(defaultSwitch);
        }

        public void addProperty(final Option option, final String property, final String value) {
            properties.add(option);
            propertyNames.add(property);
            propertyValues.add(value);
        }

        public void addProperty(final String property, final String value) {
            propertyNames.add(property);
            propertyValues.add(value);
        }

        public String getProperty(final Option option, final String name) {
            int index = properties.indexOf(option);
            if (index != -1 && propertyNames.get(index).equals(name)) {
                return propertyValues.get(index);
            }
            return null;
        }

        public String getDefaultProperty(final String name) {
            int index = propertyNames.indexOf(name);
            if (index != -1) {
                return propertyValues.get(index);
            }
            return null;
        }

        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }

        public Option getCurrentOption() {
            return currentOption;
        }

        public void setCurrentOption(final Option currentOption) {
            this.currentOption = currentOption;
        }

        public List<Option> getSwitches() {
            return switches;
        }

        public List<String> getPropertyNames(final Option option) {
            int index = properties.indexOf(option);
            if (index != -1) {
                return propertyNames;
            }
            return null;
        }

        public List<String> getPropertyValues(final Option option) {
            int index = properties.indexOf(option);
            if (index != -1) {
                return propertyValues;
            }
            return null;
        }

        public List<String> getDefaultPropertyNames() {
            return propertyNames;
        }

        public List<String> getDefaultPropertyValues() {
            return propertyValues;
        }

        public List<Option> getProperties() {
            return properties;
        }

        public List<String> getDefaultProperties() {
            return propertyValues;
        }
    }
}