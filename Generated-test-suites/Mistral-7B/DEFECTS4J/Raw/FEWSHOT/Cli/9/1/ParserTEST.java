package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyOptionsAndArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertNull(commandLine.getArgs());
        assertFalse(commandLine.getOptions().isEmpty());
    }

    @Test
    public void testParseWithEmptyOptionsAndArgumentsWithProperties() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        CommandLine commandLine = parser.parse(options, new String[0], properties);
        assertNull(commandLine.getArgs());
        assertFalse(commandLine.getOptions().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        String[] args = {"-a", "--optionB=valueB", "-c", "valueC"};
        Options options = new Options();
        Option optionA = Option.builder("a").build();
        Option optionB = Option.builder("b").required(false).hasArg().build();
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);
        List<Option> optionsList = commandLine.getOptions();
        assertEquals(2, optionsList.size());
        Option optionAFromCommandLine = optionsList.get(0);
        assertEquals(optionA, optionAFromCommandLine);
        Option optionBFromCommandLine = optionsList.get(1);
        assertEquals(optionB, optionBFromCommandLine);
        List<String> argsList = commandLine.getArgs();
        assertEquals(1, argsList.size());
        assertEquals("valueC", argsList.get(0));
    }

    @Test
    public void testParseWithOptionsAndArgumentsWithProperties() throws ParseException {
        String[] args = {"-a", "--optionB=valueB", "-c", "valueC"};
        Options options = new Options();
        Option optionA = Option.builder("a").build();
        Option optionB = Option.builder("b").required(false).hasArg().build();
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.put("optionA", "valueA");
        CommandLine commandLine = parser.parse(options, args, properties);
        List<Option> optionsList = commandLine.getOptions();
        assertEquals(2, optionsList.size());
        Option optionAFromCommandLine = optionsList.get(0);
        assertEquals(optionA, optionAFromCommandLine);
        Option optionBFromCommandLine = optionsList.get(1);
        assertEquals(optionB, optionBFromCommandLine);
        List<String> argsList = commandLine.getArgs();
        assertEquals(1, argsList.size());
        assertEquals("valueC", argsList.get(0));
        assertEquals("valueA", commandLine.getOptionValues(optionA.getKey())[0]);
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndStopAtNonOption() throws ParseException {
        String[] args = {"-a", "--optionB=valueB", "-c", "--optionD", "valueE"};
        Options options = new Options();
        Option optionA = Option.builder("a").build();
        Option optionB = Option.builder("b").required(false).hasArg().build();
        Option optionD = Option.builder("d").required(false).hasArg().build();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionD);
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, null, true);
        List<Option> optionsList = commandLine.getOptions();
        assertEquals(3, optionsList.size());
        Option optionAFromCommandLine = optionsList.get(0);
        assertEquals(optionA, optionAFromCommandLine);
        Option optionBFromCommandLine = optionsList.get(1);
        assertEquals(optionB, optionBFromCommandLine);
        Option optionDFromCommandLine = optionsList.get(2);
        assertEquals(optionD, optionDFromCommandLine);
        List<String> argsList = commandLine.getArgs();
        assertEquals(2, argsList.size());
        assertEquals("--optionD", argsList.get(0));
        assertEquals("valueE", argsList.get(1));
    }

    @Test
    public void testParseWithOptionsAndArgumentsAndStopAtNonOptionWithProperties() throws ParseException {
        String[] args = {"-a", "--optionB=valueB", "-c", "--optionD", "valueE"};
        Options options = new Options();
        Option optionA = Option.builder("a").build();
        Option optionB = Option.builder("b").required(false).hasArg().build();
        Option optionD = Option.builder("d").required(false).hasArg().build();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionD);
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.put("optionA", "valueA");
        CommandLine commandLine = parser.parse(options, args, properties, true);
        List<Option> optionsList = commandLine.getOptions();
        assertEquals(3, optionsList.size());
        Option optionAFromCommandLine = optionsList.get(0);
        assertEquals(optionA, optionAFromCommandLine);
        Option optionBFromCommandLine = optionsList.get(1);
        assertEquals(optionB, optionBFromCommandLine);
        Option optionDFromCommandLine = optionsList.get(2);
        assertEquals(optionD, optionDFromCommandLine);
        List<String> argsList = commandLine.getArgs();
        assertEquals(2, argsList.size());
        assertEquals("--optionD", argsList.get(0));
        assertEquals("valueE", argsList.get(1));
        assertEquals("valueA", commandLine.getOptionValues(optionA.getKey())[0]);
    }

    @Test
    public void testParseWithOnlyRequiredOptions() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();
        Option optionA = Option.builder("a").required(true).build();
        Option optionB = Option.builder("b").required(true).build();
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithOnlyRequiredOptionsWithProperties() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();
        Option optionA = Option.builder("a").required(true).build();
        Option optionB = Option.builder("b").required(true).build();
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, properties));
    }

    @Test
    public void testParseWithValidOptionGroup() throws ParseException {
        String[] args = {"--group", "--optionA"};
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Group");
        Option optionA = Option.builder("a").build();
        optionGroup.addOption(optionA);
        options.addOption(optionGroup);
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);
        List<Option> optionsList = commandLine.getOptions();
        assertEquals(1, optionsList.size());
        Option optionGroupFromCommandLine = optionsList.get(0);
        assertSame(optionGroup, optionGroupFromCommandLine);
        List<String> argsList = commandLine.getArgs();
        assertEquals(0, argsList.size());
    }

    @Test
    public void testParseWithValidOptionGroupWithValue() throws ParseException {
        String[] args = {"--group", "--optionA=valueA"};
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Group");
        Option optionA = Option.builder("a").required(false).hasArg().build();
        optionGroup.addOption(optionA);
        options.addOption(optionGroup);
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);
        List<Option> optionsList = commandLine.getOptions();
        assertEquals(1, optionsList.size());
        Option optionGroupFromCommandLine = optionsList.get(0);
        assertSame(optionGroup, optionGroupFromCommandLine);
        List<String> argsList = commandLine.getArgs();
        assertEquals(0, argsList.size());
        assertEquals("valueA", commandLine.getOptionValues(optionA.getKey())[0]);
    }

    @Test
    public void testParseWithInvalidOptionGroup() throws ParseException {
        String[] args = {"--group", "--invalidOption"};
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Group");
        Option optionA = Option.builder("a").build();
        Option optionB = Option.builder("b").build();
        Option invalidOption = Option.builder("invalidOption").build();
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        options.addOption(optionGroup);
        Parser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithInvalidOptionGroupWithValue() throws ParseException {
        String[] args = {"--group", "--invalidOption=value"};
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Group");
        Option optionA = Option.builder("a").required(false).hasArg().build();
        Option optionB = Option.builder("b").build();
        Option invalidOption = Option.builder("invalidOption").build();
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        options.addOption(optionGroup);
        Parser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }
}