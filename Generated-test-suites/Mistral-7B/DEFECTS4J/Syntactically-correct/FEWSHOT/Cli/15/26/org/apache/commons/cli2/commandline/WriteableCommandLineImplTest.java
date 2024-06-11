package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        assertEquals("Test option", cmd.getOptionLabel(rootOption));
        assertFalse(cmd.hasOption(rootOption));
        assertNull(cmd.getValues(rootOption, null));
        assertNull(cmd.getSwitch(rootOption, false));
        assertEquals(Collections.emptyMap(), cmd.getProperties());
        assertEquals(Collections.emptyList(), cmd.getOptions());
        assertEquals(Collections.emptySet(), cmd.getOptionTriggers());
    }

    @Test
    public void testAddOption() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option1 = new Option("opt1", "option 1", true, "Option 1");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        cmd.addOption(option1);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(option1, cmd.getOptions().get(0));
        assertEquals("Option 1", cmd.getOptionLabel(option1));
        assertTrue(cmd.hasOption(option1));
        assertNull(cmd.getValues(option1, null));
        assertNull(cmd.getSwitch(option1, false));
        assertEquals(Collections.emptyMap(), cmd.getProperties());
    }

    @Test
    public void testAddValue() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        List values = cmd.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(option);

        cmd.addSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(option);

        cmd.addSwitch(option, true);
        cmd.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option1 = new Option("opt1", "option 1", true, "Option 1");
        final Option option2 = new Argument("arg1", "Argument 1", true);
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        cmd.addOption(option1);
        cmd.addOption(option2);

        assertTrue(cmd.hasOption(option1));
        assertFalse(cmd.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option1 = new Option("opt1", "option 1", true, "Option 1");
        final Option option2 = new Argument("arg1", "Argument 1", true);
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        cmd.addOption(option1);
        cmd.addOption(option2);

        Option optionFound = cmd.getOption("opt1");
        assertEquals(option1, optionFound);

        Option argumentFound = cmd.getOption("arg1");
        assertEquals(option2, argumentFound);

        Option unknownOption = cmd.getOption("unknown");
        assertNull(unknownOption);
    }

    @Test
    public void testGetValues() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        List values = cmd.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List emptyValues = cmd.getValues(option, Collections.emptyList());
        assertEquals(Collections.emptyList(), emptyValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        List values = cmd.getUndefaultedValues(option);
        assertEquals(Collections.emptyList(), values);

        cmd.setDefaultValues(option, Collections.singletonList("valueDefault"));
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        values = cmd.getUndefaultedValues(option);
        assertEquals(Collections.singletonList("value1"), values);

        cmd.setDefaultValues(option, null);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        values = cmd.getUndefaultedValues(option);
        assertEquals(Collections.singletonList("value2"), values);
    }

    @Test
    public void testGetSwitch() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(option);

        cmd.addSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, false);
        assertTrue(switchValue);

        Boolean defaultSwitchValue = cmd.getSwitch(option, false);
        assertFalse(defaultSwitchValue);
    }

    @Test
    public void testGetProperty() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final PropertyOption propertyOption = new PropertyOption("prop", "Property", true);
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(propertyOption);

        cmd.addProperty(propertyOption, "property", "value");

        assertEquals("value", cmd.getProperty(propertyOption, "defaultValue"));
        assertEquals("defaultValue", cmd.getProperty(propertyOption, "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final PropertyOption propertyOption = new PropertyOption("prop1", "Property 1", true);
        final PropertyOption propertyOption2 = new PropertyOption("prop2", "Property 2", true);
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(propertyOption);
        cmd.addOption(propertyOption2);

        cmd.addProperty(propertyOption, "prop1", "value1");
        cmd.addProperty(propertyOption2, "prop2", "value2");

        Map properties = cmd.getProperties(propertyOption);
        assertEquals(Collections.singletonMap("prop1", "value1"), properties);

        properties = cmd.getProperties(propertyOption2);
        assertEquals(Collections.singletonMap("prop2", "value2"), properties);

        properties = cmd.getProperties();
        assertEquals(Collections.emptyMap(), properties);

        assertEquals(Collections.singletonMap("prop1", "value1"), cmd.getProperties(propertyOption));
        assertEquals(Collections.singletonMap("prop2", "value2"), cmd.getProperties(propertyOption2));
    }

    @Test
    public void testToString() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.singletonList("arg1"));
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        String cmdString = cmd.toString();
        assertEquals("-test test arg1 opt value1 value2", cmdString);
    }

    @Test
    public void testGetOptionsList() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option1 = new Option("opt1", "option 1", true, "Option 1");
        final Option option2 = new Argument("arg1", "Argument 1", true);
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        cmd.addOption(option1);
        cmd.addOption(option2);

        List options = cmd.getOptions();
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggersSet() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option1 = new Option("opt1", "option 1", true, "Option 1");
        final Option option2 = new Argument("arg1", "Argument 1", true);
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        cmd.addOption(option1);
        cmd.addOption(option2);

        Set optionTriggers = cmd.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("arg1"));
    }

    @Test
    public void testSetDefaultValues() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(option);

        List defaults = Arrays.asList("value1", "value2");
        cmd.setDefaultValues(option, defaults);

        List values = cmd.getValues(option, null);
        assertEquals(defaults, values);

        List emptyValues = cmd.getValues(option, Collections.emptyList());
        assertEquals(Collections.emptyList(), emptyValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmd.addOption(option);

        cmd.setDefaultSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        final Option rootOption = new Option(".test", "test", false, "Test option");
        final Option option = new Option("opt", "option", false, "Option");
        final WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));
        cmd.addOption(option);

        List normalised = cmd.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
        assertEquals("opt", normalised.get(2));
    }
}