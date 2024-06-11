package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testDefaultParser() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Short option b with an int value");
        options.addOption("c", null, "Short option c with a string value");
        options.addOption("d", "default", "Short option d with a default value");
        options.addOption("e", false, "Short option e without a value");
        options.addOption("f", null, "Short option f with a variable argument");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-a", "-b", "1", "-c", "test", "--d", "--e"});

        assertEquals(5, commandLine.getArgs().size());
        assertEquals("test", commandLine.getOptionValue("c"));
        assertEquals("default", commandLine.getOptionValue("d"));
        assertFalse(commandLine.hasOption("e"));
        assertEquals((Integer) 1, commandLine.getOptionValue("b"));

        List<String> args = commandLine.getArgs();
        assertEquals("", args.get(0));
        assertEquals("", args.get(1));
        assertEquals("test", args.get(2));
        assertEquals("", args.get(3));
        assertEquals("", args.get(4));
    }

    @Test
    public void testDefaultParserWithValueParsing() throws Exception {
        Options options = new Options();
        options.addOption("b", 1, "Short option b with an int value");
        options.addOption("i", "10", "Short option i with an int value");
        options.addOption("l", Long.MAX_VALUE, "Short option l with a long value");
        options.addOption("f", "42.5", "Short option f with a float value");
        options.addOption("d", "2022-01-01", "Short option d with a date value");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-b", "2", "--i", "15", "--l", "9223372036854775807", "--f", "42.6", "--d", "2022-01-02"});

        assertEquals(0, commandLine.getArgs().size());
        assertEquals(2, commandLine.getOptionValues("b").length);
        assertEquals(15, commandLine.getOptionValue("i", Integer.class));
        assertEquals(9223372036854775807L, commandLine.getOptionValue("l", Long.class));
        assertEquals(42.6f, commandLine.getOptionValue("f", Float.class));
        assertArrayEquals(new String[]{"2022-01-02"}, commandLine.getOptionValues("d"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Short option a");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-b"});
    }

    @Test(expected = ParseException.class)
    public void testMissingValue() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", 1, "Short option b with an int value");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-a"});
    }

    @Test(expected = ParseException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Short option a");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-z"});
    }

    @Test
    public void testParserWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", "default", "Short option b with a default value");

        Properties properties = new Properties();
        properties.put("b", "value");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0], properties);

        assertEquals(0, commandLine.getArgs().size());
        assertEquals("value", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParserWithGroup() throws Exception {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);

        group.addOption(new Option("a", true, "Short option a in group"));
        group.addOption(new Option("b", true, "Short option b in group"));

        options.addOptionGroup(group);
        options.addOption(new Option("c", true, "Short option c not in group"));

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"--group"});

        assertSame(group, commandLine.getOptionGroup());
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testMissingGroupOption() throws Exception {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);

        group.addOption(new Option("a", true, "Short option a in group"));
        group.addOption(new Option("b", true, "Short option b in group"));

        options.addOption(new Option("c", true, "Short option c not in group"));

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"--group"});
    }

    @Test
    public void testParserWithVariableArgumentOption() throws Exception {
        Options options = new Options();
        options.addOption("f", null, "Short option f with a variable argument");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[]{"-f", "arg1", "arg2", "arg3"});

        List<String> args = commandLine.getOptionValues("f");
        assertEquals(3, args.size());
        assertEquals("arg1", args.get(0));
        assertEquals("arg2", args.get(1));
        assertEquals("arg3", args.get(2));
    }

    @Test
    public void testParserWithCustomFlattener() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Short option a");

        Parser parser = new DefaultParser();
        parser.setHelpGenerator(new HelpFormatter());
        parser.setOptionParser(new MyOptionParser());

        CommandLine commandLine = parser.parse(options, new String[]{"-a"});

        assertEquals("option a", commandLine.getOptionLabel("a"));
    }

    private static class MyOptionParser implements OptionParser {
        @Override
        public String[] parse(Options options, String[] args) throws ParseException {
            return new String[]{"myOptionLabel", "myArgument"};
        }
    }
}