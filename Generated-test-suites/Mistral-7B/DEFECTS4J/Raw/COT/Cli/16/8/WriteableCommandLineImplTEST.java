package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WriteableCommandLineImpl test cases.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "option", false, "Test switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("o", "option", false, "Test switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Option returnedOption = commandLine.getOption("o");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument("arg", "Argument", true, "Test argument");
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addArgument(argument);
        commandLine.addArgument(argument);
        List values = commandLine.getValues(option, null);
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "option", false, "Test switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("o", "option", false, "Test property option");
        PropertyOption propertyOption = new PropertyOption("property", "Test property", true, "Test property description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("o", "option", false, "Test property option");
        PropertyOption propertyOption = new PropertyOption("property1", "Test property1", false, "Test property1 description");
        PropertyOption propertyOption2 = new PropertyOption("property2", "Test property2", false, "Test property2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "option value1 value2";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true, "Test option1");
        Option option2 = new Option("o2", "option2", true, "Test option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        List expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        assertEquals(expectedOptions, options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", true, "Test option1");
        Option option2 = new Option("o2", "option2", true, "Test option2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        Set expectedOptionTriggers = new HashSet();
        expectedOptionTriggers.add("o1");
        expectedOptionTriggers.add("o2");
        assertEquals(expectedOptionTriggers, optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "option", false, "Test switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "option", true, "Test option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List normalised = commandLine.getNormalised();
        List expectedNormalised = new ArrayList();
        expectedNormalised.add("value1");
        expectedNormalised.add("value2");
        assertEquals(expectedNormalised, normalised);
    }
}