import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        Assert.assertEquals(Option.class, commandLine.getOption(0).getClass());
        Assert.assertEquals("test", commandLine.getOption(0).getLongOpt());
        Assert.assertEquals(false, commandLine.getOption(0).isRequired());
        Assert.assertEquals("Test flag", commandLine.getOption(0).getDescription());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        Map properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.get("property"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String property = commandLine.getProperty("test.property");
        Assert.assertEquals("value",
                property);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Assert.assertFalse(commandLine.looksLikeOption(""));
        Assert.assertFalse(commandLine.looksLikeOption(" "));
        Assert.assertTrue(commandLine.looksLikeOption("--test"));
        Assert.assertTrue(commandLine.looksLikeOption("-t"));
        Assert.assertTrue(commandLine.looksLikeOption("-test"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        commandLine.addOption(new Option("t", "test", false, "Test flag"));
        commandLine.addOption(new Option("f", "file", true, "File path"));
        commandLine.addValue(new Option("f"), "/path/to/file");
        Assert.assertEquals("-t --test -f /path/to/file", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        commandLine.addOption(new Option("t", "test", false, "Test flag"));
        commandLine.addOption(new Option("f", "file", true, "File path"));
        Assert.assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        commandLine.addOption(new Option("t", "test", false, "Test flag"));
        commandLine.addOption(new Option("f", "file", true, "File path"));
        Assert.assertEquals(2, commandLine.getOptionTriggers().size());
    }
}