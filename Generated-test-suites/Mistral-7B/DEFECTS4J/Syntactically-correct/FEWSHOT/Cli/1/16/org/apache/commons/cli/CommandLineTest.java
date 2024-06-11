package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testNewCommandLine() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getArgList());
        assertNull(commandLine.getOptions());
    }

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "a", false, "a help message"));
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption('a'));
        assertFalse(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "a", false, "a help message"));
        commandLine.addOption(new Option("a", "a", true, "a help message"));
        commandLine.addArg("foo");
        assertNull(commandLine.getOptionValue("b"));
        assertNull(commandLine.getOptionValue('b'));
        assertNull(commandLine.getOptionValue("a"));
        assertNull(commandLine.getOptionValue('a'));
        commandLine.addArg("--a=bar");
        assertEquals("bar", commandLine.getOptionValue("a"));
        assertEquals("bar", commandLine.getOptionValue('a'));
        assertEquals("bar", commandLine.getOptionValue("a", "default"));
        assertEquals("bar", commandLine.getOptionValue('a', "default"));
        assertEquals("default", commandLine.getOptionValue("b", "default"));
        assertEquals("default", commandLine.getOptionValue('b', "default"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "a", false, "a help message"));
        commandLine.addOption(new Option("a", "a", true, "a help message"));
        commandLine.addArg("--a=foo");
        commandLine.addArg("--a=bar");
        String[] values = commandLine.getOptionValues("a");
        assertArrayEquals(new String[]{"foo", "bar"}, values);
        String[] values2 = commandLine.getOptionValues('a');
        assertArrayEquals(new String[]{"foo", "bar"}, values2);
        String[] values3 = commandLine.getOptionValues("b");
        assertArrayEquals(new String[0], values3);
        String[] values4 = commandLine.getOptionValues('b');
        assertArrayEquals(new String[0], values4);
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "a", false, "a help message"));
        commandLine.addOption(new Option("b", "b", false, "b help message"));
        List<Option> options = Arrays.asList(commandLine.getOptions());
        List<Option> expected = Arrays.asList(
                new Option("a", "a", false, "a help message"),
                new Option("b", "b", false, "b help message")
        );
        assertEquals(expected, options);
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("foo");
        List<String> args = (List<String>) commandLine.getArgList();
        assertEquals(1, args.size());
        assertEquals("foo", args.get(0));
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "a", false, "a help message");
        commandLine.addOption(option);
        Option option2 = new Option("b", "b", false, "b help message");
        commandLine.addOption(option2);
        List<Option> options = Arrays.asList(commandLine.getOptions());
        List<Option> expected = Arrays.asList(option, option2);
        assertEquals(expected, options);
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        Option option1 = new Option("a", "a", false, "a help message");
        Option option2 = new Option("b", "b", false, "b help message");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Iterator<Option> iterator = commandLine.iterator();
        List<Option> expected = Arrays.asList(option1, option2);
        List<Option> options = new ArrayList<>();
        while (iterator.hasNext()) {
            options.add(iterator.next());
        }
        assertEquals(expected, options);
    }

    @Test
    public void testToString() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("--foo");
        commandLine.addArg("bar");
        commandLine.addOption(new Option("a", "a", false, "a help message"));
        commandLine.addOption(new Option("b", "b", false, "b help message"));
        commandLine.addOption(new Option("c", "c", true, "c help message", new String[]{"foo", "bar"}));
        commandLine.addOption(new Option("d", "d", true, "d help message"));
        String expected = "[ CommandLine: [ options: [Option(name=a, shortName=a, optional=true, valueSeparator=, usage=a help message), Option(name=b, shortName=b, optional=true, valueSeparator=, usage=b help message), Option(name=c, shortName=c, optional=false, valueSeparator=, usage=c help message), Option(name=d, shortName=d, optional=true, valueSeparator=, usage=d help message), Option(name=d, shortName=d, optional=true, valueSeparator=, usage=d help message)] "
                + "[ args: [--foo, bar, --c=foo, bar, --d, --d]] ]";
        assertEquals(expected, commandLine.toString());
    }
}