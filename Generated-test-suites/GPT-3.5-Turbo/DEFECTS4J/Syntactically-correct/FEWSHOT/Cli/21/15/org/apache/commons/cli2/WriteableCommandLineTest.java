package org.apache.commons.cli2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        verify(commandLine, times(1)).addOption(option);
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);
        verify(commandLine, times(1)).addValue(option, value);
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.getUndefaultedValues(option);
        verify(commandLine, times(1)).getUndefaultedValues(option);
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = mock(List.class);
        commandLine.setDefaultValues(option, defaultValues);
        verify(commandLine, times(1)).setDefaultValues(option, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        verify(commandLine, times(1)).addSwitch(option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        verify(commandLine, times(1)).setDefaultSwitch(option, defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        verify(commandLine, times(1)).addProperty(option, property, value);
    }

    @Test
    public void testAddDefaultProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        verify(commandLine, times(1)).addProperty(property, value);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-f";
        commandLine.looksLikeOption(argument);
        verify(commandLine, times(1)).looksLikeOption(argument);
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = mock(Option.class);
        commandLine.setCurrentOption(currentOption);
        verify(commandLine, times(1)).setCurrentOption(currentOption);
    }
}