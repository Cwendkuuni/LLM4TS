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
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option2", "o2");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionDuplicate() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddValueMultiple() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        Option returnedOption = writeableCommandLine.getOption("o1");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchDefaultFalse() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetSwitchDefaultTrue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(option, "property1", "value1");
        Map propertiesMap = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, propertiesMap.size());
        Assert.assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testAddPropertyMultiple() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");
        Map propertiesMap = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, propertiesMap.size());
        Assert.assertEquals("value1", propertiesMap.get("property1"));
        Assert.assertEquals("value2", propertiesMap.get("property2"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(option, "property1", "value1");
        Map propertiesMap = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, propertiesMap.size());
        Assert.assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testGetPropertiesEmpty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        Map propertiesMap = writeableCommandLine.getProperties(option);
        Assert.assertEquals(0, propertiesMap.size());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", "o1");
        Option option2 = new Option("option2", "o2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option2, "value2");
        String result = writeableCommandLine.toString();
        String expected = "'o1' 'value1' 'o2' 'value2'";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", "o1");
        Option option2 = new Option("option2", "o2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", "o1");
        Option option2 = new Option("option2", "o2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertEquals("o1", optionTriggers.toString());
        Assert.assertEquals("o2", optionTriggers.toString());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        writeableCommandLine.setDefaultValues(option, defaults);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultSwitchFalse() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.setDefaultSwitch(option, false);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testSetDefaultSwitchNull() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("option1", "o1");
        writeableCommandLine.setDefaultSwitch(option, null);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertNull(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("option1", "o1");
        Option option2 = new Option("option2", "o2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option2, "value2");
        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(4, normalised.size());
        Assert.assertEquals("option1", ((Option) normalised.get(0)).getLongOpt());
        Assert.assertEquals("value1", ((Option) normalised.get(1)).getValue());
        Assert.assertEquals("option2", ((Option) normalised.get(2)).getLongOpt());
        Assert.assertEquals("value2", ((Option) normalised.get(3)).getValue());
    }
}