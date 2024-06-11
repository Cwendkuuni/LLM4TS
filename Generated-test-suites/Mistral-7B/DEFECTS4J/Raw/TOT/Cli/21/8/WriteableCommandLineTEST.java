import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOptionAndGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--option Value");
        Option option = Option.builder("option").longOpt("longOption").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "TestValue");

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("TestValue"), values);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CommandStarter().parse("");
        Option option = Option.builder("option").longOpt("longOption").build();

        try {
            commandLine.addSwitch(option, true);
        } catch (IllegalStateException e) {
            Assert.assertEquals("Switch value already set for option: option", e.getMessage());
        }

        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getOptionValue(option, false));

        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getOptionValue(option, false));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(new WriteableCommandLine("--option Test").looksLikeOption("--option"));
        Assert.assertFalse(new WriteableCommandLine("--option Test").looksLikeOption("--otherOption"));
        Assert.assertTrue(new WriteableCommandLine("--option Test").looksLikeOption("-o"));
        Assert.assertFalse(new WriteableCommandLine("--option Test").looksLikeOption("-t"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithSameOptionTwice() {
        WriteableCommandLine commandLine = new CommandStarter().parse("");
        Option option = Option.builder("option").longOpt("longOption").build();

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CommandStarter().parse("");
        Option option = Option.builder("option").longOpt("longOption").build();

        commandLine.addProperty(option, "property", "TestProperty");
        Assert.assertEquals("TestProperty", commandLine.getOptionValue(option, "property"));

        commandLine.addProperty(option, "property", "NewTestProperty");
        Assert.assertEquals("NewTestProperty", commandLine.getOptionValue(option, "property"));
    }

    @Test
    public void testAddDefaultValues() {
        WriteableCommandLine commandLine = new CommandStarter().parse("");
        Option option = Option.builder("option").longOpt("longOption").hasArg(false).build();

        List<String> defaultValues = Arrays.asList("DefaultValue1", "DefaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList(), values);

        List<String> allValues = commandLine.getValuesForOption(option);
        Assert.assertEquals(Arrays.asList("DefaultValue1", "DefaultValue2"), allValues);
    }

    @Test
    public void testAddValueToOption() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--option Value1 --option Value2");
        Option option = Option.builder("option").longOpt("longOption").hasArg(true).build();

        commandLine.addValue(option, "TestValue");

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("TestValue", "Value1", "Value2"), values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueToOptionTwice() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--option Value");
        Option option = Option.builder("option").longOpt("longOption").hasArg(true).build();

        commandLine.addValue(option, "TestValue");
        commandLine.addValue(option, "AnotherTestValue");
    }

    private static CommandStarter newCommandStarter() {
        return new CommandStarter();
    }

    static class CommandStarter {
        public CommandLine parse(String... args) {
            return new ParseCommandLine();
        }
    }

    static class ParseCommandLine implements CommandLine {
        private final List<Option> options = new ArrayList<>();

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            // Not supported
        }

        @Override
        public List getUndefaultedValues(Option option) {
            return new ArrayList<>();
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            // Not supported
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            // Not supported
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            // Not supported
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            // Not supported
        }

        @Override
        public void addProperty(String property, String value) {
            // Not supported
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return false;
        }

        @Override
        public Option getCurrentOption() {
            return null;
        }

        @Override
        public void setCurrentOption(Option option) {
            // Not supported
        }

        @Override
        public int getArgCount() {
            return 0;
        }

        @Override
        public String[] getArgs() {
            return new String[0];
        }

        @Override
        public Option getOption(String longOpt) {
            for (Option option : options) {
                if (option.longOpt().equals(longOpt)) {
                    return option;
                }
            }
            return null;
        }

        @Override
        public List<Option> getOptions() {
            return options;
        }

        @Override
        public List<String> getValuesForOption(Option option) {
            return new ArrayList<>();
        }

        @Override
        public void parse(String[] args) {
            // Not supported
        }

        @Override
        public String getUsage() {
            return "";
        }
    }
}