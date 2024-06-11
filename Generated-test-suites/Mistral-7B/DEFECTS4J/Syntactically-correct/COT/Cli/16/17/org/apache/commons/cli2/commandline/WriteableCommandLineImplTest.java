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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Collections.emptyList());
        Option option = new Option("o", "opt", false, "Option test");

        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg"), Collections.emptyList());
        Option option = new Option("a", "arg", true, "Argument test");
        List<String> valueList = new ArrayList<>();
        valueList.add("value1");
        valueList.add("value2");

        commandLine.addOption(option);
        commandLine.addValue(option, "value3");

        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values.get(0), "value1");
        Assert.assertEquals(values.get(1), "value2");
        Assert.assertEquals(values.get(2), "value3");
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch"), Collections.emptyList());
        Option option = new Option("s", "switch", false, "Switch test");

        commandLine.addOption(option);
        commandLine.addSwitch(option, false);

        boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertFalse(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch"), Collections.emptyList());
        Option option = new Option("s", "switch", false, "Switch test");

        commandLine.addOption(option);
        commandLine.addSwitch(option, false);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Collections.emptyList());
        Option option = new Option("o", "opt", false, "Option test");

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("o");
        Assert.assertNotNull(retrievedOption);
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg"), Collections.emptyList());
        Option option = new Option("a", "arg", true, "Argument test");
        List<String> valueList = new ArrayList<>();
        valueList.add("value1");
        valueList.add("value2");

        commandLine.addOption(option);
        commandLine.addValue(option, "value3");
        commandLine.addValue(option, "value4");

        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(values.size(), 4);
        Assert.assertEquals(values.get(0), "value1");
        Assert.assertEquals(values.get(1), "value2");
        Assert.assertEquals(values.get(2), "value3");
        Assert.assertEquals(values.get(3), "value4");
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg"), Collections.emptyList());
        Option option = new Option("a", "arg", true, "Argument test");

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0), "value1");
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch"), Collections.emptyList());
        Option option = new Option("s", "switch", false, "Switch test");

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        switchValue = commandLine.getSwitch(option, true);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("prop"), Collections.emptyList());
        Option option = new Option("p", "prop", false, "Property test");

        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "defaultValue");
        Assert.assertEquals(propertyValue, "value");
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("prop"), Collections.emptyList());
        Option option = new Option("p", "prop", false, "Property test");

        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addProperty(option, "property", properties);

        Map<String, String> propertiesMap = commandLine.getProperties(option);
        Assert.assertEquals(propertiesMap.size(), 2);
        Assert.assertEquals(propertiesMap.get("property1"), "value1");
        Assert.assertEquals(propertiesMap.get("property2"), "value2");
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg"), Collections.emptyList());
        Option option = new Option("a", "arg", true, "Argument test");

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        String commandLineString = commandLine.toString();
        Assert.assertEquals(commandLineString, "value1 arg");
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Collections.emptyList());
        Option option1 = new Option("o1", "opt1", false, "Option test 1");
        Option option2 = new Option("o2", "opt2", false, "Option test 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(options.size(), 2);
        Assert.assertEquals(options.get(0), option1);
        Assert.assertEquals(options.get(1), option2);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Collections.emptyList());
        Option option1 = new Option("o1", "opt1", false, "Option test 1");
        Option option2 = new Option("o2", "opt2", false, "Option test 2");
        Option option3 = new Option("o3", "opt3", false, "Option test 3");

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(optionTriggers.size(), 3);
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("o1", "o2", "o3")));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Collections.emptyList());
        Option option1 = new Option("o1", "opt1", false, "Option test 1");
        Option option2 = new Option("o2", "opt2", false, "Option test 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertTrue(commandLine.hasOption(option2));
        Assert.assertFalse(commandLine.hasOption(new Option("o3", "opt3", false, "Option test 3")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg"), Collections.emptyList());
        Option option = new Option("a", "arg", true, "Argument test");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("value1");
        defaultValues.add("value2");

        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values.get(0), "value1");
        Assert.assertEquals(values.get(1), "value2");
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch"), Collections.emptyList());
        Option option = new Option("s", "switch", false, "Switch test");
        Boolean defaultSwitch = true;

        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt"), Collections.emptyList());
        commandLine.addOption(new Option("o1", "opt1", false, "Option test 1"));
        commandLine.addOption(new Option("o2", "opt2", false, "Option test 2"));

        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(normalised.size(), 2);
        Assert.assertEquals(normalised.get(0), "opt1");
        Assert.assertEquals(normalised.get(1), "opt2");
    }
}