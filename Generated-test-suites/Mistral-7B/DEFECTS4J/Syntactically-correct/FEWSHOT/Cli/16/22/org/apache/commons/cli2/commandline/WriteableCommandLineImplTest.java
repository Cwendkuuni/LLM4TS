package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Test empty WriteableCommandLineImpl
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertEquals("", writeableCommandLine.toString());
        assertTrue(writeableCommandLine.getOptions().isEmpty());
        assertTrue(writeableCommandLine.getOptionTriggers().isEmpty());
        assertTrue(writeableCommandLine.getNormalised().isEmpty());

        // Test WriteableCommandLineImpl with options
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        Option option3 = new Option("opt3", "option3");
        WritableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.singletonList("arg1 arg2"));
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addOption(option3);
        assertEquals("arg1 arg2 opt1 opt2 opt3", writeableCommandLine.toString());
        assertEquals(3, writeableCommandLine.getOptions().size());
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("opt2"));
        assertTrue(optionTriggers.contains("opt3"));
        List normalised = writeableCommandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));

        // Test WriteableCommandLineImpl with properties
        Option propertyOption = new Option("prop", true, "propVal");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());
        writeableCommandLine.addProperty("key1", "value1");
        writeableCommandLine.addProperty("key2", "value2");
        Properties properties = writeableCommandLine.getProperties(propertyOption);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("key1"));
        assertEquals("value2", properties.getProperty("key2"));

        // Test WriteableCommandLineImpl with values
        Argument argument = new Argument("arg", true, "argVal");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(argument, Collections.emptyList());
        writeableCommandLine.addValue(argument, "value1");
        writeableCommandLine.addValue(argument, "value2");
        List values = writeableCommandLine.getValues(argument, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        // Test WriteableCommandLineImpl with default values
        Argument argument = new Argument("arg", true, "argVal");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(argument, Collections.emptyList());
        List defaultValues = Collections.singletonList("defaultValue");
        writeableCommandLine.setDefaultValues(argument, defaultValues);
        List values = writeableCommandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));

        // Test WriteableCommandLineImpl with switches
        Option switchOption = new Option("switch", false, null);
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(switchOption, Collections.emptyList());
        writeableCommandLine.addSwitch(switchOption, true);
        Boolean switchValue = writeableCommandLine.getSwitch(switchOption, false);
        assertTrue(switchValue);

        // Test WriteableCommandLineImpl with multiple switches
        Option switchOption1 = new Option("switch1", false, null);
        Option switchOption2 = new Option("switch2", false, null);
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(switchOption1, Collections.emptyList());
        writeableCommandLine.addSwitch(switchOption1, true);
        writeableCommandLine.addSwitch(switchOption2, true);
        Map switches = writeableCommandLine.getSwitches();
        assertEquals(2, switches.size());
        assertTrue(switches.containsKey(switchOption1));
        assertTrue(switches.containsKey(switchOption2));
        assertTrue(((Boolean) switches.get(switchOption1)).booleanValue());
        assertTrue(((Boolean) switches.get(switchOption2)).booleanValue());

        // Test WriteableCommandLineImpl with default switches
        Option switchOption = new Option("switch", false, null);
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(switchOption, Collections.emptyList());
        writeableCommandLine.setDefaultSwitch(switchOption, true);
        Boolean switchValue = writeableCommandLine.getSwitch(switchOption, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImplWithProperties() {
        Option propertyOption = new Option("prop", true, "propVal");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());
        writeableCommandLine.addProperty("key1", "value1");
        writeableCommandLine.addProperty("key2", "value2");
        Properties properties = writeableCommandLine.getProperties();
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("key1"));
        assertEquals("value2", properties.getProperty("key2"));

        // Test WriteableCommandLineImpl with merged properties
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());
        writeableCommandLine.addProperty("key1", "value1");
        writeableCommandLine.addProperty("key1", "value2");
        Properties properties = writeableCommandLine.getProperties();
        assertEquals(1, properties.size());
        assertEquals("value2", properties.getProperty("key1"));
    }

    @Test
    public void testWriteableCommandLineImplWithInvalidArgument() {
        Option option = new Option("opt", false, null);
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        try {
            writeableCommandLine.addValue(option, "value");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testWriteableCommandLineImplWithNullArgument() {
        Option option = new Option("opt", false, null);
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        try {
            writeableCommandLine.addSwitch(option, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testWriteableCommandLineImplWithDuplicateOption() {
        Option option = new Option("opt", false, null);
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        try {
            writeableCommandLine.addOption(option);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testWriteableCommandLineImplWithDuplicateProperty() {
        Option propertyOption = new Option("prop", true, "propVal");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());
        writeableCommandLine.addProperty("key1", "value1");
        writeableCommandLine.addProperty("key1", "value2");
        Properties properties = writeableCommandLine.getProperties();
        assertEquals(1, properties.size());
        assertEquals("value2", properties.getProperty("key1"));
    }
}