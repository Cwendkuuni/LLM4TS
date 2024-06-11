package org.apache.commons.cli2.test;

import org.apache.commons.cli2.*;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new Cli2Parser().parse("test");
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").valueSeparator(' ').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getOptionValues("o");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetValuesForOption() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValuesForOption(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValuesForNonexistentOption() {
        Option option = Option.builder("x").longOpt("nonexistent").build();
        commandLine.getValuesForOption(option);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getOption(option).isSet());

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getOption(option).isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        List<String> values = commandLine.getValuesForOption(option);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }
}