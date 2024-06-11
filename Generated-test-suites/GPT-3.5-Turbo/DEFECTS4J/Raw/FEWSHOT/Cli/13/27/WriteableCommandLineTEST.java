package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandlineImpl();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption("opt1"));

        Option option2 = new Option("opt2", "option2", true, "Option 2");
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption("opt2"));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        commandLine.addValue(option1, "value1");
        List<String> values1 = commandLine.getValues(option1);
        assertEquals("value1", values1.get(0));

        Option option2 = new Option("opt2", "option2", true, "Option 2");
        commandLine.addValue(option2, "value2");
        List<String> values2 = commandLine.getValues(option2);
        assertEquals("value2", values2.get(0));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        List<String> values1 = commandLine.getValues(option1);
        assertEquals("value1", values1.get(0));
        assertEquals("value2", values1.get(1));

        Option option2 = new Option("opt2", "option2", true, "Option 2");
        commandLine.addValue(option2, "value3");
        commandLine.addValue(option2, "value4");
        List<String> values2 = commandLine.getValues(option2);
        assertEquals("value3", values2.get(0));
        assertEquals("value4", values2.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        List<String> defaultValues1 = new ArrayList<>();
        defaultValues1.add("default1");
        commandLine.setDefaultValues(option1, defaultValues1);
        List<String> values1 = commandLine.getValues(option1);
        assertEquals("default1", values1.get(0));

        Option option2 = new Option("opt2", "option2", true, "Option 2");
        List<String> defaultValues2 = new ArrayList<>();
        defaultValues2.add("default2");
        defaultValues2.add("default3");
        commandLine.setDefaultValues(option2, defaultValues2);
        List<String> values2 = commandLine.getValues(option2);
        assertEquals("default2", values2.get(0));
        assertEquals("default3", values2.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1));

        Option option2 = new Option("opt2", "option2", true, "Option 2");
        commandLine.addSwitch(option2, false);
        assertFalse(commandLine.getSwitch(option2));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithIllegalStateException() {
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1));

        Option option2 = new Option("opt2", "option2", true, "Option 2");
        commandLine.setDefaultSwitch(option2, false);
        assertFalse(commandLine.getSwitch(option2));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1"));

        commandLine.addProperty("property2", "value2");
        assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-opt"));
        assertFalse(commandLine.looksLikeOption("value"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("val"));
    }
}