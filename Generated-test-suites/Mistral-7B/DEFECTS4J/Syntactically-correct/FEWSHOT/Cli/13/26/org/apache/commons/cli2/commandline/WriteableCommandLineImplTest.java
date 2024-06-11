package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmd;

    @org.junit.Before
    public void setUp() {
        Option option = new Option("opt", "option", true, "an option");
        cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
    }

    @org.junit.Test
    public void testAddOption() {
        Option option = new Option("opt2", "option2", false, "another option");
        cmd.addOption(option);
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));
    }

    @org.junit.Test
    public void testAddValue() {
        Option option = new Option("arg", "argument", true, "an argument");
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));

        cmd.addValue(option, "value2");
        assertEquals(1, cmd.getOptions().size());
        assertEquals(2, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));
        assertEquals("value2", cmd.getValues(option, null).get(1));

        cmd.addValue(option, "value3");
        assertEquals(1, cmd.getOptions().size());
        assertEquals(3, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));
        assertEquals("value2", cmd.getValues(option, null).get(1));
        assertEquals("value3", cmd.getValues(option, null).get(2));
    }

    @org.junit.Test
    public void testAddSwitch() {
        Option option = new Option("sw", "switch", false, "a switch");
        cmd.addOption(option);

        cmd.addSwitch(option, true);
        assertTrue(cmd.hasOption(option));
        assertTrue(cmd.getSwitch(option, false));

        cmd.addSwitch(option, false);
        assertFalse(cmd.getSwitch(option, true));
        assertFalse(cmd.getSwitch(option, false));
    }

    @org.junit.Test
    public void testHasOption() {
        Option option = new Option("opt3", "option3", true, "another option3");
        cmd.addOption(option);

        assertTrue(cmd.hasOption(option));
        assertFalse(cmd.hasOption(new Option("opt4", "option4", false, "another option4")));
    }

    @org.junit.Test
    public void testGetOption() {
        Option option = new Option("opt5", "option5", true, "another option5");
        cmd.addOption(option);

        Option retrievedOption = cmd.getOption("opt5");
        assertEquals(option, retrievedOption);

        Option nonexistentOption = cmd.getOption("opt6");
        assertNull(nonexistentOption);
    }

    @org.junit.Test
    public void testGetValues() {
        Option option = new Option("arg", "argument", true, "an argument");
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));

        cmd.addValue(option, "value2");
        assertEquals(1, cmd.getOptions().size());
        assertEquals(2, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));
        assertEquals("value2", cmd.getValues(option, null).get(1));

        List defaultValues = new ArrayList();
        defaultValues.add("default1");
        cmd.setDefaultValues(option, defaultValues);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(3, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));
        assertEquals("value2", cmd.getValues(option, null).get(1));
        assertEquals("default1", cmd.getValues(option, null).get(2));

        cmd.addValue(option, "value3");
        assertEquals(1, cmd.getOptions().size());
        assertEquals(4, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));
        assertEquals("value2", cmd.getValues(option, null).get(1));
        assertEquals("default1", cmd.getValues(option, null).get(2));
        assertEquals("value3", cmd.getValues(option, null).get(3));

        List emptyDefaultValues = new ArrayList();
        cmd.setDefaultValues(option, emptyDefaultValues);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(3, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));
        assertEquals("value2", cmd.getValues(option, null).get(1));
        assertEquals(null, cmd.getValues(option, null).get(2));
    }

    @org.junit.Test
    public void testGetSwitch() {
        Option option = new Option("sw", "switch", false, "a switch");
        cmd.addOption(option);

        cmd.addSwitch(option, true);
        assertTrue(cmd.hasOption(option));
        assertTrue(cmd.getSwitch(option, false));
        assertTrue(cmd.getSwitch(option, true).booleanValue());

        cmd.addSwitch(option, false);
        assertFalse(cmd.getSwitch(option, true).booleanValue());
        assertFalse(cmd.getSwitch(option, false).booleanValue());

        Option option2 = new Option("sw2", "switch2", false, "another switch2");
        cmd.addOption(option2);

        cmd.addSwitch(option2, true);
        assertTrue(cmd.hasOption(option2));
        assertTrue(cmd.getSwitch(option2, false) == null);
        assertTrue(cmd.getSwitch(option2, true).booleanValue());
    }

    @org.junit.Test
    public void testAddProperty() {
        cmd.addProperty("prop1", "value1");
        assertEquals("value1", cmd.getProperty("prop1", "default1"));
        assertEquals("default1", cmd.getProperty("prop2", "default1"));
    }

    @org.junit.Test
    public void testToString() {
        Option option = new Option("opt", "option", true, "an option");
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        assertEquals("option value1", cmd.toString());

        List values = new ArrayList();
        values.add("value2");
        values.add("value3");
        cmd.setDefaultValues(option, values);

        cmd.addValue(option, "value4");
        assertEquals("option value2 value3 value4", cmd.toString());

        cmd.setDefaultValues(option, Collections.emptyList());
        assertEquals("option", cmd.toString());
    }

    @org.junit.Test
    public void testGetOptions() {
        Option option = new Option("opt", "option", true, "an option");
        cmd.addOption(option);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));
    }

    @org.junit.Test
    public void testGetOptionTriggers() {
        Option option = new Option("opt", "option", true, "an option");
        cmd.addOption(option);

        Set optionTriggers = cmd.getOptionTriggers();
        assertEquals(option.getTriggers().size() + 1, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt"));

        option = new Option("opt2", "option2", true, "another option2");
        cmd.addOption(option);

        optionTriggers = cmd.getOptionTriggers();
        assertEquals(option.getTriggers().size() + 1 + option2.getTriggers().size(), optionTriggers.size());
        assertTrue(optionTriggers.contains("opt"));
        assertTrue(optionTriggers.contains("opt2"));
    }

    @org.junit.Test
    public void testSetDefaultValues() {
        Option option = new Option("opt", "option", true, "an option");
        cmd.addOption(option);

        List defaultValues = new ArrayList();
        defaultValues.add("value1");
        cmd.setDefaultValues(option, defaultValues);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));

        assertEquals(1, cmd.getDefaultValues(option).size());
        assertEquals("value1", cmd.getDefaultValues(option).get(0));

        List emptyDefaultValues = new ArrayList();
        cmd.setDefaultValues(option, emptyDefaultValues);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(0, cmd.getValues(option, null).size());
        assertNull(cmd.getDefaultValues(option));

        List values = new ArrayList();
        values.add("value2");
        cmd.setDefaultValues(option, values);

        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getValues(option, null).size());
        assertEquals("value2", cmd.getValues(option, null).get(0));
        assertEquals(1, cmd.getDefaultValues(option).size());
        assertEquals("value2", cmd.getDefaultValues(option).get(0));
    }

    @org.junit.Test
    public void testSetDefaultSwitch() {
        Option option = new Option("sw", "switch", false, "a switch");
        cmd.addOption(option);

        cmd.setDefaultSwitch(option, true);
        assertTrue(cmd.hasOption(option));
        assertTrue(cmd.getSwitch(option, false) == null);
        assertTrue(cmd.getSwitch(option, true).booleanValue());

        cmd.setDefaultSwitch(option, false);
        assertFalse(cmd.getSwitch(option, true).booleanValue());
        assertFalse(cmd.getSwitch(option, false).booleanValue());

        Option option2 = new Option("sw2", "switch2", false, "another switch2");
        cmd.addOption(option2);

        cmd.setDefaultSwitch(option2, true);
        assertTrue(cmd.hasOption(option2));
        assertTrue(cmd.getSwitch(option2, false) == null);
        assertTrue(cmd.getSwitch(option2, true).booleanValue());

        cmd.setDefaultSwitch(option2, false);
        assertFalse(cmd.getSwitch(option2, true).booleanValue());
        assertFalse(cmd.getSwitch(option2, false).booleanValue());
    }

    @org.junit.Test
    public void testGetNormalised() {
        Option option = new Option("opt", "option", true, "an option");
        cmd.addOption(option);

        List normalised = cmd.getNormalised();
        assertEquals(0, normalised.size());

        cmd.addValue(option, "value1");
        assertEquals(1, normalised.size());
        assertEquals("value1", normalised.get(0));

        List values = new ArrayList();
        values.add("value2");
        values.add("value3");
        cmd.setDefaultValues(option, values);

        cmd.addValue(option, "value4");
        assertEquals(4, normalised.size());
        assertEquals("value1", normalised.get(0));
        assertEquals("value2", normalised.get(1));
        assertEquals("value3", normalised.get(2));
        assertEquals("value4", normalised.get(3));

        cmd.setDefaultValues(option, Collections.emptyList());
        assertEquals(1, normalised.size());
        assertEquals("option", normalised.get(0));
    }
}