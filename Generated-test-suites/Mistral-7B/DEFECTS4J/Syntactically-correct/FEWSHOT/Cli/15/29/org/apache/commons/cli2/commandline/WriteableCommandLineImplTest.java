package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("option");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(1, undefaultedValues.size());
        assertEquals("value1", undefaultedValues.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperty() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        commandLine.addProperty(option, "property", properties);
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_AddProperty() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", properties);
        Map optionProperties = commandLine.getProperties(option);
        assertEquals(1, optionProperties.size());
        Properties expectedProperties = new Properties();
        expectedProperties.setProperty("property1", "value1");
        assertEquals(expectedProperties, optionProperties);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperties() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", properties);
        Map propertiesMap = commandLine.getProperties();
        assertEquals(1, propertiesMap.size());
        Properties expectedProperties = new Properties();
        expectedProperties.setProperty("property1", "value1");
        assertEquals(expectedProperties, propertiesMap);
    }

    @Test
    public void testWriteableCommandLineImpl_LooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", "option", false, "Option description"), Collections.emptyList());
        commandLine.setPrefixes(Collections.singletonList("--"));
        assertFalse(commandLine.looksLikeOption(""));
        assertFalse(commandLine.looksLikeOption(""));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("--other"));
        assertFalse(commandLine.looksLikeOption("-other"));
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("arg1"));
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        assertEquals("arg1 option value1", commandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptions() {
        Option option1 = new Option("option1", "option1", false, "Option 1 description");
        Option option2 = new Option("option2", "option2", false, "Option 2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptionTriggers() {
        Option option1 = new Option("option1", "option1", false, "Option 1 description");
        Option option2 = new Option("option2", "option2", false, "Option 2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testWriteableCommandLineImpl_SetDefaultValues() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        List defaults = new ArrayList();
        defaults.add("value1");
        defaults.add("value2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_SetDefaultSwitch() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_GetNormalised() {
        Option option = new Option("option", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("arg1"));
        commandLine.addOption(option);
        List normalised = commandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals("arg1", normalised.get(0));
    }
}