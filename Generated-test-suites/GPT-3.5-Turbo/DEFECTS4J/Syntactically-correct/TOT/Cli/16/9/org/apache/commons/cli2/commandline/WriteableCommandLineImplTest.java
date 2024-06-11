import org.junit.Test;
import org.junit.Assert;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addValue(option, "value1");

        Assert.assertEquals("value1", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addOption(option1);

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("-a"));
        Assert.assertNull(commandLine.getOption("-b"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");

        Assert.assertEquals(2, commandLine.getValues(option1, null).size());
        Assert.assertEquals(1, commandLine.getValues(option2, null).size());
        Assert.assertEquals(0, commandLine.getValues(option2, Collections.singletonList("value3")).size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");

        Assert.assertEquals(2, commandLine.getUndefaultedValues(option1).size());
        Assert.assertEquals(1, commandLine.getUndefaultedValues(option2).size());
        Assert.assertEquals(0, commandLine.getUndefaultedValues(option2).size());
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        Assert.assertTrue(commandLine.getSwitch(option1, null));
        Assert.assertFalse(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addProperty(option, "key1", "value1");

        Assert.assertEquals("value1", commandLine.getProperty(option, "key1", "defaultValue"));
        Assert.assertEquals("defaultValue", commandLine.getProperty(option, "key2", "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addProperty(option, "key1", "value1");

        Assert.assertEquals("value1", commandLine.getProperty(option, "key1", null));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addProperty(option, "key1", "value1");

        Assert.assertEquals(1, commandLine.getProperties(option).size());
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        Assert.assertTrue(commandLine.looksLikeOption("-a"));
        Assert.assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");

        Assert.assertEquals("-a value1 value2 -b value3", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("-a");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");
        List<String> arguments = new ArrayList<>();
        arguments.add("-a");
        arguments.add("-b");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, arguments);

        Assert.assertEquals(arguments, commandLine.getNormalised());
    }
}