package org.apache.commons.cli2;

import static org.junit.Assert.*;

import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        commandLine.addOption(option);
        verify(commandLine).addOption(option);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOptionThrowsExceptionWhenExceptionIsThrown() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        doThrow(new UnsupportedOperationException()).when(commandLine).addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        commandLine.addValue(option, "value1");
        verify(commandLine).addValue(option, "value1");
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueThrowsExceptionWhenExceptionIsThrown() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        doThrow(new IllegalStateException()).when(commandLine).addValue(option, "value1");
        commandLine.addValue(option, "value1");
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        List values = Arrays.asList("value1", "value2");
        when(commandLine.getValues(option)).thenReturn(values);
        assertEquals(values, commandLine.getValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        commandLine.addSwitch(option, true);
        verify(commandLine).addSwitch(option, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsExceptionWhenExceptionIsThrown() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        doThrow(new IllegalStateException()).when(commandLine).addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasOption(option.getLongOpt()));
        assertTrue(commandLine.isSet(option.getLongOpt()));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        commandLine.addProperty("propertyName", "propertyValue");
        assertEquals("propertyValue", commandLine.getOptionValue("propertyName"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "optionName=";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "argumentName";
        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testGetValuesEmptyList() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = new OptionBuilder("optionName").build();
        when(commandLine.getValues(option)).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), commandLine.getValues(option));
    }
}