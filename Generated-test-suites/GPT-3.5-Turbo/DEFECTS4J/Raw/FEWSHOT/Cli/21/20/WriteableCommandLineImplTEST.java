package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private Option option;
    private List<String> arguments;
    private WriteableCommandLineImpl cmd;

    @Before
    public void setUp() {
        option = new Option();
        arguments = Arrays.asList("arg1", "arg2", "arg3");
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
        Object value = new Object();
        cmd.addValue(option, value);
        assertTrue(cmd.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        boolean switchValue = true;
        cmd.addSwitch(option, switchValue);
        assertEquals(switchValue, cmd.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        cmd.addOption(option);
        assertEquals(option, cmd.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        cmd.addValue(option, value1);
        cmd.addValue(option, value2);
        assertTrue(cmd.getValues(option, null).contains(value1));
        assertTrue(cmd.getValues(option, null).contains(value2));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        cmd.addValue(option, value1);
        cmd.addValue(option, value2);
        assertTrue(cmd.getUndefaultedValues(option).contains(value1));
        assertTrue(cmd.getUndefaultedValues(option).contains(value2));
    }

    @Test
    public void testGetSwitch() {
        boolean switchValue = true;
        cmd.addSwitch(option, switchValue);
        assertEquals(switchValue, cmd.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        cmd.addProperty(option, property, value);
        assertEquals(value, cmd.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        cmd.addProperty(option, property, value);
        assertTrue(cmd.getProperties(option).contains(property));
    }

    @Test
    public void testGetProperty_DefaultValue() {
        String property = "property";
        String value = "value";
        assertEquals(value, cmd.getProperty(option, property, value));
    }

    @Test
    public void testGetProperties() {
        String property1 = "property1";
        String property2 = "property2";
        cmd.addProperty(option, property1, "value1");
        cmd.addProperty(option, property2, "value2");
        assertTrue(cmd.getProperties(option).contains(property1));
        assertTrue(cmd.getProperties(option).contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(cmd.looksLikeOption("-option"));
        assertTrue(cmd.looksLikeOption("--option"));
        assertFalse(cmd.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1 arg2 arg3", cmd.toString());
    }

    @Test
    public void testGetOptions() {
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        cmd.addOption(option);
        assertTrue(cmd.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaults = Arrays.asList("default1", "default2");
        cmd.setDefaultValues(option, defaults);
        assertEquals(defaults, cmd.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        boolean defaultSwitch = true;
        cmd.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, cmd.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(arguments, cmd.getNormalised());
    }
}