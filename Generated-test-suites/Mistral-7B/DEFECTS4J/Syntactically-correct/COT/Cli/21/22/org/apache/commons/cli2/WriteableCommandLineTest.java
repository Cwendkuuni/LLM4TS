import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("-o,--optionOption", new String[]{"-o", "--optionOption"});
        Option option = new Option("o", "optionOption");
        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("-o,--optionOption value", new String[]{"-o", "--optionOption", "value"});
        Option option = new Option("o", "optionOption");
        commandLine.addValue(option, "value");

        Assert.assertEquals("value", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("-o,--optionOption value1 value2", new String[]{"-o", "--optionOption", "value1", "value2"});
        Option option = new Option("o", "optionOption");
        List values = commandLine.getUndefaultedValues(option);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("", new String[0]);
        Option option = new Option("o", "optionOption");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        Assert.assertEquals(2, commandLine.getUndefaultedValues(option).size());
        Assert.assertEquals("defaultValue1", commandLine.getUndefaultedValues(option).get(0));
        Assert.assertEquals("defaultValue2", commandLine.getUndefaultedValues(option).get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsException() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("", new String[0]);
        Option option = new Option("o", "optionOption");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("", new String[0]);
        Option option = new Option("o", "optionOption");
        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertTrue(commandLine.getOptionValue(option, false).equals("true"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("", new String[0]);
        Option option = new Option("o", "optionOption");
        commandLine.addProperty(option, "property", "propertyValue");

        Assert.assertEquals("propertyValue", commandLine.getOptionValue(option, "property"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("", new String[0]);
        commandLine.addProperty("property", "defaultPropertyValue");

        Assert.assertEquals("defaultPropertyValue", commandLine.getOptionValue("property", ""));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("-o"));
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--option"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("--value"));
    }
}