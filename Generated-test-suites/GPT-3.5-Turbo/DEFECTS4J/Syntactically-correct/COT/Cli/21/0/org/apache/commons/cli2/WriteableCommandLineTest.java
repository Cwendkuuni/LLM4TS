import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        Integer value = 10;
        commandLine.addValue(option, value);
        Assert.assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        Integer value = 10;
        commandLine.addValue(option, value);
        Assert.assertEquals(1, commandLine.getUndefaultedValues(option).size());
        Assert.assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        List<Integer> defaultValues = List.of(1, 2, 3);
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitchValid() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", true);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitchValue(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchInvalid() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", true);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test", true);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(true, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl("test");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Assert.assertEquals(value, commandLine.getPropertyValue(option, property));
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        Assert.assertEquals(value, commandLine.getDefaultPropertyValue(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Assert.assertTrue(commandLine.looksLikeOption("-test"));
        Assert.assertFalse(commandLine.looksLikeOption("test"));
    }

}