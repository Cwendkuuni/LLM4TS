package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Writer;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        when(commandLine.addOption(option)).thenReturn(commandLine);

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionThrowsExceptionWhenImmutable() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        when(commandLine.addOption(option)).thenThrow(UnsupportedOperationException.class);

        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = "test";

        when(commandLine.getOption(Option.class, "testOption")).thenReturn(option);
        when(option.getValue()).thenReturn(new ArrayList<>());

        commandLine.addValue(option, value);

        List<Object> values = option.getValue();
        assertTrue(values.contains(value));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueThrowsExceptionWhenImmutable() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = "test";

        when(commandLine.getOption(Option.class, "testOption")).thenReturn(option);
        when(option.getValue()).thenReturn(new ArrayList<>());

        commandLine.addValue(option, value);

        List<Object> values = option.getValue();
        assertTrue(values.contains(value));

        // Now, let's test the exception
        when(commandLine.addValue(option, value)).thenThrow(UnsupportedOperationException.class);

        commandLine.addValue(option, value);
    }

    @Test
    public void testGetValuesForOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value1 = "value1";
        Object value2 = "value2";

        when(commandLine.getOption(Option.class, "testOption")).thenReturn(option);
        when(option.getValue()).thenReturn(new ArrayList<>());

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<Object> values = commandLine.getValuesForOption("testOption");

        assertTrue(values.containsAll(List.of(value1, value2)));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<Object> defaultValues = List.of("default1", "default2");

        when(commandLine.getOption(Option.class, "testOption")).thenReturn(option);

        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = option.getValue();
        assertTrue(values.containsAll(defaultValues));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        when(commandLine.getOption(Option.class, "testOption")).thenReturn(option);

        commandLine.addSwitch(option, switchValue);

        assertTrue(option.isSet());
        assertTrue(option.getValue().size() == 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsExceptionWhenAlreadySet() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean switchValue = true;

        when(commandLine.getOption(Option.class, "testOption")).thenReturn(option);
        when(option.isSet()).thenReturn(true);

        commandLine.addSwitch(option, switchValue);

        // The exception should be thrown
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;

        when(commandLine.getOption(Option.class, "testOption")).thenReturn(option);

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(option.isSet());
        assertTrue(option.getValue().size() == 0);
        assertTrue(option.isValueSet());
        assertTrue(option.getValue(null).equals(defaultSwitch));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertTrue(commandLine.hasOption(property));
        assertEquals(value, commandLine.getOptionValue(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--test";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "testArg";

        assertFalse(commandLine.looksLikeOption(argument));
    }
}