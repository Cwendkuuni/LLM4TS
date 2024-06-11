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
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[]{"--option", "value1", "value2"});
        Option option = commandLine.getOptionNamed("option");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchInvalidState() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.hasOption("o"));
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("o"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, false);
        Assert.assertFalse(commandLine.hasOption("o"));
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("o"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getProperties().get("option.property"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        commandLine.addProperty("property", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getProperties().get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--option"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("--value"));
    }

    @Test
    public void testCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[]{"--option", "value"});
        commandLine.setCurrentOption(Option.builder("o").longOpt("option").build());
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals("option", currentOption.getLongOpt());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }
}