package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Test empty WriteableCommandLine
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertEquals("", commandLine.toString());
        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        assertFalse(commandLine.hasOption(new Option()));

        // Test WriteableCommandLine with options and values
        Option option = new Option("o", "opt", true, "Option value");
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        commandLine = new WriteableCommandLineImpl(option, values);
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        Option optionTest = (Option) options.get(0);
        assertEquals("opt", optionTest.getLongOpt());
        assertEquals("o", optionTest.getOpt());
        assertEquals(true, optionTest.isRequired());
        assertEquals("Option value", optionTest.getDescription());
        List valuesTest = commandLine.getValues(option, null);
        assertEquals(2, valuesTest.size());
        assertEquals("value1", valuesTest.get(0));
        assertEquals("value2", valuesTest.get(1));

        // Test WriteableCommandLine with switch
        Option switchOption = new Option("s", "switch", false, "Switch is on");
        commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addSwitch(switchOption, true);
        assertTrue(commandLine.getSwitch(switchOption, false));

        // Test WriteableCommandLine with property
        Option propertyOption = new Option("p", "property", true, "Property value");
        propertyOption.setArgName("propertyName");
        commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty(propertyOption, "property1", "value1");
        Map properties = commandLine.getProperties(propertyOption);
        assertEquals(1, properties.size());
        assertEquals("value1", properties.get("propertyName"));

        // Test WriteableCommandLine with multiple options and values
        Option option2 = new Option("i", "input", true, "Input file");
        commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addOption(option2);
        commandLine.addValue(option, "value3");
        commandLine.addValue(option2, "input.txt");
        options = commandLine.getOptions();
        assertEquals(2, options.size());
        optionTest = (Option) options.get(0);
        assertNotEquals(option2, optionTest);
        optionTest = (Option) options.get(1);
        assertEquals("input", optionTest.getLongOpt());
        assertEquals("i", optionTest.getOpt());
        assertFalse(optionTest.isRequired());
        assertEquals("Input file", optionTest.getDescription());
        valuesTest = commandLine.getValues(option, null);
        assertEquals(1, valuesTest.size());
        assertEquals("value3", valuesTest.get(0));
        valuesTest = commandLine.getValues(option2, null);
        assertEquals(1, valuesTest.size());
        assertEquals("input.txt", valuesTest.get(0));
    }
}