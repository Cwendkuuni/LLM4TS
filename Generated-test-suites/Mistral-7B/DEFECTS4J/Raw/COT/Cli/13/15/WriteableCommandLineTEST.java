import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("value", commandLine.getOptionValues("optionName").get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues("optionName");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        Option option = Option.builder("optionName").build();
        List<String> defaults = java.util.Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getOptionValues("optionName");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, commandLine.getOptionValue('o'));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(true, commandLine.getOptionValue('o', false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        commandLine.addProperty("propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getProperty("propertyName"));
    }

    @Test
    public void testLooksLikeOptionWithLongOptionName() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        String longOptionName = "very-long-option-name";
        Assert.assertTrue(commandLine.looksLikeOption(longOptionName));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testLooksLikeOptionWithValue() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a valid implementation of WriteableCommandLine
        String optionValue = "value";
        Assert.assertFalse(commandLine.looksLikeOption(optionValue));
    }

}