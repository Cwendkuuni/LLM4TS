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

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionThrowsException() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = new Object();

        commandLine.addValue(option, value);

        assertTrue(option.getValue().contains(value));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueThrowsException() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addValue(null, null);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<String> values = new ArrayList<>(Arrays.asList("value1", "value2"));

        when(commandLine.getOptions()).thenReturn(Arrays.asList(option));
        when(option.getValue()).thenReturn(values);

        List<Object> result = commandLine.getUndefaultedValues(option);

        assertSame(result, values);
    }

    @Test
    public void testGetUndefaultedValuesReturnsEmptyList() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        when(commandLine.getOptions()).thenReturn(Arrays.asList(option));
        when(option.getValue()).thenReturn(Arrays.asList());

        List<Object> result = commandLine.getUndefaultedValues(option);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<String> defaultValues = Arrays.asList("value1", "value2");

        commandLine.setDefaultValues(option, defaultValues);

        assertSame(option.getDefaultValues(), defaultValues);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);

        assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsException() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(option.isSet() ^ defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        assertTrue(option.getValues(property).contains(value));
    }

    @Test
    public void testAddPropertyForDefaultProperties() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertTrue(commandLine.getDefaultValues(property).contains(value));
    }

    @Test
    public void testLooksLikeOptionReturnsTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionReturnsFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testGetCurrentOptionReturnsNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertNull(commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.setCurrentOption(option);

        assertSame(commandLine.getCurrentOption(), option);
    }
}