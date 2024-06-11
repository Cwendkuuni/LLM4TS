import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", false, "Option 1");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("s", "switch", false, "Switch 1");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "Option 1");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "Option 1");
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("s", "switch", false, "Switch 1");
        commandLine.addSwitch(option, false);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertFalse(switchValue);

        commandLine.addSwitch(option, true);
        switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("p", "property", true, "Property Option");
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        commandLine.addOption(new Option("o", "opt", false, "Option 1"));
        commandLine.addOption(new Option("s", "switch", false, "Switch 1"));
        commandLine.addValue(new Option("v", "value", true, "Value Option"), "value1");
        commandLine.addValue(new Option("v", "value", true, "Value Option"), "value2");

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "option1 -o opt -s switch \"value1\" \"value2\"";
        Assert.assertEquals(expectedCommandLineString, commandLineString);
    }
}