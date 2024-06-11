import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLineImpl() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        List arguments = new ArrayList();
        arguments.add("--test");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        Assert.assertEquals(rootOption, writeableCommandLine.getOption(writeableCommandLine.getOptionNames().iterator().next()));
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option1 = new Option("option1", "option1", true, "Option 1");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);

        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Assert.assertEquals(option1, writeableCommandLine.getOption(option1.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", true, "Option with Value");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
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
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", false, "Option with Switch");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, Boolean.FALSE));
    }

    @Test
    public void testGetOption() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", false, "Option with Switch");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        Option actualOption = writeableCommandLine.getOption("option");
        Assert.assertEquals(option, actualOption);
    }

    @Test
    public void testGetValues() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", true, "Option with Value");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", true, "Option with Value");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", false, "Option with Switch");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, Boolean.FALSE));

        writeableCommandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, Boolean.FALSE));

        writeableCommandLine.setDefaultSwitch(option, false);
        Assert.assertFalse(writeableCommandLine.getSwitch(option, Boolean.FALSE));
    }

    @Test
    public void testGetProperty() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", false, "Option with Property");
        PropertyOption propertyOption = new PropertyOption("property", "property", true, "Property Option");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(propertyOption);

        writeableCommandLine.addProperty(propertyOption, "property", "value1");
        Assert.assertEquals("value1", writeableCommandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", false, "Option with Property");
        PropertyOption propertyOption = new PropertyOption("property", "property", true, "Property Option");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(propertyOption);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        writeableCommandLine.addProperty(propertyOption, "property", properties);

        Set propertiesSet = writeableCommandLine.getProperties(propertyOption);
        Assert.assertEquals(1, propertiesSet.size());
        Assert.assertEquals("property1", propertiesSet.iterator().next());
        Assert.assertEquals("value1", writeableCommandLine.getProperty("property.property1"));
    }

    @Test
    public void testToString() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option1 = new Option("option1", "option1", true, "Option 1");
        Option option2 = new Option("option2", "option2", false, "Option 2");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option1, "value2");
        writeableCommandLine.addSwitch(option2, true);

        Assert.assertEquals("--test --option1 value1 value2 --option2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option1 = new Option("option1", "option1", true, "Option 1");
        Option option2 = new Option("option2", "option2", false, "Option 2");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Assert.assertEquals(2, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option1 = new Option("option1", "option1", true, "Option 1");
        Option option2 = new Option("option2", "option2", false, "Option 2");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Assert.assertEquals(2, writeableCommandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", true, "Option with Value");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        List values = new ArrayList();
        values.add("defaultValue1");
        values.add("defaultValue2");

        writeableCommandLine.setDefaultValues(option, values);

        List defaults = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, defaults.size());
        Assert.assertEquals("defaultValue1", defaults.get(0));
        Assert.assertEquals("defaultValue2", defaults.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option = new Option("option", "option", false, "Option with Switch");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultSwitch(option, Boolean.TRUE);

        Boolean switchValue = writeableCommandLine.getSwitch(option, Boolean.FALSE);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option rootOption = new Option("test", "test", false, "Test Option");
        Option option1 = new Option("option1", "option1", true, "Option 1");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(option1);

        List arguments = new ArrayList();
        arguments.add("--test");
        arguments.add("--option1 arg1 arg2");

        writeableCommandLine.setNormalised(arguments);

        Assert.assertEquals(2, writeableCommandLine.getNormalised().size());
        Assert.assertEquals("--test", writeableCommandLine.getNormalised().get(0).toString());
        Assert.assertEquals("--option1 arg1 arg2", writeableCommandLine.getNormalised().get(1).toString());
    }
}