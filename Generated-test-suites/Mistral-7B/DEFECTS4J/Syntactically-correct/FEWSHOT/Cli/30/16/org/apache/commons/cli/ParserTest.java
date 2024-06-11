package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParseSingleOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "A test option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"-o"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionCmd = optionsList.get(0);
        assertEquals("o", optionCmd.getOpt());
        assertFalse(optionCmd.getValue().isPresent());
        assertEquals("A test option", optionCmd.getLongOpt());
    }

    @Test
    public void testParseSingleValueOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionCmd = optionsList.get(0);
        assertEquals("o", optionCmd.getOpt());
        assertEquals("value", optionCmd.getValue().get());
        assertEquals("An option with value", optionCmd.getLongOpt());
    }

    @Test
    public void testParseMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2 with value");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new DefaultParser();
        String[] arguments = {"-o1", "-o2", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option option1Cmd = optionsList.get(0);
        assertEquals("o1", option1Cmd.getOpt());
        assertFalse(option1Cmd.getValue().isPresent());
        assertEquals("Option 1", option1Cmd.getLongOpt());
        Option option2Cmd = optionsList.get(1);
        assertEquals("o2", option2Cmd.getOpt());
        assertEquals("value", option2Cmd.getValue().get());
        assertEquals("Option 2 with value", option2Cmd.getLongOpt());
    }

    @Test
    public void testParseMultipleValuesOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with multiple values");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "value1", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.getArgs().isEmpty());
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionCmd = optionsList.get(0);
        assertEquals("o", optionCmd.getOpt());
        List<String> values = optionCmd.getValue();
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testParseMultipleArgs() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "value1", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> argsList = cmd.getArgs();
        assertEquals(2, argsList.size());
        assertEquals("arg1", argsList.get(0));
        assertEquals("arg2", argsList.get(1));
    }

    @Test
    public void testParseRequiredOptionMissing() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "A required option");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseMissingValueOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"-o"};
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseProperties() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with value");
        options.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("o", "value1");
        properties.setProperty("o", "value2");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionCmd = optionsList.get(0);
        List<String> values = optionCmd.getValue();
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testParseWithGroup() {
        OptionGroup group = new OptionGroup("g", "Group");
        Option option = new Option("o", "option", true, "An option with value", group);
        group.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        String[] arguments = {"-g", "-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionCmd = optionsList.get(0);
        List<String> values = optionCmd.getValue();
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
        OptionGroup groupCmd = cmd.getOptionGroup();
        assertSame(group, groupCmd);
    }

    @Test
    public void testParseWithMultipleGroups() {
        OptionGroup group1 = new OptionGroup("g1", "Group 1");
        OptionGroup group2 = new OptionGroup("g2", "Group 2");
        Option option = new Option("o", "option", true, "An option with value", group1);
        group1.addOption(option);
        OptionGroup group = new OptionGroup("g", "Group");
        group.addOptionGroup(group1);
        group.addOptionGroup(group2);
        Options options = new Options();
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        String[] arguments = {"-g", "-g1", "-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionCmd = optionsList.get(0);
        List<String> values = optionCmd.getValue();
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
        OptionGroup groupCmd = cmd.getOptionGroup();
        assertSame(group, groupCmd);
        OptionGroup group1Cmd = groupCmd.findOptionGroup("g1");
        assertSame(group1, group1Cmd);
    }

    @Test
    public void testParseWithMixedArgsAndOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "-o", "value", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> argsList = cmd.getArgs();
        assertEquals(2, argsList.size());
        assertEquals("arg1", argsList.get(0));
        assertEquals("arg2", argsList.get(1));
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionCmd = optionsList.get(0);
        List<String> values = optionCmd.getValue();
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "-o", "value", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        List<String> argsList = cmd.getArgs();
        assertEquals(2, argsList.size());
        assertEquals("arg1", argsList.get(0));
        assertEquals("arg2", argsList.get(1));
        List<Option> optionsList = cmd.getOptions();
        assertEquals(0, optionsList.size());
    }

    @Test
    public void testParseWithStopAtNonOptionAndExtraArgs() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"arg1", "-o", "value", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments, true);
        List<String> argsList = cmd.getArgs();
        assertEquals(2, argsList.size());
        assertEquals("arg1", argsList.get(0));
        assertEquals("arg2", argsList.get(1));
        List<Option> optionsList = cmd.getOptions();
        assertEquals(0, optionsList.size());
    }

    @Test
    public void testParseWithStopAtNonOptionAndMissingOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "An option with value");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] arguments = {"-oo"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments, true));
    }
}