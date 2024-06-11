package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(new CommandLine(), cmd);
    }

    @Test
    public void testParseWithValidOptionsAndArguments() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "3", "-c", "1", "-d", "4", "-e", "5"});

        assertEquals(4, cmd.getArgs().length);
        assertEquals("3", cmd.getOptionValue("b"));
        assertEquals("1", cmd.getOptionValue("c"));
        assertEquals("4", cmd.getOptionValue("d"));
        assertEquals("5", cmd.getOptionValue("e"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
    }

    @Test
    public void testParseWithValidOptions() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "-c", "-d", "-e"});

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertFalse(cmd.getArgs().length > 0);
    }

    @Test
    public void testParseWithInvalidOptions() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, new String[]{"-f", "-a", "-c", "-d", "-e"});
            fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }

        assertTrue(cmd.getArgs().length == 0);
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithMissingRequiredOptions() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addRequiredOption("e", true, "required option E");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "-c", "-d"});
            fail();
        } catch (MissingOptionException e) {
            // expected
        }

        assertTrue(cmd.getArgs().length == 0);
        assertFalse(cmd.hasOption("e"));
    }

    @Test
    public void testParseWithMissingOptionalOptionsAndArguments() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");
        options.addOption("f", "option F");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "-c", "1"});

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("d"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.hasOption("f"));
        assertTrue(cmd.getArgs().length == 1);
        assertEquals("1", cmd.getOptionValue("c"));
    }

    @Test
    public void testParseWithInvalidArguments() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, new String[]{"-a", "-b", "-1", "-d", "4", "-e", "5a"});
            fail();
        } catch (MissingArgumentException e) {
            // expected
        }

        assertTrue(cmd.getArgs().length == 0);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertEquals(1, cmd.getOptionIndex("b"));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");

        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        properties.setProperty("b", "valueB");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{}, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertFalse(cmd.getArgs().length > 0);
    }

    @Test
    public void testParseWithInvalidProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");

        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        properties.setProperty("b", "valueB");
        properties.setProperty("f", "valueF");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, new String[]{}, properties);
            fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }

        assertTrue(cmd.getArgs().length == 0);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithMixedArgumentsAndProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");

        Properties properties = new Properties();
        properties.setProperty("b", "valueB");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "1", "-c"}, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.getArgs().length > 0);
    }

    @Test
    public void testParseWithGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "group options", 1, Collections.emptyList(), null);
        group.addOption("a", true, "group option A");
        group.addOption("b", true, "group option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-c", "--group1", "-a"});

        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.getArgs().length > 0);
        assertSame(group, cmd.getOptionGroup("group1"));
    }

    @Test
    public void testParseWithGroupAndMissingOption() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "group options", 1, Collections.emptyList(), null);
        group.addOption("a", true, "group option A");
        group.addOption("b", true, "group option B");
        options.addOption("c", 3, "option C");
        options.addOption("d", 4, "option D");
        options.addOption("e", 5, "option E");
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-c", "--group1"});

        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.getArgs().length > 0);
    }
}