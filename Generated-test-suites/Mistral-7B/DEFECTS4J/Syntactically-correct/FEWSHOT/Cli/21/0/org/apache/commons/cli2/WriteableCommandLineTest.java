package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

    @Test
    public void testAddOption() {
        Option option = mock(Option.class);
        commandLine.addOption(option);
        verify(commandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = mock(Option.class);
        Object value = "testValue";
        commandLine.addValue(option, value);
        verify(option).setValue(value);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = mock(Option.class);
        when(option.getValues()).thenReturn(Arrays.asList("value1", "value2"));
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(values, Arrays.asList("value1", "value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = mock(Option.class);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        verify(option).setDefaultValues(defaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = mock(Option.class);
        boolean value = true;
        commandLine.addSwitch(option, value);
        verify(option).setValue(value);
        try {
            commandLine.addSwitch(option, true);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        verify(option).setDefaultValue(defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        Option option = mock(Option.class);
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        verify(option).addProperty(property, value);
    }

    @Test
    public void testAddPropertyDefault() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        Option option = mock(Option.class);
        when(option.getName()).thenReturn(property);
        Option currentOption = mock(Option.class);
        when(commandLine.getCurrentOption()).thenReturn(currentOption);
        commandLine.addProperty(option, property, value);
        verify(option).addProperty(property, value);
        verify(currentOption).setValue(value);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "testArgument";
        assertTrue(commandLine.looksLikeOption(argument));
        assertFalse(commandLine.looksLikeOption(""));
    }

    @Test
    public void testGetCurrentOption() {
        Option option = mock(Option.class);
        when(commandLine.getCurrentOption()).thenReturn(option);
        assertSame(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option option = mock(Option.class);
        commandLine.setCurrentOption(option);
        verify(commandLine).setCurrentOption(option);
    }

    private void assertEquals(List expected, List actual) {
        if (expected == null) {
            assertNull(actual);
        } else {
            assertEquals(expected.size(), actual.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), actual.get(i));
            }
        }
    }
}