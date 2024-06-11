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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionB"));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("optionB", commandLine.getOptions().get(0).getLongOpt());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", true, true, "value1"));
        commandLine.addValue(new Option("optionA"), "value2");
        List values = commandLine.getValues(new Option("optionA"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        commandLine.addSwitch(new Option("optionA"), true);
        Assert.assertTrue(commandLine.getSwitch(new Option("optionA"), false));
        Assert.assertTrue(commandLine.getSwitch(new Option("optionA"), true));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        commandLine.addSwitch(new Option("optionA"), true);
        commandLine.addSwitch(new Option("optionA"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        Assert.assertTrue(commandLine.hasOption(new Option("optionA")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        Option option = commandLine.getOption("optionA");
        Assert.assertEquals("optionA", option.getLongOpt());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, true, "value1"));
        commandLine.addValue(new Option("optionA"), "value2");
        commandLine.addValue(new Option("optionA"), "value3");
        List values = commandLine.getValues(new Option("optionA"), null);
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, true, "value1"));
        commandLine.addValue(new Option("optionA"), "value2");
        List values = commandLine.getUndefaultedValues(new Option("optionA"));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value2", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        commandLine.addSwitch(new Option("optionA"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("optionA"), false);
        Assert.assertFalse(switchValue);
        switchValue = commandLine.getSwitch(new Option("optionA"), true);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("propertyA", true, null, "valueA"));
        commandLine.addProperty(new PropertyOption("propertyA"), "newValue");
        String propertyValue = commandLine.getProperty("propertyA");
        Assert.assertEquals("newValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("propertyA", true, null, "valueA"));
        commandLine.addProperty(new PropertyOption("propertyA"), "newValue");
        Map<String, String> properties = commandLine.getProperties(new PropertyOption());
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("newValue", properties.get("propertyA"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        commandLine.addValue(new Option("optionA"), "value1");
        commandLine.addValue(new Option("optionA"), "value2");
        String commandLineString = commandLine.toString();
        String expected = "optionA value1 value2";
        Assert.assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        commandLine.addOption(new Option("optionB", false, null, ""));
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals("optionA", options.get(0).getLongOpt());
        Assert.assertEquals("optionB", options.get(1).getLongOpt());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        commandLine.addOption(new Option("optionB", false, null, ""));
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertEquals("optionA", optionTriggers.iterator().next());
        Assert.assertEquals("optionB", optionTriggers.iterator().next());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, true, "defaultValue"));
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(new Option("optionA"), defaultValues);
        List values = commandLine.getValues(new Option("optionA"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        commandLine.setDefaultSwitch(new Option("optionA"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("optionA"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList<>());
        commandLine.addOption(new Option("optionA", false, null, ""));
        commandLine.addValue(new Option("optionA"), "value1");
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("optionA value1", normalised.get(0));
    }
}