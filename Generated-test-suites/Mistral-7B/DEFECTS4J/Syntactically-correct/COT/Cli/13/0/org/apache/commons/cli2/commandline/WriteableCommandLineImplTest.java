import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", "bval");
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        Option actualOption = (Option) options.get(0);
        Assert.assertEquals("b", actualOption.getLongOpt());
        Assert.assertEquals("bval", actualOption.getValueDesc());
        Assert.assertEquals(Collections.emptyList(), actualOption.getArgs());
        Assert.assertEquals(Collections.singletonMap("b", option), commandLine.getNameToOption());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "aval");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Collections.singletonList("value1"), values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "aval");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "aval");
        commandLine.addOption(option);
        Option actualOption = commandLine.getOption("a");
        Assert.assertEquals(option, actualOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "aval");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Collections.unmodifiableList(Arrays.asList("value1", "value2")), values);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean actualSwitch = commandLine.getSwitch(option, false);
        Assert.assertTrue(actualSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");
        Properties properties = commandLine.getProperties();
        Assert.assertEquals("value", properties.getProperty("property"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property", "value");
        String actualProperty = commandLine.getProperty("property", null);
        Assert.assertEquals("value", actualProperty);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        Assert.assertTrue(properties.containsAll(Arrays.asList("property1", "property2")));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Assert.assertTrue(commandLine.looksLikeOption("a"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addValue(commandLine.getOption("a"), "value1");
        commandLine.addValue(commandLine.getOption("a"), "value2");
        String actualToString = commandLine.toString();
        String expectedToString = "[\"value1\", \"value2\"]";
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "aval");
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        Assert.assertEquals(Collections.singletonList(option), options);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option1 = new Option("a", "aval1");
        Option option2 = new Option("b", "aval2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("a", "b")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", "aval");
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("a", null);
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        commandLine.addValue(commandLine.getOption("a"), "value1");
        commandLine.addValue(commandLine.getOption("a"), "value2");
        List normalised = commandLine.getNormalised();
        List expectedNormalised = new ArrayList();
        expectedNormalised.add("value1");
        expectedNormalised.add("value2");
        Assert.assertEquals(expectedNormalised, normalised);
    }
}