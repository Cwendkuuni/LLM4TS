package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_create() {
        Option rootOption = new Option("opt", "option", false, "Description");
        List<String> arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals("option arg1 arg2", writeableCommandLine.toString());
        assertEquals(arguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option rootOption = new Option("opt", "option", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("arg", "argument", true, "Description");
        writeableCommandLine.addOption(option);

        assertEquals(1, writeableCommandLine.getOptions().size());
        Option addedOption = writeableCommandLine.getOption("arg");
        assertSame(option, addedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("arg", "argument", true, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertArrayEquals(new Object[]{"value1", "value2"}, values.toArray());
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
        assertTrue(writeableCommandLine.hasOption(new Option("arg", "argument", false, "Description")));
        assertFalse(writeableCommandLine.hasOption(new Option("arg2", "argument2", false, "Description")));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        Option returnedOption = writeableCommandLine.getOption("arg");
        assertSame(option, returnedOption);
        assertFalse(writeableCommandLine.getOption("arg2") instanceof Option);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("arg", "argument", true, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertArrayEquals(new Object[]{"value1", "value2"}, values.toArray());

        List emptyValues = writeableCommandLine.getValues(option, Collections.emptyList());
        assertTrue(emptyValues.isEmpty());
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = new Option("arg", "argument", true, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
        List emptyValues = writeableCommandLine.getUndefaultedValues(option);
        assertTrue(emptyValues.isEmpty());
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertFalse(writeableCommandLine.getSwitch(option, true));
        assertTrue(writeableCommandLine.getSwitch(option, null) == Boolean.TRUE);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", properties);
        assertEquals("value", writeableCommandLine.getProperty(option, "key", "defaultValue"));
        assertEquals(properties, writeableCommandLine.getProperty(option, "property", null));
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", properties);
        Properties returnedProperties = writeableCommandLine.getProperty(option, "property");
        assertSame(properties, returnedProperties);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        Properties properties = new Properties();
        properties.setProperty("key1", "value1");
        properties.setProperty("key2", "value2");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", properties);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        Set returnedPropertiesSet = new HashSet(properties.keySet());
        assertEquals(returnedPropertiesSet, propertiesSet);

        Set emptyPropertiesSet = writeableCommandLine.getProperties();
        assertTrue(emptyPropertiesSet.isEmpty());
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        Set prefixes = writeableCommandLine.getPrefixes();
        assertTrue(writeableCommandLine.looksLikeOption("opt"));
        assertTrue(writeableCommandLine.looksLikeOption("--opt"));
        assertTrue(writeableCommandLine.looksLikeOption("--no-opt"));
        assertFalse(writeableCommandLine.looksLikeOption("opt-"));
        assertFalse(writeableCommandLine.looksLikeOption("opt--"));
        assertFalse(writeableCommandLine.looksLikeOption(""));
        assertFalse(writeableCommandLine.looksLikeOption("arg"));
        assertFalse(writeableCommandLine.looksLikeOption("opt arg"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertEquals("--arg", writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = new Option("arg1", "argument1", false, "Description 1");
        Option option2 = new Option("arg2", "argument2", false, "Description 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        assertEquals(2, writeableCommandLine.getOptions().size());
        List optionsList = writeableCommandLine.getOptions();
        assertFalse(optionsList.contains(new Option("arg3", "argument3", false, "Another description")));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = new Option("arg1", "argument1", false, "Description 1");
        Option option2 = new Option("arg2", "argument2", false, "Description 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("arg1"));
        assertTrue(optionTriggers.contains("arg2"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("arg", "argument", true, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        List defaultValues = Arrays.asList("value1", "value2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertArrayEquals(new Object[]{"value1", "value2"}, values.toArray());
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, null) == Boolean.TRUE);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option = new Option("arg", "argument", false, "Description");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        List arguments = Arrays.asList("arg1", "arg2");
        writeableCommandLine.setNormalised(arguments);
        List normalisedList = writeableCommandLine.getNormalised();
        assertEquals(2, normalisedList.size());
        assertArrayEquals(new Object[]{"arg1", "arg2"}, normalisedList.toArray());
    }

}