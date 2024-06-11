package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("option", "desc", true);
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertSame(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("option", "desc", true);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getOptionValues("option");
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("option", "desc", true);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getOptionValues("option");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("option", "desc", true);
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getOptionValues("option");
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("option", "desc", false);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.isSetOption("option"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("option", "desc", false);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = new Option("option", "desc", false);
        commandLine.setDefaultSwitch(option, true);
        assertFalse(commandLine.isSetOption("option"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption(""));
    }
}