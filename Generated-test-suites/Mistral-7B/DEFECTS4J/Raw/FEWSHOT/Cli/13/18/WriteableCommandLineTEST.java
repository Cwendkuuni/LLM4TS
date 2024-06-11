package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        Options options = mock(Options.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        Option option = new Option("o", "opt", false, "Option description");
        when(options.findOption("o")).thenReturn(option);

        commandLine.setOptions(options);

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Options options = mock(Options.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option description");
        when(options.findOption("o")).thenReturn(option);

        commandLine.setOptions(options);

        commandLine.addValue(option, "value1");

        List<Object> values = commandLine.getOptionValues("o");

        assertSame("value1", values.get(0));
        assertTrue(values.size() == 1);
    }

    @Test
    public void testGetValues() {
        Options options = mock(Options.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option description");
        when(options.findOption("o")).thenReturn(option);

        commandLine.setOptions(options);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<Object> values = commandLine.getOptionValues("o");

        assertSame("value1", values.get(0));
        assertSame("value2", values.get(1));
        assertTrue(values.size() == 2);
    }

    @Test
    public void testSetDefaultValues() {
        Options options = mock(Options.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", true, "Option description");
        when(options.findOption("o")).thenReturn(option);

        commandLine.setOptions(options);

        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getOptionValues("o");

        assertSame("defaultValue1", values.get(0));
        assertSame("defaultValue2", values.get(1));
        assertTrue(values.size() == 2);
    }

    @Test
    public void testAddSwitch() {
        Options options = mock(Options.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", false, "Option description");
        when(options.findOption("o")).thenReturn(option);

        commandLine.setOptions(options);

        commandLine.addSwitch(option, true);

        Boolean value = commandLine.getSwitch("o");
        assertTrue(value == Boolean.TRUE);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Options options = mock(Options.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", false, "Option description");
        when(options.findOption("o")).thenReturn(option);

        commandLine.setOptions(options);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Options options = mock(Options.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = new Option("o", "opt", false, "Option description");
        when(options.findOption("o")).thenReturn(option);

        commandLine.setOptions(options);

        commandLine.setDefaultSwitch(option, true);

        Boolean value = commandLine.getSwitch("o");
        assertTrue(value == Boolean.TRUE);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        commandLine.addProperty("property", "value");

        String propertyValue = commandLine.getProperty("property");

        assertSame("value", propertyValue);
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        Option option = new Option("o", "opt", false, "Option description");

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("opt"));
    }

    @Test
    public void testLooksLikeOptionWithValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        Option option = new Option("o", "opt", true, "Option description");

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("opt value"));
    }

    @Test
    public void testLooksLikeOptionWithValuesAndDash() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        Option option = new Option("o", "opt", true, "Option description");

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("opt-value"));
    }

    @Test
    public void testLooksLikeOptionWithDash() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        Option option = new Option("o", "opt", false, "Option description");

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("-o"));
    }

    @Test
    public void testLooksLikeOptionWithNoArg() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        Option option = new Option("o", null, false, "Option description");

        commandLine.addOption(option);

        assertTrue(commandLine.looksLikeOption("o"));
    }
}