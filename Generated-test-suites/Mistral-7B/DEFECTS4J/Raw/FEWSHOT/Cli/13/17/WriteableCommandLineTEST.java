package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

        when(option.getOpt()).thenReturn("a");

        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOption() {
        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        Option option = mock(Option.class);
        Object value = "value";

        when(option.getValue()).thenReturn(Arrays.asList("value1", "value2"));

        commandLine.addValue(option, value);

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals(value, commandLine.getValues(option).get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullOption() {
        Option option = mock(Option.class);
        Object value = "value";

        commandLine.addValue(null, value);
    }

    @Test
    public void testGetValues() {
        Option option = mock(Option.class);

        when(option.getValue()).thenReturn(Arrays.asList("value1", "value2"));

        commandLine.addOption(option);
        commandLine.addValue(option, "value3");

        List<Object> values = commandLine.getValues(option);

        assertEquals(3, values.size());
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = mock(Option.class);
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("defaultValue1", commandLine.getValues(option).get(0));
        assertEquals("defaultValue2", commandLine.getValues(option).get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNullOption() {
        Option option = mock(Option.class);
        List<Object> defaultValues = new ArrayList<>();

        commandLine.setDefaultValues(null, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = mock(Option.class);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.isSet(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = mock(Option.class);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.isSet(option, defaultSwitch));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultSwitchNullOption() {
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(null, defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getPropertyValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }
}