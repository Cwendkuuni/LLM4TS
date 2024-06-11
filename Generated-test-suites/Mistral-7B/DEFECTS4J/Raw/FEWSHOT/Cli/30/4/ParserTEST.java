package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.UnrecognizedOptionException;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"--foo", "-b", "bar", "baz"};
        CommandLine cmd = parser.parse(new Options(), args, false);

        Option foo = new Option("f", "foo", false, "A foo option.");
        Option bar = new Option("b", "bar", true, "A bar option.");

        cmd.addOption(foo);
        cmd.addOption(bar);

        assertFalse(cmd.hasOption("qux"));
        assertNull(cmd.getOptionValue("qux"));

        assertTrue(cmd.hasOption("foo"));
        assertSame(foo, cmd.getOption("foo"));

        assertTrue(cmd.hasOption("bar"));
        assertSame(bar, cmd.getOption("bar"));
        assertTrue(Arrays.asList(cmd.getArgs()).contains("baz"));
    }

    @Test
    public void testParseWithGroup() throws Exception {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"--group", "--qux", "-b", "bar", "baz"};
        CommandLine cmd = parser.parse(new Options(), args, false);

        OptionGroup group = new OptionGroup("group", "Group options.");
        Option qux = new Option("q", "qux", false, "A qux option.", group);
        Option bar = new Option("b", "bar", true, "A bar option.");

        group.addOption(qux);
        group.addOption(bar);

        cmd.addOption(group);

        assertTrue(cmd.hasOption("qux"));
        assertSame(qux, cmd.getOption("qux"));
        assertTrue(cmd.hasOption("bar"));
        assertSame(bar, cmd.getOption("bar"));
        assertTrue(Arrays.asList(cmd.getArgs()).contains("baz"));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"--foo", "-b", "bar", "baz"};
        Properties props = new Properties();
        props.setProperty("foo", "qux");

        CommandLine cmd = parser.parse(new Options(), args, props, false);

        Option foo = new Option("f", "foo", false, "A foo option.");
        Option bar = new Option("b", "bar", true, "A bar option.");

        cmd.addOption(foo);
        cmd.addOption(bar);

        assertFalse(cmd.hasOption("qux"));
        assertNull(cmd.getOptionValue("qux"));

        assertTrue(cmd.hasOption("foo"));
        assertSame(foo, cmd.getOption("foo"));

        assertTrue(cmd.hasOption("bar"));
        assertSame(bar, cmd.getOption("bar"));
        assertTrue(Arrays.asList(cmd.getArgs()).contains("baz"));

        assertEquals("qux", cmd.getOptionValue("foo"));
    }

    @Test
    public void testParseWithNoOptions() throws Exception {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"baz"};
        CommandLine cmd = parser.parse(new Options(), args, false);

        assertFalse(cmd.hasOptions());
        assertTrue(Arrays.asList(cmd.getArgs()).contains("baz"));
    }

    @Test
    public void testParseWithBadArguments() throws Exception {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"--baz", "--qux", "-b", "bar", "baz"};
        CommandLine cmd = parser.parse(new Options(), args, false);

        Option foo = new Option("f", "foo", false, "A foo option.");
        Option bar = new Option("b", "bar", true, "A bar option.");
        Option qux = new Option("q", "qux", false, "A qux option.");

        cmd.addOption(foo);
        cmd.addOption(bar);

        assertTrue(cmd.hasOption("baz"));
        assertNull(cmd.getOptionValue("baz"));

        assertFalse(cmd.hasOption("qux"));
        assertNull(cmd.getOptionValue("qux"));

        assertTrue(cmd.hasOption("bar"));
        assertSame(bar, cmd.getOption("bar"));
        assertTrue(Arrays.asList(cmd.getArgs()).contains("baz"));

        assertThrows(MissingOptionException.class, () -> cmd.getOptionValue("foo"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws Exception {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"--foo"};
        CommandLine cmd = parser.parse(new Options(), args, false);

        Option foo = new Option("f", "foo", true, "A foo option.");

        cmd.addOption(foo);

        assertFalse(cmd.hasOption("foo"));
        assertNull(cmd.getOptionValue("foo"));
        assertThrows(MissingOptionException.class, () -> cmd.getCommandLine());
    }

    @Test
    public void testParseWithMissingRequiredGroupOption() throws Exception {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"--group"};
        CommandLine cmd = parser.parse(new Options(), args, false);

        OptionGroup group = new OptionGroup("group", "Group options.");

        cmd.addOption(group);

        assertFalse(cmd.hasOption("group"));
        assertNull(cmd.getOptionValue("group"));
        assertThrows(MissingOptionException.class, () -> cmd.getCommandLine());
    }

    @Test
    public void testParseWithMissingRequiredArg() throws Exception {
        Parser parser = new DefaultParser();

        String[] args = new String[]{"--bar"};
        CommandLine cmd = parser.parse(new Options(), args, false);

        Option bar = new Option("b", "bar", true, "A bar option.");

        cmd.addOption(bar);

        assertFalse(cmd.hasOption("bar"));
        assertNull(cmd.getOptionValue("bar"));
        assertThrows(MissingArgumentException.class, () -> cmd.getOptionValue("bar"));
    }
}