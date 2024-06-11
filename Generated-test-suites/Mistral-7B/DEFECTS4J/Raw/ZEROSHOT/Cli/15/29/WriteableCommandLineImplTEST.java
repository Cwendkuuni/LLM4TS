package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
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
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "output directory");
        Option propertyOption = new Option("p", "profile", true, "application profile");
        propertyOption.setType(PropertyOption.TYPE_PROPERTY_LIST);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addOption(propertyOption);

        commandLine.addValue(option, "test");
        commandLine.addProperty(propertyOption, "profile", "production");

        Map<Option, Map<String, Object>> expectedOptionValues = new HashMap<>();
        Map<String, Object> expectedPropertyValues = new HashMap<>();

        expectedOptionValues.put(option, Collections.singletonList("test"));
        expectedPropertyValues.put("profile", "production");

        Map<Option, Properties> expectedOptionProperties = new HashMap<>();
        Properties expectedPropertyProperties = new Properties();
        expectedPropertyProperties.setProperty("profile", "production");

        expectedOptionProperties.put(propertyOption, expectedPropertyProperties);

        Map<Option, Map<String, List<Object>>> expectedValues = new HashMap<>();
        Map<String, List<Object>> expectedOptionValueLists = new HashMap<>();

        expectedValues.put(option, expectedOptionValueLists);
        expectedOptionValueLists.put("output", Collections.singletonList("test"));

        Map<Option, Boolean> expectedSwitches = new HashMap<>();
        expectedSwitches.put(option, Boolean.FALSE);

        Map<Option, Map<String, Object>> optionToProperties = commandLine.getOptionToProperties();
        assertEquals(expectedOptionProperties, optionToProperties);

        Map<Option, List> values = commandLine.getValues();
        assertEquals(expectedValues, values);

        Map<Option, Boolean> switches = commandLine.getSwitches();
        assertEquals(expectedSwitches, switches);

        Option optionFromCommandLine = commandLine.getOption("o");
        assertSame(option, optionFromCommandLine);

        List valuesForOption = commandLine.getValues(option, null);
        assertSame(Collections.singletonList("test"), valuesForOption);

        Option propertyOptionFromCommandLine = commandLine.getOption("p");
        assertSame(propertyOption, propertyOptionFromCommandLine);

        List propertyValues = commandLine.getValues(propertyOption, Collections.emptyList());
        assertSame(Collections.singletonList("production"), propertyValues);

        Set properties = commandLine.getProperties(propertyOption);
        assertSame(Collections.singleton("profile"), properties);

        String property = commandLine.getProperty("profile");
        assertEquals("production", property);

        List options = commandLine.getOptions();
        List expectedOptions = new ArrayList<>();
        expectedOptions.add(option);
        expectedOptions.add(propertyOption);
        assertEquals(expectedOptions, options);

        String trigger = "o";
        Option optionFromCommandLineWithTrigger = commandLine.getOption(trigger);
        assertSame(option, optionFromCommandLineWithTrigger);

        assertTrue(commandLine.looksLikeOption(trigger));

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "output test";
        assertEquals(expectedCommandLineString, commandLineString);

        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("testDir"));
        commandLine.addProperty(propertyOption, "profile", "development");
        Map<Option, Map<String, Object>> optionToProperties2 = commandLine.getOptionToProperties();
        Map<String, Object> expectedPropertyValues2 = new HashMap<>();
        expectedPropertyValues2.put("profile", "development");
        Map<Option, Properties> expectedOptionProperties2 = new HashMap<>();
        Properties expectedPropertyProperties2 = new Properties();
        expectedPropertyProperties2.setProperty("profile", "development");
        expectedOptionProperties2.put(propertyOption, expectedPropertyProperties2);
        assertEquals(expectedOptionProperties2, optionToProperties2);
    }
}