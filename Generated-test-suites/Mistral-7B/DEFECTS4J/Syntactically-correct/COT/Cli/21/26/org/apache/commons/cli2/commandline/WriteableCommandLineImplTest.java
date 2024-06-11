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
        Assert.assertEquals("b", options.get(0).getLongOpt());

        Option optionB = commandLine.getOption("b");
        Assert.assertEquals("b", optionB.getLongOpt());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), "value1");
        List values = commandLine.getValues(new Option("a"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addSwitch(new Option("a"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addSwitch(new Option("a"), true);
        commandLine.addSwitch(new Option("a"), false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        Assert.assertTrue(commandLine.hasOption(new Option("a")));
        Assert.assertFalse(commandLine.hasOption(new Option("b")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        Option optionA = commandLine.getOption("a");
        Assert.assertEquals("a", optionA.getLongOpt());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), "value1");
        commandLine.addValue(new Option("a"), "value2");
        List values = commandLine.getValues(new Option("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), "value1");
        List values = commandLine.getUndefaultedValues(new Option("a"));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addSwitch(new Option("a"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("a"), false);
        Assert.assertTrue(switchValue);
        Boolean defaultSwitchValue = commandLine.getSwitch(new Option("a"), null);
        Assert.assertNull(defaultSwitchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("property", false, "defaultValue"));
        commandLine.addProperty(new PropertyOption("property"), "value");
        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("property", false, "defaultValue"));
        commandLine.addProperty(new PropertyOption("property"), "value");
        Map properties = commandLine.getProperties(new PropertyOption());
        Assert.assertEquals(1, properties.size());
        Map propertyMap = (Map) properties.get(0);
        Assert.assertEquals("value", propertyMap.get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Assert.assertFalse(commandLine.looksLikeOption(""));
        Assert.assertTrue(commandLine.looksLikeOption("a"));
        Assert.assertTrue(commandLine.looksLikeOption("--a"));
        Assert.assertTrue(commandLine.looksLikeOption("-a"));
        Assert.assertFalse(commandLine.looksLikeOption("--b"));
        Assert.assertFalse(commandLine.looksLikeOption("-b"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), "value1");
        commandLine.addValue(new Option("a"), "value2");
        String toString = commandLine.toString();
        Assert.assertEquals("-a value1 value2", toString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option optionA = (Option) options.get(0);
        Assert.assertEquals("a", optionA.getLongOpt());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals("a", optionTriggers.iterator().next());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        List defaultValues = new ArrayList();
        defaultValues.add("value1");
        defaultValues.add("value2");
        commandLine.setDefaultValues(new Option("a"), defaultValues);
        List values = commandLine.getValues(new Option("a"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.setDefaultSwitch(new Option("a"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), "value1");
        commandLine.addValue(new Option("a"), "value2");
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}