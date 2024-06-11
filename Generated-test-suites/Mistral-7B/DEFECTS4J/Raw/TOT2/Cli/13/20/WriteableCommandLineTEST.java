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
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is a concrete implementation of WriteableCommandLine
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getOptionValue("o");
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValue("o");
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValue("o");
        Assert.assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, commandLine.getOptionValue("o", Boolean.TYPE));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(true, commandLine.getOptionValue("o", Boolean.TYPE));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOptionWithOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.looksLikeOption("o"));
    }

    @Test
    public void testLooksLikeOptionWithValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addValue(option, "value");
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }
}