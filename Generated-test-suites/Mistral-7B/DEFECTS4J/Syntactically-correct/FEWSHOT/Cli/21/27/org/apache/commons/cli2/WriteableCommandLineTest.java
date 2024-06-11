package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
    }

    @Test
    public void testAddOption() {
        Option option = mock(Option.class);
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNull() {
        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        Option option = mock(Option.class);
        Object value = "test";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getOptionValue(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullOption() {
        commandLine.addValue(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullValue() {
        Option option = mock(Option.class);
        commandLine.addValue(option, null);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = mock(Option.class);
        when(option.getValues()).thenReturn(Arrays.asList("value1", "value2"));
        when(commandLine.getOptions()).thenReturn(Arrays.asList(option));
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        Option option = mock(Option.class);
        when(commandLine.getOptions()).thenReturn(Collections.emptyList());
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = mock(Option.class);
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, option.getDefaultValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNullOption() {
        commandLine.setDefaultValues(null, Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNullDefaultValues() {
        Option option = mock(Option.class);
        commandLine.setDefaultValues(option, null);
    }

    @Test
    public void testAddSwitch() {
        Option option = mock(Option.class);
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);
        assertTrue(commandLine.getValues(option).contains(switchValue));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = mock(Option.class);
        boolean switchValue = true;
        commandLine.addSwitch(option, switchValue);
        try {
            commandLine.addSwitch(option, switchValue);
        } catch (IllegalStateException e) {
            assertEquals("Switch already added: " + option.getLongOpt(), e.getMessage());
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getOptionSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = mock(Option.class);
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getPropertyValue(option, property));
    }

    @Test
    public void testAddPropertyDefault() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getPropertyValue(property));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        String argument = "testArgument";
        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        String argument = "--test";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testGetCurrentOptionNull() {
        assertNull(commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option option = mock(Option.class);
        commandLine.setCurrentOption(option);
        assertSame(option, commandLine.getCurrentOption());
    }

}