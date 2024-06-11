package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArgs() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0]);

        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("h"));
        assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testParseWithSingleArg() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"arg"});

        assertEquals(1, commandLine.getArgs().size());
        assertEquals("arg", commandLine.getArgs().get(0));
        assertFalse(commandLine.hasOption("h"));
    }

    @Test
    public void testParseWithSingleOption() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-h"});

        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("h"));
        assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testParseWithSingleOptionAndArg() throws Exception {
        Options options = new Options();
        Option option = new Option("a", "arg", true, "an argument.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption(option);

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-a", "value"});

        assertEquals(0, commandLine.getArgs().size());
        assertTrue(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("a"));
        assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParseWithMultipleOptions() throws Exception {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1.");
        Option option2 = new Option("o", "option2", false, "Option 2.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOptions(EnumSet.of(option1, option2));

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-o", "--option2"});

        assertEquals(0, commandLine.getArgs().size());
        assertFalse(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("o"));
        assertFalse(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
    }

    @Test
    public void testParseWithOptionalGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group.");
        Option option1 = new Option("o1", "option1", false, "Option 1.");
        Option option2 = new Option("o2", "option2", false, "Option 2.");
        group.addOption(option1);
        group.addOption(option2);
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption(group);

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-o1"});

        assertEquals(0, commandLine.getArgs().size());
        assertFalse(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("o1"));
        assertFalse(commandLine.hasOption("o2"));
        assertTrue(commandLine.isSet("group"));
    }

    @Test
    public void testParseWithRequiredGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group.");
        Option option1 = new Option("o1", "option1", false, "Option 1.");
        Option option2 = new Option("o2", "option2", false, "Option 2.");
        group.addOption(option1);
        group.setRequired(true);
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption(group);

        Parser parser = new DefaultParser();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0]));

        Parser parser2 = new DefaultParser();
        CommandLine commandLine = parser2.parse(options, new String[]{"-o1"});

        assertEquals(0, commandLine.getArgs().size());
        assertFalse(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("o1"));
        assertFalse(commandLine.hasOption("o2"));
        assertTrue(commandLine.isSet("group"));
    }

    @Test
    public void testParseWithOptionsAndArguments() throws Exception {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1.");
        Option option2 = new Option("o2", "option2", false, "Option 2.");
        Option option3 = new Option("a", "argument", true, "An argument.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOptions(Arrays.asList(option1, option2, option3));

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-o1", "-o2", "arg1", "arg2"});

        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertFalse(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("o1"));
        assertTrue(commandLine.hasOption("o2"));
        assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws Exception {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1.");
        Option option2 = new Option("o2", "option2", false, "Option 2.");
        Option option3 = new Option("a", "argument", true, "An argument.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOptions(Arrays.asList(option1, option2, option3));

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-o1", "-o2", "arg1", "--"});

        assertEquals(1, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertFalse(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("o1"));
        assertTrue(commandLine.hasOption("o2"));
        assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testParseWithMissingArg() throws Exception {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1.");
        Option option2 = new Option("o2", "option2", false, "Option 2.");
        Option option3 = new Option("a", "argument", true, "An argument.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOptions(Arrays.asList(option1, option2, option3));

        Parser parser = new DefaultParser();
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, new String[]{"-o1", "-o2"}));
    }

    @Test
    public void testParseWithInvalidOption() throws Exception {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1.");
        Option option2 = new Option("o2", "option2", false, "Option 2.");
        Option option3 = new Option("a", "argument", true, "An argument.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOptions(Arrays.asList(option1, option2, option3));

        Parser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, new String[]{"-x", "-o1", "-o2"}));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1.");
        Option option2 = new Option("o2", "option2", false, "Option 2.");
        Option option3 = new Option("a", "argument", true, "An argument.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOptions(Arrays.asList(option1, option2, option3));

        Properties properties = new Properties();
        properties.put("o1", "value1");
        properties.put("o2", "value2");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0], properties);

        assertEquals(0, commandLine.getArgs().size());
        assertFalse(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("o1"));
        assertEquals("value1", commandLine.getOptionValue("o1"));
        assertTrue(commandLine.hasOption("o2"));
        assertEquals("value2", commandLine.getOptionValue("o2"));
        assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testParseWithPropertiesAndArguments() throws Exception {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1.");
        Option option2 = new Option("o2", "option2", false, "Option 2.");
        Option option3 = new Option("a", "argument", true, "An argument.");
        options.addOption("h", "help", false, "Print this help message.");
        options.addOptions(Arrays.asList(option1, option2, option3));

        Properties properties = new Properties();
        properties.put("o1", "value1");
        properties.put("o2", "value2");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"arg1", "arg2"}, properties);

        assertEquals(2, commandLine.getArgs().size());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
        assertFalse(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption("o1"));
        assertEquals("value1", commandLine.getOptionValue("o1"));
        assertTrue(commandLine.hasOption("o2"));
        assertEquals("value2", commandLine.getOptionValue("o2"));
        assertFalse(commandLine.hasOption("a"));
    }
}