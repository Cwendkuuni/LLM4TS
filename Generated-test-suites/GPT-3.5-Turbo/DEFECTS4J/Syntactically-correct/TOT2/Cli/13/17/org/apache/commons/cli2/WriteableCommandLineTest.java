import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues(option);
        Assert.assertTrue(values.contains("value"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option);
        Assert.assertEquals(2, values.size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        List<String> values = commandLine.getValues(option);
        Assert.assertTrue(values.contains("default1"));
        Assert.assertTrue(values.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addProperty("key", "value");
        Assert.assertEquals("value", commandLine.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Assert.assertTrue(commandLine.looksLikeOption("-test"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }
}