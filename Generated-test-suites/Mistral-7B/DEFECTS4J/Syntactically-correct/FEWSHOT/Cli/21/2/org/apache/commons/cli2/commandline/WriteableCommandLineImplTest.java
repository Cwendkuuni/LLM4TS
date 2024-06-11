package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testgetProperty() {
        Option option = new Option("p", "property", true, "property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "propertyName", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperty(option, "propertyName"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("propertyName", "propertyValue");
        commandLine.addProperty(option, properties);
        assertEquals(1, commandLine.getProperties(option).size());
        assertEquals("propertyName", commandLine.getProperties(option).iterator().next());
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("propertyName1", "propertyValue1");
        properties.setProperty("propertyName2", "propertyValue2");
        commandLine.addProperty(option, properties);
        Set propertiesSet = commandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.contains("propertyName1"));
        assertTrue(propertiesSet.contains("propertyName2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSwitchAlreadySet() {
        Option option = new Option("v", "verbose", false, "verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }
}