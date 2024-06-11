package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    private static WriteableCommandLine createTestCommandLine() {
        return new DefaultWriteableCommandLine();
    }

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = createTestCommandLine();
        Option option = new Option("a", "example", true, "description");
        cmd.addOption(option);
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = createTestCommandLine();
        Option option = new Option("a", "example", true, "description");
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine cmd = createTestCommandLine();
        Option option = new Option("a", "example", true, "description");
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        assertEquals(1, cmd.getOptions().size());
        assertEquals(2, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));
        assertEquals("value2", option.getValues().get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = createTestCommandLine();
        Option option = new Option("a", "example", true, "description");
        List<String> defaultValues = Arrays.asList("value1", "value2");
        cmd.addOption(option);
        cmd.setDefaultValues(option, defaultValues);
        assertEquals(1, cmd.getOptions().size());
        assertEquals(2, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));
        assertEquals("value2", option.getValues().get(1));
        assertTrue(option.isSetDefaultValue());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = createTestCommandLine();
        Option option = new Option("a", "example", false, "description");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertEquals(1, cmd.getOptions().size());
        assertEquals(true, option.getValues()[0]);
        assertTrue(option.isSetValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine cmd = createTestCommandLine();
        Option option = new Option("a", "example", false, "description");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = createTestCommandLine();
        Option option = new Option("a", "example", false, "description");
        cmd.addOption(option);
        cmd.setDefaultSwitch(option, true);
        assertEquals(1, cmd.getOptions().size());
        assertTrue(option.getValues()[0]);
        assertTrue(option.isSetDefaultValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = createTestCommandLine();
        cmd.addProperty("property", "value");
        assertEquals("value", cmd.getPropertyValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = createTestCommandLine();
        assertTrue(cmd.looksLikeOption("--option"));
        assertTrue(cmd.looksLikeOption("-o"));
        assertFalse(cmd.looksLikeOption("value"));
        assertFalse(cmd.looksLikeOption(""));
    }
}