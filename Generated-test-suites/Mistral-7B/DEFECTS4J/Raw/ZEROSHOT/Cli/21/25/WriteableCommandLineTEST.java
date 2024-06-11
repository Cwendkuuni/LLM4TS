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
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").required(false).build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").required(false).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        Assert.assertEquals("value", commandLine.getOptionValue("o"));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").required(false).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").required(false).build();
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
        Assert.assertEquals(2, defaultValues.size());
        Assert.assertEquals("defaultValue1", commandLine.getOptionValue("o", 0));
        Assert.assertEquals("defaultValue2", commandLine.getOptionValue("o", 1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").required(false).build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getOptionValue("o") == null);
        Assert.assertTrue(commandLine.hasOption("o"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").required(false).build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").required(false).build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getOptionValue("o") == null);
        Assert.assertTrue(commandLine.hasOption("o"));
        Assert.assertTrue(commandLine.getIsOptionSet("o"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").required(false).build();
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getOptionValue("o", "property"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addProperty("property", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getOptionValue("", "property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Assert.assertFalse(commandLine.looksLikeOption(""));
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
    }

    private static class CliCommandLine extends CommandLineBase implements WriteableCommandLine {
        // Empty implementation for test purpose
    }

}