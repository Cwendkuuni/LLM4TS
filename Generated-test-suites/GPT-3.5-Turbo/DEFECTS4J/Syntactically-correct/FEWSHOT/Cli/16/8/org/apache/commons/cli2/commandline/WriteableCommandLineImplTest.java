package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;

    @Before
    public void setUp() {
        option = new Option();
        argument = new Argument();
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        assertNull(commandLine.getValues(option, null));
        commandLine.addValue(option, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option, null));
        commandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("tr"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("tr"));
    }

    @Test
    public void testGetValues() {
        assertNull(commandLine.getValues(option, null));
        commandLine.addValue(option, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option, null));
        commandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
        List<String> defaultValues = Arrays.asList("default1", "default2");
        assertEquals(defaultValues, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetUndefaultedValues() {
        assertNull(commandLine.getUndefaultedValues(option));
        commandLine.addValue(option, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getUndefaultedValues(option));
        commandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property1"));
        commandLine.addProperty(new PropertyOption(), "property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));
        assertNull(commandLine.getProperty("property2", "defaultValue"));
        commandLine.addProperty(new PropertyOption(), "property2", "value2");
        assertEquals("value2", commandLine.getProperty("property2", "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Set<String> properties = commandLine.getProperties();
        assertTrue(properties.isEmpty());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testGetPropertyWithOptions() {
        Option propertyOption = new PropertyOption();
        assertNull(commandLine.getProperty(propertyOption, "property1", null));
        commandLine.addProperty(propertyOption, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(propertyOption, "property1", null));
        assertNull(commandLine.getProperty(propertyOption, "property2", "defaultValue"));
        commandLine.addProperty(propertyOption, "property2", "value2");
        assertEquals("value2", commandLine.getProperty(propertyOption, "property2", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        Option propertyOption = new PropertyOption();
        Set<String> properties = commandLine.getProperties(propertyOption);
        assertTrue(properties.isEmpty());
        commandLine.addProperty(propertyOption, "property1", "value1");
        commandLine.addProperty(propertyOption, "property2", "value2");
        properties = commandLine.getProperties(propertyOption);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("argument"));
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("---option"));
    }

    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());
        commandLine.addValue(argument, "value1");
        assertEquals("value1", commandLine.toString());
        commandLine.addValue(argument, "value2");
        assertEquals("value1 value2", commandLine.toString());
        commandLine.addValue(argument, "value with spaces");
        assertEquals("\"value1\" \"value2\" \"value with spaces\"", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertTrue(options.isEmpty());
        commandLine.addOption(option);
        options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertTrue(optionTriggers.isEmpty());
        commandLine.addOption(option);
        optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        assertEquals(arguments, commandLine.getNormalised());
    }
}