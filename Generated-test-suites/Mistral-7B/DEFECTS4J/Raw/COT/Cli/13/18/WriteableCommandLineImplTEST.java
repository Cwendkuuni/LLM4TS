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
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        Assert.assertTrue(commandLine.hasOption(new Option("a", "arg")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addOption(new Option("a", "arg"));
        Option option = commandLine.getOption("a");
        Assert.assertNotNull(option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addValue(new Option("a", "arg"), "value1");
        List values = commandLine.getValues(new Option("a", "arg"), Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addSwitch(new Option("a", "arg"), true);
        commandLine.addSwitch(new Option("a", "arg"), false);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addSwitch(new Option("a", "arg"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("a", "arg"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addProperty("property", "value");
        String propertyValue = commandLine.getProperty("property", "");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testgetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addProperty("property", "value");
        Set properties = commandLine.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        Assert.assertTrue(commandLine.looksLikeOption("a arg"));
        Assert.assertFalse(commandLine.looksLikeOption("b arg"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addValue(new Option("a", "arg"), "value1");
        commandLine.addValue(new Option("a", "arg"), "value2");
        String toString = commandLine.toString();
        Assert.assertEquals("value1 value2", toString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addOption(new Option("a", "arg"));
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option option = (Option) options.get(0);
        Assert.assertEquals("arg", option.getLongOpt());
        Assert.assertEquals("a", option.getOpt());
        Assert.assertEquals(Collections.emptyList(), option.getValues());
        Assert.assertEquals(false, option.hasArg());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals(false, option.isHidden());
        Assert.assertEquals(Collections.emptyMap(), option.getOptions());
        Assert.assertEquals(Collections.emptyMap(), option.getDefaultValues());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addOption(new Option("a", "arg"));
        commandLine.addOption(new Option("b", "arg"));
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("a", "b")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        List defaultValues = new ArrayList();
        defaultValues.add("value1");
        commandLine.setDefaultValues(new Option("a", "arg"), defaultValues);
        List values = commandLine.getValues(new Option("a", "arg"), Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.setDefaultSwitch(new Option("a", "arg"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("a", "arg"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a", "arg"), new ArrayList<>());
        commandLine.addValue(new Option("a", "arg"), "value1");
        commandLine.addValue(new Option("a", "arg"), "value2");
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}