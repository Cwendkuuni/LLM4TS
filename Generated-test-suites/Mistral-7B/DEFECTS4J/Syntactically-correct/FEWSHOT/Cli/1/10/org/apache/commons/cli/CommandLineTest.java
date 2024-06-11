package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCreateEmptyCommandLine() {
        CommandLine commandLine = new CommandLine();
        List args = commandLine.getArgList();
        Map options = commandLine.getOptions();
        assertEquals(0, args.size());
        assertTrue(options.isEmpty());
    }

    @Test
    public void testGetOptionValueForBooleanOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("f", "foo", false, "test"));
        commandLine.parse("--foo");
        assertFalse(commandLine.hasOption("f"));
        assertNull(commandLine.getOptionValue("f"));
        commandLine.addOption(new Option("f", "foo", false, "test"));
        commandLine.parse("--foo");
        assertTrue(commandLine.hasOption("f"));
        assertNull(commandLine.getOptionValue("f"));
        commandLine.parse("--foo --bar");
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("bar"));
        assertNull(commandLine.getOptionValue("f"));
        assertNull(commandLine.getOptionValue("bar"));
    }

    @Test
    public void testGetOptionValueForStringOptionWithNoArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", true, "default"));
        commandLine.parse("--apple");
        assertEquals("default", commandLine.getOptionValue("a"));
        commandLine.parse("--apple value");
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValueForStringOptionWithArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", true, "default"));
        commandLine.parse("--apple value");
        assertEquals("value", commandLine.getOptionValue("a"));
        commandLine.parse("--apple value1 value2");
        String[] values = commandLine.getOptionValues("a");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
        commandLine.parse("--apple value1 value2 --banana");
        values = commandLine.getOptionValues("a");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
        assertEquals("value1", commandLine.getOptionValue("a", "defaultValue"));
        assertEquals("value2", commandLine.getOptionValue("a", "defaultValue2"));
    }

    @Test
    public void testGetOptionObjectForBooleanOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("f", "foo", false, "test"));
        commandLine.parse("--foo");
        assertNull(commandLine.getOptionObject("f"));
        commandLine.parse("--foo");
        assertNull(commandLine.getOptionObject("f"));
        commandLine.parse("--foo --bar");
        assertNull(commandLine.getOptionObject("f"));
        assertNull(commandLine.getOptionObject("bar"));
        assertNull(commandLine.getOptionObject('f'));
        assertNull(commandLine.getOptionObject('b'));
        commandLine.parse("--foo --bar --baz");
        Option optionF = commandLine.getOption("f");
        assertEquals(OptionType.NO_ARGUMENT, optionF.getType());
        assertEquals("foo", optionF.getLongOpt());
        assertEquals('f', optionF.getOpt());
        assertEquals("test", optionF.getDescription());
        assertNull(optionF.getValue());
        Option optionBaz = commandLine.getOption("baz");
        assertEquals(OptionType.NO_ARGUMENT, optionBaz.getType());
        assertEquals("baz", optionBaz.getLongOpt());
        assertEquals('b', optionBaz.getOpt());
        assertEquals("default", optionBaz.getDescription());
        assertNull(optionBaz.getValue());
    }

    @Test
    public void testGetOptionObjectForStringOptionWithNoArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", true, "default"));
        commandLine.parse("--apple");
        Option optionA = commandLine.getOption("a");
        assertEquals(OptionType.VALUE_OPTION, optionA.getType());
        assertEquals("apple", optionA.getLongOpt());
        assertEquals('a', optionA.getOpt());
        assertEquals("test", optionA.getDescription());
        assertNull(optionA.getValue());
    }

    @Test
    public void testGetOptionObjectForStringOptionWithArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", true, "default"));
        commandLine.parse("--apple value1 value2");
        Option optionA = commandLine.getOption("a");
        assertEquals(OptionType.VALUE_OPTION, optionA.getType());
        assertEquals("apple", optionA.getLongOpt());
        assertEquals('a', optionA.getOpt());
        assertEquals("test", optionA.getDescription());
        String[] values = optionA.getValue();
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("f", "foo", false, "test"));
        commandLine.addOption(new Option("a", "apple", true, "default"));
        commandLine.parse("--foo --apple value1 value2 --banana");
        Option[] options = commandLine.getOptions();
        List expectedOptions = new LinkedList();
        expectedOptions.add(commandLine.getOption("f"));
        expectedOptions.add(commandLine.getOption("a"));
        expectedOptions.add(commandLine.getOption("banana"));
        assertArrayEquals((Object[])expectedOptions.toArray(new Object[expectedOptions.size()]), options);
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--foo --apple value1 value2 --banana value3 value4");
        String[] args = commandLine.getArgs();
        List expectedArgs = new LinkedList();
        expectedArgs.add("--banana");
        expectedArgs.add("value3");
        expectedArgs.add("value4");
        assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), args);
    }

    @Test
    public void testGetStringValuesForOptionWithLongOpt() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("longOption", "longOpt", true, "default"));
        commandLine.parse("--longOpt value1 value2");
        String[] values = commandLine.getOptionValues("longOption");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    public void testGetStringValuesForOptionWithShortOpt() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("o", "option", true, "default"));
        commandLine.parse("--o value1 value2");
        String[] values = commandLine.getOptionValues("o");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test
    public void testGetStringValuesForOptionWithNoArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("o", "option", false, "default"));
        commandLine.parse("--o");
        String[] values = commandLine.getOptionValues("o");
        assertArrayEquals(new String[0], values);
    }

    @Test
    public void testGetStringValuesForOptionThatDoesNotExist() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--nonexistentOption");
        String[] values = commandLine.getOptionValues("nonexistentOption");
        assertArrayEquals(new String[0], values);
    }

    @Test
    public void testGetStringValuesForOptionWithMultipleArguments() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("o", "option", true, "default"));
        commandLine.parse("--o value1 value2 value3");
        String[] values = commandLine.getOptionValues("o");
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, values);
    }

    @Test
    public void testGetStringValuesForOptionWithEmptyArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("o", "option", true, "default"));
        commandLine.parse("--o");
        String[] values = commandLine.getOptionValues("o");
        assertArrayEquals(new String[0], values);
        commandLine.parse("--o value1");
        values = commandLine.getOptionValues("o");
        assertArrayEquals(new String[]{"value1"}, values);
        commandLine.parse("--o value1 value2");
        values = commandLine.getOptionValues("o");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
        commandLine.parse("--o value1 value2 value3 --o");
        values = commandLine.getOptionValues("o");
        assertArrayEquals(new String[]{"value1", "value2", "value3", ""}, values);
    }

    @Test
    public void testHasOptionForBooleanOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("f", "foo", false, "test"));
        assertFalse(commandLine.hasOption("f"));
        commandLine.parse("--foo");
        assertTrue(commandLine.hasOption("f"));
    }

    @Test
    public void testHasOptionForStringOptionWithNoArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", true, "default"));
        assertFalse(commandLine.hasOption("a"));
        commandLine.parse("--apple");
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOptionForStringOptionWithArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", true, "default"));
        assertFalse(commandLine.hasOption("a"));
        commandLine.parse("--apple value1 value2");
        assertTrue(commandLine.hasOption("a"));
        commandLine.parse("--apple value1");
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOptionForNonexistentOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("nonexistentOption"));
    }

    @Test
    public void testGetOptionValueForBooleanOptionWithDefaultValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("f", "foo", false, "defaultValue"));
        commandLine.parse("--foo");
        assertFalse(commandLine.hasOption("f"));
        assertNull(commandLine.getOptionValue("f"));
        commandLine.parse("--foo");
        assertTrue(commandLine.hasOption("f"));
        assertNull(commandLine.getOptionValue("f"));
        commandLine.parse("--foo --bar");
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("bar"));
        assertNull(commandLine.getOptionValue("f"));
        assertNull(commandLine.getOptionValue("bar"));
        commandLine.parse("--foo --bar --baz");
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("bar"));
        assertTrue(commandLine.hasOption("baz"));
        assertNull(commandLine.getOptionValue("f"));
        assertNull(commandLine.getOptionValue("bar"));
        assertNull(commandLine.getOptionValue("baz"));
    }

    @Test
    public void testGetOptionValueForStringOptionWithNoArgumentAndDefaultValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", true, "defaultValue"));
        commandLine.parse("--apple");
        assertEquals("defaultValue", commandLine.getOptionValue("a"));
        commandLine.parse("--apple value");
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValueForStringOptionWithArgumentAndDefaultValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "apple", true, "defaultValue"));
        commandLine.parse("--apple value1 value2");
        String[] values = commandLine.getOptionValues("a");
        assertArrayEquals(new String[]{"value1", "value2"}, values);
        assertEquals("value1 value2", commandLine.getOptionValue("a", "defaultValue"));
        assertEquals("value1", commandLine.getOptionValue("a", "defaultValue1"));
    }

    @Test
    public void testGetOptionValueForNonexistentOption() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("nonexistentOption"));
    }

    @Test
    public void testGetOptionValueForStringOptionWithEmptyArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("o", "option", true, "default"));
        commandLine.parse("--o");
        assertNull(commandLine.getOptionValue("o"));
        commandLine.parse("--o value1");
        assertEquals("value1", commandLine.getOptionValue("o"));
        commandLine.parse("--o value1 value2");
        assertEquals("value1", commandLine.getOptionValue("o"));
        commandLine.parse("--o value1 value2 value3 --o");
        assertEquals("value1 value2 value3", commandLine.getOptionValue("o", "default"));
    }

    @Test
    public void testGetOptionValueForStringOptionWithArgumentsAndDefaultValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("o", "option", true, "default1 default2 default3"));
        commandLine.parse("--o value1 value2");
        assertEquals("value1 value2", commandLine.getOptionValue("o"));
        assertEquals("value1 value2", commandLine.getOptionValue("o", "default1 default2 default3"));
        commandLine.parse("--o value1 value2 value3");
        assertEquals("value1 value2 value3", commandLine.getOptionValue("o"));
        assertEquals("value1 value2 value3", commandLine.getOptionValue("o", "default1 default2 default3"));
        commandLine.parse("--o value1 value2 value3 value4");
        assertEquals("value1 value2 value3 value4", commandLine.getOptionValue("o", "default1 default2 default3"));
    }
}