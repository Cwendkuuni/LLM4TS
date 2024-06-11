import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("b"));
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals("b", options.get(1).getLongOpt());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(commandLine.getOption("a"), "value1");
        List<Object> values = commandLine.getValues(commandLine.getOption("a"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a").required(false).build());
        commandLine.addSwitch(commandLine.getOption("a"), true);
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a").required(false).build());
        commandLine.addSwitch(commandLine.getOption("a"), true);
        commandLine.addSwitch(commandLine.getOption("a"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        boolean result = commandLine.hasOption(commandLine.getOption("a"));
        Assert.assertTrue(result);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        Option option = commandLine.getOption("a");
        Assert.assertEquals("a", option.getLongOpt());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(commandLine.getOption("a"), "value1");
        commandLine.addValue(commandLine.getOption("a"), "value2");
        List<Object> values = commandLine.getValues(commandLine.getOption("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(commandLine.getOption("a"), "value1");
        List<Object> values = commandLine.getUndefaultedValues(commandLine.getOption("a"));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a").required(false).build());
        commandLine.addSwitch(commandLine.getOption("a"), true);
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a", true, true, "prop"));
        commandLine.addProperty(commandLine.getOption("a"), "value1");
        String propertyValue = commandLine.getProperty(commandLine.getOption("a"), "defaultValue");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a", true, true, "prop"));
        commandLine.addProperty(commandLine.getOption("a"), "value1");
        Map<String, String> properties = commandLine.getProperties(commandLine.getOption("a"));
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value1", properties.get("prop"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(commandLine.getOption("a"), "value1");
        String result = commandLine.toString();
        Assert.assertEquals("value1", result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals("a", options.get(0).getLongOpt());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        Set<String> triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals("a", triggers.iterator().next());
    }
}