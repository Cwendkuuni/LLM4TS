import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value1 value2");
        Option option = Option.builder("test").build();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value1 value2");
        Option option = Option.builder("test").build();
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("test").build();
        List<String> defaults = new ArrayList<>();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals("test", ((Option) commandLine.getOptions().get(0)).getLongOpt());
        Assert.assertEquals("test", ((Option) commandLine.getOptions().get(1)).getLongOpt());
        Assert.assertEquals("defaultValue1", commandLine.getOption("test").getValue().get(0));
        Assert.assertEquals("defaultValue2", commandLine.getOption("test").getValue().get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("test").build();
        try {
            commandLine.addSwitch(option, true);
            commandLine.addSwitch(option, true);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }
        try {
            commandLine.addSwitch(option, false);
            commandLine.addSwitch(option, true);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }
        commandLine.addSwitch(option, false);
        Assert.assertEquals(false, commandLine.getOption("test").getValue().get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("test").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(true, commandLine.getOption("test").getValue().get(0));
        commandLine.setDefaultSwitch(option, false);
        Assert.assertEquals(false, commandLine.getOption("test").getValue().get(0));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("test").build();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Assert.assertEquals(2, commandLine.getOption("test").getValues().size());
        Assert.assertEquals("value1", commandLine.getOption("test").getValue().get(0).getValue());
        Assert.assertEquals("value2", commandLine.getOption("test").getValue().get(1).getValue());
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--test"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("test"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("-t"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("testValue"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDefaultSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("test").build();
        commandLine.setDefaultSwitch(option, true);
        commandLine.setDefaultSwitch(option, true);
    }
}