package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Option rootOption = new Option("t", "test", false, "Test option");
        List arguments = new ArrayList();
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(rootOption, commandLine.getRootOption());
        assertEquals(arguments, commandLine.getArguments());
    }

    /**
     * Test addOption method.
     */
    @org.junit.Test
    public void testAddOption() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("o", "output", true, "Output file");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("o"));
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
    }

    /**
     * Test addValue method.
     */
    @org.junit.Test
    public void testAddValue() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("o", "output", true, "Output file");
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
    }

    /**
     * Test addSwitch method.
     */
    @org.junit.Test
    public void testAddSwitch() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("d", "debug", false, "Debug mode");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    /**
     * Test hasOption method.
     */
    @org.junit.Test
    public void testHasOption() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("o", "output", true, "Output file");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    /**
     * Test getOption method.
     */
    @org.junit.Test
    public void testGetOption() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("o", "output", true, "Output file");
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    /**
     * Test getValues method.
     */
    @org.junit.Test
    public void testGetValues() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("o", "output", true, "Output file");
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));

        List defaultValues = new ArrayList();
        defaultValues.add("default.txt");
        commandLine.setDefaultValues(option, defaultValues);
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertTrue(values.contains("output.txt"));
        assertTrue(values.contains("default.txt"));
    }

    /**
     * Test getUndefaultedValues method.
     */
    @org.junit.Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("o", "output", true, "Output file");
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
    }

    /**
     * Test getSwitch method.
     */
    @org.junit.Test
    public void testGetSwitch() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("d", "debug", false, "Debug mode");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));
    }

    /**
     * Test getProperty method.
     */
    @org.junit.Test
    public void testGetProperty() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option propertyOption = new PropertyOption("p", "property", true, "Property value");
        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "property.value");
        assertEquals("property.value", commandLine.getProperty("property"));
    }

    /**
     * Test addProperty method.
     */
    @org.junit.Test
    public void testAddProperty() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option propertyOption = new PropertyOption("p", "property", true, "Property value");
        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "property.value");
        Map properties = commandLine.getProperties(propertyOption);
        assertEquals(1, properties.size());
        assertEquals("property.value", properties.get("property"));

        Map properties2 = commandLine.getProperties();
        assertEquals(0, properties2.size());
    }

    /**
     * Test getProperties method.
     */
    @org.junit.Test
    public void testGetProperties() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option propertyOption = new PropertyOption("p", "property", true, "Property value");
        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "property.value1");
        commandLine.addProperty(propertyOption, "property.value2");
        Map properties = commandLine.getProperties(propertyOption);
        assertEquals(2, properties.size());
        assertTrue(properties.containsKey("property"));
        assertEquals("property.value2", properties.get("property"));

        Map properties2 = commandLine.getProperties();
        assertEquals(0, properties2.size());
    }

    /**
     * Test looksLikeOption method.
     */
    @org.junit.Test
    public void testLooksLikeOption() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        assertTrue(commandLine.looksLikeOption("t"));
        assertFalse(commandLine.looksLikeOption("o"));
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("o", "output", true, "Output file");
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        String commandLineString = commandLine.toString();
        String expected = "test -o output.txt";
        assertEquals(expected, commandLineString);
    }

    /**
     * Test getNormalised method.
     */
    @org.junit.Test
    public void testGetNormalised() {
        Option rootOption = new Option("t", "test", false, "Test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, null);
        Option option = new Option("o", "output", true, "Output file");
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("output.txt", normalised.get(1));
    }
}