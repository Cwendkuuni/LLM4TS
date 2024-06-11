import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "opt"), new ArrayList<>());
        Option option = new Option("opt2", "opt2");
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        Option actualOption = (Option) options.get(0);
        Option expectedOption = new Option("opt2", "opt2");

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "arg"), new ArrayList<>());
        commandLine.addValue(new Option("arg"), "value");

        List values = commandLine.getValues(new Option("arg"), null);
        List expectedValues = new ArrayList<>();
        expectedValues.add("value");

        Assert.assertEquals(expectedValues, values);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), new ArrayList<>());
        commandLine.addSwitch(new Option("switch"), true);

        Boolean switchValue = commandLine.getSwitch(new Option("switch"), false);

        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), new ArrayList<>());
        commandLine.addSwitch(new Option("switch"), true);
        commandLine.addSwitch(new Option("switch"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "opt"), new ArrayList<>());
        Option option = new Option("opt", "opt");

        boolean hasOption = commandLine.hasOption(option);
        boolean expectedHasOption = true;

        Assert.assertEquals(expectedHasOption, hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "opt"), new ArrayList<>());
        Option option = new Option("opt", "opt");
        commandLine.addOption(option);

        Option actualOption = commandLine.getOption("opt");
        Option expectedOption = option;

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "arg"), new ArrayList<>());
        commandLine.addValue(new Option("arg"), "value1");
        commandLine.addValue(new Option("arg"), "value2");

        List values = commandLine.getValues(new Option("arg"), null);
        List expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        Assert.assertEquals(expectedValues, values);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "arg"), new ArrayList<>());
        commandLine.addValue(new Option("arg"), "value1");

        List values = commandLine.getUndefaultedValues(new Option("arg"));
        List expectedValues = new ArrayList<>();
        expectedValues.add("value1");

        Assert.assertEquals(expectedValues, values);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), new ArrayList<>());
        commandLine.addSwitch(new Option("switch"), false);

        Boolean switchValue = commandLine.getSwitch(new Option("switch"), true);
        Boolean expectedSwitchValue = false;

        Assert.assertEquals(expectedSwitchValue, switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption("prop", "prop"), new ArrayList<>());
        commandLine.addProperty(new PropertyOption("prop"), "value");

        String propertyValue = commandLine.getProperty("prop");

        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption("prop", "prop"), new ArrayList<>());

        Map<String, String> properties = new HashMap<>();
        properties.put("prop1", "value1");
        commandLine.addProperty(new PropertyOption("prop"), "prop1", "value1");

        Map<String, String> actualProperties = commandLine.getProperties();
        Map<String, String> expectedProperties = properties;

        Assert.assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption("prop", "prop"), new ArrayList<>());
        commandLine.addProperty(new PropertyOption("prop"), "prop1", "value1");

        Set<String> properties = commandLine.getProperties().keySet();
        Set<String> expectedProperties = Collections.singleton("prop1");

        Assert.assertEquals(expectedProperties, properties);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "opt"), new ArrayList<>());

        boolean looksLikeOption = commandLine.looksLikeOption("opt-prefix opt");
        boolean expectedLooksLikeOption = true;

        Assert.assertEquals(expectedLooksLikeOption, looksLikeOption);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "opt"), new ArrayList<>());
        commandLine.addValue(new Option("opt"), "value1");
        commandLine.addValue(new Option("opt"), "value2");

        String commandLineToString = commandLine.toString();
        String expectedCommandLineToString = "value1 value2";

        Assert.assertEquals(expectedCommandLineToString, commandLineToString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "opt"), new ArrayList<>());
        Option option = new Option("opt2", "opt2");
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        List expectedOptions = new ArrayList<>();
        expectedOptions.add(option);

        Assert.assertEquals(expectedOptions, options);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "opt"), new ArrayList<>());
        Option option = new Option("opt2", "opt2");
        commandLine.addOption(option);

        Set optionTriggers = commandLine.getOptionTriggers();
        Set expectedOptionTriggers = Collections.singleton("opt2");

        Assert.assertEquals(expectedOptionTriggers, optionTriggers);
    }
}