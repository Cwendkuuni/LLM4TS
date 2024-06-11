package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        assertEquals(1, writeableCommandLine.getOptions().size());
        Option optionFromMap = writeableCommandLine.getOption("opt");
        assertSame(option, optionFromMap);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        assertFalse(writeableCommandLine.getSwitch(option, false));
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        Option optionFromMap = writeableCommandLine.getOption("opt");
        assertSame(option, optionFromMap);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        List values = writeableCommandLine.getValues(option, null);
        assertNull(values);

        writeableCommandLine.addValue(option, "value1");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        values = writeableCommandLine.getValues(option, defaultValues);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("defaultValue1", values.get(2));
        assertEquals("defaultValue2", values.get(3));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        List values = writeableCommandLine.getUndefaultedValues(option);
        assertNull(values);

        writeableCommandLine.addValue(option, "value1");
        values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        assertNull(writeableCommandLine.getSwitch(option, false));
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));
        assertSame(Boolean.TRUE, writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "prop", false, "Property test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("testKey", "testValue");
        writeableCommandLine.addProperty(option, "testKey", "testValue");

        assertEquals("testValue", writeableCommandLine.getProperty("prop", "testValue"));
        assertEquals("testValue", writeableCommandLine.getProperty(option, "testKey", "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "prop", false, "Property test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("testKey1", "testValue1");
        properties.setProperty("testKey2", "testValue2");
        writeableCommandLine.addProperty(option, "testKey1", "testValue1");
        writeableCommandLine.addProperty(option, "testKey2", "testValue2");

        Map optionProperties = writeableCommandLine.getProperties(option);
        assertEquals(2, optionProperties.size());
        assertEquals("testValue1", optionProperties.get("testKey1"));
        assertEquals("testValue2", optionProperties.get("testKey2"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "prop", false, "Property test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("testKey1", "testValue1");
        properties.setProperty("testKey2", "testValue2");
        writeableCommandLine.addProperty(option, "testKey1", "testValue1");
        writeableCommandLine.addProperty(option, "testKey2", "testValue2");

        Map allProperties = writeableCommandLine.getProperties();
        assertEquals(2, allProperties.size());
        assertEquals("testValue1", allProperties.get("testKey1"));
        assertEquals("testValue2", allProperties.get("testKey2"));

        Set propertyNames = writeableCommandLine.getProperties(option).keySet();
        assertEquals(2, propertyNames.size());
        assertTrue(propertyNames.containsAll(Arrays.asList("testKey1", "testKey2")));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "opt", false, "Option test"), Collections.emptyList());
        assertTrue(writeableCommandLine.looksLikeOption("opt"));
        assertFalse(writeableCommandLine.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        writeableCommandLine.addOption(option);

        String expected = String.join(" ", Arrays.asList("arg1", "arg2", "opt"));
        assertEquals(expected, writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("o1", "o2")));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        WriteableCommandLineImpl writeableCommandLineWithDefaultValues = new WriteableCommandLineImpl(option, Arrays.asList("defaultValue1", "defaultValue2"));
        writeableCommandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));
        List defaultValues = writeableCommandLine.getValues(option, null);
        assertEquals(2, defaultValues.size());
        assertEquals("defaultValue1", defaultValues.get(0));
        assertEquals("defaultValue2", defaultValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        WriteableCommandLineImpl writeableCommandLineWithDefaultSwitch = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineWithDefaultSwitch.addSwitch(option, true);
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        writeableCommandLine.addOption(option);

        List normalised = writeableCommandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
        assertEquals("opt", normalised.get(2));
    }
}