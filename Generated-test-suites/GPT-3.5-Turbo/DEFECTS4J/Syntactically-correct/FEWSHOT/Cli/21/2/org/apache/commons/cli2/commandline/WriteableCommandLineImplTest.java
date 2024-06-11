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
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionImpl("--root", "Root option", true);
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("--test", "Test option", false);
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentImpl("--arg", "Argument option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("-s", "Switch option", false);
        commandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionImpl("--test", "Test option", false);
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new OptionImpl("--option1", "Option 1", false);
        Option option2 = new OptionImpl("--option2", "Option 2", false);
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("--option1"));
        assertNull(commandLine.getOption("--option2"));
    }

    @Test
    public void testGetValues() {
        Option option = new ArgumentImpl("--arg", "Argument option");
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("default1", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new ArgumentImpl("--arg", "Argument option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionImpl("-s", "Switch option", false);
        commandLine.addSwitch(option, false);
        assertEquals(Boolean.FALSE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new OptionImpl("--property", "Property option", true);
        String property = "key";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("--property", "Property option", true);
        String property = "key";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property));
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl("-o", "Option", false);
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals("-o value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionImpl("--option1", "Option 1", false);
        Option option2 = new OptionImpl("--option2", "Option 2", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionImpl("--option1", "Option 1", false);
        Option option2 = new OptionImpl("--option2", "Option 2", false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("--option1"));
        assertTrue(triggers.contains("--option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new ArgumentImpl("--arg", "Argument option");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("-s", "Switch option", false);
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionImpl("-o", "Option", false);
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals("-o value", commandLine.getNormalised().get(0));
    }
}