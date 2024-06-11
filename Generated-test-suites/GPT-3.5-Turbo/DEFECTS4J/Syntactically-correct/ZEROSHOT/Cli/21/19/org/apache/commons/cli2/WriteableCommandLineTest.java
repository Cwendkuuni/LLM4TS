import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private List<Object> defaultValues;
    private List<Object> values;
    private String property;
    private String value;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl();
        defaultValues = new ArrayList<>();
        values = new ArrayList<>();
        property = "property";
        value = "value";
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, value);
        assertEquals(commandLine.getUndefaultedValues(option), commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option, defaultValues);
        assertTrue(commandLine.getDefaultValues(option).containsAll(defaultValues));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitches(option).contains(true));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingValue() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertEquals(commandLine.getDefaultSwitch(option), true);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, property, value);
        assertEquals(commandLine.getProperty(option, property), value);
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty(property, value);
        assertEquals(commandLine.getProperty(property), value);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
    }

    @Test
    public void testSetAndGetCurrentOption() {
        commandLine.setCurrentOption(option);
        assertEquals(commandLine.getCurrentOption(), option);
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;
        private List<Object> optionValues;
        private List<Object> undefaultedValues;
        private List<Object> defaultSwitchValues;
        private List<Object> switches;
        private List<String> properties;

        private Option currentOption;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            optionValues = new ArrayList<>();
            undefaultedValues = new ArrayList<>();
            defaultSwitchValues = new ArrayList<>();
            switches = new ArrayList<>();
            properties = new ArrayList<>();
        }

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            optionValues.add(value);
        }

        @Override
        public List<Object> getUndefaultedValues(Option option) {
            return undefaultedValues;
        }

        @Override
        public void setDefaultValues(Option option, List<Object> defaultValues) {
            this.defaultSwitchValues = defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (switches.contains(value)) {
                throw new IllegalStateException();
            }
            switches.add(value);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            defaultSwitchValues.add(defaultSwitch);
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            properties.add(value);
        }

        @Override
        public void addProperty(String property, String value) {
            properties.add(value);
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        @Override
        public void setCurrentOption(Option currentOption) {
            this.currentOption = currentOption;
        }

        @Override
        public Option getCurrentOption() {
            return currentOption;
        }

        public List<Option> getOptions() {
            return options;
        }

        public List<Object> getValues(Option option) {
            return optionValues;
        }

        public List<Object> getDefaultValues(Option option) {
            return defaultSwitchValues;
        }

        public List<Object> getSwitches(Option option) {
            return switches;
        }

        public String getProperty(Option option, String property) {
            return properties.get(properties.indexOf(property) + 1);
        }

        public String getProperty(String property) {
            return properties.get(properties.indexOf(property) + 1);
        }
    }

    private class OptionImpl implements Option {

    }
}