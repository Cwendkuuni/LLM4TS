package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        commandLine.addOption(option);

        verify(commandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        Object value = "foo";

        commandLine.addValue(option, value);

        verify(option).setValue(value);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<String> values = new ArrayList<>();
        values.add("foo");
        values.add("bar");

        when(commandLine.getValues(option)).thenReturn(values);

        List<String> result = commandLine.getValues(option);

        assertEquals(values, result);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        verify(option).setDefaultValues(defaultValues);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);

        verify(option).setValues(Collections.singletonList(value.toString()));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        verify(option).setDefaultValue(defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        verify(commandLine).addProperty(property, value);
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "foo";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--foo";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValueFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--foo=value";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValueTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--foo value";

        assertTrue(commandLine.looksLikeOption(argument));
    }
}