package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    @Test
    public void testWriteableCommandLineImpl_constructor() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        List arguments = new ArrayList();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(rootOption, commandLine.getRootOption());
        assertEquals(arguments, commandLine.getArguments());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(Collections.singletonMap("opt", option), commandLine.getNameToOption());
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("opt", "opt", false, "Option");
        Object value = "value";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.addValue(option, value);

        List values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList(value), values);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, Boolean.FALSE));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("opt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteableCommandLineImpl_getOption_invalidTrigger() {
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.getOption("invalid");
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("opt", "opt", false, "Option");
        Object value1 = "value1";
        Object value2 = "value2";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList(value1, value2), values);
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = new Option("opt", "opt", false, "Option");
        Object value1 = "value1";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.addValue(option, value1);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(Collections.singletonList(value1), values);
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, Boolean.FALSE).booleanValue());
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("opt", "opt", false, "Option");
        String property = "property";
        String value = "value";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = new Option("opt", "opt", false, "Option");
        String property = "property";
        String value = "value";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.addProperty(option, property, value);

        Properties properties = commandLine.getProperties(option);
        assertEquals(property, properties.getProperty(property));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty_defaultValue() {
        Option option = new Option("opt", "opt", false, "Option");
        String property = "property";
        String defaultValue = "defaultValue";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addProperty(option, property, defaultValue);

        assertEquals(defaultValue, commandLine.getProperty(property, null));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("opt", "opt", false, "Option");
        String property1 = "property1";
        String property2 = "property2";
        String value1 = "value1";
        String value2 = "value2";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);

        Set properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("opt", "opt", false, "Option");
        String arg1 = "arg1";
        String arg2 = "arg2";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList(arg1, arg2));
        commandLine.addOption(option);

        assertEquals("arg1 arg2 " + option.getLongOpt(), commandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = new Option("opt1", "opt1", false, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = new Option("opt1", "opt1", false, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Arrays.asList("opt1", "opt2"), commandLine.getOptionTriggers());
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("opt", "opt", false, "Option");
        List defaults = Arrays.asList("value1", "value2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.setDefaultValues(option, defaults);

        List values = commandLine.getValues(option, null);
        assertEquals(defaults, values);
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("opt", "opt", false, "Option");
        Boolean defaultSwitch = Boolean.TRUE;

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getSwitch(option, Boolean.FALSE));
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option = new Option("opt", "opt", false, "Option");
        String arg = "arg";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList(arg));
        commandLine.addOption(option);

        assertEquals(Arrays.asList(arg), commandLine.getNormalised());
    }
}