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
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionThrowsExceptionForInterface() {
        // Interfaces cannot be instantiated or modified directly, causing the test to fail
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertSame(Arrays.asList("value1"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        when(commandLine.getOptions()).thenReturn(Arrays.asList(option));
        when(option.getValue()).thenReturn(new ArrayList(Arrays.asList("value1", "value2")));

        List values = commandLine.getUndefaultedValues(option);
        assertSame(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.addOption(option);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesThrowsExceptionForNullList() {
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, null);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsExceptionForAlreadySetSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value1");

        assertEquals("value1", commandLine.getPropertyValue(option, "property"));
    }

    @Test
    public void testAddPropertyForDefaultProperty() {
        commandLine.addProperty("defaultProperty", "defaultValue1");

        assertEquals("defaultValue1", commandLine.getPropertyValue("defaultProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testCurrentOption() {
        Option currentOption = mock(Option.class);
        when(commandLine.getCurrentOption()).thenReturn(currentOption);

        assertSame(currentOption, commandLine.setCurrentOption(currentOption));
    }
}