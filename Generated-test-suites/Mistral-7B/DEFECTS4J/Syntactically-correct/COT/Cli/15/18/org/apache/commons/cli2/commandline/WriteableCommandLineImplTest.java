import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WriteableCommandLineImpl.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        option = new Option("a", "arg", true, "an argument");
        parser = new DefaultParser();
    }

    @Test
    public void testAddOption() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        Assert.assertEquals(Arrays.asList(option), commandLine.getOptions());
    }

    @Test
    public void testAddValue() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testAddSwitch() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_alreadySet() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "a"));
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "a"));
        Option returnedOption = commandLine.getOption("a");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "arg1", "arg2"));
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "arg1", "arg2"));
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    @Test
    public void testGetSwitch() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "--a"));
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine = new WriteableCommandLineImpl(new Option("p", "property", true, "a property"), Arrays.asList("test", "--property=value"));
        Assert.assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        commandLine = new WriteableCommandLineImpl(new Option("p", "property", true, "a property"), Arrays.asList("test"));
        commandLine.addProperty(option, "property", "value");
        Map properties = commandLine.getProperties(option);
        Assert.assertEquals("value", properties.get("property"));
    }

    @Test
    public void testToString() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "arg1", "arg2"));
        Assert.assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetNormalised() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test--arg1=value1 arg2 arg3"));
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(Arrays.asList("arg1=value1", "arg2"), normalised);
    }

    @Test
    public void testSetDefaultValues() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testLooksLikeOption() {
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        Assert.assertTrue(commandLine.looksLikeOption("--a"));
        Assert.assertFalse(commandLine.looksLikeOption("--not-a"));
    }
}