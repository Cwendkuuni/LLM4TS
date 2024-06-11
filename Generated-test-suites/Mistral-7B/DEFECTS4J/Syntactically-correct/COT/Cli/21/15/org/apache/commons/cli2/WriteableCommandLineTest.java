import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test", new String[0]);
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test", new String[]{"value"});
        Option option = Option.builder("test").build();
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test value1 value2", new String[0]);
        Option option = Option.builder("test").build();
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertTrue(values.contains("value1"));
        Assert.assertTrue(values.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test", new String[0]);
        Option option = Option.builder("test").build();
        List defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertFalse(values.contains("default1"));
        Assert.assertFalse(values.contains("default2"));
        Assert.assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test", new String[0]);
        Option option = Option.builder("test").build();
        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, option.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test", new String[0]);
        Option option = Option.builder("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test", new String[0]);
        Option option = Option.builder("test").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(true, option.getValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test", new String[0]);
        Option option = Option.builder("test").build();
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getProperties().get("test.property"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test", new String[0]);
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperties().get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--test"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("--test value"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test value", new String[0]);
        Assert.assertEquals(commandLine.getOption("/test"), commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test value1 value2", new String[0]);
        Option option = new Option("test2", true, false, "t");
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }
}