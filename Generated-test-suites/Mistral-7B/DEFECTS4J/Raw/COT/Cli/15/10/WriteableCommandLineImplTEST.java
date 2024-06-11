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
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option2"));
        Option option2 = writeableCommandLine.getOption("option2");
        Assert.assertEquals("option2", option2.getLongOpt());
        Assert.assertEquals("option2", option2.getShortName());
        Assert.assertNotNull(option2.getValues());
        Assert.assertEquals(0, option2.getValues().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", true, "v"));
        writeableCommandLine.addValue(new Option("option"), "value1");
        Option option = writeableCommandLine.getOption("option");
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals("v", option.getShortName());
        Assert.assertNotNull(option.getValues());
        Assert.assertEquals(1, option.getValues().size());
        Assert.assertEquals("value1", option.getValue());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", false, "s"));
        writeableCommandLine.addSwitch(new Option("option"), true);
        Option option = writeableCommandLine.getOption("option");
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals("s", option.getShortName());
        Assert.assertEquals(null, option.getValue());
        Assert.assertEquals(true, option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", false, "s"));
        writeableCommandLine.addSwitch(new Option("option"), true);
        writeableCommandLine.addSwitch(new Option("option"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option2"));
        Assert.assertTrue(writeableCommandLine.hasOption(new Option("option1")));
        Assert.assertTrue(writeableCommandLine.hasOption(new Option("option2")));
        Assert.assertFalse(writeableCommandLine.hasOption(new Option("option3")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option1", true));
        Option option = writeableCommandLine.getOption("option1");
        Assert.assertEquals("option1", option.getLongOpt());
        Assert.assertEquals(null, option.getShortName());
        Assert.assertEquals(true, option.isRequired());
        Assert.assertNotNull(option.getValues());
        Assert.assertEquals(0, option.getValues().size());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", true, "v"));
        writeableCommandLine.addValue(new Option("option"), "value1");
        writeableCommandLine.addValue(new Option("option"), "value2");
        Option option = writeableCommandLine.getOption("option");
        List values = option.getValues();
        Assert.assertEquals("option", option.getLongOpt());
        Assert.assertEquals(null, option.getShortName());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", true, "v"));
        writeableCommandLine.addValue(new Option("option"), "value1");
        Option option = writeableCommandLine.getOption("option");
        List values = option.getUndefaultedValues();
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", false, "s"));
        writeableCommandLine.addSwitch(new Option("option"), true);
        Option option = writeableCommandLine.getOption("option");
        Assert.assertEquals(true, option.getSwitch());
    }

    @Test
    public void testGetSwitchDefaultValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", false, "s"));
        Option option = writeableCommandLine.getOption("option");
        Assert.assertFalse(option.getSwitch());
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new PropertyOption("property", "defaultValue"));
        writeableCommandLine.addProperty(new PropertyOption("property"), "value");
        Option option = writeableCommandLine.getOption("option");
        String property = writeableCommandLine.getProperty("property");
        Assert.assertEquals("value", property);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new PropertyOption("property", "defaultValue"));
        Option option = writeableCommandLine.getOption("option");
        String property = writeableCommandLine.getProperty("property", "defaultValue");
        Assert.assertEquals("defaultValue", property);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new PropertyOption("property1", "value1"));
        writeableCommandLine.addOption(new PropertyOption("property2", "value2"));
        Option option = writeableCommandLine.getOption("option");
        Set properties = option.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", true, "v"));
        writeableCommandLine.addValue(new Option("option"), "value1");
        writeableCommandLine.addValue(new Option("option"), "value2");
        Assert.assertEquals("option value1 value2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option2"));
        Assert.assertEquals(2, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option1", true, "v"));
        writeableCommandLine.addOption(new Option("option2", false, "s"));
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("option1"));
        Assert.assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", true, "v"));
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        writeableCommandLine.setDefaultValues(new Option("option"), defaults);
        Option option = writeableCommandLine.getOption("option");
        List values = option.getValues();
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", false, "s"));
        writeableCommandLine.setDefaultSwitch(new Option("option"), true);
        Option option = writeableCommandLine.getOption("option");
        Assert.assertTrue(option.getSwitch());
    }

    @Test
    public void testSetDefaultSwitchNull() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option", false, "s"));
        writeableCommandLine.setDefaultSwitch(new Option("option"), null);
        Option option = writeableCommandLine.getOption("option");
        Assert.assertFalse(option.getSwitch());
    }
}