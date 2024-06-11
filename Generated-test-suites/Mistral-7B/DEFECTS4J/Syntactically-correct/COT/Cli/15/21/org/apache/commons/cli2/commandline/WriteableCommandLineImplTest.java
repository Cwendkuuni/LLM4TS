package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testConstructors() {
        Option rootOption = new Option("root", "root option", true, "rootValue");
        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertNotNull(writeableCommandLine);
        assertEquals(rootOption, writeableCommandLine.getRootOption());
        assertEquals(arguments, writeableCommandLine.getArguments());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
        assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List valueList = writeableCommandLine.getValues(option, null);
        assertEquals(1, valueList.size());
        assertEquals("value1", valueList.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        Option result = writeableCommandLine.getOption(option.getPreferredName());
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List valueList = writeableCommandLine.getValues(option, null);
        assertEquals(2, valueList.size());
        assertEquals("value1", valueList.get(0));
        assertEquals("value2", valueList.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List valueList = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, valueList.size());
        assertEquals("value1", valueList.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertFalse(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testgetProperty() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        writeableCommandLine.addProperty(option, "property", properties);
        String result = writeableCommandLine.getProperty(option, "defaultValue");
        assertEquals("value", result);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addProperty(option, "property", properties);
        Map result = writeableCommandLine.getProperties(option);
        assertEquals(2, result.size());
        assertEquals("property1", result.keySet().iterator().next());
        assertEquals("value1", result.get("property1"));
        assertEquals("property2", result.keySet().iterator().next());
        assertEquals("value2", result.get("property2"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addProperty(option, "property", properties);
        Set result = writeableCommandLine.getProperties(option);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("property1", "property2")));
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));
        writeableCommandLine.addOption(option);
        String expected = "arg1 arg2 opt";
        String result = writeableCommandLine.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List result = writeableCommandLine.getOptions();
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(option1, option2)));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set result = writeableCommandLine.getOptionTriggers();
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(option1.getPreferredName(), option2.getPreferredName())));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("opt", "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }
}