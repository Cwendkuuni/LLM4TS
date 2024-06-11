package org.apache.commons.cli2;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("o");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("o"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("o");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option).size());
        assertTrue(commandLine.getValues(option).contains("value1"));
        assertTrue(commandLine.getValues(option).contains("value2"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("o");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.getUndefaultedValues(option).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("o");
        commandLine.setDefaultValues(option, List.of("default1", "default2"));
        assertTrue(commandLine.getValues(option).contains("default1"));
        assertTrue(commandLine.getValues(option).contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("o");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchState(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_Exception() {
        Option option = new OptionImpl("o");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("o");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitchState(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("o");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals("value1", commandLine.getProperty(option, "property1"));
        assertEquals("value2", commandLine.getProperty(option, "property2"));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals("value1", commandLine.getProperty(null, "property1"));
        assertEquals("value2", commandLine.getProperty(null, "property2"));
    }

    @Test
    public void testLooksLikeOption_True() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-option"));
    }

    @Test
    public void testLooksLikeOption_False() {
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption("123"));
        assertFalse(commandLine.looksLikeOption(""));
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new OptionImpl("o");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }
}