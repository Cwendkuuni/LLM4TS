import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", "b", false, "Test Option B");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", "b", false, "Test Option B");
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", true, "Test Option A");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals(1, commandLine.getValues(option, null).size());
        Assert.assertEquals("value1", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "Test Option A");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "Test Option A");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "Test Option A");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("a"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", true, "Test Option A");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", true, "Test Option A");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "Test Option A");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "Test Option A");
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("test", "testValue");
        commandLine.addProperty(option, "test", properties);
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        Assert.assertEquals("testValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "Test Option A");
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("test1", "testValue1");
        commandLine.addProperty(option, "test1", properties);
        Properties properties2 = new Properties();
        properties2.setProperty("test2", "testValue2");
        commandLine.addProperty(option, "test2", properties2);
        Map propertiesMap = commandLine.getProperties(option);
        Set propertySet = propertiesMap.keySet();
        Assert.assertEquals(2, propertySet.size());
        Assert.assertTrue(propertySet.containsAll(Arrays.asList("test1", "test2")));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("--test", "test", false, "Test Option"));
        commandLine.addOption(new Option("--test2", "test2", false, "Test Option 2"));
        commandLine.addOption(new Option("--test3", "test3", true, "Test Option 3 with value"));
        commandLine.addOption(new Option("--test4", "test4", true, "Test Option 4 with value1, value2"));
        commandLine.addValue(new Option("--test"), "testValue");
        commandLine.addValue(new Option("--test4"), "value1");
        commandLine.addValue(new Option("--test4"), "value2");
        Assert.assertEquals("--test testValue --test2 --test3 --test4 value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("--test", "test", false, "Test Option"));
        List options = commandLine.getOptions();
        Option option = new Option("--test", "test", false, "Test Option");
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("--test", "test", false, "Test Option"));
        commandLine.addOption(new Option("--test2", "test2", false, "Test Option 2"));
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("--test", "--test2")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", true, "Test Option A");
        commandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "Test Option A");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("--test", "test", false, "Test Option"));
        commandLine.addOption(new Option("--test2", "test2", false, "Test Option 2"));
        commandLine.addOption(new Option("--test3", "test3", true, "Test Option 3 with value"));
        commandLine.addOption(new Option("--test4", "test4", true, "Test Option 4 with value1, value2"));
        commandLine.addValue(new Option("--test"), "testValue");
        commandLine.addValue(new Option("--test4"), "value1");
        commandLine.addValue(new Option("--test4"), "value2");
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(5, normalised.size());
        Assert.assertEquals("--test", normalised.get(0));
        Assert.assertEquals("testValue", normalised.get(1));
        Assert.assertEquals("--test2", normalised.get(2));
        Assert.assertEquals("--test3", normalised.get(3));
        Assert.assertEquals("--test4", normalised.get(4));
    }
}