package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    private static final Option OPTION_ONE = new Option("o", "option-one", true, "an option with a value");
    private static final Option OPTION_TWO = new Option("t", "option-two", false, "an option without a value");
    private static final Option ARGUMENT_ONE = new Argument("a", "an argument with a value");
    private static final Option ARGUMENT_TWO = new Argument("b", "an argument without a value");
    private static final Option PROPERTY_OPTION = new PropertyOption("p", "property", "default value", "a property");

    @Test
    public void testNewInstance() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
        assertNull(commandLine.toString());
    }

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        commandLine.addOption(OPTION_TWO);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(OPTION_ONE, commandLine.getOptions().get(0));
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertEquals(OPTION_ONE.getPreferredName(), commandLine.getOptionTriggers().iterator().next());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        commandLine.addOption(OPTION_ONE);
        commandLine.addValue(OPTION_ONE, "value-one");

        List values = commandLine.getValues(OPTION_ONE, null);
        assertEquals(1, values.size());
        assertEquals("value-one", values.get(0));

        commandLine.addValue(OPTION_ONE, "value-two");
        values = commandLine.getValues(OPTION_ONE, null);
        assertEquals(2, values.size());
        assertEquals("value-one", values.get(0));
        assertEquals("value-two", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        commandLine.addOption(OPTION_ONE);
        commandLine.addSwitch(OPTION_ONE, true);

        Boolean switchValue = commandLine.getSwitch(OPTION_ONE, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        commandLine.addOption(OPTION_ONE);
        commandLine.addSwitch(OPTION_ONE, true);
        commandLine.addSwitch(OPTION_One, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        commandLine.addOption(OPTION_ONE);

        assertTrue(commandLine.hasOption(OPTION_ONE));
        assertFalse(commandLine.hasOption(OPTION_TWO));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        commandLine.addOption(OPTION_ONE);

        Option option = commandLine.getOption(OPTION_ONE.getPreferredName());
        assertSame(OPTION_ONE, option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Arrays.asList("value-one", "value-two"));

        List values = commandLine.getValues(OPTION_ONE, null);
        assertEquals(2, values.size());
        assertEquals("value-one", values.get(0));
        assertEquals("value-two", values.get(1));

        values = commandLine.getValues(OPTION_ONE, Arrays.asList("value-three"));
        assertEquals(3, values.size());
        assertEquals("value-one", values.get(0));
        assertEquals("value-two", values.get(1));
        assertEquals("value-three", values.get(2));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Arrays.asList("value-one", "value-two"));

        List values = commandLine.getUndefaultedValues(OPTION_ONE);
        assertEquals(1, values.size());
        assertEquals("value-one", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        commandLine.addOption(OPTION_ONE);
        commandLine.addSwitch(OPTION_ONE, true);

        Boolean switchValue = commandLine.getSwitch(OPTION_ONE, false);
        assertTrue(switchValue);

        switchValue = commandLine.getSwitch(OPTION_ONE, null);
        assertTrue(switchValue);

        switchValue = commandLine.getSwitch(OPTION_TWO, null);
        assertNull(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(PROPERTY_OPTION, Collections.emptyList());
        commandLine.addOption(PROPERTY_OPTION);
        commandLine.addProperty(PROPERTY_OPTION, "value");

        assertEquals("value", commandLine.getProperty(PROPERTY_OPTION.getName(), "default value"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(PROPERTY_OPTION, Collections.emptyList());
        commandLine.addOption(PROPERTY_OPTION);

        Properties properties = new Properties();
        properties.setProperty("key", "value");
        commandLine.addProperty(PROPERTY_OPTION, "key", properties);

        assertEquals(1, commandLine.getProperties(PROPERTY_OPTION).size());
        assertEquals("key", commandLine.getProperties(PROPERTY_OPTION).iterator().next());
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(PROPERTY_OPTION, Collections.emptyList());
        commandLine.addOption(PROPERTY_OPTION);
        commandLine.addProperty(PROPERTY_OPTION, "key1", "value1");
        commandLine.addProperty(PROPERTY_OPTION, "key2", "value2");

        Set properties = commandLine.getProperties(PROPERTY_OPTION);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("key1"));
        assertTrue(properties.contains("key2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(ARGUMENT_ONE, Arrays.asList("arg1", "arg2"));

        assertEquals("arg1 arg2", commandLine.toString());

        commandLine = new WriteableCommandLineImpl(OPTION_ONE, Arrays.asList("value-one"));
        assertEquals("option-one value-one", commandLine.toString());

        commandLine = new WriteableCommandLineImpl(OPTION_ONE, Arrays.asList("value-one", "value-two"));
        assertEquals("option-one value-one value-two", commandLine.toString());

        commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        commandLine.addSwitch(OPTION_ONE, true);
        assertEquals("option-one", commandLine.toString());

        commandLine = new WriteableCommandLineImpl(PROPERTY_OPTION, Collections.emptyList());
        commandLine.addOption(PROPERTY_OPTION);
        commandLine.addProperty(PROPERTY_OPTION, "key", "value");
        assertEquals("--p key=value", commandLine.toString());
    }

    @Test
    public void testGetOptionsAndOptionTriggers() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Arrays.asList("value-one"));
        commandLine.addOption(OPTION_TWO);

        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(OPTION_ONE));
        assertTrue(options.contains(OPTION_TWO));

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList(OPTION_ONE.getPreferredName(), OPTION_TWO.getPreferredName())));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        ArrayList defaultValues = new ArrayList();
        defaultValues.add("default-value-one");
        commandLine.setDefaultValues(OPTION_ONE, defaultValues);

        List values = commandLine.getValues(OPTION_ONE, null);
        assertEquals(1, values.size());
        assertEquals("default-value-one", values.get(0));

        commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        ArrayList defaultSwitches = new ArrayList();
        defaultSwitches.add(Boolean.TRUE);
        commandLine.setDefaultSwitch(OPTION_ONE, defaultSwitches.get(0));

        Boolean switchValue = commandLine.getSwitch(OPTION_ONE, null);
        assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(OPTION_ONE, Collections.emptyList());
        Boolean defaultSwitch1 = Boolean.TRUE;
        commandLine.setDefaultSwitch(OPTION_ONE, defaultSwitch1);
        Boolean defaultSwitch2 = Boolean.FALSE;
        commandLine.setDefaultSwitch(OPTION_ONE, defaultSwitch2);

        Boolean switchValue = commandLine.getSwitch(OPTION_ONE, null);
        assertNull(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(ARGUMENT_ONE, Arrays.asList("arg1", "arg2"));

        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains("arg1"));
        assertTrue(normalised.contains("arg2"));

        commandLine = new WriteableCommandLineImpl(OPTION_ONE, Arrays.asList("value-one"));
        normalised = commandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals("option-one", normalised.get(0));
    }
}