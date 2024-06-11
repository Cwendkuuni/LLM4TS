import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("b"));
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option optionA = (Option) options.get(0);
        Option optionB = (Option) options.get(1);
        Assert.assertEquals("a", optionA.getOpt());
        Assert.assertEquals("b", optionB.getOpt());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), "value1");
        List values = commandLine.getValues(new Option("a"), new ArrayList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("a"));
        commandLine.addSwitch(new Option("a"), true);
        commandLine.addSwitch(new Option("a"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("a"));
        Assert.assertTrue(commandLine.hasOption(new Option("a")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("a"));
        Option option = commandLine.getOption("a");
        Assert.assertNotNull(option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), "value1");
        commandLine.addValue(new Option("a"), "value2");
        List values = commandLine.getValues(new Option("a"), new ArrayList());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("a"));
        commandLine.addSwitch(new Option("a"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property", "");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property", "");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.containsAll(Arrays.asList("property1", "property2")));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Assert.assertTrue(commandLine.looksLikeOption("a"));
        Assert.assertFalse(commandLine.looksLikeOption("x"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addValue(new Option("a"), "value1");
        commandLine.addValue(new Option("a"), "value2");
        String commandLineString = commandLine.toString();
        String expected = "'value1' 'value2'";
        Assert.assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptionsEmptyList() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        List options = commandLine.getOptions();
        Assert.assertEquals(0, options.size());
    }

    @Test
    public void testGetOptionTriggersEmptySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(0, optionTriggers.size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("a"));
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(new Option("a"), defaultValues);
        List values = commandLine.getValues(new Option("a"), new ArrayList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        commandLine.addOption(new Option("a"));
        commandLine.setDefaultSwitch(new Option("a"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("a"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalisedEmptyList() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList());
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(0, normalised.size());
    }
}