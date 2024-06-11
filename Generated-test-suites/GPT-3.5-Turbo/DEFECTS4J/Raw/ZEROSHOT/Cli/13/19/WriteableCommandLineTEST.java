import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption", "Test Option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption", "Test Option");
        Object value = "testValue";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getOptionValues(option).contains(value));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("testOption", "Test Option");
        Object value = "testValue";
        commandLine.addValue(option, value);
        assertEquals(commandLine.getArgumentValues(option).get(0), value);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption", "Test Option");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(commandLine.getDefaultValues(option), defaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption", "Test Option");
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);
        assertEquals(commandLine.getSwitch(option), switchValue);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption", "Test Option");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(commandLine.getDefaultSwitch(option), defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        assertEquals(commandLine.getProperty(property), value);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "--testOption";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options;
        private Map<Option, List<Object>> optionValues;
        private Map<Option, List<Object>> defaultValues;
        private Map<Option, Boolean> switches;
        private Map<Option, Boolean> defaultSwitches;
        private Map<String, String> properties;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            optionValues = new HashMap<>();
            defaultValues = new HashMap<>();
            switches = new HashMap<>();
            defaultSwitches = new HashMap<>();
            properties = new HashMap<>();
        }

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            if (!optionValues.containsKey(option)) {
                optionValues.put(option, new ArrayList<>());
            }
            optionValues.get(option).add(value);
        }

        @Override
        public List<Object> getArgumentValues(Option option) {
            return optionValues.getOrDefault(option, new ArrayList<>());
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues.put(option, defaultValues);
        }

        @Override
        public List<Object> getDefaultValues(Option option) {
            return defaultValues.getOrDefault(option, new ArrayList<>());
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (switches.containsKey(option)) {
                throw new IllegalStateException("Switch already added for option: " + option);
            }
            switches.put(option, value);
        }

        @Override
        public boolean getSwitch(Option option) {
            return switches.getOrDefault(option, false);
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            defaultSwitches.put(option, defaultSwitch);
        }

        @Override
        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitches.getOrDefault(option, false);
        }

        @Override
        public void addProperty(String property, String value) {
            properties.put(property, value);
        }

        @Override
        public String getProperty(String property) {
            return properties.get(property);
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
        public List<Object> getOptionValues(Option option) {
            return optionValues.getOrDefault(option, new ArrayList<>());
        }
    }
}