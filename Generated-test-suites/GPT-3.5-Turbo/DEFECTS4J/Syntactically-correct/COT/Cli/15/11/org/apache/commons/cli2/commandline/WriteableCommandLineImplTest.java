import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Create root option
        Option rootOption = new Option("rootOption");
        commandLine = new WriteableCommandLineImpl(rootOption, null);
    }

    @Test
    public void addOption_ShouldAddOptionToList() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void addValue_ValidOption_ShouldAddValueToList() {
        Option argumentOption = new Argument();
        commandLine.addValue(argumentOption, "testValue");

        Assert.assertTrue(commandLine.getValues(argumentOption, null).contains("testValue"));
    }

    @Test
    public void addValue_InvalidOption_ShouldThrowIllegalArgumentException() {
        Option option = new Option("testOption");

        try {
            commandLine.addValue(option, "testValue");
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void addSwitch_ValidOption_ShouldAddSwitchToMap() {
        Option option = new Option("testOption");
        commandLine.addSwitch(option, true);

        Assert.assertEquals(true, commandLine.getSwitch(option, null));
    }

    @Test
    public void addSwitch_InvalidOption_ShouldThrowIllegalArgumentException() {
        Option option = new Option("testOption");

        try {
            commandLine.addSwitch(option, true);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void hasOption_ExistingOption_ShouldReturnTrue() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void hasOption_NonExistingOption_ShouldReturnFalse() {
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        commandLine.addOption(option1);

        Assert.assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void getOption_ExistingTrigger_ShouldReturnOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void getOption_NonExistingTrigger_ShouldReturnNull() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        Assert.assertNull(commandLine.getOption("nonExistingOption"));
    }

    @Test
    public void getValues_ExistingOption_ShouldReturnValues() {
        Option option = new Option("testOption");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        Assert.assertTrue(commandLine.getValues(option, null).contains("value1"));
        Assert.assertTrue(commandLine.getValues(option, null).contains("value2"));
    }

    @Test
    public void getValues_NonExistingOption_ShouldReturnDefaultValues() {
        Option option = new Option("testOption");
        commandLine.setDefaultValues(option, List.of("defaultValue1", "defaultValue2"));

        Assert.assertTrue(commandLine.getValues(option, List.of("defaultValue1", "defaultValue2")).contains("defaultValue1"));
        Assert.assertTrue(commandLine.getValues(option, List.of("defaultValue1", "defaultValue2")).contains("defaultValue2"));
    }

    @Test
    public void getUndefaultedValues_ExistingOption_ShouldReturnValues() {
        Option option = new Option("testOption");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        Assert.assertTrue(commandLine.getUndefaultedValues(option).contains("value1"));
        Assert.assertTrue(commandLine.getUndefaultedValues(option).contains("value2"));
    }

    @Test
    public void getUndefaultedValues_NonExistingOption_ShouldReturnEmptyList() {
        Option option = new Option("testOption");

        Assert.assertTrue(commandLine.getUndefaultedValues(option).isEmpty());
    }

    @Test
    public void getSwitch_ExistingOption_ShouldReturnSwitchValue() {
        Option option = new Option("testOption");
        commandLine.addSwitch(option, true);

        Assert.assertEquals(true, commandLine.getSwitch(option, null));
    }

    @Test
    public void getSwitch_NonExistingOption_ShouldReturnDefaultValue() {
        Option option = new Option("testOption");
        commandLine.setDefaultSwitch(option, true);

        Assert.assertEquals(true, commandLine.getSwitch(option, true));
    }

    @Test
    public void getProperty_ExistingProperty_ShouldReturnPropertyValue() {
        Option option = new PropertyOption();
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);

        Assert.assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void getProperty_NonExistingProperty_ShouldReturnDefaultValue() {
        Option option = new PropertyOption();
        String property = "testProperty";

        Assert.assertNull(commandLine.getProperty(option, property, null));
    }

    @Test
    public void getProperties_ExistingOption_ShouldReturnProperties() {
        Option option = new PropertyOption();
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);

        Assert.assertTrue(commandLine.getProperties(option).contains(property1));
        Assert.assertTrue(commandLine.getProperties(option).contains(property2));
    }

    @Test
    public void getProperties_NonExistingOption_ShouldReturnEmptySet() {
        Option option = new PropertyOption();

        Assert.assertTrue(commandLine.getProperties(option).isEmpty());
    }

    @Test
    public void looksLikeOption_ValidOption_ShouldReturnTrue() {
        commandLine.addOption(new Option("-t"));

        Assert.assertTrue(commandLine.looksLikeOption("-t"));
    }

    @Test
    public void looksLikeOption_InvalidOption_ShouldReturnFalse() {
        commandLine.addOption(new Option("-t"));

        Assert.assertFalse(commandLine.looksLikeOption("-u"));
    }

    @Test
    public void toString_ShouldReturnStringRepresentation() {
        commandLine.addOption(new Option("-t"));
        commandLine.addValue(new Option("-v"), "value");
        commandLine.addValue(new Argument(), "arg1");
        commandLine.addValue(new Argument(), "arg2");

        Assert.assertEquals("-t -v value arg1 arg2", commandLine.toString());
    }

    @Test
    public void getOptions_ShouldReturnListOfOptions() {
        Option option1 = new Option("-t");
        Option option2 = new Option("-v");
        Option option3 = new Argument();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        List<Option> options = commandLine.getOptions();

        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
        Assert.assertTrue(options.contains(option3));
    }

    @Test
    public void getOptionTriggers_ShouldReturnSetOfOptionTriggers() {
        Option option1 = new Option("-t");
        Option option2 = new Option("-v");
        Option option3 = new Argument();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        Set<String> triggers = commandLine.getOptionTriggers();

        Assert.assertEquals(3, triggers.size());
        Assert.assertTrue(triggers.contains("-t"));
        Assert.assertTrue(triggers.contains("-v"));
        Assert.assertTrue(triggers.contains(null));
    }

    @Test
    public void setDefaultValues_ValidOption_ShouldSetDefaultValues() {
        Option option = new Option("-t");
        commandLine.setDefaultValues(option, List.of("value1", "value2"));

        Assert.assertTrue(commandLine.getValues(option, List.of("value1", "value2")).contains("value1"));
        Assert.assertTrue(commandLine.getValues(option, List.of("value1", "value2")).contains("value2"));
    }

    @Test
    public void setDefaultValues_InvalidOption_ShouldRemoveDefaultValues() {
        Option option = new Option("-t");
        commandLine.setDefaultValues(option, List.of("value1", "value2"));
        commandLine.setDefaultValues(option, null);

        Assert.assertTrue(commandLine.getValues(option, List.of("value1", "value2")).isEmpty());
    }

    @Test
    public void setDefaultSwitch_ValidOption_ShouldSetDefaultSwitch() {
        Option option = new Option("-t");
        commandLine.setDefaultSwitch(option, true);

        Assert.assertEquals(true, commandLine.getSwitch(option, true));
    }

    @Test
    public void setDefaultSwitch_InvalidOption_ShouldRemoveDefaultSwitch() {
        Option option = new Option("-t");
        commandLine.setDefaultSwitch(option, true);
        commandLine.setDefaultSwitch(option, null);

        Assert.assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void getNormalised_ShouldReturnNormalisedList() {
        List<String> arguments = List.of("arg1", "arg2");
        Option option = new Option("-t");
        commandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals(arguments, commandLine.getNormalised());
    }
}