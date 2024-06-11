package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = mock(WriteableCommandLine.class);
    }

    @Test
    public void testAddOption() {
        Option option = mock(Option.class);
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionToUnmodifiableList() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = mock(Option.class);
        commandLine.getOptions().add(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValueToOption() {
        Option option = mock(Option.class);
        Object value = "testValue";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValuesForOption(option).get(0));
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        Option option = mock(Option.class);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertTrue(Collections.isEmpty(values));
    }

    @Test
    public void testGetUndefaultedValuesNonEmpty() {
        Option option = mock(Option.class);
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetDefaultValuesToUnmodifiableList() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = mock(Option.class);
        List<Object> values = Arrays.asList("value1", "value2");
        commandLine.getOptions().add(option);
        commandLine.setDefaultValues(option, values);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = mock(Option.class);
        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.setDefaultValues(option, values);
        assertEquals(values, commandLine.getOptionDefaults(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = mock(Option.class);
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = mock(Option.class);
        boolean value = true;
        commandLine.addSwitch(option, value);
        commandLine.addSwitch(option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(option.isDefaultValueSet());
        assertEquals(defaultSwitch, option.getDefaultValue());
    }

    @Test
    public void testAddPropertyToOption() {
        Option option = mock(Option.class);
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getPropertyValue(option, property));
    }

    @Test
    public void testAddPropertyToCommandLine() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getPropertyValue(property));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        String argument = "--test";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        String argument = "test";
        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testGetCurrentOptionNull() {
        assertNull(commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = mock(Option.class);
        commandLine.setCurrentOption(currentOption);
        assertSame(currentOption, commandLine.getCurrentOption());
    }
}