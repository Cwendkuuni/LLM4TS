package org.apache.commons.cli2.option;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    private CommandLineParser parser;
    private CommandLine cmd;

    @Before
    public void setUp() {
        parser = new DefaultParser();
    }

    @Test
    public void testGroupWithMinimumAndMaximum() {
        final List options = new ArrayList();

        options.add(new Option("a", "a", false, "an option with no arguments"));
        options.add(new Option("b", "b", true, "an optional option with an argument"));
        options.add(new Option("c", "c", false, "another option with no arguments"));

        final Group group = new GroupImpl(options, "group", "this is a group", 1, 3);

        cmd = new CommandLine();

        try {
            parser.parse(group, Arrays.asList("a", "b", "c", "a"), cmd);
            assertEquals(3, cmd.getArgs().size());
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertFalse(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail("Should not have thrown a parse exception");
        }

        try {
            parser.parse(group, Arrays.asList("a", "a", "b"), cmd);
            fail("Should have thrown a parse exception");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testGroupWithAnonymousArgument() {
        final List options = new ArrayList();

        options.add(new Option("g", "g", false, "group option"));
        options.add(new Argument("arg", "an argument", true, "an optional argument"));

        final Group group = new GroupImpl(options, "group", "this is a group", 0, 1);

        cmd = new CommandLine();

        try {
            parser.parse(group, Arrays.asList("g"), cmd);
            assertTrue(cmd.hasOption("g"));
            assertTrue(cmd.hasOption("arg"));
            assertNull(cmd.getArgs().size());
        } catch (ParseException e) {
            fail("Should not have thrown a parse exception");
        }

        try {
            parser.parse(group, Arrays.asList("g", "arg1"), cmd);
            assertEquals(1, cmd.getArgs().size());
            assertEquals("arg1", cmd.getArgs().get(0));
        } catch (ParseException e) {
            fail("Should not have thrown a parse exception");
        }
    }

    @Test
    public void testGroupCanProcess() {
        final List options = new ArrayList();

        options.add(new Option("a", "a", false, "an option with no arguments"));
        options.add(new Option("b", "b", true, "an optional option with an argument"));
        options.add(new Option("c", "c", false, "another option with no arguments"));

        final Group group = new GroupImpl(options, "group", "this is a group", 1, 3);

        try {
            cmd = new CommandLine();
            cmd.parse(group, Arrays.asList("a"));
            assertTrue(group.canProcess(cmd, "a"));
        } catch (ParseException e) {
            fail("Should not have thrown a parse exception");
        }

        try {
            cmd = new CommandLine();
            cmd.parse(group, Arrays.asList("b", "arg"));
            assertFalse(group.canProcess(cmd, "a"));
        } catch (ParseException e) {
            fail("Should not have thrown a parse exception");
        }

        try {
            cmd = new CommandLine();
            cmd.parse(group, Arrays.asList("c"));
            assertFalse(group.canProcess(cmd, "b"));
        } catch (ParseException e) {
            fail("Should not have thrown a parse exception");
        }
    }

    @Test
    public void testGroupProcess() {
        final List options = new ArrayList();

        options.add(new Option("a", "a", false, "an option with no arguments"));
        options.add(new Option("b", "b", true, "an optional option with an argument"));
        options.add(new Option("c", "c", false, "another option with no arguments"));
        options.add(new Argument("arg", "an argument", true, "an optional argument"));

        final Group group = new GroupImpl(options, "group", "this is a group", 1, 3);

        cmd = new CommandLine();

        try {
            parser.parse(group, Arrays.asList("g", "arg1"), cmd);

            // test Option processing
            assertEquals("arg1", cmd.getOptionValue("a"));
            assertTrue(cmd.hasOption("b"));
            assertFalse(cmd.hasOption("c"));
            assertNull(cmd.getOptionValue("c"));

            // test Argument processing
            assertEquals("arg1", cmd.getArg(0));
            assertEquals(1, cmd.getArgs().size());
        } catch (ParseException e) {
            fail("Should not have thrown a parse exception");
        }
    }

    @Test
    public void testGroupValidate() {
        final List options = new ArrayList();

        options.add(new Option("a", "a", false, "an option with no arguments"));
        options.add(new Option("b", "b", true, "an optional option with an argument"));
        options.add(new Option("c", "c", false, "another option with no arguments"));

        final Group group = new GroupImpl(options, "group", "this is a group", 1, 3);

        cmd = new CommandLine();

        try {
            parser.parse(group, Arrays.asList("a", "b", "c"), cmd);
            group.validate(cmd);
        } catch (ParseException e) {
            fail("Should not have thrown a parse exception");
        }

        try {
            cmd = new CommandLine();
            parser.parse(group, Arrays.asList("a", "a"));
            group.validate(cmd);
            fail("Should not have processed successfully");
        } catch (OptionException e) {
            // expected
        }

        try {
            cmd = new CommandLine();
            parser.parse(group, Arrays.asList("a"));
            group.validate(cmd);
            fail("Should have thrown an OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testGroupUsage() {
        final List options = new ArrayList();

        options.add(new Option("a", "a", false, "an option with no arguments"));
        options.add(new Option("b", "b", true, "an optional option with an argument"));
        options.add(new Option("c", "c", false, "another option with no arguments"));
        options.add(new Argument("arg", "an argument", true, "an optional argument"));

        final Group group = new GroupImpl(options, "group", "this is a group", 1, 3);

        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, new HashSet(), null);

        final String expected = "group (this is a group)" + System.lineSeparator() +
                "  a: an option with no arguments" + System.lineSeparator() +
                "  b: an optional option with an argument" + System.lineSeparator() +
                "  c: another option with no arguments" + System.lineSeparator() +
                "  arg: an optional argument" + System.lineSeparator();

        assertEquals(expected, buffer.toString());
    }
}