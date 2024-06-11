package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        Assert.assertTrue(commandLine.getValues(option, null).contains("value1"));
        commandLine.addValue(option, "value2");
        Assert.assertEquals(2, commandLine.getValues(option, null).size());
        Assert.assertTrue(commandLine.getValues(option, null).contains("value1"));
        Assert.assertTrue(commandLine.getValues(option, null).contains("value2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        Assert.assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, false);
        Assert.assertEquals(Boolean.FALSE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option().withPreferredName("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        Assert.assertEquals(expectedValues, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        Assert.assertEquals(expectedValues, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);
        Assert.assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Assert.assertNull(commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        commandLine.addProperty(new Option(), "property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Assert.assertEquals("defaultValue", commandLine.getProperty(new Option(), "property", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Set properties = commandLine.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertTrue(properties.isEmpty());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Assert.assertFalse(commandLine.looksLikeOption("trigger"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Assert.assertEquals("", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        List<Option> options = commandLine.getOptions();
        Assert.assertNotNull(options);
        Assert.assertTrue(options.isEmpty());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Set triggers = commandLine.getOptionTriggers();
        Assert.assertNotNull(triggers);
        Assert.assertTrue(triggers.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultValues(option, null);
        Assert.assertNull(commandLine.getDefaultValues(option));
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, null);
        Assert.assertNull(commandLine.getDefaultSwitch(option));
        commandLine.setDefaultSwitch(option, true);
        Assert.assertEquals(Boolean.TRUE, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        List<String> normalised = commandLine.getNormalised();
        Assert.assertNotNull(normalised);
        Assert.assertTrue(normalised.isEmpty());
    }
}