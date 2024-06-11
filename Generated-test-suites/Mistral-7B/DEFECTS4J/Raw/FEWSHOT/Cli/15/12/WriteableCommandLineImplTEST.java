package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_Constructor() {
        Option rootOption = new Option("root", "rootOption", true, "root description");
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertNotNull(commandLine);
        assertEquals(rootOption, commandLine.getRootOption());
        assertNotNull(commandLine.getOptionToProperties());
        assertNotNull(commandLine.getOptions());
        assertNotNull(commandLine.getNameToOption());
        assertNotNull(commandLine.getValues());
        assertNotNull(commandLine.getSwitches());
        assertNotNull(commandLine.getDefaultValues());
        assertNotNull(commandLine.getDefaultSwitches());
        assertNotNull(commandLine.getNormalised());
        assertNotNull(commandLine.getPrefixes());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option rootOption = new Option("root", "rootOption", true, "root description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option1 = new Option("option1", "opt1", false, "option1 description");
        Option option2 = new Option("option2", "opt2", true, "option2 description");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
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

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
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

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        commandLine.addProperty(option, "property", properties);
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Properties optionProperties = commandLine.getProperty(option);
        assertEquals(2, optionProperties.size());
        assertEquals("value1", optionProperties.getProperty("property1"));
        assertEquals("value2", optionProperties.getProperty("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty_defaultValue() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        Properties properties = new Properties();
        properties.setProperty("property", "defaultValue");
        commandLine.addProperty(option, "property");
        String propertyValue = commandLine.getProperty(option, null);
        assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set optionProperties = commandLine.getProperties(option);
        assertEquals(2, optionProperties.size());
        assertTrue(optionProperties.containsAll(properties.keySet()));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        assertTrue(commandLine.looksLikeOption("root"));
        assertTrue(commandLine.looksLikeOption("root --"));
        assertTrue(commandLine.looksLikeOption("--root"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option);
        String commandLineString = commandLine.toString();
        String expected = "arg1 arg2 root option";
        assertEquals(expected, commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getListOfOptions() {
        Option option1 = new Option("option1", "opt1", false, "option1 description");
        Option option2 = new Option("option2", "opt2", false, "option2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option);
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), new ArrayList());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option = new Option("option", "o", false, "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root", true, "root description"), Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option);
        List normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
        assertEquals("option", normalised.get(2));
    }
}