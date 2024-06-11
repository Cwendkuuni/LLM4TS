import org.apache.commons.cli2.CommandLine;
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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("t", true, "t");
        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(option.getLongOpt()));
        Assert.assertEquals(option, commandLine.getOption(option.getShortOpt()));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("i"), Collections.emptyList());
        Option option = new Option("i", true, "i");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("v"), Collections.emptyList());
        Option option = new Option("v", false, "v");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("s"), Collections.emptyList());
        Option option = new Option("s", false, "s");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("h"), Collections.emptyList());
        Option option = new Option("h", false, "h");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("d"), Collections.emptyList());
        Option option = new Option("d", false, "d");
        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("d"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "a");
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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("u"), Collections.emptyList());
        Option option = new Option("u", true, "u");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("e"), Collections.emptyList());
        Option option = new Option("e", false, "e");
        commandLine.addOption(option);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("p"), Collections.emptyList());
        Option option = new Option("p", true, "p");
        Properties properties = new Properties();
        properties.put("property1", "value1");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");

        String propertyValue = commandLine.getProperty(option, "defaultValue");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("r"), Collections.emptyList());
        Option option = new Option("r", true, "r");
        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");

        Set propertyNames = commandLine.getProperties(option);
        Assert.assertEquals(2, propertyNames.size());
        Assert.assertTrue(propertyNames.containsAll(Collections.singletonList("property1")));
        Assert.assertTrue(propertyNames.containsAll(Collections.singletonList("property2")));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("g"), Collections.emptyList());

        Assert.assertTrue(commandLine.looksLikeOption("g"));
        Assert.assertFalse(commandLine.looksLikeOption("h"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("f"), Collections.emptyList());
        commandLine.addOption(new Option("f", true, "f"));
        commandLine.addOption(new Option("f", true, "f"));
        commandLine.addOption(new Option("f", true, "f"));

        String line = commandLine.toString();
        String expected = "\"ff\" f f f";
        Assert.assertEquals(expected, line);
    }
}