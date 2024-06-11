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
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(1, commandLine.getValues(option).size());
        Assert.assertEquals("value1", commandLine.getValues(option).get(0));

        commandLine.addValue(option, "value2");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(2, commandLine.getValues(option).size());
        Assert.assertEquals("value1", commandLine.getValues(option).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> result = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("value1", result.get(0));
        Assert.assertEquals("value2", result.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsExceptionIfAlreadyAdded() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addProperty(option, "property", "value1");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("value1", commandLine.getPropertyValue(option, "property"));

        commandLine.addProperty(option, "property", "value2");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("value2", commandLine.getPropertyValue(option, "property"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addProperty("property", "defaultValue1");
        Assert.assertEquals("defaultValue1", commandLine.getPropertyValue("property"));

        commandLine.addProperty("property", "defaultValue2");
        Assert.assertEquals("defaultValue2", commandLine.getPropertyValue("property"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Assert.assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.looksLikeOption("o"));
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testProcessOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);

        commandLine.processOption("o", null);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }
}