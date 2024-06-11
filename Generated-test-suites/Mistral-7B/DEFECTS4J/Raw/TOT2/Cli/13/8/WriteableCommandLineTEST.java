package org.apache.commons.cli2.test;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--test");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithExistingName() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--test --test");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
    }

    @Test(expected = NullPointerException.class)
    public void testAddOptionWithNullValue() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--test");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        commandLine.addValue(option, null);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--test");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValuesForOption(option).get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValuesForOptionEmpty() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--test");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        List<Object> values = commandLine.getValuesForOption(option);
        values.remove(0);
    }

    // Add test cases for other methods here

    @Test
    public void testLooksLikeOption() {
        assertTrue(WriteableCommandLine.LooksLikeOption.looksLikeOption("--test"));
        assertFalse(WriteableCommandLine.LooksLikeOption.looksLikeOption("testValue"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--test");
        Option option = Option.builder("test").build();
        List<Object> defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValuesForOption(option));
    }

    // Add test cases for other methods here

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--test");
        Option option = Option.builder("test").build();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getValueForOption(Option.Builder.withLongOpt("test").build()).isSet());
    }

    // Add test cases for other methods here

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("--test");
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getPropertyValues("property").get(0));
    }

    // Add test cases for other methods here

}