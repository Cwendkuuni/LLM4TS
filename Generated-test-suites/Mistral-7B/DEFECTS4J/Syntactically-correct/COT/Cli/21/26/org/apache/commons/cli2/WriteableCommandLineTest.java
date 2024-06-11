import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--option");
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--option value");
        Option option = Option.builder("option").build();
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--option value1 --option value2");
        Option option = Option.builder("option").build();
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getValue(option).isPresent());
        Assert.assertTrue(commandLine.getValue(option).get());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getProperty(option, "property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("--value"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addValue(option, "value");
    }

    @Test
    public void testAddValueWithEmptyString() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--option");
        Option option = Option.builder("option").build();
        commandLine.addValue(option, "");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("", values.get(0));
    }

    @Test
    public void testAddValueWithNull() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--option");
        Option option = Option.builder("option").build();
        commandLine.addValue(option, null);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }
}