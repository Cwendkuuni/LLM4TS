import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLine(); // Assuming CliCommandLine is an implementation of WriteableCommandLine
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        List values = commandLine.getOptionValues("optionName");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getOptionValues("optionName");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("optionName").build();
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getOptionValues("optionName");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true); // This should throw an IllegalStateException
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("optionName").build();
        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getOptionValue("optionName", false));
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getOptionValue("optionName", false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addProperty("propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getProperty("propertyName"));
    }

    @Test
    public void testLooksLikeOptionWithArgument() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--optionName"));
        Assert.assertFalse(commandLine.looksLikeOption("argument"));
    }
}