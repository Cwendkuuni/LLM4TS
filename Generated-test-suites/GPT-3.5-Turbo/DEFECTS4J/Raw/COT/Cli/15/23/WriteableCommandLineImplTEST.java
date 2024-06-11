import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value");
        Assert.assertEquals("value", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        Assert.assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        Assert.assertEquals(2, commandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        String property = commandLine.getProperty("property");
        Assert.assertNull(property);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");
        String property = commandLine.getProperty("property");
        Assert.assertEquals("value", property);
    }

    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        String property = commandLine.getProperty("property", "default");
        Assert.assertEquals("default", property);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Assert.assertEquals(2, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        boolean looksLikeOption = commandLine.looksLikeOption("-option");
        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(null, "arg1");
        commandLine.addValue(null, "arg2");
        String toString = commandLine.toString();
        Assert.assertEquals("arg1 arg2", toString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Assert.assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Assert.assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        List defaults = new ArrayList();
        commandLine.setDefaultValues(option, defaults);
        Assert.assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        List normalised = new ArrayList();
        commandLine.getNormalised().add("arg1");
        commandLine.getNormalised().add("arg2");
        Assert.assertEquals(2, commandLine.getNormalised().size());
    }
}