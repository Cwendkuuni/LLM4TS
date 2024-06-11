package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = {"-a", "1", "--b", "2", "--c=3", "--d"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.getOptionValue("a"));
        assertTrue(cmd.getOptionValue("b").equals("2"));
        assertTrue(cmd.getOptionValue("c").equals("3"));
        assertNull(cmd.getOptionValue("d"));
        assertArrayEquals(new String[]{}, cmd.getArgs());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = {"--foo"};

        Options options = new Options();
        options.addOption("foo", true, "Option Foo", "F");
        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption(options.addOption("bar", true, "Option Bar", "B"));
        group.addOption(options.addOption("baz", true, "Option Baz", "Z"));
        options.addOption("qux", true, "Option Qux", "Q");
        options.addOptionGroup(group);

        cmd.setOptions(options);

        assertThrows(MissingOptionException.class, () -> cmd.parse(args));

        List<String> expectedOptions = Arrays.asList("foo", "group", "bar", "baz", "qux");
        cmd.setOptions(options);
        cmd.parse(args);

        assertTrue(cmd.hasOption("foo"));
        assertTrue(cmd.hasOption("bar"));
        assertTrue(cmd.hasOption("baz"));
        assertTrue(cmd.hasOption("qux"));
        assertSame(group, cmd.getOptionGroup("group"));
        assertNull(cmd.getOptionValue("foo"));
        assertNull(cmd.getOptionValue("bar"));
        assertNull(cmd.getOptionValue("baz"));
        assertNull(cmd.getOptionValue("qux"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--foo=bar", "--qux=quux"};

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("qux", true, "Option Quux");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, new Properties());

        assertTrue(cmd.hasOption("foo"));
        assertTrue(cmd.hasOption("qux"));
        assertTrue(cmd.getOptionValue("foo").equals("bar"));
        assertTrue(cmd.getOptionValue("qux").equals("quux"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParseWithMixedOptionsAndArgs() throws ParseException {
        String[] args = {"--foo", "arg1", "--bar=value", "arg2"};

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", true, "Option Bar");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, new Properties());

        assertTrue(cmd.hasOption("foo"));
        assertFalse(cmd.hasOption("bar"));
        assertTrue(cmd.getOptionValue("foo"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = {"--qux"};

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");

        cmd.setOptions(options);

        assertThrows(UnrecognizedOptionException.class, () -> cmd.parse(args));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"--foo", "arg1", "--qux"};

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");

        cmd.setOptions(options);

        CommandLine cmd = cmd.parse(args, true);

        assertTrue(cmd.hasOption("foo"));
        assertArrayEquals(new String[]{"--qux"}, cmd.getArgs());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] args = {"--foo"};

        Options options = new Options();
        options.addOption("foo", true, "Option Foo", "F");

        cmd.setOptions(options);

        cmd.parse(args);
    }
}