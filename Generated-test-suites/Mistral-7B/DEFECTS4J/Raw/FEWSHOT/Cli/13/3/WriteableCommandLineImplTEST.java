package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        Option option = new Option("b", "optionB", false);
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOption(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        commandLine.addValue(commandLine.getOption("optionA"), "value1");
        commandLine.addValue(commandLine.getOption("optionA"), "value2");
        assertEquals(1, commandLine.getOption("optionA").getValues().size());
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(commandLine.getOption("optionA"), null));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", false), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("optionA"), true);
        assertTrue(commandLine.getSwitch(commandLine.getOption("optionA"), false));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_switchAlreadySet() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", false), new ArrayList<>());
        commandLine.addSwitch(commandLine.getOption("optionA"), true);
        commandLine.addSwitch(commandLine.getOption("optionA"), false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        assertTrue(commandLine.hasOption(commandLine.getOption("optionA")));
        assertFalse(commandLine.hasOption(new Option("b", "optionB", false)));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        Option option = commandLine.getOption("optionA");
        assertEquals("optionA", option.getLongOpt());
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1", "defaultValue"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        commandLine.addValue(commandLine.getOption("optionA"), "value1");
        commandLine.addValue(commandLine.getOption("optionA"), "value2");
        commandLine.addSwitch(commandLine.getOption("optionA"), true);
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        Option option = new Option("b", "optionB", false);
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        Option option1 = new Option("option1", null, false);
        Option option2 = new Option("option2", null, false);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertTrue(optionTriggers.contains("optionA"));
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(commandLine.getOption("optionA"), defaultValues);
        assertEquals(2, commandLine.getValues(commandLine.getOption("optionA"), null).size());
        assertTrue(commandLine.getValues(commandLine.getOption("optionA"), null).containsAll(defaultValues));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", false), new ArrayList<>());
        commandLine.setDefaultSwitch(commandLine.getOption("optionA"), true);
        assertTrue(commandLine.getSwitch(commandLine.getOption("optionA"), false));
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("a", "optionA", true), new ArrayList<>());
        commandLine.addValue(commandLine.getOption("optionA"), "value1");
        commandLine.addValue(commandLine.getOption("optionA"), "value2");
        assertEquals(2, commandLine.getNormalised().size());
        assertEquals("value1", commandLine.getNormalised().get(0));
        assertEquals("value2", commandLine.getNormalised().get(1));
    }
}