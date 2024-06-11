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
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String value = "testValue";

        commandLine.addValue(option, value);

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetUndefaultedValuesSingleValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String value = "testValue";

        when(commandLine.getOptions()).thenReturn(Arrays.asList(option));
        when(commandLine.getUndefaultedValues(option)).thenReturn(new ArrayList(Arrays.asList(value)));

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() throws IllegalStateException {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getOptionValues(option).contains(true));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() throws IllegalStateException {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List defaultValues = Arrays.asList("value1", "value2");

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
        List defaults = commandLine.getDefaultValues(option);
        assertSame(defaultValues, defaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getOptionValues(option).contains(true));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertFalse(commandLine.looksLikeOption("--arg"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertTrue(commandLine.looksLikeOption("-a"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, "property"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testNullPropertyValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        assertNull(commandLine.getProperty(option, null));
    }
}