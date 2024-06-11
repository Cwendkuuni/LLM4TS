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
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_empty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertTrue(commandLine.isEmpty());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("option", "Option description", false, "o");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(option, commandLine.getOption("option"));
        assertTrue(commandLine.hasOption(option));

        Option parentOption = new Option("parent", "Parent option description", false, "p");
        Option childOption = new Option("child", "Child option description", false, "c");
        childOption.setParent(parentOption);
        commandLine = new WriteableCommandLineImpl(parentOption, Collections.emptyList());
        commandLine.addOption(parentOption);
        commandLine.addOption(childOption);
        assertEquals(Collections.singletonList(parentOption), commandLine.getOptions());
        assertEquals(parentOption, commandLine.getOption("parent"));
        assertTrue(commandLine.hasOption(parentOption));
        assertEquals(Collections.singletonList(childOption), commandLine.getOption("parent").getOptions());
        assertEquals(childOption, commandLine.getOption("parent/child"));
        assertTrue(commandLine.hasOption("parent/child"));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("option", "Option description", false, "o");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList("value1"), values);
        assertEquals(Collections.singletonList("value1"), commandLine.getValues("option"));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList("value2"), values);
        assertEquals(Collections.singletonList("value2"), commandLine.getValues("option"));

        commandLine.addValue(option, "value1,value2");
        values = commandLine.getValues(option, null);
        assertEquals(Collections.singletonList(Arrays.asList("value1", "value2")), values);
        assertEquals(Collections.singletonList(Arrays.asList("value1", "value2")), commandLine.getValues("option"));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("option", "Option description", false, "o");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
        assertEquals(Boolean.TRUE, switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        assertFalse(switchValue);
        assertEquals(Boolean.FALSE, switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("option", "Option description", false, "o");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);

        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("option", "Option description", false, "o");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("arg1"));
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "option arg1 value1";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = new Option("option1", "Option description 1", false, "o1");
        Option option2 = new Option("option2", "Option description 2", false, "o2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = new Option("option1", "Option description 1", false, "o1");
        Option option2 = new Option("option2", "Option description 2", false, "o2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("option", "Option description", false, "o");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(defaultValues, values);
        assertEquals(defaultValues, commandLine.getValues("option"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("option", "Option description", false, "o");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option1 = new Option("option1", "Option description 1", false, "o1");
        Option option2 = new Option("option2", "Option description 2", false, "o2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List normalised = commandLine.getNormalised();
        List expectedNormalised = Arrays.asList("option1", "arg1", "option2", "arg2");
        assertEquals(expectedNormalised, normalised);
    }
}