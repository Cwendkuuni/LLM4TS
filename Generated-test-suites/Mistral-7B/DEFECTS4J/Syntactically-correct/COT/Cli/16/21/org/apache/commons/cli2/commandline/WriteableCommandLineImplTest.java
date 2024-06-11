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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("b", "bval", true, "Boolean option b");
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "aval", true, "Option A");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(0));
        Assert.assertEquals(Collections.singletonList("value1"), commandLine.getValues(option, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aopt");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aopt");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aopt");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("aopt"));
        Assert.assertEquals(option, commandLine.getOption(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aopt");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals(Collections.singletonList("value1"), commandLine.getValues(option, null));
        commandLine.addValue(option, "value2");
        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aopt");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals(Collections.singletonList("value1"), commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aopt");
        commandLine.addOption(option);
        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "prop", true, "Option A with a property");
        commandLine.addOption(option);
        commandLine.addProperty(option, "propval", "Property value for option A");
        Assert.assertEquals("Property value for option A", commandLine.getProperty(option, "propval"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "prop", true, "Option A with a property");
        commandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("prop1", "val1");
        commandLine.addProperty(option, "prop", properties);
        Assert.assertEquals(properties, commandLine.getProperties(option));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "prop", true, "Option A with a property");
        commandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("prop1", "val1");
        commandLine.addProperty(option, "prop", properties);
        Assert.assertEquals(properties, commandLine.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Assert.assertFalse(commandLine.looksLikeOption("b"));
        Assert.assertTrue(commandLine.looksLikeOption("a"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addValue(commandLine.getOption(0), "value1");
        commandLine.addValue(commandLine.getOption(0), "value2");
        Assert.assertEquals("[value1, value2]", commandLine.toString());
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addValue(commandLine.getOption(0), "value1");
        commandLine.addValue(commandLine.getOption(0), "value2");
        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getNormalised());
    }
}