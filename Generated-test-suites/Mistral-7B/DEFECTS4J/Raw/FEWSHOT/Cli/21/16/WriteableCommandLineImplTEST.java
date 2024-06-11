package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("f"));
        assertEquals(option, commandLine.getOption("foo"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals("value1", values.get(0));
        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addOption(option);
        assertSame(option, commandLine.getOption("f"));
        assertSame(option, commandLine.getOption("foo"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        List defaultValues = Arrays.asList("default1", "default2");
        values = commandLine.getValues(option, defaultValues);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals(2, defaultValues.size());
        assertEquals("default1", defaultValues.get(0));
        assertEquals("default2", defaultValues.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addProperty(option, "property", "value1");
        assertEquals("value1", commandLine.getProperty(option, "value2"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property", properties);
        Map propertiesMap = commandLine.getProperties(option);
        assertEquals(1, propertiesMap.size());
        assertEquals("property1", propertiesMap.keySet().iterator().next());
        assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property", properties);
        Set propertiesSet = commandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(Arrays.asList("property1")));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("f", "foo"), Arrays.asList());
        assertTrue(commandLine.looksLikeOption("--foo"));
        assertTrue(commandLine.looksLikeOption("-f"));
        assertTrue(commandLine.looksLikeOption("--foo "));
        assertTrue(commandLine.looksLikeOption("-f "));
        assertFalse(commandLine.looksLikeOption("--bar"));
        assertFalse(commandLine.looksLikeOption("-b"));
    }

    @Test
    public void testToString() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addSwitch(option, true);
        assertEquals("arg1 arg2 --foo=\"value1 value2\"", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f1", "foo1");
        Option option2 = new Option("f2", "foo2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList());
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("f1", "foo1");
        Option option2 = new Option("f2", "foo2");
        Option option3 = new Option("f3", "foo3");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("f1", "f2", "f3")));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("f", "foo");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
        assertEquals("--foo=\"value1 value2\"", normalised.get(2).toString());
    }
}