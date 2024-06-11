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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));

        Option option = commandLine.getOption("test");
        Assert.assertNotNull(option);
        Assert.assertEquals("test", option.getLongOpt());
        Assert.assertEquals("t", option.getOpt());
        Assert.assertTrue(option.isRequired());
        Assert.assertFalse(option.hasValue());
        Assert.assertFalse(option.isUnbounded());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));
        commandLine.addOption(new Option("test", true, "t"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", 1, "t"));
        commandLine.addValue(commandLine.getOption("test"), "value");

        List values = commandLine.getValues(commandLine.getOption("test"), null);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", false, "t"));
        commandLine.addSwitch(commandLine.getOption("test"), true);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("test"), false);
        Assert.assertNotNull(switchValue);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));

        boolean hasOption = commandLine.hasOption(commandLine.getOption("test"));
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));

        Option option = commandLine.getOption("test");
        Assert.assertNotNull(option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", 1, "t"));
        commandLine.addValue(commandLine.getOption("test"), "value1");
        commandLine.addValue(commandLine.getOption("test"), "value2");

        List values = commandLine.getValues(commandLine.getOption("test"), null);
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.size());

        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", 1, "t"));
        commandLine.addValue(commandLine.getOption("test"), "value1");

        List values = commandLine.getUndefaultedValues(commandLine.getOption("test"));
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", false, "t"));
        commandLine.addSwitch(commandLine.getOption("test"), false);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("test"), true);
        Assert.assertNull(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("property", "propertyValue"));
        commandLine.addProperty(commandLine.getOption("property"), "newPropertyValue");

        String propertyValue = commandLine.getProperty(commandLine.getOption("property"));
        Assert.assertEquals("propertyValue", propertyValue);

        String newPropertyValue = commandLine.getProperty(commandLine.getOption("property"), "defaultValue");
        Assert.assertEquals("newPropertyValue", newPropertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new PropertyOption("property1", "propertyValue1"));
        commandLine.addOption(new PropertyOption("property2", "propertyValue2"));

        Set properties = commandLine.getProperties(commandLine.getOption("option"));
        Assert.assertNotNull(properties);
        Assert.assertEquals(2, properties.size());

        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));

        boolean looksLikeOption = commandLine.looksLikeOption("test");
        Assert.assertTrue(looksLikeOption);

        boolean looksLikeOption2 = commandLine.looksLikeOption("test2");
        Assert.assertFalse(looksLikeOption2);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));
        commandLine.addValue(commandLine.getOption("test"), "value1");
        commandLine.addValue(commandLine.getOption("test"), "value2");

        String toString = commandLine.toString();
        Assert.assertEquals("value1 value2 test", toString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));

        List options = commandLine.getOptions();
        Assert.assertNotNull(options);
        Assert.assertEquals(1, options.size());
        Option option = (Option) options.get(0);
        Assert.assertEquals("test", option.getLongOpt());
        Assert.assertEquals("t", option.getOpt());
        Assert.assertTrue(option.isRequired());
        Assert.assertFalse(option.hasValue());
        Assert.assertFalse(option.isUnbounded());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));
        commandLine.addOption(new Option("test2", true, "t2"));

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertNotNull(optionTriggers);
        Assert.assertEquals(2, optionTriggers.size());

        Assert.assertTrue(optionTriggers.contains("test"));
        Assert.assertTrue(optionTriggers.contains("test2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", 1, "t"));

        List defaultValues = new ArrayList();
        defaultValues.add("defVal1");
        commandLine.setDefaultValues(commandLine.getOption("test"), defaultValues);

        List values = commandLine.getValues(commandLine.getOption("test"), null);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defVal1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", false, "t"));

        Boolean defaultSwitch = Boolean.FALSE;
        commandLine.setDefaultSwitch(commandLine.getOption("test"), defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("test"), null);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("test", true, "t"));
        commandLine.addValue(commandLine.getOption("test"), "value1");

        List normalised = commandLine.getNormalised();
        Assert.assertNotNull(normalised);
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1 test", ((String) normalised.get(1)));
    }
}