import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addValue(option, "value1");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option");

        Assert.assertEquals(option, retrievedOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionNull() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());

        commandLine.getOption(null);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);

        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchFalse() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addSwitch(option, false);

        Boolean switchValue = commandLine.getSwitch(option, true);

        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "defaultValue");

        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        String propertyValue = commandLine.getProperty(option, "defaultValue");

        Assert.assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set properties = commandLine.getProperties(option);

        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        commandLine.addValue(new Option("value"), "value1");
        commandLine.addValue(new Option("value"), "value2");

        String commandLineString = commandLine.toString();

        Assert.assertEquals("value1 value2", commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();

        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("option1"));
        Assert.assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        List defaults = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaults);

        List values = commandLine.getValues(option, null);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", "Option description");

        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, false);

        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Arrays.asList("--option value1", "--option value2"));

        List normalised = commandLine.getNormalised();

        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}