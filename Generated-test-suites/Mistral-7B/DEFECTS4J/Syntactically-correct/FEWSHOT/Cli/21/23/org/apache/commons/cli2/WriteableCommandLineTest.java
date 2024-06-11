package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addOption(option);

        assertSame(option, commandLine.getOption(option.getOpt()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionThrowsUOE() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = new Object();

        commandLine.addValue(option, value);

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueThrowsIAE() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = new Object();

        commandLine.addValue(null, value);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value1 = new Object();
        Object value2 = new Object();
        List values = new ArrayList<>();
        values.add(value1);
        values.add(value2);

        when(commandLine.getOption(option.getOpt())).thenReturn(option);
        when(option.getValues()).thenReturn(values);

        List result = commandLine.getUndefaultedValues(option);

        assertEquals(values, result);
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        List result = commandLine.getUndefaultedValues(option);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List defaultValues = Arrays.asList(new Object(), new Object());

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesThrowsIAE() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.setDefaultValues(null, null);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);

        assertTrue(commandLine.hasOption(option.getOpt()));
        assertTrue(commandLine.getOption(option.getOpt()).isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsISE() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);
        commandLine.addSwitch(option, switchValue);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.getOption(option.getOpt()).isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getPropertyValue(option, property));
    }

    @Test
    public void testAddPropertyEmpty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "property";

        commandLine.addProperty(option, property);

        assertNull(commandLine.getPropertyValue(option, property));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getPropertyValue(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testGetCurrentOptionNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertNull(commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option currentOption = mock(Option.class);

        commandLine.setCurrentOption(currentOption);

        assertSame(currentOption, commandLine.getCurrentOption());
    }
}