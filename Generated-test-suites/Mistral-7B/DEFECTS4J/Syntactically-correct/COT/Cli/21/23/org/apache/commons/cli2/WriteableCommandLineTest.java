package org.apache.commons.cli2.test;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for WriteableCommandLine class
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertSame(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);

        List defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
        assertEquals(2, defaults.size());
        assertTrue(defaults.contains("default1"));
        assertTrue(defaults.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);

        try {
            commandLine.addSwitch(option, true);
            fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            commandLine.addSwitch(option, false);
            Option currentOption = commandLine.getCurrentOption();
            assertTrue(currentOption.isSet());
            assertFalse(currentOption.getValue().isPresent());
        } catch (IllegalStateException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        Option currentOption = commandLine.getCurrentOption();
        assertTrue(currentOption.isSet());
        assertTrue(currentOption.getValue().isPresent());
        assertTrue((boolean) currentOption.getValue().get());
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");
        Option currentOption = commandLine.getCurrentOption();
        assertEquals("value", currentOption.getLongOpt());
        assertEquals("value", currentOption.getValue().get());
    }

    @Test
    public void testAddPropertyDefault() {
        commandLine.addProperty("property", "defaultValue");
        String value = commandLine.getProperty("property");
        assertEquals("defaultValue", value);
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("hello"));
        commandLine.addOption(Option.builder("h").build());
        assertFalse(commandLine.looksLikeOption("hello"));
        assertTrue(commandLine.looksLikeOption("h"));
    }

    @Test
    public void testCurrentOption() {
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        commandLine.parse("option value");

        Option currentOption = commandLine.getCurrentOption();
        assertEquals(option, currentOption);
        assertEquals("value", currentOption.getValue().get());
    }

    @After
    public void tearDown() {
        commandLine = null;
    }
}