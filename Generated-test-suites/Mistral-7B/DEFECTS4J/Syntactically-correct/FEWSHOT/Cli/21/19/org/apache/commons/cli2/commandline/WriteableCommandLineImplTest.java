package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("opt"));
        assertTrue(commandLine.getOptionTriggers().contains("opt"));
    }

    @Test
    public void testAddOptionWithTriggers() {
        Option option = new Option("opt", "option", true, "Option description");
        List triggers = new ArrayList();
        triggers.add("t");
        option.setTriggers(triggers);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("opt"));
        assertTrue(commandLine.getOptionTriggers().contains("opt"));
        assertTrue(commandLine.getOptionTriggers().contains("t"));
    }

    @Test
    public void testAddOptionWithParent() {
        Option parent = new Option("p", "parent", true, "Parent option description");
        Option option = new Option("opt", "option", true, "Option description", parent);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(parent, Collections.emptyList());
        commandLine.addOption(parent);
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("opt"));
        assertTrue(commandLine.getOptionTriggers().contains("opt"));
        assertTrue(commandLine.getOptionTriggers().contains("p"));
        assertSame(parent, option.getParent());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddValueWithDefault() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        List values = commandLine.getValues(option, defaults);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test    public void testGetOption() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("opt"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionNull() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.getOption("opt");
    }

    @Test    public void testGetValues() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test    public void testGetValuesWithDefault() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List defaults = new ArrayList();
        defaults.add("defaultValue");
        List values = commandLine.getValues(option, defaults);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test    public void testGetValuesEmpty() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(0, values.size());
    }

    @Test    public void testGetSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test    public void testGetSwitchDefault() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Boolean switchValue = commandLine.getSwitch(option, true);
        assertFalse(switchValue);
    }

    @Test    public void testgetProperty() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", "defaultValue"));
    }

    @Test    public void testgetPropertyWithDefault() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property"));
    }

    @Test    public void testgetPropertyNull() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(null, commandLine.getProperty(option, "property"));
    }

    @Test    public void testGetProperties() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test    public void testGetPropertiesEmpty() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Set properties = commandLine.getProperties(option);
        assertEquals(0, properties.size());
    }

    @Test    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertFalse(commandLine.looksLikeOption("arg"));
        WriteableCommandLineImpl commandLine2 = new WriteableCommandLineImpl(new Option("-o", "option", false, "Option description"), Collections.emptyList());
        assertTrue(commandLine2.looksLikeOption("o"));
        assertTrue(commandLine2.looksLikeOption("option"));
        assertTrue(commandLine2.looksLikeOption("-o"));
        assertTrue(commandLine2.looksLikeOption("--option"));
    }

    @Test    public void testToString() {
        Option option = new Option("-o", "option", false, "Option description");
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test    public void testGetOptions() {
        Option option1 = new Option("-o1", "option1", false, "Option description1");
        Option option2 = new Option("-o2", "option2", false, "Option description2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test    public void testGetOptionTriggers() {
        Option option1 = new Option("-o1", "option1", false, "Option description1");
        Option option2 = new Option("-o2", "option2", false, "Option description2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(4, optionTriggers.size());
        assertTrue(optionTriggers.contains("-o1"));
        assertTrue(optionTriggers.contains("-o2"));
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test    public void testSetDefaultValues() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test    public void testSetDefaultSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test    public void testGetNormalised() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List normalised = commandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals("option value1", normalised.get(0));
    }
}