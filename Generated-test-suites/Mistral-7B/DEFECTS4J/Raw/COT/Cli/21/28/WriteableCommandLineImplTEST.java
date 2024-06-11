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
        Option option1 = (Option) options.get(0);
        Assert.assertEquals("option1", option1.getLongOpt());
        Assert.assertEquals(0, option1.getArgs().length);
        Assert.assertFalse(option1.isRequired());
        Assert.assertNull(option1.getValue());

        Option option2 = (Option) nameToOption.get("option2");
        Assert.assertEquals("option2", option2.getLongOpt());
        Assert.assertEquals(0, option2.getArgs().length);
        Assert.assertFalse(option2.isRequired());
        Assert.assertNull(option2.getValue());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option").required(true));
        commandLine.addValue(commandLine.getOption("option"), "value1");

        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
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
        commandLine.addSwitch(commandLine.getOption("option"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option2"));

        boolean hasOption1 = commandLine.hasOption(commandLine.getOption("option"));
        Assert.assertTrue(hasOption1);

        boolean hasOption2 = commandLine.hasOption(commandLine.getOption("option2"));
        Assert.assertFalse(hasOption2);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option"));

        Option option = commandLine.getOption("option");
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals(0, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option").argName("arg").required(true).args(new String[]{"arg1", "arg2"}));
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
        commandLine.addOption(new Option("option").argName("arg").required(true).args(new String[]{"arg1", "arg2"}));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");

        List values = commandLine.getUndefaultedValues(commandLine.getOption("option"));
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("option"), true);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);

        switchValue = commandLine.getSwitch(commandLine.getOption("option"), null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addProperty(new PropertyOption(), "property", "value1");

        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("value1", propertyValue);

        propertyValue = commandLine.getProperty("property", "defaultValue");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());

        commandLine.addProperty(new PropertyOption(), "property1", "value1");
        commandLine.addProperty(new PropertyOption(), "property2", "value2");

        Map properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Map propertiesMap = (Map) properties.get(0);
        Assert.assertEquals("property1", propertiesMap.keySet().iterator().next());
        Assert.assertEquals("value1", propertiesMap.get("property1"));
        Assert.assertEquals("property2", properties.get(1).keySet().iterator().next());
        Assert.assertEquals("value2", properties.get(1).get("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");

        String commandLineString = commandLine.toString();
        String expected = "value1 value2";
        Assert.assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addOption(new Option("option1"));
        commandLine.addOption(new Option("option2"));

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());

        Option option1 = (Option) options.get(0);
        Assert.assertEquals("option1", option1.getLongOpt());
        Assert.assertEquals(0, option1.getArgs().length);
        Assert.assertFalse(option1.isRequired());
        Assert.assertNull(option1.getValue());

        Option option2 = (Option) options.get(1);
        Assert.assertEquals("option2", option2.getLongOpt());
        Assert.assertEquals(0, option2.getArgs().length);
        Assert.assertFalse(option2.isRequired());
        Assert.assertNull(option2.getValue());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
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
        commandLine.addOption(new Option("option").required(true).args(new String[]{"arg"}));

        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");

        commandLine.setDefaultValues(commandLine.getOption("option"), defaults);

        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());

        commandLine.setDefaultSwitch(commandLine.getOption("option"), true);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}