import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        Assert.assertEquals(option, writeableCommandLine.getOption("optionName"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWhenAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        Assert.assertEquals(option, writeableCommandLine.getOption("optionName"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchWhenNotSet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.addOption(option);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        option.setLongOpt("longOptionName");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "propertyName", "propertyValue");
        String propertyValue = writeableCommandLine.getProperty(option, "propertyName", "defaultValue");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testGetPropertyWhenNotSet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        writeableCommandLine.addOption(option);
        String propertyValue = writeableCommandLine.getProperty(option, "propertyName", "defaultValue");
        Assert.assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("propertyName1", "propertyValue1");
        writeableCommandLine.addProperty(option, properties);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, propertiesSet.size());
        Assert.assertTrue(propertiesSet.contains("propertyName1"));
    }

    @Test
    public void testGetPropertiesWhenNotSet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        writeableCommandLine.addOption(option);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        Assert.assertEquals(0, propertiesSet.size());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(new Option("option1", "option1Description"));
        writeableCommandLine.addOption(new Option("option2", "option2Description"));
        Assert.assertEquals("option1 option2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("option1"));
        Assert.assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultValuesWhenNotSet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.setDefaultValues(option, null);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultSwitchWhenNotSet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("optionName", "optionDescription");
        writeableCommandLine.setDefaultSwitch(option, null);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }
}