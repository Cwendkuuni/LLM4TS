package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli. Argument;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.WriteableCommandLine;
import org.apache.commons.cli.WriteableCommandLineImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument arg;

    @Before
    public void setUp() {
        option = OptionBuilder.withLongOpt("testOption")
                .withDescription("Test Option")
                .create();
        arg = ArgumentBuilder.withLongOpt("testArg")
                .withDescription("Test Argument")
                .create();
    }

    @Test
    public void testAddOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(Collections.singletonMap(option.getPreferredName(), option), commandLine.getOptionTriggers());
    }

    @Test
    public void testAddValue() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList("value1"), values);
    }

    @Test
    public void testAddValueWithDefaultValues() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.setDefaultValues(option, Collections.singletonList("defaultValue1"));
        List values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList("value1"), values);
        List defaultValues = commandLine.getValues(option, Collections.emptyList());
        assertEquals(Collections.singletonList("defaultValue1"), defaultValues);
    }

    @Test
    public void testAddSwitch() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testGetOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Option result = commandLine.getOption("testOption");
        assertSame(option, result);
    }

    @Test
    public void testGetValue() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValue(option));
    }

    @Test
    public void testGetValueNotFound() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertNull(commandLine.getValue(option));
    }

    @Test
    public void testGetSwitch() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitchNotFound() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperties() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("testProperty", "testValue");
        commandLine.addProperty(option, "testProperty", "testValue");
        Map result = commandLine.getProperties(option);
        assertEquals(properties, result);
    }

    @Test
    public void testGetPropertiesEmpty() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Map result = commandLine.getProperties(option);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testToString() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        assertEquals("testOption value1", commandLine.toString());
    }

    @Test
    public void testLooksLikeOption() {
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption("testOption"));
        assertFalse(commandLine.looksLikeOption("testArg"));
    }

    @Test
    public void testNormalised() {
        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("--testOption value1", "--testArg value2", "--testOption value3"));
        List result = commandLine.getNormalised();
        List expected = new ArrayList();
        expected.add("value1");
        expected.add("value2");
        assertEquals(expected, result);
    }
}