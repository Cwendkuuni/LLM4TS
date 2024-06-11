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
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("t"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues("t");
        Assert.assertEquals(Arrays.asList("value"), values);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value1 value2");
        Option option = Option.builder("t").longOpt("test").build();
        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("t"));
        Assert.assertTrue(commandLine.getOptionValue("t", false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getPropertyValues("property").get(0));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        Assert.assertFalse(WriteableCommandLine.class.getPrototypeInstance().looksLikeOption("arg"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--arg");
        Assert.assertTrue(commandLine.looksLikeOption("arg"));
    }
}