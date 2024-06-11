package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.addOption(option);
        assertEquals(option, cmd.getOption(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        List values = cmd.getUndefaultedValues(option);
        assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.addOption(option);
        List values = cmd.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.addOption(option);
        List defaultValues = Arrays.asList("value1", "value2");
        cmd.setDefaultValues(option, defaultValues);
        List values = cmd.getUndefaultedValues(option);
        assertEquals(defaultValues, values);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.addOption(option);
        cmd.setDefaultSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.addOption(option);
        cmd.addProperty(option, "property", "value1");
        assertEquals("value1", cmd.getOption(0).getLongOpt(), "property");
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        cmd.addProperty("property", "defaultValue");
        assertEquals("defaultValue", cmd.getPropertyValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        assertTrue(cmd.looksLikeOption("--option"));
        assertTrue(cmd.looksLikeOption("-o"));
        assertFalse(cmd.looksLikeOption("arg"));
    }

    @Test
    public void testGetCurrentOptionNull() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        assertNull(cmd.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = new Option("a", "a", false, "a help message");
        cmd.setCurrentOption(option);
        assertSame(option, cmd.getCurrentOption());
    }
}