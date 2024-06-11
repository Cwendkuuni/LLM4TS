package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        Object value = "foo";

        commandLine.addValue(option, value);

        List<Object> values = commandLine.getOptionValues(option);
        assertSame(value, values.get(0));
        assertSame(commandLine, option.getLongOption());
        assertSame(commandLine, option.getShortOption());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value1 = "value1";
        Object value2 = "value2";

        when(commandLine.getOptionValues(option)).thenReturn(Arrays.asList(value1, value2));

        List<Object> values = commandLine.getOptionValues(option);
        assertSame(value1, values.get(0));
        assertSame(value2, values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<String> defaultValues = Arrays.asList("value1", "value2");

        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getOptionValues(option);
        assertSame(defaultValues, values);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);

        assertTrue(commandLine.isSet(option));
        assertSame(value, commandLine.getBooleanValue(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertSame(defaultSwitch, commandLine.getDefaultValue(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertSame(value, commandLine.getPropertyValue(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithNoDoubleDash() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "option";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithSingleDash() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "-option";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithNoValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option value";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValueAndLongName() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--long-option value";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValueAndShortName() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "-o value";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithSingleDashAndValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "-- value";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithTwoValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option value1 value2";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithEmptyValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithEmptyValueAndLongName() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--long-option";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithEmptyValueAndShortName() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "-o";

        assertFalse(commandLine.looksLikeOption(argument));
    }
}