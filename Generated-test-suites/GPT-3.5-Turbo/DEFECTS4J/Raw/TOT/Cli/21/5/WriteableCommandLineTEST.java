import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option1 = new Option("option1", "desc1", true, "arg1");
        Option option2 = new Option("option2", "desc2", false, null);

        // Test adding option1
        commandLine.addOption(option1);
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option1, options.get(0));

        // Test adding option2
        commandLine.addOption(option2);
        options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "desc", true, "arg");
        Integer value = 10;

        // Test adding value to option
        commandLine.addOption(option);
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "desc", true, "arg");
        Integer value1 = 10;
        Integer value2 = 20;

        // Test getting undefaulted values from option
        commandLine.addOption(option);
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, undefaultedValues.size());
        Assert.assertEquals(value1, undefaultedValues.get(0));
        Assert.assertEquals(value2, undefaultedValues.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "desc", true, "arg");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(0);
        defaultValues.add(1);

        // Test setting default values for option
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> retrievedDefaultValues = commandLine.getDefaultValues(option);
        Assert.assertEquals(defaultValues.size(), retrievedDefaultValues.size());
        for (int i = 0; i < defaultValues.size(); i++) {
            Assert.assertEquals(defaultValues.get(i), retrievedDefaultValues.get(i));
        }
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "desc", false, null);
        boolean value = true;

        // Test adding switch to option
        commandLine.addOption(option);
        command.setSwitch(option, value);
        boolean switchValue = commandLine.getSwitch(option);
        Assert.assertEquals(value, switchValue);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "desc", false, null);
        Boolean defaultSwitch = true;

        // Test setting default switch for option
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean retrievedDefaultSwitch = commandLine.getDefaultSwitch(option);
        Assert.assertEquals(defaultSwitch, retrievedDefaultSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("option", "desc", true, "arg");
        String property = "property";
        String value = "value";

        // Test adding property to option
        commandLine.addOption(option);
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";

        // Test adding property to default property set
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument1 = "-option";
        String argument2 = "--option";
        String argument3 = "nonOption";

        // Test looking like option
        Assert.assertTrue(commandLine.looksLikeOption(argument1));
        Assert.assertTrue(commandLine.looksLikeOption(argument2));
        Assert.assertFalse(commandLine.looksLikeOption(argument3));
    }


    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options;
        private List<Object> defaultValues;
        private List<List<Object>> values;
        private List<Boolean> switches;
        private List<String> properties;

        public WriteableCommandLineImpl() {
            options = new ArrayList<>();
            defaultValues = new ArrayList<>();
            values = new ArrayList<>();
            switches = new ArrayList<>();
            properties = new ArrayList<>();
        }

        @Override
        public void addOption(final Option option) {
            options.add(option);
        }

        @Override
        public void addValue(final Option option, final Object value) {
            int index = options.indexOf(option);
            if (index != -1) {
                values.get(index).add(value);
            }
        }

        @Override
        public List<Object> getUndefaultedValues(final Option option) {
            int index = options.indexOf(option);
            if (index != -1) {
                return values.get(index);
            }
            return new ArrayList<>();
        }

        @Override
        public void setDefaultValues(final Option option, final List defaultValues) {
            int index = options.indexOf(option);
            if (index != -1) {
                this.defaultValues.set(index, defaultValues);
            }
        }

        @Override
        public void addSwitch(final Option option, final boolean value) throws IllegalStateException {
            int index = options.indexOf(option);
            if (index != -1) {
                switches.set(index, value);
            }
        }

        @Override
        public void setDefaultSwitch(final Option option, final Boolean defaultSwitch) {
            int index = options.indexOf(option);
            if (index != -1) {
                switches.set(index, defaultSwitch);
            }
        }

        @Override
        public void addProperty(final Option option, final String property, final String value) {
            int index = options.indexOf(option);
            if (index != -1) {
                properties.add(property + ":" + value);
            }
        }

        @Override
        public void addProperty(final String property, final String value) {
            properties.add(property + ":" + value);
        }

        @Override
        public boolean looksLikeOption(final String argument) {
            return argument.startsWith("-");
        }

        @Override
        public Option getCurrentOption() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setCurrentOption(Option currentOption) {
            throw new UnsupportedOperationException();
        }
    }
}