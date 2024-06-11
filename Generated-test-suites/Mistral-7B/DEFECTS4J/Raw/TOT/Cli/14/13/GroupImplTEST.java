package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        options = new Options();
        formatter = new HelpFormatter();
    }

    @Test
    public void testBasicUsage() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "arg1", true, "Arg 1"),
                new Option("b", "arg2", true, "Arg 2"),
                new Option("c", "arg3", true, "Arg 3")
            ),
            "MyGroup",
            "My Group",
            -1,
            3
        );

        options.addOption(group);

        final CommandLine cmd = new CommandLine("-a --arg2 --arg3");
        group.process(cmd, cmd.iterator());

        Assert.assertEquals("Arg 1", cmd.getOptionValue("a"));
        Assert.assertEquals("Arg 2", cmd.getOptionValue("b"));
        Assert.assertEquals("Arg 3", cmd.getOptionValue("c"));

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testMinimum() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "arg1", true, "Arg 1"),
                new Option("b", "arg2", true, "Arg 2"),
                new Option("c", "arg3", true, "Arg 3")
            ),
            "MyGroup",
            "My Group",
            2,
            3
        );

        options.addOption(group);

        Assert.assertThrows(ParseException.class, () -> new CommandLine("-a"));
        Assert.assertThrows(ParseException.class, () -> new CommandLine("-a --arg2"));
        Assert.assertThrows(ParseException.class, () -> new CommandLine("-a --arg2 --arg3 --arg4"));

        Assert.assertDoesNotThrow(() -> new CommandLine("-a --arg2 --arg3"));
    }

    @Test
    public void testMaximum() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "arg1", true, "Arg 1"),
                new Option("b", "arg2", true, "Arg 2"),
                new Option("c", "arg3", true, "Arg 3")
            ),
            "MyGroup",
            "My Group",
            1,
            3
        );

        options.addOption(group);

        Assert.assertThrows(ParseException.class, () -> new CommandLine("-a --arg2 --arg3 --arg4"));
        Assert.assertDoesNotThrow(() -> new CommandLine("-a --arg2"));
        Assert.assertDoesNotThrow(() -> new CommandLine("-a --arg2 --arg3"));
    }

    @Test
    public void testBursting() throws ParseException {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "arg1", true, "Arg 1"),
                new Option("b", "arg2", true, "Arg 2"),
                new Option("c", "arg3", true, "Arg 3")
            ),
            "MyGroup",
            "My Group",
            -1,
            3
        );

        options.addOption(group);

        // single group option
        Assert.assertThrows(ParseException.class, () -> new CommandLine("-a"));
        Assert.assertThrows(ParseException.class, () -> new CommandLine("-a arg1"));

        // multiple group options
        Assert.assertThrows(ParseException.class, () -> new CommandLine("-a arg1 arg2"));
        Assert.assertThrows(ParseException.class, () -> new CommandLine("-a arg1 arg2 arg3"));

        // valid usage
        Assert.assertDoesNotThrow(() -> new CommandLine("-a arg1 arg2 arg3"));

        // multiple group options with different triggers
        Assert.assertDoesNotThrow(() -> new CommandLine("-a arg1 arg2 --c arg3"));
        Assert.assertDoesNotThrow(() -> new CommandLine("-a arg1 arg2 --b arg3"));

        // multiple group options with same trigger
        Assert.assertDoesNotThrow(() -> new CommandLine("-a arg1 arg2 arg3 arg4"));
    }

    @Test
    public void testDescription() {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "arg1", true, "Arg 1")
            ),
            "MyGroup",
            "My Group with description"
        );

        final CommandLine cmd = new CommandLine();
        group.process(cmd, cmd.iterator());

        Assert.assertEquals("My Group with description", cmd.getDescription());
    }

    @Test
    public void testHelp() {
        final Group group = new GroupImpl(
            asList(
                new Option("a", "arg1", true, "Arg 1")
            ),
            "MyGroup",
            "My Group with description"
        );

        final String help = formatter.printHelp("app", options, true);

        Assert.assertTrue(help.contains("My Group with description"));
    }

    private <T> List<T> asList(final T... values) {
        return new ArrayList<>(List.of(values));
    }
}