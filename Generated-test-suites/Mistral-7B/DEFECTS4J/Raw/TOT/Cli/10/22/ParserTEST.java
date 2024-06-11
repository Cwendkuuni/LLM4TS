package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        final String[] args = {"-a", "1", "-b", "2", "--foo", "bar"};
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("foo", false, "Option Foo");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("c")); // no option C
        assertEquals(0, cmd.getArgs().length); // no arguments left

        assertEquals("Option A\nOption B\n", cmd.getOptions().toString());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("c"));

        assertEquals(true, cmd.hasOption("foo"));
        assertEquals(null, cmd.getOptionValue("foo"));

        assertEquals("--foo=bar", cmd.getOption("foo").getValue());
    }

    @Test
    public void testParseWithOptionalArgs() throws ParseException {
        final String[] args = {"-a", "1", "-b", "2", "--foo", "bar", "baz"};
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("foo", true, "Option Foo");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertEquals("baz", cmd.getArgs()[0]);
        assertEquals(1, cmd.getArgs().length);
    }

    @Test
    public void testParseWithRequiredArgs() throws ParseException {
        final String[] args = {"-a", "1"};
        final Options options = new Options();
        options.addOption("a", true, "Option A", "arg");

        CommandLineParser parser = new DefaultParser();
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithMissingOptions() throws ParseException {
        final String[] args = {"1", "2"};
        final Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithUnknownOptionGroup() throws ParseException {
        final String[] args = {"--group", "unknown"};
        final Options options = new Options();
        options.addOptionGroup("group", "Group");
        options.getOptionGroup("group").addOption("unknown", true, "Unknown");

        CommandLineParser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        final String[] args = {"--foo", "bar", "-a", "1", "-b", "2"};
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("foo", false, "Option Foo");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(true, cmd.hasOption("foo"));
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertEquals(true, cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(0, cmd.getArgs().length);

        assertEquals("Option Foo\nOption A\nOption B", cmd.getOptions().toString());
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("c")); // no option C
    }

    @Test
    public void testParseWithMixedOptionsAndArgumentsAndGroups() throws ParseException {
        final String[] args = {"--foo", "bar", "-a", "1", "-b", "2", "--group", "group1"};
        final Options options = new Options();
        options.addOption("a", true, "Option A", "arg");
        options.addOption("b", true, "Option B", "arg");
        options.addOption("foo", false, "Option Foo");
        options.addOptionGroup("group", "Group");
        options.getOptionGroup("group").addOption("group1", false, "Group 1");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(true, cmd.hasOption("foo"));
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertEquals(true, cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(true, cmd.hasOption("group1"));
        assertNull(cmd.getOptionValue("c")); // no option C

        assertEquals("Option Foo\nOption A\nOption B\nGroup 1", cmd.getOptions().toString());
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testParseWithArgumentsOnly() throws ParseException {
        final String[] args = {"arg1", "arg2"};
        final Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getOptions().size());
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }
}