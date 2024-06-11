package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    @org.junit.Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("o", "opt", false, "Option test");
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        Option optionFromCommandLine = (Option) options.get(0);
        assertEquals(option.getShortOpt(), optionFromCommandLine.getShortOpt());
        assertEquals(option.getLongOpt(), optionFromCommandLine.getLongOpt());
        assertEquals(option.isRequired(), optionFromCommandLine.isRequired());
        assertEquals(option.getDescription(), optionFromCommandLine.getDescription());
    }

    @org.junit.Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("a", "arg", true, "Argument test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @org.junit.Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("d", "debug", false, "Debug option test");
        commandLine.addOption(option);
        boolean switchValue = commandLine.getSwitch(option, false);
        assertFalse(switchValue);

        commandLine.addSwitch(option, true);
        switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @org.junit.Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("o", "opt", false, "Option test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @org.junit.Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("o", "opt", false, "Option test");
        commandLine.addOption(option);
        Option optionFromCommandLine = commandLine.getOption("opt");
        assertEquals(option, optionFromCommandLine);
    }

    @org.junit.Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("a", "arg", true, "Argument test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        defaultValues.add("default2");
        values = commandLine.getValues(option, defaultValues);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("default1", values.get(2));
        assertEquals("default2", values.get(3));
    }

    @org.junit.Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("a", "arg", true, "Argument test");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @org.junit.Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("d", "debug", false, "Debug option test");
        commandLine.addOption(option);
        boolean switchValue = commandLine.getSwitch(option, false);
        assertFalse(switchValue);

        commandLine.addSwitch(option, true);
        switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, false);
        assertFalse(switchValue);
    }

    @org.junit.Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("p", "prop", true, "Property option test");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        String propertyValue = commandLine.getProperty("property1");
        assertEquals("value1", propertyValue);

        commandLine.addProperty(option, "property2", "value2");
        propertyValue = commandLine.getProperty("property2");
        assertEquals("value2", propertyValue);

        propertyValue = commandLine.getProperty("property1", "defaultValue");
        assertEquals("value1", propertyValue);

        commandLine.addProperty(option, "property1", null);
        propertyValue = commandLine.getProperty("property1");
        assertNull(propertyValue);
    }

    @org.junit.Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("p", "prop", true, "Property option test");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        Set propertiesSet = commandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.contains("property1"));

        commandLine.addProperty(option, "property2", "value2");
        propertiesSet = commandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.contains("property1"));
        assertTrue(propertiesSet.contains("property2"));
    }

    @org.junit.Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option("p", "prop", true, "Property option test");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        Set propertiesSet = commandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.contains("property1"));

        commandLine.addProperty(option, "property2", "value2");
        propertiesSet = commandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.contains("property1"));
        assertTrue(propertiesSet.contains("property2"));

        propertiesSet = commandLine.getProperties();
        assertEquals(0, propertiesSet.size());
    }

    @org.junit.Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addOption(new Option("a", "arg", true, "Argument test"));
        commandLine.addOption(new Option("o", "opt", false, "Option test"));
        commandLine.addValue(commandLine.getOption("arg"), "value1");
        commandLine.addSwitch(commandLine.getOption("opt"), true);
        String commandLineString = commandLine.toString();
        String expected = "-a value1 -o";
        assertEquals(expected, commandLineString);
    }

    @org.junit.Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addOption(new Option("a", "arg", true, "Argument test"));
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        Option option = (Option) options.get(0);
        assertEquals("arg", option.getShortOpt());
        assertEquals("arg", option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals("Argument test", option.getDescription());
    }

    @org.junit.Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addOption(new Option("a", "arg", true, "Argument test"));
        commandLine.addOption(new Option("o", "opt", false, "Option test"));
        commandLine.addOption(new Option("t", "test", false, "Test option test"));
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.contains("a"));
        assertTrue(optionTriggers.contains("o"));
        assertTrue(optionTriggers.contains("t"));
    }
}