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
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option");
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option value");
        Option option = Option.builder("option").longOpt("longOption").valueSeparator('=').hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        List<String> values = commandLine.getOptionValues("option");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option value1 value2");
        Option option = Option.builder("option").longOpt("longOption").valueSeparator('=').hasArg(true).build();
        commandLine.addOption(option);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option");
        Option option = Option.builder("option").longOpt("longOption").valueSeparator('=').hasArg(true).build();
        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
        Assert.assertEquals(2, commandLine.getOptionValues("option").size());
        Assert.assertEquals("defaultValue1", commandLine.getOptionValues("option").get(0));
        Assert.assertEquals("defaultValue2", commandLine.getOptionValues("option").get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("longOption").build().withValue(true);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("longOption").build().withValue(true);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("longOption").build().withValue(true);
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(true, option.isDefaultValueSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.addProperty(option, "property", "testProperty");
        Assert.assertEquals("testProperty", commandLine.getOptionProperties().get(option));
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        commandLine.addProperty("property", "testProperty");
        Assert.assertEquals("testProperty", commandLine.getOptionProperties().get(Option.BUILDER.getDefaultName()));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertEquals(true, WriteableCommandLine.class.getDeclaredMethod("looksLikeOption").invoke(null, "--option"));
        Assert.assertEquals(false, WriteableCommandLine.class.getDeclaredMethod("looksLikeOption").invoke(null, "testValue"));
    }

    @Test
    public void testCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option");
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option1 --option2");
        Option option1 = Option.builder("option1").longOpt("longOption1").build();
        Option option2 = Option.builder("option2").longOpt("longOption2").build();
        commandLine.setCurrentOption(option1);
        commandLine.setCurrentOption(option2);
        Assert.assertEquals(option2, commandLine.getCurrentOption());
    }
}