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
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option", new CliOptions());
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option value");
        Option option = Option.builder("option").required(false).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option value1 value2");
        Option option = Option.builder("option").required(false).build();
        commandLine.addOption(option);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option");
        Option option = Option.builder("option").required(false).build();
        List defaults = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--switch true");
        Option option = Option.builder("switch").build();
        commandLine.addSwitch(option, true); //First call is fine
        commandLine.addSwitch(option, true); //Second call should raise an exception
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--switch");
        Option option = Option.builder("switch").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("switch"));
        Assert.assertTrue(commandLine.getOption("switch").isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--property propertyValue");
        Option option = Option.builder("property").build();
        commandLine.addProperty(option, "property", "propertyValue");
        String value = commandLine.getOption("property").getValue();
        Assert.assertEquals("propertyValue", value);
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        commandLine.addProperty("property", "defaultPropertyValue");
        String value = commandLine.getProperty("property");
        Assert.assertEquals("defaultPropertyValue", value);
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--option"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("--value"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("--option="));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption(""));
    }

    @Test
    public void testCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option optionValue");
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals("option", currentOption.getLongOpt());
        Assert.assertEquals("optionValue", currentOption.getValue());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.setCurrentOption(option);
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals("option", currentOption.getLongOpt());
    }
}