package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.base.Option;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("opt").valueSeparator(':').build();
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("opt").valueSeparator(':').build();
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        List<String> values = Arrays.asList(cmd.getValuesForOption("opt"));
        assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testGetValuesForOptionWithNoValues() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("opt").build();
        cmd.addOption(option);
        List<String> values = Arrays.asList(cmd.getValuesForOption("opt"));
        assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("opt").build();
        cmd.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        cmd.setDefaultValues(option, defaultValues);
        List<String> values = Arrays.asList(cmd.getValuesForOption("opt"));
        assertEquals(defaultValues, values);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("opt").build();
        try {
            cmd.addSwitch(option, true);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertTrue(option.getValues().get(0).equals(Boolean.TRUE));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("opt").build();
        cmd.addOption(option);
        cmd.setDefaultSwitch(option, true);
        assertTrue(option.getValues().get(0).equals(Boolean.TRUE));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        cmd.addProperty("prop", "value");
        assertEquals("value", cmd.getProperty("prop"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        assertTrue(cmd.looksLikeOption("--opt"));
        assertTrue(cmd.looksLikeOption("-o"));
        assertFalse(cmd.looksLikeOption("--value"));
        assertFalse(cmd.looksLikeOption("-"));
        assertNull(cmd.looksLikeOption(null));
    }
}