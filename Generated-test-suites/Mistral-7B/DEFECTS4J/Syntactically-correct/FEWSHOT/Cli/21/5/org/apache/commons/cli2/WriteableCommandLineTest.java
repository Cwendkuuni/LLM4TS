package org.apache.commons.cli2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        commandLine.addOption(option);
        verify(commandLine, times(1)).addOption(option);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionThrowsExceptionForInterface() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        try {
            commandLine.addOption(option);
        } finally {
            reset(commandLine);
        }
        verifyNoMoreInteractions(commandLine);
        throw new UnsupportedOperationException("WriteableCommandLine is an interface and cannot be instantiated");
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        Object value = "value";
        commandLine.addValue(option, value);
        verify(commandLine, times(1)).addValue(option, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueThrowsExceptionForInterface() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        Object value = "value";
        try {
            commandLine.addValue(option, value);
        } finally {
            reset(commandLine);
        }
        verifyNoMoreInteractions(commandLine);
        throw new UnsupportedOperationException("WriteableCommandLine is an interface and cannot be instantiated");
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        when(commandLine.getOptions()).thenReturn(new Options());
        when(commandLine.getOptions().findOption(option.getOpt()))
                .thenReturn(option);
        when(option.getValue()).thenReturn(values.toArray(new String[values.size()]));

        List result = commandLine.getUndefaultedValues(option);
        assertEquals(values, result);
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        when(commandLine.getOptions()).thenReturn(new Options());
        when(commandLine.getOptions().findOption(option.getOpt()))
                .thenReturn(option);
        when(option.getValue()).thenReturn(Collections.emptyList().toArray(new String[0]));

        List result = commandLine.getUndefaultedValues(option);
        assertTrue(result.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUndefaultedValuesThrowsExceptionForInterface() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        try {
            commandLine.getUndefaultedValues(option);
        } finally {
            reset(commandLine);
        }
        verifyNoMoreInteractions(commandLine);
        throw new UnsupportedOperationException("WriteableCommandLine is an interface and cannot be instantiated");
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        verify(commandLine, times(1)).setDefaultValues(option, defaults);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetDefaultValuesThrowsExceptionForInterface() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        List defaults = Arrays.asList("default1", "default2");
        try {
            commandLine.setDefaultValues(option, defaults);
        } finally {
            reset(commandLine);
        }
        verifyNoMoreInteractions(commandLine);
        throw new UnsupportedOperationException("WriteableCommandLine is an interface and cannot be instantiated");
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", false, "Option test");
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);
        verify(commandLine, times(1)).addSwitch(option, switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsExceptionWhenSwitchAlreadyAdded() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", false, "Option test");
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);
        doThrow(new IllegalStateException("Option switch already added")).when(commandLine).addSwitch(option, switchValue);

        try {
            commandLine.addSwitch(option, switchValue);
        } finally {
            reset(commandLine);
        }
        verify(commandLine, times(1)).addSwitch(option, switchValue);
        verify(commandLine, times(1)).addSwitch(option, switchValue);
        throw new IllegalStateException("Option switch already added");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddSwitchThrowsExceptionForInterface() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", false, "Option test");
        boolean switchValue = true;
        try {
            commandLine.addSwitch(option, switchValue);
        } finally {
            reset(commandLine);
        }
        verifyNoMoreInteractions(commandLine);
        throw new UnsupportedOperationException("WriteableCommandLine is an interface and cannot be instantiated");
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", false, "Option test");
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        verify(commandLine, times(1)).setDefaultSwitch(option, defaultSwitch);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetDefaultSwitchThrowsExceptionForInterface() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", false, "Option test");
        Boolean defaultSwitch = Boolean.TRUE;
        try {
            commandLine.setDefaultSwitch(option, defaultSwitch);
        } finally {
            reset(commandLine);
        }
        verifyNoMoreInteractions(commandLine);
        throw new UnsupportedOperationException("WriteableCommandLine is an interface and cannot be instantiated");
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        verify(commandLine, times(1)).addProperty(option, property, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddPropertyThrowsExceptionForInterface() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        String property = "property";
        String value = "value";
        try {
            commandLine.addProperty(option, property, value);
        } finally {
            reset(commandLine);
        }
        verifyNoMoreInteractions(commandLine);
        throw new UnsupportedOperationException("WriteableCommandLine is an interface and cannot be instantiated");
    }

    @Test
    public void testAddPropertyToDefaultProperties() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        verify(commandLine, times(1)).addProperty(property, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddPropertyToDefaultPropertiesThrowsExceptionForInterface() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";
        try {
            commandLine.addProperty(property, value);
        } finally {
            reset(commandLine);
        }
        verifyNoMoreInteractions(commandLine);
        throw new UnsupportedOperationException("WriteableCommandLine is an interface and cannot be instantiated");
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
        String argument = "--option";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValueTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option=value";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testGetCurrentOptionIsNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        assertNull(commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option test");
        commandLine.setCurrentOption(option);
        verify(commandLine, times(1)).setCurrentOption(option);
    }
}