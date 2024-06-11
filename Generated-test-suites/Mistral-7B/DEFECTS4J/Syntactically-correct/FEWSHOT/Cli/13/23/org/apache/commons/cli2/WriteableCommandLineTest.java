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
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseContext;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();
        String value = "value";

        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getValuesForOption("opt").get(0));
    }

    @Test
    public void testGetValuesForOptionEmpty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();

        assertEquals(Arrays.asList(), commandLine.getValuesForOption(option));
    }

    @Test
    public void testGetValuesForOptionSingleValue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();
        String value = "value";

        commandLine.addValue(option, value);

        assertEquals(Arrays.asList(value), commandLine.getValuesForOption(option));
    }

    @Test
    public void testGetValuesForOptionMultipleValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();
        String value1 = "value1";
        String value2 = "value2";

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        assertEquals(Arrays.asList(value1, value2), commandLine.getValuesForOption(option));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValuesForOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchIllegalStateException() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();
        boolean value = true;

        commandLine.addSwitch(option, value);
        commandLine.addSwitch(option, value);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();
        boolean value = true;

        commandLine.addSwitch(option, value);

        assertTrue(commandLine.getOptionValues(option).getBooleanValue());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = OptionBuilder.create("opt").build();
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.getOptionValues(option).getBooleanValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getPropertyValues(property).get(0));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String argument = "opt";

        assertTrue(commandLine.looksLikeOption(argument));
    }

    private ParseContext createParseContext() {
        return new ParseContext();
    }
}