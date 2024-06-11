package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option option;
    private List arguments;

    @Before
    public void setUp() {
        option = new Option("test");
        arguments = new ArrayList();
    }
  
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Object value = "test value";
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue.equals(value));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption(option.getPreferredName());
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValuesWithDefaultValues() {
        List defaultValues = new ArrayList();
        defaultValues.add("default value");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addValue(option, "test value");
        List values = commandLine.getValues(option, defaultValues);
        assertTrue(values.contains("test value"));
        assertFalse(values.contains("default value"));
    }

    @Test
    public void testGetValuesWithoutDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addValue(option, "test value");
        List values = commandLine.getValues(option, null);
        assertTrue(values.contains("test value"));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);
        commandLine.addValue(option, "test value");
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains("test value"));
    }
}