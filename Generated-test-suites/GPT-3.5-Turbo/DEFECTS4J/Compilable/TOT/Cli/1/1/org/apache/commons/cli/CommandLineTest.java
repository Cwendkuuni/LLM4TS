package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        option1 = new Option("t", "test", false, "test option");
        option2 = new Option("v", "verbose", true, "verbose option");
    }

    @Test
    public void testHasOption_withValidShortOption_returnsTrue() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption("t"));
    }

    @Test
    public void testHasOption_withValidLongOption_returnsTrue() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testHasOption_withInvalidOption_returnsFalse() {
        assertFalse(commandLine.hasOption("invalid"));
    }

    @Test
    public void testHasOption_withDuplicateOptions_returnsTrue() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption("t"));
        assertTrue(commandLine.hasOption("v"));
    }

    @Test
    public void testHasOption_withNoOptions_returnsFalse() {
        assertFalse(commandLine.hasOption("t"));
    }

    @Test
    public void testGetOptionObject_withValidShortOption_returnsOptionObject() {
        commandLine.addOption(option1);
        assertSame(option1, commandLine.getOptionObject("t"));
    }

    @Test
    public void testGetOptionObject_withValidLongOption_returnsOptionObject() {
        commandLine.addOption(option1);
        assertSame(option1, commandLine.getOptionObject("test"));
    }

    @Test
    public void testGetOptionObject_withInvalidOption_returnsNull() {
        assertNull(commandLine.getOptionObject("invalid"));
    }

    @Test
    public void testGetOptionValue_withValidShortOption_returnsOptionValue() {
        commandLine.addOption(option2);
        commandLine.addArg("value1");
        assertEquals("value1", commandLine.getOptionValue("v"));
    }

    @Test
    public void testGetOptionValue_withValidLongOption_returnsOptionValue() {
        commandLine.addOption(option2);
        commandLine.addArg("value1");
        assertEquals("value1", commandLine.getOptionValue("verbose"));
    }

    @Test
    public void testGetOptionValue_withInvalidOption_returnsNull() {
        assertNull(commandLine.getOptionValue("invalid"));
    }

    @Test
    public void testGetOptionValues_withValidShortOption_returnsOptionValues() {
        commandLine.addOption(option2);
        commandLine.addArg("value1");
        commandLine.addArg("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("v"));
    }

    @Test
    public void testGetOptionValues_withValidLongOption_returnsOptionValues() {
        commandLine.addOption(option2);
        commandLine.addArg("value1");
        commandLine.addArg("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("verbose"));
    }

    @Test
    public void testGetOptionValues_withInvalidOption_returnsNull() {
        assertNull(commandLine.getOptionValues("invalid"));
    }

    @Test
    public void testGetOptionValue_withDefault_withValidOption_returnsOptionValue() {
        commandLine.addOption(option2);
        commandLine.addArg("value");
        assertEquals("value", commandLine.getOptionValue("v", "default"));
    }

    @Test
    public void testGetOptionValue_withDefault_withInvalidOption_returnsDefaultValue() {
        assertEquals("default", commandLine.getOptionValue("invalid", "default"));
    }

    @Test
    public void testGetArgs_withArguments_returnsArgumentsArray() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testGetArgs_withNoArguments_returnsEmptyArray() {
        assertArrayEquals(new String[]{}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList_withArguments_returnsArgumentsList() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertEquals(2, commandLine.getArgList().size());
    }

    @Test
    public void testGetArgList_withNoArguments_returnsEmptyList() {
        assertTrue(commandLine.getArgList().isEmpty());
    }

    @Test
    public void testAddArg_withArgument_addsToArgumentsList() {
        commandLine.addArg("arg1");
        assertTrue(commandLine.getArgList().contains("arg1"));
    }

    @Test
    public void testAddOption_withOption_addsToOptionsMap() {
        commandLine.addOption(option1);
        assertNotNull(commandLine.getOptionObject("t"));
    }

    @Test
    public void testIterator_withOptions_returnsIteratorOverOptions() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().length);
    }
}