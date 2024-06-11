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

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseContext;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.withLongOpt("option").build();

        commandLine.addOption(option);

        assertSame(option, commandLine.getOptionByName("option"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.withLongOpt("option").build();
        String value = "value";

        commandLine.addOption(option);
        commandLine.addValue(option, value);

        assertSame(value, commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.withLongOpt("option").build();
        List values = new ArrayList(Arrays.asList("value1", "value2"));

        when(commandLine.getOptions()).thenReturn(Arrays.asList(option));
        when(commandLine.getValues(option)).thenReturn(values);

        List unfaultedValues = commandLine.getUndefaultedValues(option);

        assertSame(values, unfaultedValues);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.withLongOpt("option").build();
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option);

        assertSame(defaultValues, values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchIllegalStateException() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.withLongOpt("option").build();

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.withLongOpt("option").build();

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getValue(option, false, false, ParseContext.EMPTY_CONTEXT).booleanValue());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.withLongOpt("option").build();

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getValue(option, false, false, ParseContext.EMPTY_CONTEXT).booleanValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.withLongOpt("option").build();
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getValue(property, false, false, ParseContext.EMPTY_CONTEXT));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getValue(property, false, false, ParseContext.EMPTY_CONTEXT));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "--option";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testCurrentOptionNull() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        WriteableCommandLine.Option currentOption = null;

        commandLine.setCurrentOption(currentOption);

        assertNull(commandLine.getCurrentOption());
    }

    @Test
    public void testSetAndGetCurrentOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        WriteableCommandLine.Option option = mock(WriteableCommandLine.Option.class);

        commandLine.setCurrentOption(option);

        assertEquals(option, commandLine.getCurrentOption());
    }
}