import org.apache.commons.cli2.CommandLine;
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

        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value=test");

        Option option = Option.builder("t").longOpt("test").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");

        List value = commandLine.getOptionValue("test", null);
        Assert.assertEquals("testValue", value.get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value1 value2 value3");

        Option option = Option.builder("t").longOpt("test").isRequired(false).build();
        commandLine.addOption(option);

        List<String> values = commandLine.getOptionValues("test");
        Assert.assertNotNull(values);
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");

        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getOptionValues("test", null);
        Assert.assertNotNull(values);
        Assert.assertEquals(defaultValues.size(), values.size());
        for (int i = 0; i < defaultValues.size(); i++) {
            Assert.assertEquals(defaultValues.get(i), values.get(i));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");

        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");

        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.hasOption("test") && commandLine.getOptionValue("test", null) == null
                && commandLine.getBooleanValue("test", false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");

        commandLine.addProperty("property", "defaultValue");

        Assert.assertEquals("defaultValue", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("--test"));
        Assert.assertFalse(commandLine.looksLikeOption("test"));
    }
}