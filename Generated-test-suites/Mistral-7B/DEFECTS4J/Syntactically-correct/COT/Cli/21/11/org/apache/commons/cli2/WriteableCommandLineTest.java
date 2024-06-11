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
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption");
        Option option = Option.builder("testOption").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOptionByName("testOption"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value1 value2");
        Option option = Option.builder("testOption").build();
        commandLine.addValue(option, "value1");
        Assert.assertEquals(Arrays.asList("value1"), commandLine.getUndefaultedValues(option));
        commandLine.addValue(option, "value2");
        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getUndefaultedValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueIllegalStateException() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value");
        Option option = Option.builder("testOption").build();
        commandLine.addValue(option, "value");
        commandLine.addValue(option, "value2");
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value1 value2");
        Option option = Option.builder("testOption").build();
        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getUndefaultedValues(option));
        Assert.assertEquals(0, commandLine.getUndefaultedValues(Option.builder("nonexistentOption").build()).size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption");
        Option option = Option.builder("testOption").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(Arrays.asList("default1", "default2"), commandLine.getUndefaultedValues(option));
        Assert.assertEquals(Arrays.asList(), commandLine.getUndefaultedValues(Option.builder("nonexistentOption").build()));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testSwitch");
        Option option = Option.builder("testSwitch").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchIllegalStateException() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testSwitch");
        Option option = Option.builder("testSwitch").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testSwitch");
        Option option = Option.builder("testSwitch").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.isSet());
        Assert.assertTrue(option.getValue().equals("true"));
        Assert.assertFalse(commandLine.getOptionByName("testSwitch").getValue().equals("false"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--property propertyName value");
        Option option = Option.builder("property").build();
        commandLine.addProperty(option, "propertyName", "value");
        Assert.assertEquals("value", commandLine.getOption(option).getValue());
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        commandLine.addProperty("propertyName", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getProperty("propertyName"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.class.getDeclaredMethod("looksLikeOption").invoke(new CliCommandLineParser().parse("--test"), "test"));
        Assert.assertFalse(WriteableCommandLine.class.getDeclaredMethod("looksLikeOption").invoke(new CliCommandLineParser().parse("--test"), ""));
    }

    @Test
    public void testCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value");
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals("testOption", currentOption.getLongOpt());
        Assert.assertEquals("value", currentOption.getValue());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value");
        Option option = Option.builder("testOption2").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }
}