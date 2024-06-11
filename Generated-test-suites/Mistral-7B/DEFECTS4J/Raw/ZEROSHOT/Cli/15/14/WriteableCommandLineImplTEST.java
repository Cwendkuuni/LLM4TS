package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link WriteableCommandLineImpl}.
 */
public class WriteableCommandLineImplTest {

    private Option option;
    private List arguments;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        option = new Option("o", "option", false, "An option");
        arguments = Arrays.asList("--option", "value1", "--other", "value2");
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, Collections.emptyList());
        Option anotherOption = new Option("a", "anotherOption", false, "Another option");
        cli.addOption(anotherOption);
        Set optionTriggers = cli.getOptionTriggers();
        assertTrue(optionTriggers.contains(anotherOption.getPreferredName()));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("o");
        assertSame(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        List expectedValues = Arrays.asList("value1", "value2");
        assertSame(expectedValues, values);
    }

    @Test
    public void testToString() {
        String expectedResult = "--option=value1 value2 --other=value2";
        String result = commandLine.toString();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetSwitch() {
        assertFalse(commandLine.hasSwitch(option, false));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property1", "value1");
        Properties properties = commandLine.getProperties(option);
        Properties expectedProperties = new Properties();
        expectedProperties.setProperty("property1", "value1");
        assertSame(expectedProperties, properties);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option, "property1", "value1");
        String propertyValue = commandLine.getProperty(option, "property1", "defaultValue");
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option, "property1", "value1");
        Set<String> propertiesKeys = commandLine.getProperties(option).stringPropertyNames();
        Set<String> expectedPropertiesKeys = Collections.singleton("property1");
        assertEquals(expectedPropertiesKeys, propertiesKeys);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        commandLine.addProperty(option, "property1", "value1");
        String propertyValue = commandLine.getProperty(option, "property2", "defaultValue");
        assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        List expectedValues = Arrays.asList("value1");
        assertSame(expectedValues, values);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("--other"));
    }

    @Test
    public void testHasOptionWithLongArguments() {
        Option longOption = new Option("l", "longOption", false, "A long option");
        WriteableCommandLine commandLineWithLongOption = new WriteableCommandLineImpl(longOption, Arrays.asList("--longOption", "arg1", "--longOption", "arg2"));
        assertTrue(commandLineWithLongOption.hasOption(longOption));
    }

    @Test
    public void testGetValuesWithDefaultValues() {
        Option optionWithDefaultValue = new Option("d", "defaultValue", true, "A default option with a default value");
        commandLine.setDefaultValues(optionWithDefaultValue, Arrays.asList("defaultValue"));
        List values = commandLine.getValues(optionWithDefaultValue, null);
        List expectedValues = Arrays.asList("defaultValue");
        assertSame(expectedValues, values);
    }

    @Test
    public void testGetValuesWithNoDefaultValues() {
        commandLine.setDefaultValues(option, null);
        List values = commandLine.getValues(option, null);
        assertNull(values);
    }

    @Test
    public void testGetSwitchWithDefaultSwitch() {
        Option optionWithDefaultSwitch = new Option("d", "defaultSwitch", true, "A default option with a default switch");
        commandLine.setDefaultSwitch(optionWithDefaultSwitch, true);
        assertTrue(commandLine.getSwitch(optionWithDefaultSwitch, null));
    }

    @Test
    public void testGetSwitchWithNoDefaultSwitch() {
        commandLine.setDefaultSwitch(option, null);
        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        List expectedNormalised = Arrays.asList("--option", "value1", "value2", "--other=value2");
        assertSame(expectedNormalised, normalised);
    }
}