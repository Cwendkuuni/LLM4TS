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
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.get(0) instanceof Option);
        Assert.assertEquals("option2", ((Option) options.get(0)).getLongOpt());
        Assert.assertFalse(((Option) options.get(0)).isRequired());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option", new Argument("value")));
        commandLine.addValue(commandLine.getOption("option"), "testValue");
        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("option"), true);
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("option"), true);
        commandLine.addSwitch(commandLine.getOption("option"), false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        commandLine.addOption(new Option("option2"));
        Assert.assertTrue(commandLine.hasOption(commandLine.getOption("option1")));
        Assert.assertFalse(commandLine.hasOption(commandLine.getOption("option3")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option2"));
        Option option = commandLine.getOption("option");
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option", new Argument("value")));
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
        commandLine.addOption(new Option("option", new Argument("value")));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        List unfaultedValues = commandLine.getUndefaultedValues(commandLine.getOption("option"));
        Assert.assertEquals(0, unfaultedValues.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("option"), false);
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), true);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("property", "defaultValue", new Argument("value")));
        commandLine.addProperty(commandLine.getOption("option"), "property", "propertyValue");
        String propertyValue = commandLine.getProperty(commandLine.getOption("option"), "defaultValue");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("property1", "defaultValue1", new Argument("value1")));
        commandLine.addOption(new PropertyOption("property2", "defaultValue2", new Argument("value2")));
        Map properties = commandLine.getProperties(commandLine.getOption("option"));
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.containsKey("property1"));
        Assert.assertEquals("value1", properties.get("property1"));
        Assert.assertTrue(properties.containsKey("property2"));
        Assert.assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option1"));
        commandLine.addOption(new Option("option2", new Argument("value")));
        commandLine.addValue(commandLine.getOption("option1"), "value1");
        commandLine.addValue(commandLine.getOption("option1"), "value2");
        commandLine.addProperty(commandLine.getOption("option"), "property", "propertyValue");
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "option1 value1 value2 option value propertyPropertyValue";
        Assert.assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option1"));
        commandLine.addOption(new Option("option2"));
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.get(0) instanceof Option);
        Assert.assertTrue(options.get(1) instanceof Option);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option1"));
        commandLine.addOption(new Option("option2"));
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("option1", "option2")));
    }
}