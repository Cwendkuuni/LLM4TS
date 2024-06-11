import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void addOptionTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Test option");
        commandLine.addOption(option);
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option);
        Assert.assertEquals(expectedOptions, ((WriteableCommandLineImpl) commandLine).getOptions());   
    }

    @Test
    public void addValueTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Test option");
        String value = "testValue";
        commandLine.addValue(option, value);
        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add(value);
        Assert.assertEquals(expectedValues, ((WriteableCommandLineImpl) commandLine).getOptionValues(option));   
    }

    @Test
    public void getUndefaultedValuesTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Test option");
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);
        Assert.assertEquals(expectedValues, commandLine.getUndefaultedValues(option));   
    }

    @Test
    public void setDefaultValuesTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Test option");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, ((WriteableCommandLineImpl) commandLine).getDefaultValues(option));   
    }

    @Test
    public void addSwitchTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Test option");
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);
        Assert.assertEquals(switchValue, ((WriteableCommandLineImpl) commandLine).getSwitchValue(option));   
    }

    @Test
    public void setDefaultSwitchTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Test option");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Assert.assertEquals(defaultSwitch, ((WriteableCommandLineImpl) commandLine).getDefaultSwitch(option));   
    }

    @Test
    public void addPropertyTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Test option");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Assert.assertEquals(value, ((WriteableCommandLineImpl) commandLine).getProperty(option, property));   
    }

    @Test
    public void addDefaultPropertyTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        Assert.assertEquals(value, ((WriteableCommandLineImpl) commandLine).getDefaultProperty(property));   
    }

    @Test
    public void looksLikeOptionTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-t";
        Assert.assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void setCurrentOptionTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", "Test option");
        ((WriteableCommandLineImpl) commandLine).setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        private List<Option> options = new ArrayList<>();
        private List<Object> optionValues = new ArrayList<>();
        private List<Object> defaultValues = new ArrayList<>();
        private boolean switchValue;
        private Boolean defaultSwitch;
        private String currentOption;
        private String property;

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
            return optionValues;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            switchValue = value;
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

        @Override
        public Option getCurrentOption() {
            return currentOption;
        }

        @Override
        public void setCurrentOption(Option currentOption) {
            this.currentOption = currentOption;
        }

        public List<Option> getOptions() {
            return options;
        }

        public List<Object> getOptionValues(Option option) {
            return optionValues;
        }

        public List<Object> getDefaultValues(Option option) {
            return defaultValues;
        }

        public boolean getSwitchValue(Option option) {
            return switchValue;
        }

        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitch;
        }

        public String getProperty(Option option, String property) {
            return this.property;
        }

        public String getDefaultProperty(String property) {
            return this.property;
        }
    }
}