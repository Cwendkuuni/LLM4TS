import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOptionParser().getOptions().stream().findFirst().get());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("testValue1", values.get(0));
        Assert.assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        List<String> defaults = new ArrayList<>();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
        List<String> defaultValues = commandLine.getOptionParser().getOption("optionName").getDefaultValues();
        Assert.assertEquals(2, defaultValues.size());
        Assert.assertEquals("defaultValue1", defaultValues.get(0));
        Assert.assertEquals("defaultValue2", defaultValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        try {
            commandLine.addSwitch(option, true);
        } catch (IllegalStateException e) {
            Assert.assertEquals("optionName is already defined with switch value", e.getMessage());
        }
        try {
            commandLine.addSwitch(option, false);
            commandLine.addSwitch(option, true);
            boolean value = commandLine.getOption("optionName").getValue();
            Assert.assertTrue(value);
        } catch (IllegalStateException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.setDefaultSwitch(option, true);
        boolean value = commandLine.getOption("optionName").getValue();
        Assert.assertTrue(value);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addProperty(option, "propertyName", "propertyValue");
        String value = commandLine.getOption("optionName").getProperty("propertyName");
        Assert.assertEquals("propertyValue", value);
    }

    @Test
    public void testAddPropertyWithEmptyValue() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addProperty(option, "propertyName", "");
        String value = commandLine.getOption("optionName").getProperty("propertyName");
        Assert.assertEquals("", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyWithNullValue() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addProperty(option, "propertyName", null);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Assert.assertFalse(commandLine.looksLikeOption(""));
        Assert.assertFalse(commandLine.looksLikeOption("test"));
        Option option = Option.builder("testOption").build();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.looksLikeOption("testOption"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new CliOptions(); // Assuming CliOptions implements WriteableCommandLine
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.setCurrentOption(option1);
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option1, currentOption);
    }
}