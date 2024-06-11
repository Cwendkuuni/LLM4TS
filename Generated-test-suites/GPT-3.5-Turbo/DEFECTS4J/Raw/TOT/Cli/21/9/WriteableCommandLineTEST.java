import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");

        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");

        commandLine.addValue(option, "value");

        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains("value"));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(2, undefaultedValues.size());
        assertTrue(undefaultedValues.contains("value1"));
        assertTrue(undefaultedValues.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");

        commandLine.addProperty(option, "prop", "value");

        assertEquals("value", commandLine.getProperty(option, "prop"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        commandLine.addProperty("prop", "value");

        assertEquals("value", commandLine.getProperty("prop"));
        assertNull("prop", commandLine.getProperty("nonexistent"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        assertNull(commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("o");

        commandLine.setCurrentOption(option);

        assertEquals(option, commandLine.getCurrentOption());
    }

    private static class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options = new ArrayList<>();
        private List<Object> values = new ArrayList<>();
        private List<Object> undefaultedValues = new ArrayList<>();
        private List<Object> defaultValues = new ArrayList<>();
        private List<Object> switches = new ArrayList<>();
        private List<Boolean> defaultSwitches = new ArrayList<>();
        private List<String> properties = new ArrayList<>();
        private List<String> defaultProperties = new ArrayList<>();
        private List<String> propertyValues = new ArrayList<>();
        private List<String> defaultPropertyValues = new ArrayList<>();
        private List<String> commandLineArguments = new ArrayList<>();
        private Option currentOption = null;

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
        public List<Object> getUndefaultedValues(Option option) {
            return undefaultedValues;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public List<Option> getOptions() {
            return options;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            switches.add(value);
        }

        @Override
        public boolean hasSwitch(Option option) {
            return switches.contains(true);
        }

        @Override
        public boolean getSwitch(Option option) {
            return switches.contains(true);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
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
        public String getProperty(Option option, String property) {
            return propertyValues.get(properties.indexOf(property));
        }

        @Override
        public String getProperty(String property) {
            return defaultPropertyValues.get(defaultProperties.indexOf(property));
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        @Override
        public Option getCurrentOption() {
            return currentOption;
        }

        @Override
        public void setCurrentOption(Option currentOption) {
            this.currentOption = currentOption;
        }
    }
}