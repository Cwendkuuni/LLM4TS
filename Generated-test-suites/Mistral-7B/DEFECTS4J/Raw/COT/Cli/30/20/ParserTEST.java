package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParser() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option test");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[] {"-o"});
        assertEquals("Option test", commandLine.getOptionValue("opt"));
        assertFalse(commandLine.hasOption("unknown"));
        assertNull(commandLine.getOptionValue("unknown"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParserWithArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option test with argument");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[] {"-o", "test"});
        assertEquals("test", commandLine.getOptionValue("opt"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParserWithLongOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("longOption", "longopt", false, "Long option test");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[] {"--longOption"});
        assertEquals("Long option test", commandLine.getOptionValue("longOption"));
        assertFalse(commandLine.hasOption("opt"));
        assertNull(commandLine.getOptionValue("opt"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testParserWithLongOptionArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("longOption", "longopt", true, "Long option test with argument");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[] {"--longOption", "arg"});
        assertEquals("arg", commandLine.getOptionValue("longOption"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParserMissingOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option test");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[] {});
    }

    @Test(expected = ParseException.class)
    public void testParserMissingArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option test with argument");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[] {"-o"});
    }

    @Test
    public void testParserWithRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("opt1", "opt1", false, "Option 1");
        options.addRequiredOption("opt2", "opt2", false, "Option 2");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[] {"-opt1", "-opt2"});
        assertTrue(commandLine.hasOption("opt1"));
        assertTrue(commandLine.hasOption("opt2"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test(expected = MissingOptionException.class)
    public void testParserMissingRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("opt1", "opt1", false, "Option 1");
        options.addRequiredOption("opt2", "opt2", false, "Option 2");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[] {"-opt1"});
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option test with argument");
        Properties properties = new Properties();
        properties.setProperty("opt", "test");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, null, properties, false);
        assertEquals("test", commandLine.getOptionValue("opt"));
        assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithNonParsedArguments() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option test with argument");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[] {"arg1", "arg2"});
        assertArrayEquals(new String[] {"arg1", "arg2"}, commandLine.getArgs());
    }
}