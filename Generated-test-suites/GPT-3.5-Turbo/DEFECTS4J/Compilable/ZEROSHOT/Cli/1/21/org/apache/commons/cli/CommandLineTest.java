package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("opt"));
        assertFalse(commandLine.hasOption('o'));
        commandLine.addOption(new Option("opt", false, "Option with no argument"));
        assertTrue(commandLine.hasOption("opt"));
        assertTrue(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("opt"));
        assertNull(commandLine.getOptionObject('o'));
        Option option = new Option("opt", false, "Option with no argument");
        option.setType(Integer.class);
        commandLine.addOption(option);
        assertNull(commandLine.getOptionObject("opt"));
        assertNull(commandLine.getOptionObject('o'));
        commandLine.addArg("123");
        assertEquals(Integer.valueOf(123), commandLine.getOptionObject("opt"));
        assertEquals(Integer.valueOf(123), commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("opt"));
        assertNull(commandLine.getOptionValue('o'));
        Option option = new Option("opt", true, "Option with argument");
        commandLine.addOption(option);
        assertNull(commandLine.getOptionValue("opt"));
        assertNull(commandLine.getOptionValue('o'));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue("opt"));
        assertEquals("value", commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("opt"));
        assertNull(commandLine.getOptionValues('o'));
        Option option = new Option("opt", true, "Option with argument");
        commandLine.addOption(option);
        assertNull(commandLine.getOptionValues("opt"));
        assertNull(commandLine.getOptionValues('o'));
        commandLine.addArg("value1");
        commandLine.addArg("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("opt"));
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueWithDefaultValue() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue("opt", "default"));
        assertEquals("default", commandLine.getOptionValue('o', "default"));
        Option option = new Option("opt", true, "Option with argument");
        commandLine.addOption(option);
        assertEquals("default", commandLine.getOptionValue("opt", "default"));
        assertEquals("default", commandLine.getOptionValue('o', "default"));
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue("opt", "default"));
        assertEquals("value", commandLine.getOptionValue('o', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[]{}, commandLine.getArgs());
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.getArgList().size());
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertEquals(2, commandLine.getArgList().size());
        assertEquals("arg1", commandLine.getArgList().get(0));
        assertEquals("arg2", commandLine.getArgList().get(1));
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("opt"));
        Option option = new Option("opt", false, "Option with no argument");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("opt"));
        assertSame(option, commandLine.getOptions()[0]);
    }
}