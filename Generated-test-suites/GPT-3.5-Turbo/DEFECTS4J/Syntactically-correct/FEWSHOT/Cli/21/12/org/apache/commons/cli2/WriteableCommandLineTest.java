import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("-f");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("-f");
        Object value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("-f");
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);
        assertEquals(expectedValues, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("-f");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("-f");
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertTrue(commandLine.getSwitches().get(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        Option option = new Option("-f");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("-f");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("-f");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property));
    }

    @Test
    public void testAddDefaultProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getDefaultProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-f"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options;
        private List<Object> values;
        private List<Object> defaultValues;
        private List<Boolean> switches;
        private List<Boolean> defaultSwitches;
        private List<String> properties;
        private List<String> defaultProperties;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            values = new ArrayList<>();
            defaultValues = new ArrayList<>();
            switches = new ArrayList<>();
            defaultSwitches = new ArrayList<>();
            properties = new ArrayList<>();
            defaultProperties = new ArrayList<>();
        }

        public List<Option> getOptions() {
            return options;
        }

        public List<Object> getValues(Option option) {
            return values;
        }

        public List<Object> getUndefaultedValues(Option option) {
            return values;
        }

        public List<Object> getDefaultValues(Option option) {
            return defaultValues;
        }

        public List<Boolean> getSwitches() {
            return switches;
        }

        public Map<Option, Boolean> getSwitchesMap() {
            return switches;
        }

        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitches.get(0);
        }

        public Map<Option, Boolean> getDefaultSwitches() {
            Map<Option, Boolean> defaultSwitchesMap = new HashMap<>();
            defaultSwitchesMap.put(options.get(0), defaultSwitches.get(0));
            return defaultSwitchesMap;
        }

        public String getProperty(Option option, String property) {
            return properties.get(0);
        }

        public Map<Option, String> getPropertyMap(String property) {
            Map<Option, String> propertyMap = new HashMap<>();
            propertyMap.put(options.get(0), properties.get(0));
            return propertyMap;
        }

        public String getDefaultProperty(String property) {
            return defaultProperties.get(0);
        }

        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }

        public void addOption(Option option) {
            options.add(option);
        }

        public void addValue(Option option, Object value) {
            values.add(value);
        }

        public void setDefaultValues(Option option, List<Object> defaultValues) {
            this.defaultValues = defaultValues;
        }

        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            switches.add(value);
        }

        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            defaultSwitches.add(defaultSwitch);
        }

        public void addProperty(Option option, String property, String value) {
            properties.add(value);
        }

        public void addProperty(String property, String value) {
            defaultProperties.add(value);
        }
    }
}