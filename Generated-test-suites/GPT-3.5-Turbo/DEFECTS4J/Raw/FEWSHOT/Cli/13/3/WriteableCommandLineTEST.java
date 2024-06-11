package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class WriteableCommandLineTest {
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new Option("f", "file", true, "file path");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("file"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new Option("d", "directory", true, "directory path");
        commandLine.addOption(option);
        commandLine.addValue(option, "/path/to/directory");
        List<String> values = new ArrayList<>();
        values.add("/path/to/directory");
        assertEquals(values, commandLine.getValues(option));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new Option("n", "number", true, "a number");
        commandLine.addOption(option);
        commandLine.addValue(option, "1");
        commandLine.addValue(option, "2");
        commandLine.addValue(option, "3");
        List<String> values = new ArrayList<>();
        values.add("1");
        values.add("2");
        values.add("3");
        assertEquals(values, commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new Option("v", "value", true, "a value");
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new Option("s", "switch", false, "a switch");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new Option("s", "switch", false, "a switch");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new Option("s", "switch", false, "a switch");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addProperty("key", "value");
        assertEquals("value", commandLine.getProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-f"));
        assertTrue(commandLine.looksLikeOption("--file"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}