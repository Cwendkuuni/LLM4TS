package org.apache.commons.cli2;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").required(false).build();
        cmd.addOption(option);

        assertEquals(1, cmd.getOptions().size());
        assertEquals("o", cmd.getOptions().get(0).getOpt());
        assertEquals("option", cmd.getOptions().get(0).getLongOpt());
        assertFalse(cmd.getOptions().get(0).isRequired());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').required(true).build();
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        assertEquals(1, cmd.getOptions().size());
        assertEquals(2, cmd.getOptionValues(option).size());
        assertEquals("value1", cmd.getOptionValues(option).get(0));
        assertEquals("value2", cmd.getOptionValues(option).get(1));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").required(false).build();
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        List<String> values = cmd.getOptionValues("o");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").required(false).build();
        cmd.addOption(option);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        cmd.setDefaultValues(option, defaultValues);

        assertEquals(2, cmd.getDefaultValues(option).size());
        assertEquals("default1", cmd.getDefaultValues(option).get(0));
        assertEquals("default2", cmd.getDefaultValues(option).get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        cmd.addOption(option);

        try {
            cmd.addSwitch(option, true);
            cmd.addSwitch(option, false);
        } catch (IllegalStateException e) {
            // expected
        }

        assertTrue(cmd.getOptionNamed("switch").isSet());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        cmd.addOption(option);

        cmd.setDefaultSwitch(option, true);

        assertTrue(cmd.getOptionNamed("switch").isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();
        cmd.addProperty("property", "value1");

        assertEquals("value1", cmd.getProperties().get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        // negative test
        assertFalse(cmd.looksLikeOption(""));

        // positive tests
        cmd.addOption(Option.builder("o").longOpt("option").build());
        assertTrue(cmd.looksLikeOption("o"));
        assertTrue(cmd.looksLikeOption("option"));
        assertTrue(cmd.looksLikeOption("--option"));
        assertTrue(cmd.looksLikeOption("-o"));
        assertTrue(cmd.looksLikeOption("-option"));
    }
}