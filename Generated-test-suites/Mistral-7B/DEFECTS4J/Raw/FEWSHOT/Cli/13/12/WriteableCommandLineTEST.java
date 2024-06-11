package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = (List<String>) commandLine.getOptionValues("o");
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetOptionValues() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = (List<String>) commandLine.getOptionValues("o");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = (List<String>) commandLine.getOptionValues("o");
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getPropertyValues("property").get(0));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        assertFalse(commandLine.looksLikeOption(" argument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        commandLine.addOption(Option.builder("o").longOpt("option").build());
        assertTrue(commandLine.looksLikeOption("o"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--option value"));
        assertTrue(commandLine.looksLikeOption("-o value"));
    }
}