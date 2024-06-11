package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("o", "output", true, "output directory");
        Option option2 = new Option("i", "input", true, "input file");
        Option option3 = new Option("h", "help", false, "print help");

        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
    }

    @Test
    public void testAddOption() {
        assertEquals(3, commandLine.getOptions().size());
        Option option4 = new Option("t", "test");
        commandLine.addOption(option4);
        assertEquals(4, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option1);
        commandLine.addValue(option1, "/test");
        List values = commandLine.getValues(option1, null);
        assertEquals("/test", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option4 = new Option("d", "debug");
        commandLine.addOption(option4);
        assertFalse(commandLine.getSwitch(option4, false));
        commandLine.addSwitch(option4, true);
        assertTrue(commandLine.getSwitch(option4, false));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(new Option("x", "x")));
    }

    @Test
    public void testGetOption() {
        Option getOption = commandLine.getOption("o");
        assertEquals(option1, getOption);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "/test");
        List values = commandLine.getValues(option1, null);
        assertEquals("/test", values.get(0));

        commandLine.addValue(option1, "/output");
        values = commandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("/test", values.get(0));
        assertEquals("/output", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option4 = new Option("d", "debug");
        commandLine.addOption(option4);
        commandLine.addSwitch(option4, true);
        commandLine.addSwitch(option4, false);
    }

    @Test
    public void testGetSwitch() {
        Option option4 = new Option("d", "debug");
        commandLine.addOption(option4);
        assertNull(commandLine.getSwitch(option4, false));
        commandLine.addSwitch(option4, true);
        assertTrue(commandLine.getSwitch(option4, false));
    }

    @Test
    public void testGetProperty() {
        commandLine.addOption(new Option("p", "property", true, "property value"));
        commandLine.addProperty(option1, "output.directory", "/test");
        assertEquals("/test", commandLine.getProperty("output.directory"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addOption(new Option("p", "property", true, "property value"));
        commandLine.addProperty(option1, "output.directory", "/test");
        Map<String, String> expected = new HashMap<>();
        expected.put("output.directory", "/test");
        assertEquals(expected, commandLine.getProperties(option1));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option1, "/test");
        commandLine.addValue(option2, "test.txt");
        assertEquals("--output /test --input test.txt", commandLine.toString());
    }
}