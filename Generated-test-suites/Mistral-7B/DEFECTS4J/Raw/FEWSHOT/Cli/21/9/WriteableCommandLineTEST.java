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

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        option = mock(Option.class);
        commandLine = mock(WriteableCommandLine.class);
    }

    @Test
    public void testAddOption() {
        when(commandLine.addOption(option)).thenReturn(commandLine);

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOption() {
        when(commandLine.addOption(null)).thenCallRealMethod();

        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        List values = new ArrayList<>();
        values.add("value");

        when(option.getValue()).thenReturn(values);
        when(commandLine.addValue(option, "valueToAdd"))
                .thenReturn(commandLine);

        List<Object> valuesAfterAdd = commandLine.getValues(option);

        assertTrue(valuesAfterAdd.contains("valueToAdd"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullOption() {
        when(commandLine.addValue(null, "valueToAdd")).thenCallRealMethod();

        commandLine.addValue(null, "valueToAdd");
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        when(option.getValue()).thenReturn(values);

        List<Object> valuesFromCommandLine = commandLine.getUndefaultedValues(option);

        assertEquals(values, valuesFromCommandLine);
    }

    @Test
    public void testGetUndefaultedValuesEmptyList() {
        when(option.getValue()).thenReturn(new ArrayList<>());

        List<Object> valuesFromCommandLine = commandLine.getUndefaultedValues(option);

        assertTrue(valuesFromCommandLine.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        List defaults = Arrays.asList("defaultValue1", "defaultValue2");

        when(commandLine.setDefaultValues(option, defaults)).thenCallRealMethod();

        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, option.getDefaultValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNullOption() {
        when(commandLine.setDefaultValues(null, defaults)).thenCallRealMethod();

        commandLine.setDefaultValues(null, defaults);
    }

    @Test
    public void testAddSwitch() {
        when(commandLine.addSwitch(option, true)).thenThrow(new IllegalStateException("Switch already added"));

        try {
            commandLine.addSwitch(option, true);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().equals("Switch already added"));
        }

        assertFalse(commandLine.getOptions().get(0).isDefault());
    }

    @Test
    public void testAddSwitchNullOption() {
        when(commandLine.addSwitch(null, true)).thenCallRealMethod();

        commandLine.addSwitch(null, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        when(commandLine.setDefaultSwitch(option, true)).thenCallRealMethod();

        commandLine.setDefaultSwitch(option, true);

        assertTrue(option.isDefault());
    }

    @Test
    public void testAddProperty() {
        when(commandLine.addProperty(option, "property", "value")).thenCallRealMethod();

        commandLine.addProperty(option, "property", "value");

        assertEquals("value", option.getProperties().get("property"));
    }

    @Test
    public void testAddPropertyNullOption() {
        when(commandLine.addProperty(null, "property", "value"))
                .thenCallRealMethod();

        commandLine.addProperty(null, "property", "value");
    }

    @Test
    public void testAddPropertyDefaultCommandLine() {
        when(commandLine.addProperty("property", "value")).thenCallRealMethod();

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperties().get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }
}