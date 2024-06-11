package org.apache.commons.cli2.commandline;

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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test", "test"), Arrays.asList("testArg"));

        commandLine.addOption(new Option("option1", "option1", true));
        commandLine.addOption(new Option("option2", "option2", false));

        List options = commandLine.getOptions();
        Map<String, Option> nameToOption = commandLine.getOptionTriggers();

        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.contains(commandLine.getOption("test")));
        Assert.assertTrue(options.contains(nameToOption.get("option1")));
        Assert.assertTrue(options.contains(nameToOption.get("option2")));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("option").longOpt("option")
                .valueSeparator('=')
                .build(), Arrays.asList("defaultValue"));

        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");

        List valueList = commandLine.getValues(commandLine.getOption("option"), null);

        Assert.assertEquals(2, valueList.size());
        Assert.assertEquals("value1", valueList.get(0));
        Assert.assertEquals("value2", valueList.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("switch").build(), null);

        commandLine.addSwitch(commandLine.getOption("switch"), true);
        commandLine.addSwitch(commandLine.getOption("switch"), false);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("switch"), null);

        Assert.assertTrue(switchValue == null || switchValue.booleanValue());
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test", "test"), Arrays.asList("testArg"));

        Assert.assertTrue(commandLine.hasOption(commandLine.getOption("test")));
        Assert.assertFalse(commandLine.hasOption(new Option("nonexistent", "nonexistent")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test", "test"), Arrays.asList("testArg"));

        Option option = commandLine.getOption("test");
        Assert.assertNotNull(option);
        Assert.assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("value", "value"), Arrays.asList("defaultValue"));

        commandLine.addValue(commandLine.getOption("value"), "value1");
        commandLine.addValue(commandLine.getOption("value"), "value2");

        List valueList = commandLine.getValues(commandLine.getOption("value"), null);

        Assert.assertEquals(2, valueList.size());
        Assert.assertEquals("value1", valueList.get(0));
        Assert.assertEquals("value2", valueList.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("value", "value"), Arrays.asList("defaultValue"));

        commandLine.addValue(commandLine.getOption("value"), "value1");

        List valueList = commandLine.getUndefaultedValues(commandLine.getOption("value"));

        Assert.assertEquals(1, valueList.size());
        Assert.assertEquals("value1", valueList.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), null);

        commandLine.addSwitch(commandLine.getOption("switch"), true);
        commandLine.addSwitch(commandLine.getOption("switch"), false);

        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("switch"), null);

        Assert.assertEquals(Boolean.TRUE, switchValue);

        switchValue = commandLine.getSwitch(commandLine.getOption("switch"), null);

        Assert.assertEquals(Boolean.FALSE, switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("property", "property"), Arrays.asList("defaultValue"));

        commandLine.addProperty(commandLine.getOption("property"), "propertyKey", "propertyValue");

        String propertyValue = commandLine.getProperty(commandLine.getOption("property"), "defaultPropertyValue");

        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(
                new Option("property", "property"), Arrays.asList("defaultValue"));

        Map<String, String> properties = new HashMap<>();
        properties.put("propertyKey1", "propertyValue1");
        properties.put("propertyKey2", "propertyValue2");

        commandLine.addProperty(commandLine.getOption("property"), "propertyKey1", "propertyValue1");
        commandLine.addProperty(commandLine.getOption("property"), "propertyKey2", "propertyValue2");

        Map<String, String> optionProperties = commandLine.getProperties(commandLine.getOption("property"));

        Assert.assertEquals(2, optionProperties.size());
        Assert.assertEquals("propertyValue1", optionProperties.get("propertyKey1"));
        Assert.assertEquals("propertyValue2", optionProperties.get("propertyKey2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), null);

        commandLine.addSwitch(commandLine.getOption("switch"), true);
        commandLine.addSwitch(commandLine.getOption("switch"), true);
    }
}