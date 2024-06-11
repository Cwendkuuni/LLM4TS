import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        Object value = new Object();

        commandLine.addValue(option, value);

        Assert.assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        Object value = new Object();

        List<Object> values = new ArrayList<>();
        values.add(value);

        commandLine.addValue(option, value);

        Assert.assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        List<Object> defaultValues = new ArrayList<>();

        commandLine.setDefaultValues(option, defaultValues);

        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        boolean value = true;

        commandLine.addSwitch(option, value);

        Assert.assertEquals(value, commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        Assert.assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        Assert.assertEquals(value, commandLine.getProperty(option, property));
    }

    @Test
    public void testAddProperty_defaultSet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        Assert.assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-f";

        boolean result = commandLine.looksLikeOption(argument);

        Assert.assertTrue(result);
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options = new ArrayList<>();
        private List<Object> values = new ArrayList<>();
        private List<Object> defaultValues = new ArrayList<>();
        private boolean aSwitch;
        private Boolean defaultSwitch;
        private String property;

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            values.add(value);
        }

        @Override
        public List<Object> getUndefaultedValues(Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            if (aSwitch) {
                throw new IllegalStateException();
            }
            aSwitch = value;
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            this.defaultSwitch = defaultSwitch;
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            this.property = value;
        }

        @Override
        public void addProperty(String property, String value) {
            this.property = value;
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }
    }
}


// Test cases for the WriteableCommandLine class