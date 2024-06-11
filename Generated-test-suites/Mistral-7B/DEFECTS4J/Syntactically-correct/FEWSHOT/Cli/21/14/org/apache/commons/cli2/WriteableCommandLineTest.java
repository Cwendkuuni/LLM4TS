package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = "testValue";

        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getValuesForOption(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List values = Arrays.asList("value1", "value2");

        when(commandLine.getOptions()).thenReturn(Arrays.asList(option));
        when(option.getValues()).thenReturn(values);

        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, option.getDefaultValues());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);

        assertTrue(option.getValues().contains(switchValue));

        assertThrows(IllegalStateException.class, () -> commandLine.addSwitch(option, switchValue));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, option.getDefaultValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "testProperty";
        String value = "testValue";

        commandLine.addProperty(option, property, value);

        assertEquals(property, option.getProperties().get(property));
    }

    @Test
    public void testAddPropertyToDefault() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "testProperty";
        String value = "testValue";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getDefaultPropertyValue(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "testOption";

        assertFalse(commandLine.looksLikeOption(argument));

        Option option = mock(Option.class);
        when(option.getLongOpt()).thenReturn("testLongOpt");

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option currentOption = mock(Option.class);

        commandLine.setCurrentOption(currentOption);

        assertSame(currentOption, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option currentOption = mock(Option.class);

        commandLine.setCurrentOption(currentOption);

        assertSame(currentOption, commandLine.getCurrentOption());

        assertNull(commandLine.getCurrentOption());
    }
}