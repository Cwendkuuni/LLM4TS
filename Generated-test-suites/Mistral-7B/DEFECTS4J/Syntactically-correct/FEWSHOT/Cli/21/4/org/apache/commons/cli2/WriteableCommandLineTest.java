package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
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

        assertEquals(option, commandLine.getParsedOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = "foo";

        commandLine.addValue(option, value);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(value, values.get(0));
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        List values = commandLine.getUndefaultedValues(option);

        assertEquals(Arrays.asList(), values);
    }

    @Test
    public void testGetUndefaultedValuesWithValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value1 = "value1";
        Object value2 = "value2";

        when(commandLine.getParsedOptions()).thenReturn(Arrays.asList(option));
        when(option.getValues()).thenReturn(Arrays.asList(value1, value2));

        List values = commandLine.getUndefaultedValues(option);

        assertEquals(Arrays.asList(value1, value2), values);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUndefaultedValuesThrowsException() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.getUndefaultedValues(option);
    }

    @Test
    public void testSetDefaultValuesEmpty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List defaultValues = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getParsedOptions().get(0).getDefaultValues());
    }

    @Test
    public void testSetDefaultValuesWithValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List defaultValues = Arrays.asList("default1", "default2");
        Option parsedOption = mock(Option.class);

        when(commandLine.getParsedOptions()).thenReturn(Arrays.asList(parsedOption));
        when(parsedOption.getDefaultValues()).thenReturn(Arrays.asList());

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, parsedOption.getDefaultValues());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);

        assertEquals(switchValue, commandLine.getParsedOptions().get(0).getValues().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsException() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);
        commandLine.addSwitch(option, switchValue);
    }

    @Test
    public void testSetDefaultSwitchTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);        Option option = mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getParsedOptions().get(0).getDefaultValue());
    }

    @Test
    public void testSetDefaultSwitchFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);        Option option = mock(Option.class);
        Boolean defaultSwitch = false;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertNull(commandLine.getParsedOptions().get(0).getDefaultValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        assertEquals(property, commandLine.getParsedOptions().get(0).getPropertyValues().get(0).getKey());
        assertEquals(value, commandLine.getParsedOptions().get(0).getPropertyValues().get(0).getValue());
    }

    @Test
    public void testAddPropertyToDefaultProperties() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(property, commandLine.getParsedProperties().get(0).getKey());
        assertEquals(value, commandLine.getParsedProperties().get(0).getValue());
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testCurrentOptionNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertNull(commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.setCurrentOption(option);

        assertSame(option, commandLine.getCurrentOption());
    }
}