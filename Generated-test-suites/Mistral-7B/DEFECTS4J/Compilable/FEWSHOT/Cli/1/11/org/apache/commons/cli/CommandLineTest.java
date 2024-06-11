package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCommandLineCreation() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getArgList());
        assertNull(commandLine.getOptions());
    }

    @Test
    public void testHasOption_Boolean() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("a"));

        commandLine.addOption(new Option("a", "example", false, "Examples something."));
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testHasOption_Char() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption('a'));

        commandLine.addOption(new Option("a", "example", false, "Examples something."));
        assertTrue(commandLine.hasOption('a'));
        assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine commandLine = new CommandLine();

        Option option = new Option("example", "ex", true, "Example option.");
        commandLine.addOption(option);

        String value = "exampleValue";
        commandLine.addArg(value);

        Object obj = commandLine.getOptionObject("example");
        assertEquals(value, obj);

        assertEquals(null, commandLine.getOptionObject("nonexistent"));
    }

    @Test
    public void testGetOptionObject_Char() {
        CommandLine commandLine = new CommandLine();

        Option option = new Option("example", "ex", true, "Example option.");
        commandLine.addOption(option);

        String value = "exampleValue";
        commandLine.addArg(value);

        Object obj = commandLine.getOptionObject('e');
        assertEquals(value, obj);

        assertEquals(null, commandLine.getOptionObject('n'));
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine commandLine = new CommandLine();

        Option option = new Option("example", "ex", false, "Example option.");
        commandLine.addOption(option);

        commandLine.addArg("exampleValue");

        String value = commandLine.getOptionValue("example");
        assertEquals("exampleValue", value);
        assertNull(commandLine.getOptionValue("nonexistent"));
    }

    @Test
    public void testGetOptionValue_Char() {
        CommandLine commandLine = new CommandLine();

        Option option = new Option("example", "ex", false, "Example option.");
        commandLine.addOption(option);

        commandLine.addArg("exampleValue");

        String value = commandLine.getOptionValue('e');
        assertEquals("exampleValue", value);
        assertNull(commandLine.getOptionValue('n'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine commandLine = new CommandLine();

        Option option = new Option("example", "ex", true, "Example option.");
        commandLine.addOption(option);

        commandLine.addArg("exampleValue1");
        commandLine.addArg("exampleValue2");

        String[] expected = {"exampleValue1", "exampleValue2"};
        assertArrayEquals(expected, commandLine.getOptionValues("example"));
        assertNull(commandLine.getOptionValues("nonexistent"));
    }

    @Test
    public void testGetOptionValues_Char() {
        CommandLine commandLine = new CommandLine();

        Option option = new Option("example", "ex", true, "Example option.");
        commandLine.addOption(option);

        commandLine.addArg("exampleValue1");
        commandLine.addArg("exampleValue2");

        String[] expected = {"exampleValue1", "exampleValue2"};
        assertArrayEquals(expected, commandLine.getOptionValues('e'));
        assertNull(commandLine.getOptionValues('n'));
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();

        Option option1 = new Option("example1", "e1", false, "Example option 1.");
        Option option2 = new Option("example2", "e2", false, "Example option 2.");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option[] expected = {option1, option2};
        assertArrayEquals(expected, commandLine.getOptions());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        List expected = Arrays.asList("arg1", "arg2");
        assertEquals(expected, commandLine.getArgList());
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        List expected = Arrays.asList("arg1", "arg2");
        assertEquals(expected, commandLine.getArgList());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();

        Option option = new Option("example", "ex", false, "Example option.");
        commandLine.addOption(option);

        Option[] expected = {option};
        assertArrayEquals(expected, commandLine.getOptions());
    }
}