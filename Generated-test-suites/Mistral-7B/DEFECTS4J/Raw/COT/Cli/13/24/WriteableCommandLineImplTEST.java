import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testConstructor() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option", "opt", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        Assert.assertEquals(Arrays.asList(option), commandLine.getOptions());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option", "opt", true, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option", "opt", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("option", "opt", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", "opt", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "opt", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        Option result = commandLine.getOption("opt");
        Assert.assertSame(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option", "opt", true, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1"), values);

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option", "opt", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");
        Properties properties = commandLine.getProperties();
        Assert.assertEquals("value", properties.getProperty("property"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");
        String result = commandLine.getProperty("property", "defaultValue");
        Assert.assertEquals("value", result);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        Assert.assertFalse(commandLine.looksLikeOption("notOption"));
        Option option = new Option("option", "opt");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.looksLikeOption("opt"));
        Assert.assertTrue(commandLine.looksLikeOption("option"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));
        Assert.assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = new Option("option1", "opt1");
        Option option2 = new Option("option2", "opt2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        Assert.assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option option = new Option("option", "opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        Set triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, triggers.size());
        Assert.assertTrue(triggers.contains("opt"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option", "opt", true, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option", "opt", false, "Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalisedList() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), normalised);
    }
}