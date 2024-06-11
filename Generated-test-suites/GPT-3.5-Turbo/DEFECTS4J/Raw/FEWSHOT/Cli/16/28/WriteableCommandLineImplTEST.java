package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);

        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("invalidOption")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("option"));
        assertNull(commandLine.getOption("invalidOption"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, null);

        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
        assertTrue(commandLine.getValues(option, null).isEmpty());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getUndefaultedValues(option);

        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
        assertTrue(commandLine.getUndefaultedValues(option).isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
        assertFalse(commandLine.getSwitch(new Option("invalidOption"), false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, "property", null));
        assertNull(commandLine.getProperty(option, "invalidProperty", null));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, "property", null));
        assertTrue(commandLine.getProperties(option).contains("property"));
        assertFalse(commandLine.getProperties().contains("property"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.setDefaultValues(option, new ArrayList<String>());

        assertTrue(commandLine.getValues(option, null).isEmpty());

        commandLine.setDefaultValues(option, null);
        assertTrue(commandLine.getValues(option, null).isEmpty());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
        List<String> normalised = commandLine.getNormalised();

        assertTrue(normalised.isEmpty());
    }
}