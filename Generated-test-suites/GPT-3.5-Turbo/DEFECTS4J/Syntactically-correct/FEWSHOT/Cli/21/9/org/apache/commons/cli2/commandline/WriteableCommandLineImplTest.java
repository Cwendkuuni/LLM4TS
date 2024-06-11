package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private final Option option = new DefaultOptionBuilder().withShortName("o").create();

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl(option, null);
    }

    @Test
    public void testAddOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        final String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        final boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("o"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        final String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        final String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        final boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        final String property = "property";
        final String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        final String property = "property";
        final String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        final String property = "property";
        final String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testGetPropertiesOption() {
        final String property = "property";
        final String value = "value";
        commandLine.addProperty(option, property, value);
        assertTrue(commandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("o"));
    }

    @Test
    public void testToString() {
        final String arg = "arg";
        commandLine.addOption(option);
        commandLine.addValue(option, arg);
        assertEquals(arg, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().isEmpty());
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains("o"));
    }

    @Test
    public void testSetDefaultValues() {
        final String defaultValue = "default";
        commandLine.setDefaultValues(option, null);
        assertNull(commandLine.getValues(option, null));
        commandLine.setDefaultValues(option, Collections.singletonList(defaultValue));
        assertEquals(defaultValue, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        final boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertTrue(commandLine.getNormalised().isEmpty());
        final String arg = "arg";
        commandLine.addOption(option);
        commandLine.addValue(option, arg);
        assertTrue(commandLine.getNormalised().contains(arg));
    }
}