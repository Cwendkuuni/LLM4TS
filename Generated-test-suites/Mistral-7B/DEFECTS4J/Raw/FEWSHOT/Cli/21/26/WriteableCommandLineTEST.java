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
        commandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
    }

    @Test
    public void testAddOption() {
        when(commandLine.addOption(option)).thenReturn(commandLine);
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOption_ThrowsException() {
        when(commandLine.addOption(option)).thenThrow(UnsupportedOperationException.class);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        List values = new ArrayList<>();
        when(option.getValues()).thenReturn(values);

        when(commandLine.addValue(option, "value")).thenReturn(commandLine);
        commandLine.addValue(option, "value");
        assertTrue(values.contains("value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValue_ThrowsException() {
        Option invalidOption = mock(Option.class);
        when(option.getValues()).thenThrow(IllegalArgumentException.class);

        when(commandLine.addValue(invalidOption, "value")).thenCallRealMethod();
        commandLine.addValue(invalidOption, "value");
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = mock(Option.class);
        List values = Arrays.asList("value1", "value2");
        when(option.getValues()).thenReturn(values);
        when(commandLine.getUndefaultedValues(option)).thenReturn(values);

        List result = commandLine.getUndefaultedValues(option);
        assertSame(values, result);
    }

    @Test
    public void testGetUndefaultedValues_EmptyList() {
        Option option = mock(Option.class);
        when(option.getValues()).thenReturn(new ArrayList<>());
        when(commandLine.getUndefaultedValues(option)).thenReturn(new ArrayList<>());

        List result = commandLine.getUndefaultedValues(option);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = mock(Option.class);
        List defaults = Arrays.asList("value1", "value2");

        when(commandLine.setDefaultValues(option, defaults)).thenReturn(commandLine);
        commandLine.setDefaultValues(option, defaults);
        assertTrue(commandLine.getDefaultValues(option).equals(defaults));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValues_ThrowsException() {
        Option option = mock(Option.class);
        when(commandLine.setDefaultValues(option, new ArrayList<>())).thenThrow(IllegalArgumentException.class);

        commandLine.setDefaultValues(option, new ArrayList<>());
    }

    @Test
    public void testAddSwitch() {
        when(commandLine.addSwitch(option, true)).thenReturn(commandLine);
        commandLine.addSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsException() {
        when(commandLine.addSwitch(option, true)).thenThrow(IllegalStateException.class);

        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        when(commandLine.setDefaultSwitch(option, true)).thenReturn(commandLine);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getDefaultValue().equals(Boolean.TRUE));
    }

    @Test
    public void testAddProperty() {
        when(commandLine.addProperty(option, "property", "value")).thenReturn(commandLine);
        commandLine.addProperty(option, "property", "value");
        assertTrue(option.getProperties().containsKey("property"));
        assertEquals("value", option.getProperties().get("property"));
    }

    @Test
    public void testAddProperty_EmptyString() {
        when(commandLine.addProperty(option, "property", "")).thenReturn(commandLine);
        commandLine.addProperty(option, "property", "");
        assertTrue(option.getProperties().containsKey("property"));
        assertEquals("", option.getProperties().get("property"));
    }

    @Test
    public void testAddProperty_Null() {
        when(commandLine.addProperty(option, "property", null)).thenReturn(commandLine);
        commandLine.addProperty(option, "property", null);
        assertTrue(option.getProperties().containsKey("property"));
        assertNull(option.getProperties().get("property"));
    }

    @Test
    public void testAddProperty_Default() {
        when(commandLine.addProperty("property", "value")).thenReturn(commandLine);
        commandLine.addProperty("property", "value");
        assertTrue(commandLine.getProperties().containsKey("property"));
        assertEquals("value", commandLine.getProperties().get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = mock(Option.class);
        when(option.getLongOpt()).thenReturn("longOpt");
        when(option.getOpt()).thenReturn('o');

        when(commandLine.looksLikeOption("longOpt")).thenReturn(true);
        when(commandLine.looksLikeOption("shortOpt")).thenReturn(false);
        when(commandLine.looksLikeOption("longOpt-arg")).thenReturn(false);

        assertTrue(commandLine.looksLikeOption("longOpt"));
        assertFalse(commandLine.looksLikeOption("shortOpt"));
        assertFalse(commandLine.looksLikeOption("longOpt-arg"));
    }

    @Test
    public void testCurrentOption() {
        Option currentOption = mock(Option.class);
        when(commandLine.getCurrentOption()).thenReturn(currentOption);

        Option newCurrentOption = mock(Option.class);
        commandLine.setCurrentOption(newCurrentOption);

        assertSame(newCurrentOption, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = mock(Option.class);
        Option newCurrentOption = mock(Option.class);

        commandLine.setCurrentOption(currentOption);
        commandLine.setCurrentOption(newCurrentOption);

        assertSame(newCurrentOption, commandLine.getCurrentOption());
        assertFalse(commandLine.getCurrentOption().equals(currentOption));
    }
}