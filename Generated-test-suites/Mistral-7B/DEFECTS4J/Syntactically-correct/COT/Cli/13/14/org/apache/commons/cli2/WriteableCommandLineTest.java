import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option");
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option value");
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addValue(option, "value");
        Assert.assertEquals("value", commandLine.getOptionValues("o").get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--option value1 value2");
        Option option = Option.builder("o").longOpt("option").build();
        Assert.assertEquals(2, commandLine.getOptionValues(option).size());
        Assert.assertEquals("value1", commandLine.getOptionValues(option).get(0));
        Assert.assertEquals("value2", commandLine.getOptionValues(option).get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("o").longOpt("option").build();
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        Assert.assertEquals(2, commandLine.getOptionDefaultValues(option).size());
        Assert.assertEquals("default1", commandLine.getOptionDefaultValues(option).get(0));
        Assert.assertEquals("default2", commandLine.getOptionDefaultValues(option).get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("o"));
        Assert.assertTrue(commandLine.isSet("o"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, false);
        Assert.assertFalse(commandLine.isSet("o"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getPropertyValues("property").get(0));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--option"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("value"));
    }

}