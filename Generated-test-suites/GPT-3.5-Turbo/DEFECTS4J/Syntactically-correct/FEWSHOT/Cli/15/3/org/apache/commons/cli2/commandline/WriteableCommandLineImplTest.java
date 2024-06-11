package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option option;
    private List<String> arguments;
    private WriteableCommandLineImpl cmd;

    @Before
    public void setUp() {
        option = new Option();
        arguments = new ArrayList<>();
        cmd = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
        assertTrue(cmd.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        cmd.addValue(argument, "value");
        assertTrue(cmd.hasOption(argument));
        assertFalse(cmd.hasOption(option));
        assertEquals("value", cmd.getValues(argument, null).get(0));
        assertNull(cmd.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        cmd.addSwitch(option, true);
        assertTrue(cmd.hasOption(option));
        assertEquals(Boolean.TRUE, cmd.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(cmd.hasOption(option));
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertNull(cmd.getOption("trigger"));
        cmd.addOption(option);
        assertEquals(option, cmd.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        assertEquals(Arrays.asList(), cmd.getValues(argument, null));
        cmd.addValue(argument, "value");
        assertEquals(Arrays.asList("value"), cmd.getValues(argument, null));
        assertEquals(Arrays.asList(), cmd.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        assertNull(cmd.getSwitch(option, null));
        cmd.addSwitch(option, false);
        assertEquals(Boolean.FALSE, cmd.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        assertEquals(value, cmd.getProperty(property));
        cmd.addProperty(option, property, value);
        assertEquals(value, cmd.getProperty(option, property, null));
        assertEquals(Arrays.asList(property), cmd.getProperties());
        assertEquals(Arrays.asList(property), cmd.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(cmd.looksLikeOption("arg"));
        assertTrue(cmd.looksLikeOption("-option"));
    }

    @Test
    public void testToString() {
        assertEquals("", cmd.toString());
        arguments.add("arg");
        assertEquals("arg", cmd.toString());
    }

    @Test
    public void testGetOptions() {
        assertTrue(cmd.getOptions().isEmpty());
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        assertTrue(cmd.getOptionTriggers().isEmpty());
        cmd.addOption(option);
        assertTrue(cmd.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Option defaultOption = new Option();
        List<String> defaults = Arrays.asList("default");
        cmd.setDefaultValues(defaultOption, defaults);
        assertEquals(defaults, cmd.getValues(defaultOption, null));
        cmd.setDefaultValues(defaultOption, null);
        assertNull(cmd.getValues(defaultOption, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option defaultOption = new Option();
        assertFalse(cmd.getSwitch(defaultOption, false));
        cmd.setDefaultSwitch(defaultOption, true);
        assertTrue(cmd.getSwitch(defaultOption, false));
        cmd.setDefaultSwitch(defaultOption, null);
        assertFalse(cmd.getSwitch(defaultOption, false));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Arrays.asList(), cmd.getNormalised());
        arguments.add("arg");
        assertEquals(Arrays.asList("arg"), cmd.getNormalised());
    }
}