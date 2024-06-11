package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testCreateCommandLine() {
        CommandLine cmd = new CommandLine();
        assertEquals(Collections.emptyList(), cmd.getArgs());
        assertEquals(Collections.emptyMap(), cmd.getOptions());
    }

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("option"));
        CommandLine cmdWithOption = new CommandLine();
        Option option = new Option("option", true, false, "value");
        cmdWithOption.addOption(option);
        assertTrue(cmdWithOption.hasOption("option"));
    }

    @Test
    public void testHasOptionWithChar() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption('o'));
        CommandLine cmdWithOption = new CommandLine();
        Option option = new Option('o', true, false, "value");
        cmdWithOption.addOption(option);
        assertTrue(cmdWithOption.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("option"));

        Option option = new Option("option", String.class, false, "value");
        CommandLine cmdWithOption = new CommandLine();
        cmdWithOption.addOption(option);
        assertEquals("value", cmdWithOption.getOptionObject("option"));

        Option optionWithArrayType = new Option("option", String[].class, false, "value");
        CommandLine cmdWithOptionArray = new CommandLine();
        cmdWithOptionArray.addOption(optionWithArrayType);
        String[] expectedArray = new String[]{"value1", "value2"};
        cmdWithOptionArray.addArg("value1");
        cmdWithOptionArray.addArg("value2");
        assertArrayEquals(expectedArray, cmdWithOptionArray.getOptionObject("option"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("option"));

        Option option = new Option("option", true, false, "value");
        CommandLine cmdWithOption = new CommandLine();
        cmdWithOption.addOption(option);
        assertEquals("value", cmdWithOption.getOptionValue("option"));

        Option optionWithArg = new Option("option", true, false, null);
        CommandLine cmdWithOptionArg = new CommandLine();
        cmdWithOptionArg.addOption(optionWithArg);
        cmdWithOptionArg.addArg("arg");
        assertEquals("arg", cmdWithOptionArg.getOptionValue("option"));

        Option optionWithArgAndValue = new Option("option", true, false, "defaultValue");
        CommandLine cmdWithOptionArgAndValue = new CommandLine();
        cmdWithOptionArgAndValue.addOption(optionWithArgAndValue);
        assertEquals("defaultValue", cmdWithOptionArgAndValue.getOptionValue("option"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("option"));

        Option option = new Option("option", true, false, null);
        CommandLine cmdWithOption = new CommandLine();
        cmdWithOption.addOption(option);
        cmdWithOption.addArg("arg1");
        cmdWithOption.addArg("arg2");
        String[] expectedArray = new String[]{"arg1", "arg2"};
        assertArrayEquals(expectedArray, cmdWithOption.getOptionValues("option"));
    }

    @Test
    public void testGetOptionValuesWithChar() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues('o'));

        Option option = new Option('o', true, false, null);
        CommandLine cmdWithOption = new CommandLine();
        cmdWithOption.addOption(option);
        cmdWithOption.addArg("arg1");
        cmdWithOption.addArg("arg2");
        String[] expectedArray = new String[]{"arg1", "arg2"};
        assertArrayEquals(expectedArray, cmdWithOption.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueWithDefaultValue() {
        CommandLine cmd = new CommandLine();
        assertEquals(null, cmd.getOptionValue("option", "defaultValue"));

        Option option = new Option("option", true, false, null);
        CommandLine cmdWithOption = new CommandLine();
        cmdWithOption.addOption(option);
        cmdWithOption.addArg("arg");
        assertEquals("arg", cmdWithOption.getOptionValue("option", "defaultValue"));
    }

    @Test
    public void testGetOptionValueWithCharAndDefaultValue() {
        CommandLine cmd = new CommandLine();
        assertEquals(null, cmd.getOptionValue('o', "defaultValue"));

        Option option = new Option('o', true, false, null);
        CommandLine cmdWithOption = new CommandLine();
        cmdWithOption.addOption(option);
        cmdWithOption.addArg("arg");
        assertEquals("arg", cmdWithOption.getOptionValue('o', "defaultValue"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        String[] expectedArray = new String[0];
        assertArrayEquals(expectedArray, cmd.getArgs());

        String[] args = new String[]{"arg1", "arg2"};
        CommandLine cmdWithArgs = new CommandLine();
        for (String arg : args) {
            cmdWithArgs.addArg(arg);
        }
        String[] actualArray = cmdWithArgs.getArgs();
        assertArrayEquals(args, actualArray);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        List<String> expectedList = new LinkedList<>();
        assertSame(expectedList, cmd.getArgList());

        List<String> args = Arrays.asList("arg1", "arg2");
        CommandLine cmdWithArgs = new CommandLine();
        for (String arg : args) {
            cmdWithArgs.addArg(arg);
        }
        List<String> actualList = cmdWithArgs.getArgList();
        assertSame(args, actualList);
    }

    @Test
    public void testToString() {
        CommandLine cmd = new CommandLine();
        String expectedString = "[ CommandLine: [ options: {} ] [ args: [] ] ]";
        assertEquals(expectedString, cmd.toString());

        Option option = new Option("option", true, false, null);
        CommandLine cmdWithOption = new CommandLine();
        cmdWithOption.addOption(option);
        String arg1 = "arg1";
        cmdWithOption.addArg(arg1);
        String arg2 = "arg2";
        cmdWithOption.addArg(arg2);
        String expectedStringWithArgs = "[ CommandLine: [ options: {option=Option()} ] [ args: [arg1, arg2] ] ]";
        assertEquals(expectedStringWithArgs, cmdWithOption.toString());
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg");
        List<String> expectedList = new LinkedList<>();
        expectedList.add("arg");
        List<String> actualList = cmd.getArgList();
        assertSame(expectedList, actualList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddArgWithNullArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg(null);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("option", true, false, null);
        cmd.addOption(option);
        Option expectedOption = option;
        Option actualOption = cmd.getOptions()[0];
        assertSame(expectedOption, actualOption);
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("option1", true, false, null);
        Option option2 = new Option("option2", true, false, null);
        cmd.addOption(option1);
        cmd.addOption(option2);
        Option[] expectedOptions = new Option[]{option1, option2};
        Iterator<Option> iterator = cmd.iterator();
        Option[] actualOptions = new Option[2];
        for (int i = 0; i < 2; i++) {
            actualOptions[i] = iterator.next();
        }
        assertArrayEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("option1", true, false, null);
        Option option2 = new Option("option2", true, false, null);
        cmd.addOption(option1);
        cmd.addOption(option2);
        Option[] expectedOptions = new Option[]{option1, option2};
        Option[] actualOptions = cmd.getOptions();
        assertArrayEquals(expectedOptions, actualOptions);
    }
}