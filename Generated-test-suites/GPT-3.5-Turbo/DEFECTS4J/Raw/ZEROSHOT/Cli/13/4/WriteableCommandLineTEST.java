import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
        assertTrue(commandLine.getValues(option).isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        List defaultValues = new ArrayList();
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-test";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    // Helper class
    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options = new ArrayList<>();
        private Map<Option, List<Object>> values = new HashMap<>();
        private Map<Option, List<Object>> defaultValues = new HashMap<>();
        private Map<Option, Boolean> switches = new HashMap<>();
        private Map<Option, Boolean> defaultSwitches = new HashMap<>();
        private Map<String, String> properties = new HashMap<>();

        @Override
        public void addOption(final Option option) {
            options.add(option);
        }

        @Override
        public void addValue(final Option option, final Object value) {
            if (!values.containsKey(option)) {
                values.put(option, new ArrayList<>());
            }
            values.get(option).add(value);
        }

        @Override
        public List<Object> getValues(final Option option) {
            return values.getOrDefault(option, new ArrayList<>());
        }

        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            this.defaultValues.put(option, defaultValues);
        }

        @Override
        public List<Object> getDefaultValues(final Option option) {
            return defaultValues.getOrDefault(option, new ArrayList<>());
        }

        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            if (switches.containsKey(option)) {
                throw new IllegalStateException("Switch already added");
            }
            switches.put(option, value);
        }

        @Override
        public boolean getSwitchValue(final Option option) {
            return switches.getOrDefault(option, false);
        }

        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            defaultSwitches.put(option, defaultSwitch);
        }

        @Override
        public Boolean getDefaultSwitch(final Option option) {
            return defaultSwitches.getOrDefault(option, false);
        }

        @Override
        public void addProperty(final String property, final String value) {
            properties.put(property, value);
        }

        @Override
        public String getProperty(final String property) {
            return properties.get(property);
        }

        @Override
        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }

        @Override
        public List<Option> getOptions() {
            return options;
        }
    }
}