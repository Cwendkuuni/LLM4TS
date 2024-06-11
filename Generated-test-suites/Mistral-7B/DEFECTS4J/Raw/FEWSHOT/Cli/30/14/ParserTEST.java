package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);

        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseSimpleOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Option("o", "opt3", true, "Option 3");
        option1.setRequired(true);
        option2.setRequired(true);
        option3.setRequired(false);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "opt1", "--opt2", "arg2", "--opt3", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[]{"arg2", "arg3"}, cmd.getArgs().toArray(new String[0]));
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertTrue(cmd.hasOption("opt3"));
        assertFalse(cmd.hasOption("nonexistent"));
        assertNull(cmd.getOptionValue("opt1"));
        assertEquals("arg2", cmd.getOptionValue("opt2"));
        assertEquals("arg3", cmd.getOptionValue("opt3"));
    }

    @Test
    public void testParseLongOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("longopt1", "long-opt1", false, "Long Option 1");
        Option option2 = new Option("longopt2", "long-opt2", true, "Long Option 2");
        Option option3 = new Option("longopt3", "long-opt3", true, "Long Option 3");
        option1.setRequired(true);
        option2.setRequired(true);
        option3.setRequired(false);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--longopt1", "--longopt2=arg2", "--longopt3=arg3", "arg4"};
        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[]{"arg4"}, cmd.getArgs().toArray(new String[0]));
        assertTrue(cmd.hasOption("longopt1"));
        assertTrue(cmd.hasOption("longopt2"));
        assertTrue(cmd.hasOption("longopt3"));
        assertFalse(cmd.hasOption("nonexistent"));
        assertNull(cmd.getOptionValue("longopt1"));
        assertEquals("arg2", cmd.getOptionValue("longopt2"));
        assertEquals("arg3", cmd.getOptionValue("longopt3"));
    }

    @Test
    public void testParseLongOptionsWithArgument() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("longopt1", "long-opt1", true, "Long Option 1 with argument");
        Option option2 = new Option("longopt2", "long-opt2", false, "Long Option 2");

        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"--longopt1=arg1", "--longopt2"};
        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[]{"arg1"}, cmd.getArgs().toArray(new String[0]));
        assertTrue(cmd.hasOption("longopt1"));
        assertTrue(cmd.hasOption("longopt2"));
        assertFalse(cmd.hasOption("nonexistent"));
        assertEquals("arg1", cmd.getOptionValue("longopt1"));
        assertNull(cmd.getOptionValue("longopt2"));
    }

    @Test
    public void testParseShortOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Option("o", "opt3", true, "Option 3");
        option1.setRequired(true);
        option2.setRequired(true);
        option3.setRequired(false);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "opt1", "--opt2=arg2", "-o", "opt3=arg3", "arg4"};
        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[]{"arg4"}, cmd.getArgs().toArray(new String[0]));
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertTrue(cmd.hasOption("opt3"));
        assertFalse(cmd.hasOption("nonexistent"));
        assertNull(cmd.getOptionValue("opt1"));
        assertEquals("arg2", cmd.getOptionValue("opt2"));
        assertEquals("arg3", cmd.getOptionValue("opt3"));
    }

    @Test
    public void testParseShortOptionsWithArgument() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1 with argument");
        Option option2 = new Option("o", "opt2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o=arg1", "--opt2", "-o=arg3", "arg4"};
        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[]{"arg4"}, cmd.getArgs().toArray(new String[0]));
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertFalse(cmd.hasOption("nonexistent"));
        assertEquals("arg1", cmd.getOptionValue("opt1"));
        assertNull(cmd.getOptionValue("opt2"));
    }

    @Test
    public void testParseShortOptionsWithLongOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option longOption1 = new Option("longopt1", "long-opt1", false, "Long Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option longOption2 = new Option("longopt2", "long-opt2", false, "Long Option 2");
        Option option3 = new Option("o", "opt3", true, "Option 3");
        option1.setRequired(true);
        option2.setRequired(true);
        option3.setRequired(false);

        options.addOption(option1);
        options.addOption(longOption1);
        options.addOption(option2);
        options.addOption(longOption2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "opt1", "--long-opt1", "--long-opt2", "-o", "opt3=arg3", "arg4"};
        CommandLine cmd = parser.parse(options, arguments);

        assertArrayEquals(new String[]{"arg4"}, cmd.getArgs().toArray(new String[0]));
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("longopt1"));
        assertTrue(cmd.hasOption("longopt2"));
        assertTrue(cmd.hasOption("opt3"));
        assertFalse(cmd.hasOption("nonexistent"));
        assertNull(cmd.getOptionValue("opt1"));
        assertNull(cmd.getOptionValue("longopt1"));
        assertNull(cmd.getOptionValue("longopt2"));
        assertEquals("arg3", cmd.getOptionValue("opt3"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Option("o", "opt3", true, "Option 3");
        option1.setRequired(true);
        option2.setRequired(true);
        option3.setRequired(false);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Properties properties = new Properties();
        properties.setProperty("opt1", "arg1");
        properties.setProperty("opt2", "arg2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "opt1", "--opt2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertArrayEquals(new String[]{"arg3"}, cmd.getArgs().toArray(new String[0]));
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertTrue(cmd.hasOption("opt3"));
        assertFalse(cmd.hasOption("nonexistent"));
        assertSame(option1, cmd.getOption("opt1"));
        assertSame(option2, cmd.getOption("opt2"));
        assertEquals("arg1", cmd.getOptionValue("opt1"));
        assertEquals("arg2", cmd.getOptionValue("opt2"));
        assertEquals("arg3", cmd.getOptionValue("opt3"));
        assertNull(cmd.getOptionValue("opt3"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        option1.setRequired(true);
        option2.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "opt2"};
        CommandLine cmd = parser.parse(options, arguments, null);
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Option("o", "opt3", true, "Option 3");
        option1.setRequired(true);
        option2.setRequired(true);
        option3.setRequired(false);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-o", "nonexistent"};
        CommandLine cmd = parser.parse(options, arguments, null);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Option("o", "opt3", true, "Option 3");
        option1.setRequired(true);
        option2.setRequired(true);
        option3.setRequired(false);

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments, null);
    }
}