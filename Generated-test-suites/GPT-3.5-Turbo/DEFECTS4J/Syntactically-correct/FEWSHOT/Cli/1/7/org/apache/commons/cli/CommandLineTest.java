package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {
    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOptionString() {
        assertFalse(commandLine.hasOption("foo"));
        assertFalse(commandLine.hasOption("-f"));
        assertFalse(commandLine.hasOption("--foo"));
    }

    @Test
    public void testHasOptionChar() {
        assertFalse(commandLine.hasOption('f'));
    }

    @Test
    public void testGetOptionObjectString() {
        assertNull(commandLine.getOptionObject("foo"));
        assertNull(commandLine.getOptionObject("-f"));
        assertNull(commandLine.getOptionObject("--foo"));
    }

    @Test
    public void testGetOptionObjectChar() {
        assertNull(commandLine.getOptionObject('f'));
    }

    @Test
    public void testGetOptionValueString() {
        assertNull(commandLine.getOptionValue("foo"));
        assertNull(commandLine.getOptionValue("-f"));
        assertNull(commandLine.getOptionValue("--foo"));
    }

    @Test
    public void testGetOptionValueChar() {
        assertNull(commandLine.getOptionValue('f'));
    }

    @Test
    public void testGetOptionValuesString() {
        assertNull(commandLine.getOptionValues("foo"));
        assertNull(commandLine.getOptionValues("-f"));
        assertNull(commandLine.getOptionValues("--foo"));
    }

    @Test
    public void testGetOptionValuesChar() {
        assertNull(commandLine.getOptionValues('f'));
    }

    @Test
    public void testGetOptionValueStringString() {
        assertEquals("default", commandLine.getOptionValue("foo", "default"));
        assertEquals("default", commandLine.getOptionValue("-f", "default"));
        assertEquals("default", commandLine.getOptionValue("--foo", "default"));
    }

    @Test
    public void testGetOptionValueCharString() {
        assertEquals("default", commandLine.getOptionValue('f', "default"));
    }

    @Test
    public void testGetArgs() {
        String[] args = commandLine.getArgs();
        assertEquals(0, args.length);
    }

    @Test
    public void testGetArgList() {
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] args = commandLine.getArgs();
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "foo", true, "Foo option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("foo"));
        assertEquals(option, commandLine.getOptionObject("f"));
        assertEquals(option, commandLine.getOptionObject("foo"));
        assertEquals("value1", commandLine.getOptionValue("f", "default"));
        assertEquals("value1", commandLine.getOptionValue("foo", "default"));
        assertEquals(new String[] {"value1", "value2"}, commandLine.getOptionValues("f"));
        assertEquals(new String[] {"value1", "value2"}, commandLine.getOptionValues("foo"));
        assertEquals(new String[] {"value1", "value2"}, commandLine.getOptionValues('f'));
        assertEquals(new String[] {"value1", "value2"}, commandLine.getOptionValues('f'));
        assertEquals("value1", commandLine.getOptionValue("f"));
        assertEquals("value1", commandLine.getOptionValue("foo"));
    }

    @Test
    public void testIterator() {
        Option option1 = new Option("f", "foo", true, "Foo option");
        Option option2 = new Option("b", "bar", true, "Bar option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Iterator<Option> iterator = commandLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "foo", true, "Foo option");
        Option option2 = new Option("b", "bar", true, "Bar option");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Option[] options = commandLine.getOptions();
        assertEquals(2, options.length);
        assertEquals(option1, options[0]);
        assertEquals(option2, options[1]);
    }
}