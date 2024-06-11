import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", "b", false, "test");
        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", true, "test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);

        Option returnedOption = commandLine.getOption("a");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", true, "test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", true, "test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List unfaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, unfaultedValues.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("--arg1", "arg1", false, "test1"));
        commandLine.addOption(new Option("--arg2", "arg2", true, "test2", new String[]{"value1", "value2"}));

        Assert.assertEquals("--arg1 --arg2 value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option1 = new Option("--arg1", "arg1", false, "test1");
        Option option2 = new Option("--arg2", "arg2", true, "test2", new String[]{"value1", "value2"});
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option1 = new Option("--arg1", "arg1", false, "test1");
        Option option2 = new Option("--arg2", "arg2", true, "test2", new String[]{"value1", "value2"});
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("--arg1"));
        Assert.assertTrue(optionTriggers.contains("--arg2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", true, "test");
        commandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "a", false, "test");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addOption(new Option("--arg1", "arg1", false, "test1"));
        commandLine.addOption(new Option("--arg2", "arg2", true, "test2", new String[]{"value1", "value2"}));

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("--arg1", normalised.get(0));
        Assert.assertEquals("--arg2 value1 value2", normalised.get(1));
    }
}