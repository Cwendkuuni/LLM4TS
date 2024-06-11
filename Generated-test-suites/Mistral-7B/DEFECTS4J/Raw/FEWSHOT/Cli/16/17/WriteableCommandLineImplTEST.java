package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_Empty() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        assertEquals(Collections.emptyList(), writeableCommandLine.getOptions());
        assertEquals(Collections.emptySet(), writeableCommandLine.getOptionTriggers());
        assertNull(writeableCommandLine.getValues(null, null));
        assertNull(writeableCommandLine.getSwitch(null, null));
        assertEquals(Collections.EMPTY_MAP, writeableCommandLine.optionToProperties);
        assertNull(writeableCommandLine.getProperties(null));
        assertEquals("", writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);

        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());
        assertEquals(Collections.singleton("opt"), writeableCommandLine.getOptionTriggers());
        assertEquals(Collections.emptyList(), writeableCommandLine.getValues(option, null));
        assertNull(writeableCommandLine.getSwitch(option, null));
        assertEquals(Collections.EMPTY_MAP, writeableCommandLine.optionToProperties);
        assertNull(writeableCommandLine.getProperties(option));
        assertEquals("option", writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");

        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());
        assertEquals(Collections.singleton("opt"), writeableCommandLine.getOptionTriggers());
        assertEquals(Collections.singletonList("value"), writeableCommandLine.getValues(option, null));
        assertNull(writeableCommandLine.getSwitch(option, null));
        assertEquals(Collections.EMPTY_MAP, writeableCommandLine.optionToProperties);
        assertNull(writeableCommandLine.getProperties(option));
        assertEquals("option value", writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());
        assertEquals(Collections.singleton("opt"), writeableCommandLine.getOptionTriggers());
        assertEquals(Collections.emptyList(), writeableCommandLine.getValues(option, null));
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertEquals(Collections.singletonMap(option, new Properties()), writeableCommandLine.optionToProperties);
        assertNull(writeableCommandLine.getProperties(option));
        assertEquals("option", writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
        assertFalse(writeableCommandLine.hasOption(new Option("other", "other", false, "other description")));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);

        assertEquals(option, writeableCommandLine.getOption("opt"));
        assertNull(writeableCommandLine.getOption("other"));
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List emptyValues = writeableCommandLine.getValues(option, Collections.emptyList());
        assertEquals(0, emptyValues.size());
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getUndefaultedValues(option);

        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        List emptyValues = writeableCommandLine.getUndefaultedValues(new Option("other", "other", false, "other description"));
        assertEquals(0, emptyValues.size());
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));

        assertFalse(writeableCommandLine.getSwitch(option, null));
        assertFalse(writeableCommandLine.getSwitch(option, false));
        assertFalse(writeableCommandLine.getSwitch(new Option("other", "other", false, "other description"), null));
        assertFalse(writeableCommandLine.getSwitch(new Option("other", "other", false, "other description"), false));
    }

    @Test
    public void testWriteableCommandLineImpl_AddProperty() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Map optionToPropertiesMap = writeableCommandLine.optionToProperties;

        Map expectedOptionToPropertiesMap = new HashMap();
        expectedOptionToPropertiesMap.put(option, properties);

        assertEquals(expectedOptionToPropertiesMap, optionToPropertiesMap);
    }

    @Test
    public void testWriteableCommandLineImpl_AddPropertyWithEmptyMap() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperties(option, properties);

        Map optionToPropertiesMap = writeableCommandLine.optionToProperties;

        Map expectedOptionToPropertiesMap = new HashMap();
        expectedOptionToPropertiesMap.put(option, properties);

        assertEquals(expectedOptionToPropertiesMap, optionToPropertiesMap);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperty() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperty(option, "property1", "value1");

        assertEquals("value1", writeableCommandLine.getProperty(option, "property1", "defaultValue"));
        assertEquals("defaultValue", writeableCommandLine.getProperty(option, "property2", "defaultValue"));
        assertEquals("defaultValue", writeableCommandLine.getProperty(new Option("other", "other", false, "other description"), "property1", "defaultValue"));
        assertEquals("defaultValue", writeableCommandLine.getProperty(new Option("other", "other", false, "other description"), "property2", "defaultValue"));
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperties() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperties(option, properties);

        Set propertiesSet = writeableCommandLine.getProperties(option);

        Set expectedPropertiesSet = properties.keySet();

        assertEquals(expectedPropertiesSet, propertiesSet);
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        Option option = new Option("opt", "option", false, "short description");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        writeableCommandLine.addSwitch(option, true);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperties(option, properties);

        String expectedString = "option value1 value2 --opt property1=value1 property2=value2";

        assertEquals(expectedString, writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptions() {
        Option option1 = new Option("opt1", "opt1", false, "short description 1");
        Option option2 = new Option("opt2", "opt2", false, "short description 2");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();

        List expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        assertEquals(expectedOptions, options);
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptionTriggers() {
        Option option1 = new Option("opt1", "opt1", false, "short description 1");
        Option option2 = new Option("opt2", "opt2", false, "short description 2");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();

        Set expectedOptionTriggers = new HashSet();
        expectedOptionTriggers.add("opt1");
        expectedOptionTriggers.add("opt2");

        assertEquals(expectedOptionTriggers, optionTriggers);
    }
}