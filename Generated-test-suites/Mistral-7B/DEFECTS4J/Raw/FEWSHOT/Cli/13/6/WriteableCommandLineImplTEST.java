package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
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
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        Option option = new Option("f", "file", false, "File");
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "Option value");
        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
        assertEquals("o", option.getPreferredName());
        assertTrue(option.isSettable());
        assertFalse(option.getValue().isPresent());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("a", "argument", true, "Argument value");
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
        assertEquals("a", option.getPreferredName());
        assertTrue(option.getValue().isPresent());
        assertEquals(Arrays.asList("value1", "value2"), option.getValue().get());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", null, true, "Switch");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
        assertEquals("s", option.getPreferredName());
        assertTrue(option.getValue().isPresent());
        assertTrue(((Boolean) option.getValue().get()).booleanValue());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("h", "help", false, "Print help message");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("l", "list", false, "Print list");
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("l");

        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("i", "input", true, "Input file");
        commandLine.addOption(option);

        commandLine.addValue(option, "file1.txt");
        commandLine.addValue(option, "file2.txt");

        List<Object> expectedValues = Arrays.asList("file1.txt", "file2.txt");

        List<Object> retrievedValues = commandLine.getValues(option, null);

        assertEquals(expectedValues, retrievedValues);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose mode");
        commandLine.addOption(option);

        Boolean expectedSwitch = Boolean.TRUE;
        commandLine.addSwitch(option, expectedSwitch);

        Boolean retrievedSwitch = commandLine.getSwitch(option, null);

        assertEquals(expectedSwitch, retrievedSwitch);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");

        Properties properties = commandLine.getProperties();

        assertEquals("value", properties.getProperty("property"));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property", "value");

        String expectedValue = "value";
        String retrievedValue = commandLine.getProperty("property", null);

        assertEquals(expectedValue, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Set<String> expectedProperties = Collections.unmodifiableSet(Arrays.asList("property1", "property2"));

        Set<String> retrievedProperties = commandLine.getProperties();

        assertEquals(expectedProperties, retrievedProperties);
    }

    @Test
    public void testLooksLikeOption() {
        commandLine.addOption(new Option("p", "prefix", false, "Prefix"));

        assertTrue(commandLine.looksLikeOption("p"));
        assertTrue(commandLine.looksLikeOption("prefix"));
        assertFalse(commandLine.looksLikeOption("postfix"));
    }

    @Test
    public void testToString() {
        commandLine.addOption(new Option("a", "argument", true, "Argument value"));

        String expectedValue = "arg1 arg2 ";

        String retrievedValue = commandLine.toString();

        assertEquals(expectedValue, retrievedValue);
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> expectedOptions = Arrays.asList(option1, option2);

        List<Option> retrievedOptions = commandLine.getOptions();

        assertEquals(expectedOptions, retrievedOptions);
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("o", "option2", false, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> expectedTriggers = Collections.unmodifiableSet(Arrays.asList("o", "option1", "option2"));

        Set<String> retrievedTriggers = commandLine.getOptionTriggers();

        assertEquals(expectedTriggers, retrievedTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("d", "default", true, "Default value");
        commandLine.addOption(option);

        commandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));

        List<Object> expectedDefaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        List<Object> retrievedDefaultValues = commandLine.getValues(option, null);

        assertEquals(expectedDefaultValues, retrievedDefaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose mode");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, Boolean.TRUE);

        Boolean expectedDefaultSwitch = Boolean.TRUE;

        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);

        assertEquals(expectedDefaultSwitch, retrievedDefaultSwitch);
    }

    @Test
    public void testGetNormalisedList() {
        Option option = new Option("n", "normalised", false, "Normalised");
        commandLine.addOption(option);

        List<String> expectedNormalised = Arrays.asList("arg1", "arg2");

        List<String> retrievedNormalised = commandLine.getNormalised();

        assertEquals(expectedNormalised, retrievedNormalised);
    }
}