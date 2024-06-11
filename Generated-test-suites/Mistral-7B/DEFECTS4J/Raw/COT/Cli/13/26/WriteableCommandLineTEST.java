package org.apache.commons.cli2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

/**
 * WriteableCommandLine tests
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLineParser().parse("test", Arrays.asList("--option", "value1", "--option2", "value2"));
        commandLine = (WriteableCommandLine) commandLine;
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("test").longOpt("test").required(false).build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = Option.builder("test").longOpt("test").required(false).build();
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("option").longOpt("option").valueSeparator('=').valuesSeparator(',').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getOptionValues("option");
        assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testAddValueToOptionWithMultipleValues() {
        Option option = Option.builder("option").longOpt("option").valuesSeparator(',').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues("option");
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("option").longOpt("option").required(true).build();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("option");
        assertEquals(defaultValues, values);
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("test").longOpt("test").build();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.isSet("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = Option.builder("test").longOpt("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("test").longOpt("test").build();
        commandLine.setDefaultSwitch(option, true);
        assertFalse(commandLine.isSet("test"));
        assertTrue(commandLine.hasOption("test"));
        assertTrue(commandLine.getOptionValue("test") != null);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("test", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperty("test"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        assertFalse(commandLine.looksLikeOption("testValue"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        assertTrue(commandLine.looksLikeOption("--test"));
    }

}