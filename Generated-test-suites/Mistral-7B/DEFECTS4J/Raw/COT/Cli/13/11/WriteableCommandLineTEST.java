import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator(' ').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues("o");
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("v").longOpt("value").build();
        commandLine.setDefaultValues(option, Arrays.asList("defaultValue"));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues("v");
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("v").longOpt("value").build();
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("v");
        Assert.assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.setDefaultSwitch(option, false);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.isValidOption("s"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.isValidOption("s"));
        Assert.assertTrue(commandLine.isSet("s"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createCommandLine();
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOptionWithValue() {
        WriteableCommandLine commandLine = createCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option value"));
    }

    @Test
    public void testLooksLikeOptionWithoutValue() {
        WriteableCommandLine commandLine = createCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOptionWithDash() {
        WriteableCommandLine commandLine = createCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = createCommandLine();
        Assert.assertFalse(commandLine.looksLikeOption("arg"));
    }

    private WriteableCommandLine createCommandLine() {
        return new DefaultWriteableCommandLine();
    }

    @Test
    public void testIllegalArgumentExceptionWhenAddingValueToNonValueOption() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertThrows(IllegalArgumentException.class, () -> commandLine.addValue(option, "value"));
    }

    @Test
    public void testIllegalArgumentExceptionWhenAddingSwitchToNonSwitchOption() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertThrows(IllegalArgumentException.class, () -> commandLine.addSwitch(option, true));
    }

    @Test
    public void testIllegalArgumentExceptionWhenAddingPropertyToNonPropertyCommandLine() {
        WriteableCommandLine commandLine = createCommandLine();
        Assert.assertThrows(IllegalArgumentException.class, () -> commandLine.addProperty("property", "value"));
    }
}