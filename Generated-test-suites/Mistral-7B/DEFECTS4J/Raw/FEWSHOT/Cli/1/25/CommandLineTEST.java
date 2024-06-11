package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertLineEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCreateCommandLineInstance() {
        CommandLine cmdLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        CommandLine cmdLine = new CommandLine();

        cmdLine.addOption(new Option("a", "test", false, "Test option a"));
        assertTrue(cmdLine.hasOption("a"));
        assertTrue(cmdLine.hasOption('a'));

        assertFalse(cmdLine.hasOption("b"));
        assertFalse(cmdLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmdLine = new CommandLine();

        Option option = new Option("a", "test", false, "Test option a");
        cmdLine.addOption(option);

        String optionValue = "test value";
        assertEquals(null, cmdLine.getOptionObject("a"));

        cmdLine.addArg(optionValue);

        assertEquals(String.class, option.getType());
        assertEquals(optionValue, cmdLine.getOptionObject("a"));

        Option option1 = new Option("b", "test1", false, "Test option b");
        Option option2 = new Option("b", "test2", false, "Test option b");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        assertNull(cmdLine.getOptionObject("b"));
        assertNull(cmdLine.getOptionObject('b'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmdLine = new CommandLine();

        Option option = new Option("a", "test", false, "Test option a");
        cmdLine.addOption(option);

        String optionValue = "test value";
        cmdLine.addArg(optionValue);

        assertEquals(optionValue, cmdLine.getOptionValue("a"));
        assertEquals(optionValue, cmdLine.getOptionValue('a'));

        Option option1 = new Option("b", "test1", false, "Test option b");
        Option option2 = new Option("b", "test2", false, "Test option b");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        assertNull(cmdLine.getOptionValue("b"));
        assertNull(cmdLine.getOptionValue('b'));

        cmdLine.addArg("test value 1");
        cmdLine.addArg("test value 2");

        assertEquals(optionValue, cmdLine.getOptionValue("a"));
        assertEquals(optionValue, cmdLine.getOptionValue('a'));

        assertEquals("test value 1", cmdLine.getOptionValue("b"));
        assertEquals("test value 2", cmdLine.getOptionValue('b'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmdLine = new CommandLine();

        Option option = new Option("a", "test", false, "Test option a");
        cmdLine.addOption(option);

        String optionValue1 = "test value 1";
        cmdLine.addArg(optionValue1);

        String optionValue2 = "test value 2";
        cmdLine.addArg(optionValue2);

        String[] expectedValues = { optionValue1, optionValue2 };
        assertArrayEquals(expectedValues, cmdLine.getOptionValues("a"));
        assertArrayEquals(expectedValues, cmdLine.getOptionValues('a'));

        Option option1 = new Option("b", "test1", false, "Test option b");
        Option option2 = new Option("b", "test2", false, "Test option b");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        String[] expectedValues1 = {};
        assertArrayEquals(expectedValues1, cmdLine.getOptionValues("b"));
        assertArrayEquals(expectedValues1, cmdLine.getOptionValues('b'));

        cmdLine.addArg("test value 1");
        cmdLine.addArg("test value 2");

        String[] expectedValues2 = { "test value 1", "test value 2" };
        String[] actualValues = cmdLine.getOptionValues("b");
        assertArrayEquals(expectedValues2, actualValues);
        assertArrayEquals(expectedValues2, cmdLine.getOptionValues('b'));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmdLine = new CommandLine();

        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");

        String[] expectedArgs = { "arg1", "arg2" };
        assertArrayEquals(expectedArgs, cmdLine.getArgs());

        cmdLine.addOption(new Option("a", "test", false, "Test option a"));
        cmdLine.addArg("arg3");

        List expectedArgList = Arrays.asList("arg1", "arg2", "arg3");
        assertEquals(expectedArgList, cmdLine.getArgList());
    }

    @Test
    public void testToString() {
        CommandLine cmdLine = new CommandLine();

        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");

        Option option = new Option("a", "test", false, "Test option a");
        cmdLine.addOption(option);

        Option option1 = new Option("b", "test1", false, "Test option b1");
        Option option2 = new Option("b", "test2", false, "Test option b2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("[ CommandLine: [ options: {a=Test option a, b=Test option b1, b=Test option b2} ] [ args: [arg1, arg2] ] ]");

        String actualOutput = cmdLine.toString();
        assertLineEquals(expectedOutput.toString(), actualOutput);
    }

    @Test
    public void testAddArg() {
        CommandLine cmdLine = new CommandLine();

        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");

        cmdLine.addArg("arg3");

        String[] expectedArgs = { "arg1", "arg2", "arg3" };
        assertArrayEquals(expectedArgs, cmdLine.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmdLine = new CommandLine();

        Option option = new Option("a", "test", false, "Test option a");
        cmdLine.addOption(option);

        Option option1 = new Option("b", "test1", false, "Test option b1");
        Option option2 = new Option("b", "test2", false, "Test option b2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        assertEquals(3, cmdLine.getOptions().length);
        assertEquals("a", cmdLine.getOptions()[0].getLongOpt());
        assertEquals("b", cmdLine.getOptions()[1].getLongOpt());
        assertEquals("b", cmdLine.getOptions()[2].getLongOpt());
    }

    @Test
    public void testIterator() {
        CommandLine cmdLine = new CommandLine();

        Option option = new Option("a", "test", false, "Test option a");
        cmdLine.addOption(option);

        Option option1 = new Option("b", "test1", false, "Test option b1");
        Option option2 = new Option("b", "test2", false, "Test option b2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        Iterator iterator = cmdLine.iterator();

        Option option3 = new Option("c", "test3", false, "Test option c");
        cmdLine.addOption(option3);

        assertTrue(iterator.hasNext());
        assertEquals(option, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option3, iterator.next());
        assertFalse(iterator.hasNext());
    }
}