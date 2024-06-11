import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * WriteableCommandLineTest
 */
public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value");
        Option option = Option.builder("t").longOpt("test").valueSeparator('=').build();
        commandLine.addValue(option, "testValue");
        Assert.assertEquals("testValue", commandLine.getOptionValue("test"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value1 value2");
        Option option = Option.builder("t").longOpt("test").valueSeparator('=').build();
        List<String> values = commandLine.getOptionValuesForOption(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("test"));
        Assert.assertTrue(commandLine.getOptionValue("test") != null);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("t").longOpt("test").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(2, commandLine.getOptionValuesForOption(option).size());
        Assert.assertEquals("defaultValue1", commandLine.getOptionValue("test", 0));
        Assert.assertEquals("defaultValue2", commandLine.getOptionValue("test", 1));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        commandLine.addProperty("testProperty", "testValue");
        Assert.assertEquals("testValue", commandLine.getOptionValue("testProperty"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        Assert.assertFalse(WriteableCommandLine.class.getDeclaredMethod("looksLikeOption").invoke(null, "test"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Assert.assertTrue(WriteableCommandLine.class.getDeclaredMethod("looksLikeOption").invoke(commandLine, "test"));
    }
}