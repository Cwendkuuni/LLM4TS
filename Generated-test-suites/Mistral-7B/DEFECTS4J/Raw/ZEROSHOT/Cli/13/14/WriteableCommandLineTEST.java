package org.apache.commons.cli2.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

/**
 * Test case for WriteableCommandLine class
 */
public class WriteableCommandLineTest {

    @org.junit.Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);

        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @org.junit.Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);

        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        List<String> values = commandLine.getOptionValues("o");
        assertEquals("value", values.get(0));
        assertNull(commandLine.getOptionValues("nonExistentOption"));
    }

    @org.junit.Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[]{"--option", "value1", "value2"});

        List<String> values = commandLine.getOptionValues("option");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @org.junit.Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);

        Option option = Option.builder("o").longOpt("option").build();

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getOptionValues("o");
        assertNull(values);
        assertEquals(2, commandLine.getNumDefaultValues("o"));
        assertEquals("default1", commandLine.getOptionValue("o", 0));
        assertEquals("default2", commandLine.getOptionValue("o", 1));
    }

    @org.junit.Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);

        Option option = Option.builder("s").longOpt("switch").build();

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption("s"));
        assertEquals(true, commandLine.getOptionValue("s"));
    }

    @org.junit.Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);

        Option option = Option.builder("s").longOpt("switch").build();

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.hasOption("s"));
        assertEquals(true, commandLine.getOptionValue("s", null));
    }

    @org.junit.Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[0]);

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperties().get("property"));
    }

    @org.junit.Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(-1, new String[]{"--option"});

        assertTrue(commandLine.looksLikeOption("--option"));

        WriteableCommandLine commandLine2 = new CliCommandLineParser().parse(-1, new String[]{"--value"});

        assertFalse(commandLine2.looksLikeOption("--value"));
    }
}