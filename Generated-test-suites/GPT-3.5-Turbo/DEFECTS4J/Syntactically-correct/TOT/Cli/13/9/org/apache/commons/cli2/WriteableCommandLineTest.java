import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a");
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("a");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("a");
        commandLine.addOption(option);

        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));

        assertEquals(values, commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("a");

        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("a");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitches().contains(option));
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ExceptionAlreadyAdded() {
        Option option = new Option("a");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("a");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption_True() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOption_False() {
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption("123"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;
        private List<Object> values;
        private List<List<Object>> valueList;
        private List<List<Object>> defaultValuesList;
        private List<Option> switches;
        private List<Boolean> switchValues;
        private List<Boolean> defaultSwitchValues;
        private List<String> properties;
        private List<String> propertyValues;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            valueList = new ArrayList<>();
            defaultValuesList = new ArrayList<>();
            switches = new ArrayList<>();
            switchValues = new ArrayList<>();
            defaultSwitchValues = new ArrayList<>();
            properties = new ArrayList<>();
            propertyValues = new ArrayList<>();
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
        public List<Object> getValues(Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            defaultValuesList.add(defaultValues);
        }

        @Override
        public List getDefaultValues(Option option) {
            return defaultValuesList.get(0);
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            switches.add(option);
            switchValues.add(value);
        }

        @Override
        public List<Option> getSwitches() {
            return switches;
        }

        @Override
        public boolean getSwitchValue(Option option) {
            return switchValues.get(0);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            defaultSwitchValues.add(defaultSwitch);
        }

        @Override
        public boolean getDefaultSwitch(Option option) {
            return defaultSwitchValues.get(0);
        }

        @Override
        public void addProperty(String property, String value) {
            properties.add(property);
            propertyValues.add(value);
        }

        @Override
        public String getProperty(String property) {
            int index = properties.indexOf(property);
            if (index >= 0) {
                return propertyValues.get(index);
            }
            return null;
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        @Override
        public List<Option> getOptions() {
            return options;
        }
    }
}