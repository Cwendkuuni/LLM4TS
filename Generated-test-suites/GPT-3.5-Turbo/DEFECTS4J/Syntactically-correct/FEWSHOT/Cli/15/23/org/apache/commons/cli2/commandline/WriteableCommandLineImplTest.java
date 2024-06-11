package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Argument();
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Argument();
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Argument();
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getUndefaultedValues(option).contains(value));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, ""));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testGetPropertyWithoutDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        String property = "property";
        assertNull(commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        commandLine.prefixes.add("-");
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        commandLine.normalised.add("argument");
        assertEquals("argument", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.defaultValues.get(option));
        commandLine.setDefaultValues(option, Collections.singletonList(""));
        assertEquals(Collections.singletonList(""), commandLine.defaultValues.get(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        Option option = new Option();
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.defaultSwitches.get(option));
        commandLine.setDefaultSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.defaultSwitches.get(option));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), null);
        commandLine.normalised.add("argument");
        assertTrue(commandLine.getNormalised().contains("argument"));
    }
}