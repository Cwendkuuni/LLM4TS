package org.apache.commons.cli2.commandline;

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
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        Option option = new Option("opt", "option").required(false);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        Option option = new Option("opt", "option").required(false);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        Option option = new Option("opt", "option").required(false);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);
        Assert.assertEquals(option, writeableCommandLine.getOption("opt"));
        Assert.assertEquals(option, writeableCommandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option").required(false), new ArrayList());
        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        writeableCommandLine.addValue(option, "value3");
        values = writeableCommandLine.getValues(option, new ArrayList());
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));

        writeableCommandLine.addValue(option, null);
        values = writeableCommandLine.getValues(option, new ArrayList());
        Assert.assertEquals(3, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option").required(false), new ArrayList());
        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        writeableCommandLine.addSwitch(option, false);
        switchValue = writeableCommandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);

        writeableCommandLine.addSwitch(option, null);
        switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertNull(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        writeableCommandLine.addProperty("property", "value");
        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        writeableCommandLine.addProperty("property", "value");
        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);

        String emptyPropertyValue = writeableCommandLine.getProperty("emptyProperty", null);
        Assert.assertNull(emptyPropertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");
        Set properties = writeableCommandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        writeableCommandLine.addOption(new Option("opt1", "option1"));
        writeableCommandLine.addOption(new Option("opt2", "option2"));
        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("option1 option2", commandLineString);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList());
        Assert.assertTrue(writeableCommandLine.looksLikeOption("opt"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("option"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("notAnOption"));
    }
}