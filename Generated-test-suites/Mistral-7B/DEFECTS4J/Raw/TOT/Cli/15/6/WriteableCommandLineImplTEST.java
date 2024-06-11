package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Test for WriteableCommandLineImpl class.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLine() {
        Option option = new Option("o", "output", true, "output file");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals("o", writeableCommandLine.getOption(0).getLongOpt());
        Assert.assertEquals("output", writeableCommandLine.getOption(0).getOptString());
        Assert.assertEquals(true, writeableCommandLine.getOption(0).isSet());
        Assert.assertEquals("test.txt", writeableCommandLine.getValues(writeableCommandLine.getOption(0)).get(0));

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("a", "arg", true, "an argument");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("a", normalised.get(0));
        Assert.assertEquals("value1", normalised.get(1));
        Assert.assertEquals("value2", normalised.get(2));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "a switch");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Assert.assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, Boolean.FALSE));
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("s", "switch", false, "a switch");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("a", "arg", true, "an argument");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addValue(option, "value1");

        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "a property");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "name", "value");

        String propertyValue = writeableCommandLine.getProperty(option, "name", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "a property");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "name1", "value1");
        writeableCommandLine.addProperty(option, "name2", "value2");

        Properties properties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("name1", properties.getProperty("name1"));
        Assert.assertEquals("value1", properties.getProperty("name1"));
        Assert.assertEquals("name2", properties.getProperty("name2"));
        Assert.assertEquals("value2", properties.getProperty("name2"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "output file");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("-o test.txt", commandLineString);
    }
}