package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_constructor() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(rootOption, cmd.getOption("root"));
        assertEquals(arguments, cmd.getNormalised());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
        assertEquals(option, cmd.getOption("opt"));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        List values = cmd.getValues(option, null);
        assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        Boolean switchValue = cmd.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(option);
        assertEquals(option, cmd.getOption("opt"));
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        List values = cmd.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        Boolean switchValue = cmd.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addProperty("property", "value");
        assertEquals("value", cmd.getProperty("property", ""));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addProperty("property1", "value1");
        cmd.addProperty("property2", "value2");
        Set properties = cmd.getProperties();
        assertEquals(2, properties.size());
        assertEquals("value1", cmd.getProperty("property1", ""));
        assertEquals("value2", cmd.getProperty("property2", ""));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option opt = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));
        cmd.addOption(opt);
        assertEquals("arg1 arg2 option", cmd.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option opt1 = new Option("opt1", "opt1", false, "Option 1 description");
        Option opt2 = new Option("opt2", "opt2", false, "Option 2 description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        assertEquals(Arrays.asList(opt1, opt2), cmd.getOptions());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option opt1 = new Option("opt1", "opt1", false, "Option 1 description");
        Option opt2 = new Option("opt2", "opt2", false, "Option 2 description");
        Option opt3 = new Option("opt1", "opt3", false, "Option 3 description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        Set optionTriggers = cmd.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("opt1", "opt2", "opt3")));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option opt = new Option("opt", "option", true, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(opt);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        cmd.setDefaultValues(opt, defaultValues);
        List values = cmd.getValues(opt, null);
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), values);
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option opt = new Option("opt", "option", false, "Option description");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        cmd.addOption(opt);
        Boolean defaultSwitch = true;
        cmd.setDefaultSwitch(opt, defaultSwitch);
        Boolean switchValue = cmd.getSwitch(opt, false);
        assertTrue(switchValue);
    }
}