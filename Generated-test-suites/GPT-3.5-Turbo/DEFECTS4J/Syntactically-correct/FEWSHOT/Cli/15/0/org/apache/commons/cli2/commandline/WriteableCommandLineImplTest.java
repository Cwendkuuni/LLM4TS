package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.BeforeClass;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    private static Option option;
    private static Argument argument;
    private static WriteableCommandLineImpl commandLine;

    @BeforeClass
    public static void setUp() {
        option = new Option("test");
        argument = new Argument();
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
        assertEquals(0, commandLine.getValues(option, null).size());
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(1, commandLine.getValues(option, null).size());
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        boolean value = true;
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
        assertNull(commandLine.getOption("test"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        assertEquals(0, commandLine.getValues(option, null).size());
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        assertEquals(0, commandLine.getUndefaultedValues(option).size());
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        assertNull(commandLine.getProperty("property"));
        String value = "value";
        commandLine.addProperty(option, "property", value);
        assertEquals(value, commandLine.getProperty(option, "property", null));
    }

    @Test
    public void testAddProperty() {
        assertNull(commandLine.getProperty(option, "property", null));
        String value = "value";
        commandLine.addProperty(option, "property", value);
        assertEquals(value, commandLine.getProperty(option, "property", null));
    }

    @Test
    public void testGetProperties() {
        assertEquals(0, commandLine.getProperties(option).size());
        commandLine.addProperty(option, "property", "value");
        assertEquals(1, commandLine.getProperties(option).size());
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("arg"));
        assertTrue(commandLine.looksLikeOption("-arg"));
    }

    @Test
    public void testToString() {
        String arg = "arg";
        commandLine.addOption(option);
        commandLine.addValue(argument, arg);
        assertEquals(arg, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(0, commandLine.getOptions().size());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(0, commandLine.getOptionTriggers().size());
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains("test"));
    }

    @Test
    public void testSetDefaultValues() {
        assertNull(commandLine.getValues(option, null));
        commandLine.setDefaultValues(option, null);
        assertTrue(commandLine.getValues(option, null).isEmpty());
        commandLine.setDefaultValues(option, Collections.singletonList("value"));
        assertEquals(1, commandLine.getValues(option, null).size());
    }

    @Test
    public void testSetDefaultSwitch() {
        assertNull(commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(0, commandLine.getNormalised().size());
        commandLine.addValue(argument, "value");
        assertEquals(1, commandLine.getNormalised().size());
    }
}