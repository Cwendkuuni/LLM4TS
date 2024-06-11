package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new OptionBuilder().withPreferredName("rootOption").create();
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentBuilder().withParent(new OptionBuilder().withPreferredName("option").create()).create();
        Object value = "value";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        assertNull(commandLine.getOption("option1"));
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option1"));
    }

    @Test
    public void testGetValues() {
        Option option = new ArgumentBuilder().withParent(new OptionBuilder().withPreferredName("option").create()).create();
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new ArgumentBuilder().withParent(new OptionBuilder().withPreferredName("option").create()).create();
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        assertNull(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        assertEquals(value, commandLine.getProperty(property, value));
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void testGetPropertyFromOption() {
        Option option = new PropertyOption();
        String property = "property";
        String value = "value";
        assertNull(commandLine.getProperty(option, property, null));
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        Set<String> properties = commandLine.getProperties();
        assertEquals(0, properties.size());
        Option option = new PropertyOption();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains(property));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new ArgumentBuilder().withParent(new OptionBuilder().withPreferredName("option").create()).create();
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List<Object> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(new OptionBuilder().withPreferredName("rootOption").create(), arguments);
        List<String> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}