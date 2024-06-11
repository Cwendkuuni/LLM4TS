package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        assertEquals(option, cmd.getOption("opt"));
        assertEquals(1, cmd.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        assertEquals(2, cmd.getValues(option, null).size());
        assertEquals("value1", cmd.getValues(option, null).get(0));
        assertEquals("value2", cmd.getValues(option, null).get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        assertEquals(option, cmd.getOption("opt"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        List values = cmd.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        List values = cmd.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
        assertTrue(cmd.getSwitch(option, true).booleanValue());
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "prop", true, "Property test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addProperty(option, "propName", "propValue");
        assertEquals("propValue", cmd.getProperty(option, "propName", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "prop", true, "Property test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.addProperty(option, "propName1", "propValue1");
        cmd.addProperty(option, "propName2", "propValue2");
        Set properties = cmd.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("propName1"));
        assertTrue(properties.contains("propName2"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "opt1", true, "Option test 1");
        Option option2 = new Option("o2", "opt2", true, "Option test 2");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        cmd.addOption(option1);
        cmd.addOption(option2);
        cmd.addValue(option1, "val1");
        cmd.addValue(option2, "val2");
        cmd.addSwitch(option1, true);
        assertEquals("arg1 opt1=val1 opt2 val2", cmd.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "opt1", true, "Option test 1");
        Option option2 = new Option("o2", "opt2", true, "Option test 2");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        cmd.addOption(option1);
        cmd.addOption(option2);
        List options = cmd.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "opt1", true, "Option test 1");
        Option option2 = new Option("o2", "opt2", true, "Option test 2");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        cmd.addOption(option1);
        cmd.addOption(option2);
        Set optionTriggers = cmd.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        List defaults = Arrays.asList("default1", "default2");
        cmd.setDefaultValues(option, defaults);
        List values = cmd.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);
        cmd.setDefaultSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
        assertTrue(cmd.getSwitch(option, true).booleanValue());
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        cmd.addOption(option);
        List normalised = cmd.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}