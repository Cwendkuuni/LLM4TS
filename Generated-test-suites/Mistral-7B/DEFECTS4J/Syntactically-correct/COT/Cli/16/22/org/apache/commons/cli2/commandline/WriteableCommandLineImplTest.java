import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test"}));
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionDuplicate() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test"}));
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test"}));
        commandLine.addValue(option, "value1");
        Assert.assertEquals(1, commandLine.getValues(option, null).size());
        Assert.assertEquals("value1", commandLine.getValues(option, null).get(0));

        commandLine.addValue(option, "value2");
        Assert.assertEquals(2, commandLine.getValues(option, null).size());
        Assert.assertEquals("value1", commandLine.getValues(option, null).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option, null).get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test"}));
        commandLine.addSwitch(option, true);
        Assert.assertTrue((Boolean) commandLine.getSwitch(option, false));

        commandLine.addSwitch(option, false);
        Assert.assertFalse((Boolean) commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test"}));
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test"}));
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test"}));
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionNull() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "option", true, "an option"), Arrays.asList(new String[]{"test"}));
        commandLine.getOption(null);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test", "value1", "value2"}));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test", "value1"}));
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "option", false, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"-o"}));
        commandLine.addSwitch(option, true);
        Assert.assertTrue((Boolean) commandLine.getSwitch(option, false));

        commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{}));
        Assert.assertFalse((Boolean) commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "a property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"-p", "property=value"}));
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getProperty(option, "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "a property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{}));
        commandLine.addProperty(option, "property", "value");
        Map<String, String> properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.get("property"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "a property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"-p", "property=value1", "-p", "property=value2"}));
        commandLine.addProperty(option, "property", "value1");
        Map<String, String> properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value1", properties.get("property"));

        commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{}));
        properties = commandLine.getProperties(option);
        Assert.assertEquals(0, properties.size());
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(new String[]{"test", "value1", "value2"}));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        Assert.assertEquals("test value1 value2", commandLine.toString());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "option", true, "an option"), Arrays.asList(new String[]{"-o", "test"}));
        Assert.assertTrue(commandLine.looksLikeOption("-o"));

        commandLine = new WriteableCommandLineImpl(new Option("o", "option", true, "an option"), Arrays.asList(new String[]{"test", "arg"}));
        Assert.assertTrue(commandLine.looksLikeOption("-o"));

        commandLine = new WriteableCommandLineImpl(new Option("o", "option", true, "an option"), Arrays.asList(new String[]{"arg"}));
        Assert.assertFalse(commandLine.looksLikeOption("-o"));
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        CommandLineParser parser = new DefaultParser();
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, null);
        parser.parse(writeableCommandLine, new String[]{"-o", "value"});
    }

    @Test
    public void testNullOption() {
        Assert.assertNull(new WriteableCommandLineImpl(null, Arrays.asList(new String[]{})));
    }

    @Test
    public void testNullArguments() {
        Assert.assertNull(new WriteableCommandLineImpl(new Option("o", "option", true, "an option"), null));
    }
}