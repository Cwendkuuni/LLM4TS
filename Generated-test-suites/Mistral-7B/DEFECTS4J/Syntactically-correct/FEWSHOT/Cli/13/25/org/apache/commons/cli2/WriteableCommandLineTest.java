package org.apache.commons.cli2;

import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionUnimplemented() {
        // This method is not implemented in the interface and will throw an exception if called
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        commandLine.addOption(option);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueUnimplemented() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        commandLine.addValue(option, "value");
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        when(commandLine.getOptions()).thenReturn(new Option[] { option });
        List<Object> values = commandLine.getValues(option);
        assertEquals(0, values.size());

        when(option.getValue()).thenReturn("value");
        commandLine.addValue(option, "value");
        values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        List<Object> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getOptionState(option).isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getPropertyValue("property"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        when(commandLine.getOptionState(mock(Option.class))).thenReturn(new Option.OptionBuilder("option").build().getDefaultParser().parse("option"));
        assertTrue(commandLine.looksLikeOption("option"));
    }
}