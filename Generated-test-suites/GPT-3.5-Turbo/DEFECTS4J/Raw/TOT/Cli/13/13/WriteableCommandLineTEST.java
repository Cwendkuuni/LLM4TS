import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Mock
    Option option;

    @Mock
    Object value;

    @Mock
    Boolean defaultSwitch;

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(option);

        // Verify if the option was added to the command line
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(option, value);

        // Verify if the value was added to the option in the command line
        Assert.assertTrue(commandLine.getOptionValues(option).contains(value));
    }

    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = Mockito.mock(Option.class);

        // Add values to the option
        List<Object> values = new ArrayList<>();
        values.add("Value1");
        values.add("Value2");
        commandLine.addValue(option, "Value1");
        commandLine.addValue(option, "Value2");

        // Verify if the values are retrieved correctly
        Assert.assertEquals(values, commandLine.getOptionValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("DefaultValue1");
        defaultValues.add("DefaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        // Verify if the default values were set correctly
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Add a switch value to the option
        commandLine.addSwitch(option, true);

        // Verify if the switch value was added to the option
        Assert.assertTrue(commandLine.getSwitches().get(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Add a switch value to the option
        commandLine.addSwitch(option, true);

        // Try adding the switch value again, should throw an exception
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Set the default switch value for the option
        commandLine.setDefaultSwitch(option, true);

        // Verify if the default switch value was set correctly
        Assert.assertEquals(true, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        // Verify if the properties were added correctly
        Assert.assertEquals("value1", commandLine.getProperty("property1"));
        Assert.assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Assert.assertTrue(commandLine.looksLikeOption("-option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options = new ArrayList<>();
        private List<Object> optionValues = new ArrayList<>();
        private List<Object> defaultValues = new ArrayList<>();
        private List<Boolean> switches = new ArrayList<>();
        private List<Boolean> defaultSwitches = new ArrayList<>();
        private List<String> properties = new ArrayList<>();

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            optionValues.add(value);
        }

        @Override
        public List<Object> getOptionValues(Option option) {
            List<Object> values = new ArrayList<>();
            for (Object value : optionValues) {
                if (values.contains(value)) {
                    values.add(value);
                }
            }
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public List<Object> getDefaultValues(Option option) {
            return defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (switches.contains(value)) {
                throw new IllegalStateException("Switch already added");
            }
            switches.add(value);
        }

        @Override
        public boolean getSwitch(Option option) {
            return switches.get(options.indexOf(option));
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            int optionIndex = options.indexOf(option);
            if (optionIndex < 0) {
                throw new IllegalArgumentException("Option not found");
            }
            defaultSwitches.set(optionIndex, defaultSwitch);
        }

        @Override
        public Boolean getDefaultSwitch(Option option) {
            int optionIndex = options.indexOf(option);
            if (optionIndex < 0) {
                throw new IllegalArgumentException("Option not found");
            }
            return defaultSwitches.get(optionIndex);
        }

        @Override
        public void addProperty(String property, String value) {
            for (int i = 0; i < properties.size(); i++) {
                if (properties.get(i).equals(property)) {
                    properties.remove(i);
                    break;
                }
            }
            properties.add(property + "=" + value);
        }

        @Override
        public String getProperty(String property) {
            for (String prop : properties) {
                if (prop.startsWith(property + "=")) {
                    return prop.substring(property.length() + 1);
                }
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

        @Override
        public List<Boolean> getSwitches() {
            return switches;
        }
    }
}