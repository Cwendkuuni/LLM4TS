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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", true, "Example option");
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", true, "Example option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", false, "Example switch");
        commandLine.addSwitch(option, false);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertFalse(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", false, "Example switch");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", true, "Example option");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", true, "Example option");
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("a");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", true, "Example option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", false, "Example switch");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property", "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(new Option("a", "example", false, "Example option"));
        commandLine.addOption(new Option("b", "example2", true, "Example option 2"));
        commandLine.addValue(commandLine.getOption("a"), "valueA");
        commandLine.addValue(commandLine.getOption("b"), "valueB");
        String expected = "-a valueA -b valueB";
        String actual = commandLine.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option1 = new Option("a", "example1", true, "Example option 1");
        Option option2 = new Option("b", "example2", false, "Example option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", true, "Example option");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "example", false, "Example switch");
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(new Option("a", "example", false, "Example option"));
        commandLine.addOption(new Option("b", "example2", true, "Example option 2"));
        commandLine.addValue(commandLine.getOption("a"), "valueA");
        commandLine.addValue(commandLine.getOption("b"), "valueB");
        List normalised = commandLine.getNormalised();
        String expected = "-a valueA -b valueB";
        String actual = String.join(" ", normalised);
        Assert.assertEquals(expected, actual);
    }
}