import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Option option = new Option("a", "arg", true, "An argument");
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption("a"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithDuplicateTrigger() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Option option1 = new Option("a", "arg1", true, "Arg 1");
        Option option2 = new Option("a", "arg2", true, "Arg 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Option option = new Option("a", "arg", true, "An argument");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals(1, commandLine.getOptions().size());
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Option option = new Option("a", null, false, "A switch");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Option option = new Option("a", "arg", true, "An argument");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Option option = new Option("a", "arg", true, "An argument");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("a"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Option option = new Option("a", "arg", true, "An argument");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Option option = new Option("a", null, false, "A switch");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));

        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property", ""));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property"));
        Assert.assertEquals("", commandLine.getProperty("nonExistentProperty"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertEquals("value1", commandLine.getProperty("property1"));
        Assert.assertTrue(properties.contains("property2"));
        Assert.assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        commandLine.addOption(new Option("a", "arg", false, "An option"));
        commandLine.addOption(new Option("b", "arg2", true, "Another option with an argument"));
        commandLine.addValue(commandLine.getOption("b"), "value1");

        Assert.assertEquals("arg -a arg2=\"value1\"", commandLine.toString());
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        commandLine.addOption(new Option("a", "arg", false, "An option"));
        commandLine.addOption(new Option("b", "arg2", true, "Another option with an argument"));
        commandLine.addValue(commandLine.getOption("b"), "value1");

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg", normalised.get(0));
        Assert.assertEquals("arg2=\"value1\"", normalised.get(1));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        Assert.assertTrue(commandLine.looksLikeOption("--arg"));
        Assert.assertTrue(commandLine.looksLikeOption("-a"));
        Assert.assertFalse(commandLine.looksLikeOption("arg"));
        Assert.assertFalse(commandLine.looksLikeOption(""));
    }
}