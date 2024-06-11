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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        commandLine.addOption(new Option("option2"));
        Assert.assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");
        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addSwitch(commandLine.getOption("option"), true);
        boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addSwitch(commandLine.getOption("option"), true);
        commandLine.addSwitch(commandLine.getOption("option"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        boolean hasOption = commandLine.hasOption(commandLine.getOption("option"));
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        Option option = commandLine.getOption("option");
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals("option", option.getOptString());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");
        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        List undefaultedValues = commandLine.getUndefaultedValues(commandLine.getOption("option"));
        Assert.assertEquals(0, undefaultedValues.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addSwitch(commandLine.getOption("option"), true);
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addProperty(commandLine.getOption("option"), "property", "value");
        String propertyValue = commandLine.getProperty(commandLine.getOption("option"), "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "value1");
        commandLine.addProperty(commandLine.getOption("option"), "property", properties);
        Map propertiesMap = commandLine.getProperties(commandLine.getOption("option"));
        Assert.assertEquals(1, propertiesMap.size());
        Assert.assertEquals("property1", propertiesMap.keySet().iterator().next());
        Assert.assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "value1 option";
        Assert.assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        commandLine.addOption(new Option("option1"));
        commandLine.addOption(new Option("option2"));
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option option1 = (Option) options.get(0);
        Assert.assertEquals("option1", option1.getLongOpt());
        Option option2 = (Option) options.get(1);
        Assert.assertEquals("option2", option2.getLongOpt());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        commandLine.addOption(new Option("option1"));
        commandLine.addOption(new Option("option2"));
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("option1"));
        Assert.assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(commandLine.getOption("option"), defaultValues);
        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.setDefaultSwitch(commandLine.getOption("option"), true);
        boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("option value1", normalised.get(0));
    }
}