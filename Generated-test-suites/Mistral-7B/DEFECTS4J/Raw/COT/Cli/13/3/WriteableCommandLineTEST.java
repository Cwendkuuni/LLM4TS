import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new String[0]);
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value", new String[0]);
        Option option = Option.builder("t").longOpt("test").valueSeparator(':').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getOptionValues("test");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value1 value2", new String[0]);
        Option option = Option.builder("t").longOpt("test").valueSeparator(':').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues("test");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new String[0]);
        Option option = Option.builder("t").longOpt("test").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("test");
        Assert.assertEquals(2, values.size());
        Assert.assertNull(values.get(0));
        Assert.assertEquals("default1", values.get(1));
        Assert.assertEquals("default2", values.get(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new String[0]);
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new String[0]);
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("test"));
        Assert.assertTrue(commandLine.getOptionValue("test"));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new String[0]);
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("test"));
        Assert.assertTrue(commandLine.getOptionValue("test"));
        Assert.assertFalse(commandLine.getArgs().isEmpty());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new String[0]);
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new String[0]);
        Assert.assertTrue(commandLine.looksLikeOption("--test"));
        Assert.assertFalse(commandLine.looksLikeOption("test"));
    }
}