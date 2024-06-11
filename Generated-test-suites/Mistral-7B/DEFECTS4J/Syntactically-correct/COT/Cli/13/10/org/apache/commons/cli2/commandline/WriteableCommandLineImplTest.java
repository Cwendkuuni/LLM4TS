package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testConstructor() {
        Option rootOption = new Option("option", true, true, "optionValue");
        List arguments = new ArrayList<>();
        arguments.add("--optionValue");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(rootOption, writeableCommandLine.getOption("option"));
        assertEquals(arguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option", true, true, "optionValue");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption("option"));
        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option", true, true, "optionValue");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option", false, null, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, Boolean.FALSE));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("option", false, null, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", true, true, "optionValue");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", true, true, "optionValue");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option", true, true, "optionValue");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        values = writeableCommandLine.getValues(option, defaultValues);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        assertEquals(defaultValues, writeableCommandLine.getValues(option, null));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option", false, null, null);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, Boolean.FALSE));

        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option, Boolean.TRUE));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addProperty("property", "propertyValue");
        assertEquals("propertyValue", writeableCommandLine.getProperty("property", ""));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option", true, true, "optionValue");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("option", true, true, "optionValue");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, null);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        assertEquals("[option, value1, value2]", writeableCommandLine.toString());
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        Properties properties = new Properties();
        properties.setProperty("property1", "propertyValue1");
        writeableCommandLine.addProperty("property2", "propertyValue2");
        properties.setProperty("property2", "propertyValue3");
        writeableCommandLine.setProperties(properties);
        Set propertiesSet = writeableCommandLine.getProperties();
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(properties.stringPropertyNames()));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        assertTrue(writeableCommandLine.looksLikeOption("--option"));
        assertFalse(writeableCommandLine.looksLikeOption(""));
    }
}