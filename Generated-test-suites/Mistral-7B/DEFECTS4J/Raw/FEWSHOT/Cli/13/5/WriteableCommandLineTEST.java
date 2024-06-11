package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addOption(option);

        verifyAddOption(commandLine, option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String value = "foo";

        commandLine.addValue(option, value);

        verifyAddValue(commandLine, option, value);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<String> values = new ArrayList<>(Arrays.asList("value1", "value2"));

        when(commandLine.getValues(option)).thenReturn(values);

        List<String> result = commandLine.getValues(option);

        assertEquals(values, result);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<String> defaultValues = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(option, defaultValues);

        verifySetDefaultValues(commandLine, option, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);

        verifyAddSwitch(commandLine, option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        verifySetDefaultSwitch(commandLine, option, defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption_withOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        String argument = "option";

        when(commandLine.looksLikeOption(argument)).thenReturn(true);

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOption_withoutOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    private void verifyAddOption(WriteableCommandLine commandLine, Option option) {
        commandLine.addOption(option);
    }

    private void verifyAddValue(WriteableCommandLine commandLine, Option option, String value) {
        commandLine.addValue(option, value);
    }

    private void verifySetDefaultValues(WriteableCommandLine commandLine, Option option, List<String> defaultValues) {
        commandLine.setDefaultValues(option, defaultValues);
    }

    private void verifyAddSwitch(WriteableCommandLine commandLine, Option option, boolean value) {
        commandLine.addSwitch(option, value);
    }

    private void verifySetDefaultSwitch(WriteableCommandLine commandLine, Option option, boolean defaultSwitch) {
        commandLine.setDefaultSwitch(option, defaultSwitch);
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
        } else {
            assertTrue(expected.equals(actual));
        }
    }
}