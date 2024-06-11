package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addOption(option);

        verify(commandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = "foo";

        commandLine.addValue(option, value);

        verify(option).setValue(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueDuplicate() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = "foo";

        commandLine.addValue(option, value);
        commandLine.addValue(option, value);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<Object> values = new ArrayList<>(Arrays.asList("foo", "bar"));

        when(commandLine.getOptions()).thenReturn(Collections.singletonList(option));
        when(option.getValues()).thenReturn(values);

        List<Object> result = commandLine.getUndefaultedValues(option);

        assertEquals(values, result);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<Object> defaults = new ArrayList<>(Arrays.asList("foo", "bar"));

        commandLine.setDefaultValues(option, defaults);

        verify(option).setDefaultValues(defaults.toArray());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);

        verify(option).setValue(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);
        commandLine.addSwitch(option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        verify(option).setDefaultValue(defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "foo";
        String value = "bar";

        commandLine.addProperty(option, property, value);

        verify(option).setProperty(property, value);
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "foo";
        String value = "bar";

        commandLine.addProperty(property, value);

        verify(commandLine).setDefaultProperty(property, value);
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "arg";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--arg";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValueFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--arg value";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValueTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--arg value";

        assertTrue(commandLine.looksLikeOption(argument));
    }

}