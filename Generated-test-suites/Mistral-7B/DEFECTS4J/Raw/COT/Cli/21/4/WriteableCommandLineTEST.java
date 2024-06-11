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
        WriteableCommandLine commandLine = new CmdLineParser().parse("--option");
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--option value");
        Option option = Option.builder("option").build();
        commandLine.addValue(option, "value");
        Assert.assertEquals(Arrays.asList("value"), commandLine.getUndefaultedValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueWithExistingOption() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--option value --option value2");
        Option option = Option.builder("option").build();
        commandLine.addValue(option, "value");
        commandLine.addValue(option, "value2");
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--option value1 --option value2");
        Option option = Option.builder("option").build();
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("");
        Option option = Option.builder("option").build();
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingSwitch() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--switch");
        Option option = Option.builder("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("option"));
        Assert.assertTrue(commandLine.getOption("option").isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.addProperty(option, "property", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getOption("option").getValues().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPropertyWithExistingProperty() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.addProperty(option, "property", "propertyValue");
        commandLine.addProperty(option, "property", "newPropertyValue");
    }

    @Test
    public void testAddPropertyToDefault() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("");
        commandLine.addProperty("property", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getOption(null).getValues().get(0));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--option"));
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("value"));
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("");
        Option option = Option.builder("option").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }

}