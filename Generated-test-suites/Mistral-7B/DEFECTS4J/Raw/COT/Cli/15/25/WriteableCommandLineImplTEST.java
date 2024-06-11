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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option1, commandLine.getOption(option1.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        commandLine.addValue(option1, "value1");
        List values = commandLine.getValues(option1, new ArrayList<>());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option1, "value2");
        values = commandLine.getValues(option1, new ArrayList<>());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", false, "o1");
        commandLine.addOption(option1);

        commandLine.addSwitch(option1, false);
        Assert.assertFalse(commandLine.getSwitch(option1, Boolean.TRUE));

        commandLine.addSwitch(option1, true);
        Assert.assertTrue(commandLine.getSwitch(option1, Boolean.TRUE));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", false, "o1");
        commandLine.addOption(option1);

        commandLine.addSwitch(option1, false);
        commandLine.addSwitch(option1, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        Assert.assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        Option result = commandLine.getOption(option1.getPreferredName());
        Assert.assertEquals(option1, result);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        commandLine.addValue(option1, "value1");
        List values = commandLine.getValues(option1, new ArrayList<>());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option1, "value2");
        values = commandLine.getValues(option1, new ArrayList<>());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        commandLine.addValue(option1, "value1");
        List values = commandLine.getUndefaultedValues(option1);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", false, "o1");
        commandLine.addOption(option1);

        commandLine.addSwitch(option1, false);
        Assert.assertFalse(commandLine.getSwitch(option1, Boolean.TRUE));

        commandLine.addSwitch(option1, true);
        Assert.assertTrue(commandLine.getSwitch(option1, Boolean.TRUE));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", false, "o1");
        commandLine.addOption(option1);

        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        commandLine.addProperty(option1, "prop1", "value1");

        String result = commandLine.getProperty(option1, "prop1");
        Assert.assertEquals("value1", result);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", false, "o1");
        commandLine.addOption(option1);

        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        commandLine.addProperty(option1, "prop1", "value1");

        Set propertiesSet = commandLine.getProperties(option1);
        Assert.assertEquals(1, propertiesSet.size());
        Assert.assertTrue(propertiesSet.contains("prop1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");

        String result = commandLine.toString();
        String expected = "option1 value1";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option1, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("o1"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option1, defaultValues);

        List values = commandLine.getValues(option1, new ArrayList<>());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", false, "o1");
        commandLine.addOption(option1);

        commandLine.setDefaultSwitch(option1, Boolean.TRUE);
        Assert.assertTrue(commandLine.getSwitch(option1, Boolean.TRUE));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", true, "o1");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("option1", normalised.get(0));
        Assert.assertEquals("value1", normalised.get(1));
    }
}