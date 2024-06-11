import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1", true), new ArrayList<>());
        commandLine.addOption(new Option("option2", false));
        Option option1 = commandLine.getOption("option1");
        Option option2 = commandLine.getOption("option2");
        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals(option1, commandLine.getOption("option1"));
        Assert.assertEquals(option2, commandLine.getOption("option2"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", true), new ArrayList<>());
        commandLine.addOption(new Option("option", true));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");
        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", false), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("option"), true);
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", false), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("option"), true);
        commandLine.addSwitch(commandLine.getOption("option"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", false), new ArrayList<>());
        Assert.assertFalse(commandLine.hasOption(new Option("option2", false)));
        commandLine.addOption(new Option("option2", false));
        Assert.assertTrue(commandLine.hasOption(new Option("option2", false)));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", false), new ArrayList<>());
        Option option = new Option("option", false);
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("option");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", true), new ArrayList<>());
        commandLine.addOption(new Option("option", true));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");
        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", true), new ArrayList<>());
        commandLine.addOption(new Option("option", true));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        List unfaultedValues = commandLine.getUndefaultedValues(commandLine.getOption("option"));
        Assert.assertEquals(0, unfaultedValues.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", false), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("option"), true);
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", true), new ArrayList<>());
        commandLine.addOption(new Option("option", true));
        List defaultValues = (List) commandLine.getDefaultValues(commandLine.getOption("option"));
        Assert.assertNull(defaultValues);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", true), new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(commandLine.getOption("option"), "property1", properties);
        Map<String, String> retrievedProperties = commandLine.getProperties(commandLine.getOption("option"));
        Assert.assertEquals(1, retrievedProperties.size());
        Assert.assertEquals("value1", retrievedProperties.get("property1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1", true), new ArrayList<>());
        commandLine.addOption(new Option("option2", false));
        commandLine.addValue(commandLine.getOption("option1"), "arg1");
        commandLine.addValue(commandLine.getOption("option1"), "arg2");
        String output = commandLine.toString();
        String expectedOutput = "[option1=\"arg1 arg2\" option2]";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", true), new ArrayList<>());
        commandLine.addOption(new Option("option", true));
        List normalised = commandLine.getNormalised();
        Assert.assertEquals("[option option]", normalised.toString());
    }
}