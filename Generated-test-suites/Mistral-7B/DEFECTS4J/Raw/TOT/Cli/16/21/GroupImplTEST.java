package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testBasicUsage() {
        final Options options = new Options();
        final Group group = new GroupImpl(
            asList(
                new Option("g", "group", true, "a group"),
                new Option("a", "arg", true, "an argument for group"),
                new Option("o", "option", true, "an option")
            ),
            "group",
            "This is a group",
            1,
            1
        );

        options.addOption(new Option("h", "help", false, "display help"));

        final PosixParser parser = new PosixParser();
        final CommandLine parsed = parser.parse(options, new String[]{"g", "a", "1", "o"});

        Assert.assertEquals("a", parsed.getOptionValue("a"));
        Assert.assertEquals("1", parsed.getOptionValue("o"));
        Assert.assertFalse(parsed.hasOption("h"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test(expected = OptionException.class)
    public void testMinimum() {
        final Options options = new Options();
        final Group group = new GroupImpl(
            asList(
                new Option("g", "group", true, "a group"),
                new Option("a", "arg", true, "an argument for group")
            ),
            "group",
            "This is a group",
            1,
            1
        );

        options.addOption(new Option("g", "group", true, "a group"));

        final PosixParser parser = new PosixParser();
        parser.parse(options, new String[0]);
    }

    @Test(expected = OptionException.class)
    public void testMaximum() {
        final Options options = new Options();
        final Group group = new GroupImpl(
            asList(
                new Option("g", "group", true, "a group"),
                new Option("a", "arg", true, "an argument for group")
            ),
            "group",
            "This is a group",
            1,
            1
        );

        options.addOption(new Option("g", "group", true, "a group"));
        options.addOption(new Option("g", "group", true, "a group"));

        final PosixParser parser = new PosixParser();
        parser.parse(options, new String[0]);
    }

    @Test
    public void testNoArg() {
        final Options options = new Options();
        final Group group = new GroupImpl(
            asList(
                new Option("g", "group"),
                new Option("o", "option")
            ),
            "group",
            "This is a group"
        );

        options.addOption(new Option("h", "help", false, "display help"));

        final PosixParser parser = new PosixParser();
        final CommandLine parsed = parser.parse(options, new String[]{"g"});

        Assert.assertFalse(parsed.hasOption("h"));
        Assert.assertTrue(parsed.hasOption("g"));
        Assert.assertNull(parsed.getOptionValue("g"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testGroupWithArg() {
        final Options options = new Options();
        final Group group = new GroupImpl(
            asList(
                new Option("g", "group", true, "a group"),
                new Option("a", "arg", true, "an argument for group")
            ),
            "group",
            "This is a group"
        );

        options.addOption(new Option("h", "help", false, "display help"));

        final PosixParser parser = new PosixParser();
        final CommandLine parsed = parser.parse(options, new String[]{"g", "arg1"});

        Assert.assertEquals("arg1", parsed.getOptionValue("a"));
        Assert.assertTrue(parsed.hasOption("g"));
        Assert.assertNull(parsed.getOptionValue("g"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testArgument() {
        final Options options = new Options();
        final Group group = new GroupImpl(
            asList(
                new Option("g", "group", true, "a group"),
                new Option("a", "arg", true, "an argument for group")
            ),
            "group",
            "This is a group"
        );

        options.addOption(new Option("h", "help", false, "display help"));

        final PosixParser parser = new PosixParser();
        final CommandLine parsed = parser.parse(options, new String[]{"g", "arg1", "arg2"});

        Assert.assertEquals("arg1", parsed.getOptionValue("a"));
        Assert.assertEquals("arg2", parsed.getOptionValue("a"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testNestedGroup() {
        final Options options = new Options();
        final Option group1 = new Option("g1", "group1", false, "group1 option");

        final Group group2 = new GroupImpl(
            asList(
                new Option("a1", "arg1", true, "arg1 for group2"),
                new Option("a2", "arg2", true, "arg2 for group2")
            ),
            "group2",
            "This is group2"
        );

        group1.setValue(group2);
        options.addOption(group1);

        options.addOption(new Option("h", "help", false, "display help"));

        final PosixParser parser = new PosixParser();
        final CommandLine parsed = parser.parse(options, new String[]{"g1"});
        Assert.assertTrue(parsed.hasOption("g1"));
        Assert.assertFalse(parsed.hasOption("h"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testNestedGroupWithArgs() {
        final Options options = new Options();
        final Option group1 = new Option("g1", "group1", false, "group1 option");

        final Group group2 = new GroupImpl(
            asList(
                new Option("a1", "arg1", true, "arg1 for group2"),
                new Option("a2", "arg2", true, "arg2 for group2")
            ),
            "group2",
            "This is group2"
        );

        group1.setValue(group2);
        options.addOption(group1);

        options.addOption(new Option("h", "help", false, "display help"));

        final PosixParser parser = new PosixParser();
        final CommandLine parsed = parser.parse(options, new String[]{"g1", "arg1", "arg2"});

        final Option group2Option = parsed.findOption("group2");
        Assert.assertTrue(group2Option.hasOption("a1"));
        Assert.assertTrue(group2Option.hasOption("a2"));
        Assert.assertEquals("arg1", group2Option.getValue("a1"));
        Assert.assertEquals("arg2", group2Option.getValue("a2"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    private static <T> List<T> asList(final T... values) {
        final List<T> list = new ArrayList<>();
        for (final T value : values) {
            list.add(value);
        }
        return list;
    }
}