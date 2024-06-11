import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test", new CliTestOptions());
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test value", new CliTestOptions());
        Option option = Option.builder("test").build();
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test value1 value2", new CliTestOptions());
        Option option = Option.builder("test").build();
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test", new CliTestOptions());
        Option option = Option.builder("test").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertFalse(values.contains("value1"));
        Assert.assertFalse(values.contains("value2"));
        Assert.assertEquals(2, commandLine.getOptions().get(0).getValues().size());
        Assert.assertEquals("defaultValue1", commandLine.getOptions().get(0).getValues().get(0));
        Assert.assertEquals("defaultValue2", commandLine.getOptions().get(0).getValues().get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test", new CliTestOptions());
        Option option = Option.builder("test").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.getValue().isPresent());
        Assert.assertEquals(true, option.getValue().get());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test", new CliTestOptions());
        Option option = Option.builder("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test", new CliTestOptions());
        Option option = Option.builder("test").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.getValue().isPresent());
        Assert.assertEquals(true, option.getValue().get());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test=property:value", new CliTestOptions());
        Option option = Option.builder("test").build();
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getOptions().get(0).getProperties().get("property"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test", new CliTestOptions());
        commandLine.addProperty("property", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getOptions().get(0).getProperties().get("property"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        Assert.assertFalse(WriteableCommandLine.class.newInstance().looksLikeOption("argument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = new CliTestCommandLineParser().parse("--test", new CliTestOptions());
        Assert.assertTrue(commandLine.looksLikeOption("test"));
    }
}