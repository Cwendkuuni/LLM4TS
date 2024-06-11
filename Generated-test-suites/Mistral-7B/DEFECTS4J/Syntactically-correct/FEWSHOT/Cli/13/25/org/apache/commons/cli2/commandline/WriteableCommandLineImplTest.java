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
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file.");
        List<String> arguments = Arrays.asList("test.jar", "-o", "output.txt");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(3, commandLine.getNormalised().size());

        Option optionFromCommandLine = commandLine.getOption("o");
        Option optionFromNameToOption = commandLine.getOption("output");
        assertEquals(option, optionFromCommandLine);
        assertEquals(option, optionFromNameToOption);

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));

        assertTrue(commandLine.hasOption(option));

        commandLine.addProperty("test", "true");
        assertEquals("true", commandLine.getProperty("test", "false"));
        assertEquals(1, commandLine.getProperties().size());

        Option switchOption = new Option("s", "silent", false, "Do not print messages.");
        commandLine.addOption(switchOption);
        assertTrue(commandLine.getSwitch(switchOption, false));

        commandLine.addSwitch(switchOption, true);
        assertFalse(commandLine.getSwitch(switchOption, false));

        Map<String, Option> nameToOptionMap = commandLine.getOptionTriggers();
        assertEquals("o", nameToOptionMap.get("output").getName());
        assertEquals("s", nameToOptionMap.get("silent").getName());
    }

    @Test
    public void testWriteableCommandLineImplWithArgument() {
        Option option = new Option("o", "output", true, "Output file.");
        Argument arg = new Argument("i", "inputFile", true, "Input file.");
        List<String> arguments = Arrays.asList("test.jar", "-o", "output.txt", "--input", "input.txt");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arg, arguments);

        assertEquals(2, commandLine.getOptions().size());
        assertEquals(4, commandLine.getNormalised().size());

        Option optionFromCommandLine = commandLine.getOption("o");
        Option optionFromNameToOption = commandLine.getOption("output");
        Argument argFromCommandLine = commandLine.getArgument("i");
        Argument argFromNameToOption = commandLine.getArgument("inputFile");
        assertEquals(option, optionFromCommandLine);
        assertEquals(option, optionFromNameToOption);
        assertEquals(arg, argFromCommandLine);
        assertEquals(arg, argFromNameToOption);

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("output.txt", values.get(0));

        List valuesArg = commandLine.getValues(arg, null);
        assertEquals(1, valuesArg.size());
        assertEquals("input.txt", valuesArg.get(0));

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.hasArgument(arg));

        commandLine.addProperty("test", "true");
        assertEquals("true", commandLine.getProperty("test", "false"));
        assertEquals(1, commandLine.getProperties().size());

        Option switchOption = new Option("s", "silent", false, "Do not print messages.");
        commandLine.addOption(switchOption);
        assertTrue(commandLine.getSwitch(switchOption, false));

        commandLine.addSwitch(switchOption, true);
        assertFalse(commandLine.getSwitch(switchOption, false));

        Map<String, Option> nameToOptionMap = commandLine.getOptionTriggers();
        assertEquals("o", nameToOptionMap.get("output").getName());
        assertEquals("s", nameToOptionMap.get("silent").getName());
        assertEquals("i", nameToOptionMap.get("inputFile").getName());
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultValues() {
        Option option = new Option("o", "output", true, "Output file.");
        List<String> defaultValues = Arrays.asList("output1.txt", "output2.txt");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(defaultValues));

        commandLine.addProperty("test", "true");
        assertEquals("true", commandLine.getProperty("test", "false"));
        assertEquals(1, commandLine.getProperties().size());

        Option switchOption = new Option("s", "silent", false, "Do not print messages.");
        commandLine.addOption(switchOption);
        assertTrue(commandLine.getSwitch(switchOption, false));

        commandLine.addSwitch(switchOption, true);
        assertFalse(commandLine.getSwitch(switchOption, false));

        Map<String, Option> nameToOptionMap = commandLine.getOptionTriggers();
        assertEquals("o", nameToOptionMap.get("output").getName());
        assertEquals("s", nameToOptionMap.get("silent").getName());
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultSwitches() {
        Option option = new Option("o", "output", true, "Output file.");
        Boolean defaultSwitch = Boolean.TRUE;
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue != null);
        assertTrue(switchValue.booleanValue());

        commandLine.addProperty("test", "true");
        assertEquals("true", commandLine.getProperty("test", "false"));
        assertEquals(1, commandLine.getProperties().size());

        Option switchOption = new Option("s", "silent", false, "Do not print messages.");
        commandLine.addOption(switchOption);
        assertTrue(commandLine.getSwitch(switchOption, false));

        commandLine.addSwitch(switchOption, false);
        assertFalse(commandLine.getSwitch(switchOption, false));

        Map<String, Option> nameToOptionMap = commandLine.getOptionTriggers();
        assertEquals("o", nameToOptionMap.get("output").getName());
        assertEquals("s", nameToOptionMap.get("silent").getName());
    }
}