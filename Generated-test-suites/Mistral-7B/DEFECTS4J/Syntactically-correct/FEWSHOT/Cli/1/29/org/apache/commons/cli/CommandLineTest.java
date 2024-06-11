package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCommandLineEmpty() {
        CommandLine commandLine = new CommandLine();
        String[] args = commandLine.getArgs();
        assertNull(args);
        assertFalse(commandLine.hasOption("option1"));
        assertNull(commandLine.getOptionValue("option1"));
        assertNull(commandLine.getOptionObject("option1"));
        assertNull(commandLine.getOptionValues("option1"));
        assertNull(commandLine.getOptionValue("option1", "default"));
        assertNull(commandLine.getOptionValue('o', "default"));
        assertNull(commandLine.getOptionObject('o'));
        assertNull(commandLine.getOptionValues('o'));
        assertNull(commandLine.getOptionValue('o', "default"));
        assertFalse(commandLine.hasOption('o'));
        assertNull(commandLine.getArgs());
        assertNull(commandLine.getArgList());
    }

    @Test
    public void testCommandLineWithOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.addOption(new Option("o", "opt", true, "option description"));
        commandLine.addOption(new Option("l", "longopt", true, "long option description"));
        commandLine.addOption(new Option("i", "intval", int.class, "integer value option"));
        commandLine.addOption(new Option("f", "floatval", Float.class, "float value option"));
        commandLine.addOption(new Option("s", "stringval", String.class, "string value option"));
        commandLine.addOption(new Option("b", "booleanval", Boolean.class, "boolean value option"));

        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.hasOption("l"));
        assertTrue(commandLine.hasOption("i"));
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("s"));
        assertTrue(commandLine.hasOption("b"));

        assertNull(commandLine.getOptionValue("nonexistent"));
        assertNull(commandLine.getOptionValue("o"));
        assertNull(commandLine.getOptionValue("o", "default"));
        assertNull(commandLine.getOptionValue('o'));
        assertNull(commandLine.getOptionValue('o', "default"));
        assertNull(commandLine.getOptionObject("nonexistent"));
        assertNull(commandLine.getOptionObject("o"));
        assertNull(commandLine.getOptionValues("nonexistent"));
        assertNull(commandLine.getOptionValues("o"));
        assertTrue(commandLine.getOptionValues("i").length > 0);
        assertTrue(commandLine.getOptionValues("f").length > 0);
        assertTrue(commandLine.getOptionValues("s").length > 0);
        assertNotNull(commandLine.getOptionObject("i"));
        assertNotNull(commandLine.getOptionObject("f"));
        assertNotNull(commandLine.getOptionObject("s"));
        assertNotNull(commandLine.getOptionObject("b"));
        assertNull(commandLine.getOptionValue("o", "defaultValue"));
        assertNull(commandLine.getOptionValue('o', "defaultValue"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgList().toArray(new String[0]));
    }

    @Test
    public void testCommandLineWithLongOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.addOption(new Option("longopt1", "longopt1", true, "long option description 1"));
        commandLine.addOption(new Option("longopt2", "longopt2", true, "long option description 2"));
        commandLine.addOption(new Option("longopt3", "longopt3", true, "long option description 3"));

        assertTrue(commandLine.hasOption("--longopt1"));
        assertTrue(commandLine.hasOption("--longopt2"));
        assertTrue(commandLine.hasOption("--longopt3"));
        assertNull(commandLine.getOptionValue("--nonexistent"));
        assertNull(commandLine.getOptionValue("--longopt1"));
        assertNull(commandLine.getOptionValue("--longopt1", "default"));
        assertNull(commandLine.getOptionValue("--longopt1", null));
        assertNull(commandLine.getOptionValues("--nonexistent"));
        assertNull(commandLine.getOptionValues("--longopt1"));
        assertNull(commandLine.getOptionObject("--nonexistent"));
        assertNull(commandLine.getOptionObject("--longopt1"));
        assertNull(commandLine.getOptionValues("--longopt1"));
        assertNull(commandLine.getOptionValue("--longopt1", "defaultValue"));
        assertNull(commandLine.getOptionValue("--longopt1", null));
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgList().toArray(new String[0]));
    }

    @Test
    public void testCommandLineWithMixedOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.addOption(new Option("o", "opt", true, "option description"));
        commandLine.addOption(new Option("l", "longopt", true, "long option description"));
        commandLine.addOption(new Option("i", "intval", int.class, "integer value option"));
        commandLine.addOption(new Option("f", "floatval", Float.class, "float value option"));
        commandLine.addOption(new Option("s", "stringval", String.class, "string value option"));
        commandLine.addOption(new Option("b", "booleanval", Boolean.class, "boolean value option"));

        commandLine.addArg("arg3");
        commandLine.addArg("arg4");

        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.hasOption("l"));
        assertTrue(commandLine.hasOption("i"));
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("s"));
        assertTrue(commandLine.hasOption("b"));

        assertNull(commandLine.getOptionValue("nonexistent"));
        assertNull(commandLine.getOptionValue("o"));
        assertNull(commandLine.getOptionValue("o", "default"));
        assertNull(commandLine.getOptionValue('o'));
        assertNull(commandLine.getOptionValue('o', "default"));
        assertNull(commandLine.getOptionObject("nonexistent"));
        assertNull(commandLine.getOptionObject("o"));
        assertNull(commandLine.getOptionValues("nonexistent"));
        assertNull(commandLine.getOptionValues("o"));
        assertTrue(commandLine.getOptionValues("i").length > 0);
        assertTrue(commandLine.getOptionValues("f").length > 0);
        assertTrue(commandLine.getOptionValues("s").length > 0);
        assertNotNull(commandLine.getOptionObject("i"));
        assertNotNull(commandLine.getOptionObject("f"));
        assertNotNull(commandLine.getOptionObject("s"));
        assertNotNull(commandLine.getOptionObject("b"));
        assertNull(commandLine.getOptionValue("o", "defaultValue"));
        assertNull(commandLine.getOptionValue('o', "defaultValue"));
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3", "arg4"}, commandLine.getArgs());
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3", "arg4"}, commandLine.getArgList().toArray(new String[0]));
    }
}