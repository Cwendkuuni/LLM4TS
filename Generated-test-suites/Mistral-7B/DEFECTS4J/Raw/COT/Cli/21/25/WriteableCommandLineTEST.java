import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption");
        Option option = new Option("testOption", "Test Option");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value1 value2");
        Option option = new Option("testOption", "Test Option");
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0), "value1");
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value1 value2");
        Option option = new Option("testOption", "Test Option");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values.get(0), "value1");
        Assert.assertEquals(values.get(1), "value2");
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = new Option("testOption", "Test Option");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), 0);
        List<String> defaults = commandLine.getOptionValues(option);
        Assert.assertEquals(defaults.size(), 2);
        Assert.assertEquals(defaults.get(0), "default1");
        Assert.assertEquals(defaults.get(1), "default2");
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = new Option("testSwitch", "Test Switch");
        commandLine.addSwitch(option, true);
        boolean switchValue = commandLine.getSwitch(option);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchException() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = new Option("testSwitch", "Test Switch");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = new Option("testSwitch", "Test Switch");
        commandLine.setDefaultSwitch(option, true);
        boolean switchValue = commandLine.getSwitch(option);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = new Option("testProperty", "Test Property");
        commandLine.addProperty(option, "propertyName", "propertyValue");
        String propertyValue = commandLine.getProperty(option, "propertyName");
        Assert.assertEquals(propertyValue, "propertyValue");
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        commandLine.addProperty("defaultProperty", "defaultValue");
        String propertyValue = commandLine.getProperty("defaultProperty");
        Assert.assertEquals(propertyValue, "defaultValue");
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--testOption"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("--testValue"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("testValue"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value");
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(currentOption.getName(), "testOption");
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--testOption value");
        Option option = new Option("testOtherOption", "Test Other Option");
        commandLine.setCurrentOption(option);
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(currentOption.getName(), "testOtherOption");
    }
}