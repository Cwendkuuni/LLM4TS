package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import org.junit.Test;

public class WriteableCommandLineImplTest {
    @Test
    public void testCreateWithRootOption() {
        Option rootOption = new Option("root", "rootOption", true, "Root option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        assertEquals(rootOption, commandLine.getRootOption());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddValueWithDefaultValues() {
        Option option = new Option("test", "test", false, "Test option", "default1", "default2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
        List defaultValues = commandLine.getDefaultValues(option, null);
        assertEquals(2, defaultValues.size());
        assertEquals("default1", defaultValues.get(0));
        assertEquals("default2", defaultValues.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        Option result = commandLine.getOption("test");
        assertSame(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--test"));
        commandLine.addOption(option);
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property", properties);
        Map<String, String> propertiesMap = commandLine.getProperties(option);
        assertEquals(1, propertiesMap.size());
        assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property", properties);
        Set<String> propertiesKeys = commandLine.getProperties(option).keySet();
        assertEquals(1, propertiesKeys.size());
        assertEquals("property1", propertiesKeys.iterator().next());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--test1", "--test2"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        String commandLineString = commandLine.toString();
        assertEquals("--test1 --test2", commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--test1", "--test2"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1", "test1", false, "Test option 1");
        Option option2 = new Option("test2", "test2", false, "Test option 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--test1", "--test2"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("test1"));
        assertTrue(optionTriggers.contains("test2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("--test "));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("--test", "arg1", "arg2"));
        commandLine.addOption(option);
        List<String> normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("--test", normalised.get(0));
        assertEquals("arg1", normalised.get(1));
        assertEquals("arg2", normalised.get(2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> resultDefaultValues = commandLine.getDefaultValues(option, null);
        assertEquals(defaultValues, resultDefaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "test", false, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean resultDefaultSwitch = commandLine.getSwitch(option, false);
        assertTrue(resultDefaultSwitch);
    }
}