package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option rootOption;
    private Option option1;
    private Option option2;
    private List arguments;

    @Before
    public void setUp() {
        rootOption = new Option("root", "The root option");
        option1 = new Option("option1", "The first option");
        option2 = new Option("option2", "The second option");
        arguments = new ArrayList();
    }

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addOption(option1);

        assertTrue(commandLine.getOptions().contains(option1));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        String value = "value1";
        commandLine.addValue(option1, value);

        assertTrue(commandLine.getValues(option1, Collections.emptyList()).contains(value));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addOption(option1);

        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addOption(option1);

        assertEquals(option1, commandLine.getOption(option1.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option1, value1);
        commandLine.addValue(option1, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        assertEquals(expectedValues, commandLine.getValues(option1, Collections.emptyList()));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option1, value1);
        commandLine.addValue(option1, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        assertEquals(expectedValues, commandLine.getUndefaultedValues(option1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        Option propertyOption = new PropertyOption();

        String testKey = "testKey";
        String testValue = "testValue";

        commandLine.addProperty(propertyOption, testKey, testValue);

        assertEquals(testValue, commandLine.getProperty(propertyOption, testKey, ""));

        assertEquals("", commandLine.getProperty(propertyOption, "nonExistingKey", ""));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        Option propertyOption = new PropertyOption();

        String testKey = "testKey";
        String testValue = "testValue";

        commandLine.addProperty(propertyOption, testKey, testValue);

        Set<String> properties = commandLine.getProperties(propertyOption);

        assertTrue(properties.contains(testKey));
        assertEquals(1, properties.size());
        assertTrue(properties.containsAll(Collections.singleton(testKey)));
    }

    @Test
    public void testGetPropertyWithNonExistingKey() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        Option propertyOption = new PropertyOption();

        assertNull(commandLine.getProperty(propertyOption, "nonExistingKey", null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        Option propertyOption = new PropertyOption();

        String testKey1 = "testKey1";
        String testValue1 = "testValue1";
        commandLine.addProperty(propertyOption, testKey1, testValue1);

        String testKey2 = "testKey2";
        String testValue2 = "testValue2";
        commandLine.addProperty(propertyOption, testKey2, testValue2);

        Set<String> properties = commandLine.getProperties(propertyOption);

        assertTrue(properties.contains(testKey1));
        assertTrue(properties.contains(testKey2));
        assertEquals(2, properties.size());
        assertTrue(properties.containsAll(Collections.singleton(testKey1)));
        assertTrue(properties.containsAll(Collections.singleton(testKey2)));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        String arg1 = "arg1";
        commandLine.getNormalised().add(arg1);

        assertEquals(arg1, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> expectedTriggers = new HashSet<>();
        expectedTriggers.add(option1.getPreferredName());
        expectedTriggers.add(option2.getPreferredName());

        assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        commandLine.setDefaultValues(option1, defaults);

        assertEquals(defaults, commandLine.getValues(option1, Collections.emptyList()));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        commandLine.setDefaultSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        String arg1 = "arg1";
        commandLine.getNormalised().add(arg1);

        List<String> expectedNormalised = new ArrayList<>();
        expectedNormalised.add(arg1);

        assertEquals(expectedNormalised, commandLine.getNormalised());
    }
}