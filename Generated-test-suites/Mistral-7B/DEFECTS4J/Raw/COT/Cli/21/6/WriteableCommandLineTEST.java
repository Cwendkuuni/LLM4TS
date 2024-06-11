import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is the implementing class for WriteableCommandLine
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals("test", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test1");
        commandLine.addValue(option, "test2");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1", values.get(0));
        Assert.assertEquals("test2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        List defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getOption(option).getValue());
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getOption(option).getValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getPropertyValue("option.property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        commandLine.setCurrentOption(option1);
        Assert.assertEquals(option1, commandLine.getCurrentOption());
        commandLine.setCurrentOption(option2);
        Assert.assertEquals(option2, commandLine.getCurrentOption());
    }
}