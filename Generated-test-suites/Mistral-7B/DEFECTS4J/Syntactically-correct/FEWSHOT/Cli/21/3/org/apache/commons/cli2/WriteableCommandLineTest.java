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

        // Verify that the option has been added to the command line
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        when(commandLine.getOptions()).thenReturn(new ArrayList<>());

        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String value = "testValue";

        commandLine.addValue(option, value);

        // Verify that the value has been added to the option
        assertEquals(Arrays.asList(value), commandLine.getUndefaultedValues(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addValue(option, null);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List values = new ArrayList(Arrays.asList("value1", "value2"));

        when(commandLine.getOptions()).thenReturn(Arrays.asList(option));
        when(option.getValues()).thenReturn(values);

        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        assertEquals(Arrays.asList(), commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List defaults = Arrays.asList("defaultValue1", "defaultValue2");

        commandLine.setDefaultValues(option, defaults);

        // Verify that the defaults have been set for the option
        assertEquals(defaults, option.getDefaultValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.setDefaultValues(option, null);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);

        // Verify that the switch value has been added to the option
        assertTrue(option.isSet());
        assertSame(switchValue, option.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        commandLine.addSwitch(option, switchValue);

        try {
            commandLine.addSwitch(option, switchValue);
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        // Verify that the default switch value has been set for the option
        assertTrue(option.getDefaultValue() instanceof Boolean);
        assertEquals(defaultSwitch, ((Boolean) option.getDefaultValue()).booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultSwitchNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.setDefaultSwitch(option, null);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "propertyName";
        String value = "propertyValue";

        commandLine.addProperty(option, property, value);

        // Verify that the property has been added to the option
        assertEquals(value, option.getProperties().get(property));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "propertyName";
        String value = "propertyValue";

        commandLine.addProperty(property, value);

        // Verify that the property has been added to the command line
        assertEquals(value, commandLine.getProperties().get(property));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String property = "propertyName";
        String value = "propertyValue";

        commandLine.addProperty(null, property, value);
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOptionWithValueTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertTrue(commandLine.looksLikeOption("--option value"));
    }

    @Test
    public void testLooksLikeOptionWithValueFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertFalse(commandLine.looksLikeOption("--option value "));
    }
}