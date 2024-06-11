package org.apache.commons.cli2;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Options();
        Option option = Option.builder("o").longOpt("option").desc("An option").build();
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Options();
        Option option = Option.builder("o").valueSeparator('=').longOpt("option").desc("An option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<Object> values = commandLine.getOptionValues("o");
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new Options();
        Option option = Option.builder("o").longOpt("option").desc("An option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<Object> values = commandLine.getOptionValues("o");
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Options();
        Option option = Option.builder("o").longOpt("option").desc("An option").build();
        List<Object> defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getOptionValues("o");
        assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new Options();
        Option option = Option.builder("o").longOpt("option").desc("An option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new Options();
        Option option = Option.builder("o").longOpt("option").desc("An option").build();
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().equals(Boolean.TRUE));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Options();
        Option option = Option.builder("o").longOpt("option").desc("An option").build();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getValue().equals(Boolean.TRUE));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Options();
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = new Options();
        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = new Options();
        assertFalse(commandLine.looksLikeOption("option"));
    }
}