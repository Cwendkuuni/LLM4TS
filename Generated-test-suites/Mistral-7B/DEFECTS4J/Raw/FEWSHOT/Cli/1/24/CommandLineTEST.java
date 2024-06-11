package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();

        // test for single character option
        commandLine.addOption(new Option("a", "append", false, "Append to a file instead of overwriting it."));
        assertTrue(commandLine.hasOption('a'));

        // test for long option
        commandLine.addOption(new Option("longOption", "longDescription", false, "Long option description."));
        assertTrue(commandLine.hasOption("longOption"));

        // test for non-existent option
        assertFalse(commandLine.hasOption("nonExistentOption"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();

        // test with no value
        commandLine.addOption(new Option("a", "append", false, "Append to a file instead of overwriting it."));
        assertNull(commandLine.getOptionValue("a"));

        // test with value
        commandLine.addOption(new Option("b", "base", true, "Base directory."));
        commandLine.addArg("test");
        commandLine.addArg("--base");
        commandLine.addArg("/path/to/test");
        assertEquals("/path/to/test", commandLine.getOptionValue("b"));

        // test for non-existent option
        assertNull(commandLine.getOptionValue("nonExistentOption"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();

        // test with no value
        commandLine.addOption(new Option("a", "append", false, "Append to a file instead of overwriting it."));
        assertNull(commandLine.getOptionValues("a"));

        // test with single value
        commandLine.addOption(new Option("b", "base", true, "Base directory."));
        commandLine.addArg("test");
        commandLine.addArg("--base");
        commandLine.addArg("/path/to/test");
        assertArrayEquals(new String[]{"/path/to/test"}, commandLine.getOptionValues("b"));

        // test with multiple values
        commandLine = new CommandLine();
        commandLine.addOption(new Option("c", "colors", true, "List of colors to use."));
        commandLine.addArg("--colors");
        commandLine.addArg("red");
        commandLine.addArg("--colors");
        commandLine.addArg("green");
        commandLine.addArg("--colors");
        commandLine.addArg("blue");
        List<String> colors = Arrays.asList("red", "green", "blue");
        assertEquals(colors, Arrays.asList(commandLine.getOptionValues("c")));

        // test for non-existent option
        assertNull(commandLine.getOptionValues("nonExistentOption"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();

        // test with no args
        assertArrayEquals(new String[0], commandLine.getArgs());

        // test with one arg
        commandLine.addArg("test");
        assertArrayEquals(new String[]{"test"}, commandLine.getArgs());

        // test with multiple args
        commandLine.addArg("test1");
        commandLine.addArg("--base");
        commandLine.addArg("test2");
        commandLine.addArg("--colors");
        commandLine.addArg("red");
        commandLine.addArg("--colors");
        commandLine.addArg("green");
        commandLine.addArg("--colors");
        commandLine.addArg("blue");
        String[] args = {"test1", "--base", "test2", "--colors", "red", "--colors", "green", "--colors", "blue"};
        assertArrayEquals(args, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();

        // test with no args
        List<String> argList = commandLine.getArgList();
        assertTrue(argList.isEmpty());

        // test with one arg
        commandLine.addArg("test");
        List<String> expectedArgList = Arrays.asList("test");
        assertEquals(expectedArgList, commandLine.getArgList());

        // test with multiple args
        commandLine.addArg("test1");
        commandLine.addArg("--base");
        commandLine.addArg("test2");
        commandLine.addArg("--colors");
        commandLine.addArg("red");
        commandLine.addArg("--colors");
        commandLine.addArg("green");
        commandLine.addArg("--colors");
        commandLine.addArg("blue");
        List<String> args = Arrays.asList("test1", "--base", "test2", "--colors", "red", "--colors", "green", "--colors", "blue");
        assertEquals(args, commandLine.getArgList());
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();

        // test with no options
        Option[] expectedOptions = {};
        Option[] options = commandLine.getOptions();
        assertEquals(expectedOptions, options);

        // test with single option
        commandLine.addOption(new Option("a", "append", false, "Append to a file instead of overwriting it."));
        options = commandLine.getOptions();
        Option option = new Option("a", "append", false, "Append to a file instead of overwriting it.");
        Option[] expectedOptions1 = {option};
        assertEquals(expectedOptions1, options);

        // test with multiple options
        commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "append", false, "Append to a file instead of overwriting it."));
        commandLine.addOption(new Option("b", "base", true, "Base directory."));
        options = commandLine.getOptions();
        Option optionA = new Option("a", "append", false, "Append to a file instead of overwriting it.");
        Option optionB = new Option("b", "base", true, "Base directory.");
        Option[] expectedOptions2 = {optionA, optionB};
        assertEquals(expectedOptions2, options);
    }
}