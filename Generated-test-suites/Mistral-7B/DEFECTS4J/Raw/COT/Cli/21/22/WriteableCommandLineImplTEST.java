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
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option option = (Option) options.get(0);
        Assert.assertEquals("b", option.getLongOpt());
        Assert.assertEquals(null, option.getValue());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a")).setValue("value1");
        List values = commandLine.getValues(commandLine.getOption("a"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("a"), true);
        Assert.assertTrue(commandLine.getSwitch(commandLine.getOption("a"), false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("a"), true);
        commandLine.addSwitch(commandLine.getOption("a"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        Assert.assertTrue(commandLine.hasOption(commandLine.getOption("a")));
        Assert.assertFalse(commandLine.hasOption(new Option("b")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        Option option = commandLine.getOption("a");
        Assert.assertEquals("a", option.getLongOpt());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.setValue(commandLine.getOption("a"), "value1");
        commandLine.setValue(commandLine.getOption("a"), "value2");
        List values = commandLine.getValues(commandLine.getOption("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.setValue(commandLine.getOption("a"), "value1");
        List values = commandLine.getUndefaultedValues(commandLine.getOption("a"));
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.setSwitch(commandLine.getOption("a"), false);
        Assert.assertFalse(commandLine.getSwitch(commandLine.getOption("a"), true));

        commandLine.setDefaultSwitch(commandLine.getOption("a"), true);
        Assert.assertTrue(commandLine.getSwitch(commandLine.getOption("a"), false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("p", "value"));
        commandLine.addProperty(commandLine.getOption("p"), "newValue");
        String property = commandLine.getProperty(commandLine.getOption("p"), "defaultValue");
        Assert.assertEquals("newValue", property);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("p"));
        Properties properties = new Properties();
        properties.setProperty("p1", "value1");
        commandLine.addProperty(commandLine.getOption("p"), properties);
        Map<String, String> propertiesMap = commandLine.getProperties(commandLine.getOption("p"));
        Assert.assertEquals(1, propertiesMap.size());
        Assert.assertEquals("value1", propertiesMap.get("p1"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("p"));
        Properties properties = new Properties();
        properties.setProperty("p1", "value1");
        commandLine.addProperty(commandLine.getOption("p"), properties);
        Set<String> propertiesSet = commandLine.getProperties(commandLine.getOption("p"));
        Assert.assertEquals(1, propertiesSet.size());
        Assert.assertEquals("p1", propertiesSet.iterator().next());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.setValue(commandLine.getOption("a"), "value1");
        commandLine.addValue(commandLine.getOption("a"), "value2");
        String string = commandLine.toString();
        String expected = "value1 value2";
        Assert.assertEquals(expected, string);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addOption(new Option("b"));
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addOption(new Option("b"));
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("a", "b")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        List defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(commandLine.getOption("a"), defaultValues);
        List values = commandLine.getValues(commandLine.getOption("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.setDefaultSwitch(commandLine.getOption("a"), true);
        Assert.assertTrue(commandLine.getSwitch(commandLine.getOption("a"), false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.setValue(commandLine.getOption("a"), "value1");
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
    }
}