package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.annotations.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WriteableCommandLineImpl.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        option = Mockito.mock(Option.class);
    }

    @Test
    public void testAddOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(values.size(), 1);
        assertEquals(values.get(0), "value1");
    }

    @Test
    public void testAddSwitch() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("trigger");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(values.size(), 1);
        assertEquals(values.get(0), "value1");
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(values.size(), 1);
        assertEquals(values.get(0), "value1");
    }

    @Test
    public void testGetSwitch() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals(propertyValue, "value");
    }

    @Test
    public void testAddProperty() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(properties.size(), 1);
        assertEquals(properties.get("property"), "value");
    }

    @Test
    public void testGetProperties() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(properties.size(), 2);
        assertEquals(properties.get("property1"), "value1");
        assertEquals(properties.get("property2"), "value2");
    }

    @Test
    public void testToString() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        String commandLineString = commandLine.toString();
        String expected = String.format("%s value1 value2", option.getLongOpt());
        assertEquals(expected, commandLineString);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.parse("invalidOption");
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        option.setRequired(true);
        commandLine.parse(Collections.emptyList());
    }

    @Test
    public void testParseValidOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.parse(Collections.singletonList(option.getLongOpt()));
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testParseValidOptionWithValue() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.parse(Collections.singletonList(option.getLongOpt() + " value"));
        List values = commandLine.getValues(option, null);
        assertEquals(values.size(), 1);
        assertEquals(values.get(0), "value");
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testParseValidOptionWithSwitch() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.parse(Collections.singletonList(option.getLongOpt() + ":"));
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testParseValidOptionWithProperty() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.parse(Collections.singletonList(option.getLongOpt() + "[property=value]"));
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals(propertyValue, "value");
        assertTrue(commandLine.hasOption(option));
    }
}