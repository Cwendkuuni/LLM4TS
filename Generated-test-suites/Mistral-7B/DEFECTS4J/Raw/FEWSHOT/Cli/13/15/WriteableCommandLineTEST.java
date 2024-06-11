package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
    }

    @Test
    public void testAddOption() {
        Option option = mock(Option.class);
        when(option.getOpt()).thenReturn("o");

        commandLine.addOption(option);

        Option optionFromCommandLine = commandLine.getOption("o");
        assertSame(option, optionFromCommandLine);
    }

    @Test
    public void testAddValue() {
        Option option = mock(Option.class);
        when(option.getOpt()).thenReturn("o");

        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        List values = commandLine.getValuesForOption("o");
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetValuesForOption() {
        Option option = mock(Option.class);
        when(option.getOpt()).thenReturn("o");

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValuesForOption("o");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = mock(Option.class);
        when(option.getOpt()).thenReturn("o");

        List defaultValues = new ArrayList(Arrays.asList("defaultValue1", "defaultValue2"));

        commandLine.addOption(option);
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValuesForOption("o");
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = mock(Option.class);
        when(option.getOpt()).thenReturn("o");

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = mock(Option.class);
        when(option.getOpt()).thenReturn("o");

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        when(option.getOpt()).thenReturn("o");

        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");

        String property = commandLine.getProperty("property");
        assertEquals("value", property);
    }

    @Test
    public void testLooksLikeOptionFalse() {
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        Option option = mock(Option.class);
        when(option.getOpt()).thenReturn("o");

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("o"));
    }
}