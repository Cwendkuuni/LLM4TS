package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("--option value");
        Option option = Option.builder("option").longOpt("long-option").hasArg(true).build();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("--option value");
        Option option = Option.builder("option").longOpt("long-option").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List expected = Arrays.asList("value1");
        assertEquals(expected, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("--option value1 --option value2");
        Option option = Option.builder("option").longOpt("long-option").hasArg(true).build();
        commandLine.addOption(option);
        List expected = Arrays.asList("value1", "value2");
        assertEquals(expected, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("long-option").hasArg(true).build();
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getUndefaultedValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchIllegalStateException() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("long-option").build().setValue(false);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("long-option").build().setValue(false);
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().booleanValue());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("long-option").build().setValue(false);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getValue().booleanValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("long-option").hasArg(true).build();
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getOption("option").getValues()[0]);
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("");
        commandLine.addProperty("property", "defaultValue");
        assertEquals("defaultValue", commandLine.getOption("property").getValue());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(WriteableCommandLine.looksLikeOption("--option"));
        assertFalse(WriteableCommandLine.looksLikeOption("--option value"));
        assertFalse(WriteableCommandLine.looksLikeOption("value"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetCurrentOptionIllegalStateException() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("long-option").build();
        commandLine.setCurrentOption(option);
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("");
        Option option = Option.builder("option").longOpt("long-option").build();
        commandLine.setCurrentOption(option);
        assertSame(option, commandLine.getCurrentOption());
    }
}