import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    // Test method: addOption(final Option option)
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    // Test method: addValue(final Option option, final Object value)
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getOptionValue(option));
    }

    // Test method: setDefaultValues(final Option option, final List defaultValues)
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        assertEquals(List.of("default1", "default2"), commandLine.getValues(option));
    }

    // Test method: addSwitch(final Option option, final boolean value)
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    // Test method: setDefaultSwitch(final Option option, final Boolean defaultSwitch)
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test", "Test option");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    // Test method: addProperty(final String property, final String value)
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("name", "value");
        assertEquals("value", commandLine.getProperty("name"));
    }

    // Test method: looksLikeOption(final String argument)
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-test"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options = new ArrayList<>();
        private Map<Option, List<Object>> values = new HashMap<>();
        private Map<Option, Boolean> switches = new HashMap<>();
        private Map<Option, List<Object>> defaultValues = new HashMap<>();
        private Map<Option, Boolean> defaultSwitches = new HashMap<>();
        private Map<String, String> properties = new HashMap<>();

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            values.computeIfAbsent(option, k -> new ArrayList<>()).add(value);
        }

        @Override
        public List<Object> getValues(Option option) {
            return values.getOrDefault(option, Collections.emptyList());
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues.put(option, defaultValues);
        }

        @Override
        public List<Object> getDefaultValues(Option option) {
            return defaultValues.getOrDefault(option, Collections.emptyList());
        }

        @Override
        public void addSwitch(Option option, boolean value) {
            if (switches.containsKey(option)) {
                throw new IllegalStateException("Switch already added");
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
        public String getOptionValue(Option option) {
            List<Object> optionValues = values.get(option);
            return optionValues != null && !optionValues.isEmpty() ? optionValues.get(0).toString() : null;
        }
    }
}