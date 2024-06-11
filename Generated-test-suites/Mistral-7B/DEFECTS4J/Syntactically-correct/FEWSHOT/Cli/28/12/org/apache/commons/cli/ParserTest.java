package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[0]);
        assertNotNull(commandLine);
        assertFalse(commandLine.getArgs().length > 0);
    }

    @Test
    public void testParseSimple() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-h"});
        assertEquals("Print help.", commandLine.getOptionValue("help"));
        assertNull(commandLine.getOptionValue("other"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParseSimpleWithArgument() {
        Options options = new Options();
        options.addOption("i", "inputfile", true, "input file name.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-i", "input.txt"});
        assertEquals("input.txt", commandLine.getOptionValue("inputfile"));
        assertNull(commandLine.getOptionValue("other"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParseWithMultipleOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        options.addOption("i", "inputfile", true, "input file name.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-h", "-i", "input.txt"});
        assertEquals("Print help.", commandLine.getOptionValue("help"));
        assertEquals("input.txt", commandLine.getOptionValue("inputfile"));
        assertNull(commandLine.getOptionValue("other"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("i", "inputfile", true, "input file name.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("inputfile", "input.txt");
        CommandLine commandLine = parser.parse(options, new String[0], properties);
        assertEquals("input.txt", commandLine.getOptionValue("inputfile"));
        assertNull(commandLine.getOptionValue("other"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParseWithTrailingDashes() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"--help"});
        assertEquals("Print help.", commandLine.getOptionValue("help"));
        assertNull(commandLine.getOptionValue("other"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParseWithMultipleTrailingDashes() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"--", "--help"});
        assertEquals("Print help.", commandLine.getOptionValue("help"));
        assertNull(commandLine.getOptionValue("other"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"-x"}));
    }

    @Test
    public void testParseWithUnrecognizedOptionWithArg() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"-x", "arg"}));
    }

    @Test
    public void testParseWithMissingRequiredOption() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        options.addOption("i", "inputfile", true, "input file name.");
        options.addRequiredOption("o", "outputfile", true, "output file name.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[]{"-h", "-i", "input.txt"}));
    }

    @Test
    public void testParseWithMissingRequiredGroupOption() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(true);
        group.addOption("g", "groupOption", false, "Group option.");
        options.addOption("h", "help", false, "Print help.");
        options.addOption("i", "inputfile", true, "input file name.");
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[]{"-h", "-i", "input.txt"}));
    }

    @Test
    public void testParseWithExtraArguments() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        options.addOption("i", "inputfile", true, "input file name.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-h", "-i", "input.txt", "arg1", "arg2"});
        assertEquals("Print help.", commandLine.getOptionValue("help"));
        assertEquals("input.txt", commandLine.getOptionValue("inputfile"));
        assertNull(commandLine.getOptionValue("other"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print help.");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine commandLine = parser.parse(options, new String[]{"-h", "-i", "input.txt"}, true);
        assertEquals("Print help.", commandLine.getOptionValue("help"));
        assertNull(commandLine.getOptionValue("other"));
        assertArrayEquals(new String[]{"-i", "input.txt"}, commandLine.getArgs());
    }
}