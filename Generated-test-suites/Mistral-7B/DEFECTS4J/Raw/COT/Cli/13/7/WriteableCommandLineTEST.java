import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cmd(); // Assuming Cmd is an instance of WriteableCommandLine
        Option option = Option.builder("o").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getValuesForOption("o");
        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0), "value");
    }

    @Test
    public void testGetValuesForOption() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValuesForOption("o");
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values.get(0), "value1");
        Assert.assertEquals(values.get(1), "value2");
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").build();
        List defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValuesForOption("o");
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values.get(0), "default1");
        Assert.assertEquals(values.get(1), "default2");
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingValue() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").build();
        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getValueForName("o").isPresent());
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getValueForName("o").isPresent());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").build();
        commandLine.setDefaultSwitch(option, false);
        Assert.assertFalse(commandLine.getValueForName("o").isPresent());
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getValueForName("o").isPresent());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cmd();
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getOptionValues("property").get(0));
    }

    @Test
    public void testLooksLikeOptionWithShortOption() {
        WriteableCommandLine commandLine = new Cmd();
        Assert.assertTrue(commandLine.looksLikeOption("o"));
    }

    @Test
    public void testLooksLikeOptionWithLongOption() {
        WriteableCommandLine commandLine = new Cmd();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOptionWithValue() {
        WriteableCommandLine commandLine = new Cmd();
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testLooksLikeOptionWithEmptyString() {
        WriteableCommandLine commandLine = new Cmd();
        Assert.assertFalse(commandLine.looksLikeOption(""));
    }
}