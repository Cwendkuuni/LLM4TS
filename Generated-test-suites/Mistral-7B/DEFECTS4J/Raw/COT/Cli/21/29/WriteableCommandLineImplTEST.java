import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_switchAlreadySet() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
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
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitch_defaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitch_noDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperties() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        Set properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("property1", properties.iterator().next());
    }

    @Test
    public void testGetProperty() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        Assert.assertEquals("value1", commandLine.getProperty(option, "property1", "defaultValue"));
    }

    @Test
    public void testToString() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Test Option 2")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Assert.assertEquals("-option1 arg1 arg2 -option2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Test Option 2")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Test Option 2")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, triggers.size());
        Assert.assertEquals(option1.getPreferredName(), triggers.iterator().next());
        Assert.assertEquals(option2.getPreferredName(), triggers.iterator().next());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        List defaults = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testSetDefaultValues_emptyDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, null);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testSetDefaultSwitch_noDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Test Option 2")
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
        Assert.assertEquals("arg2", normalised.get(1));
    }
}