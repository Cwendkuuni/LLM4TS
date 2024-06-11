package org.apache.commons.cli2.option;

import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

/**
 * Test case for GroupImpl class
 */
public class GroupImplTest {

    @Test
    public void testGroupWithOptions() {
        // create a group with options
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("a").withDescription("option A")
                .withLongOpt("longA").create());
        options.add(OptionBuilder.withName("b").withDescription("option B")
                .withLongOpt("longB").create());
        final GroupImpl group = new GroupImpl(options, "myGroup", "My Group", 1, 1);

        // create a parser
        final CommandLineParser parser = new DefaultParser();

        // parse a command line with valid options
        final CommandLine commandLine = parser.parse(group.getCommandLineDescription(), new String[]{"-a", "-b"});

        // assert that options have been processed correctly
        Assert.assertEquals("valueA", commandLine.getOptionValue("a"), null);
        Assert.assertEquals("valueB", commandLine.getOptionValue("b"), null);

        // test help output
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("java.exe", group.getCommandLineDescription(), true);
        Assert.assertTrue(formatter.getHelpMessage().contains("My Group"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option A"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option B"));
    }

    @Test
    public void testGroupWithInvalidOption() {
        // create a group with options
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("a").withDescription("option A")
                .withLongOpt("longA").create());
        options.add(OptionBuilder.withName("b").withDescription("option B")
                .withLongOpt("longB").create());
        final GroupImpl group = new GroupImpl(options, "myGroup", "My Group", 1, 1);

        // create a parser
        final CommandLineParser parser = new DefaultParser();

        // parse a command line with an invalid option
        try {
            parser.parse(group.getCommandLineDescription(), new String[]{"-c"});
            Assert.fail("An OptionException should have been thrown");
        } catch (OptionException e) {
            // expected
        }

        // test help output
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("java.exe", group.getCommandLineDescription(), true);
        Assert.assertTrue(formatter.getHelpMessage().contains("My Group"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option A"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option B"));
        Assert.assertTrue(formatter.getHelpMessage().contains("Unknown option: c"));
    }

    @Test
    public void testGroupWithMissingOption() {
        // create a group with options
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("a").withDescription("option A")
                .withLongOpt("longA").create());
        options.add(OptionBuilder.withName("b").withDescription("option B")
                .withLongOpt("longB").create());
        final GroupImpl group = new GroupImpl(options, "myGroup", "My Group", 1, 1);

        // create a parser
        final CommandLineParser parser = new DefaultParser();

        // parse a command line with missing option
        try {
            parser.parse(group.getCommandLineDescription(), new String[]{"-a"});
            Assert.fail("An OptionException should have been thrown");
        } catch (OptionException e) {
            // expected
        }

        // test help output
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("java.exe", group.getCommandLineDescription(), true);
        Assert.assertTrue(formatter.getHelpMessage().contains("My Group"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option A"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option B"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option A is required"));
    }

    @Test
    public void testGroupWithTooManyOptions() {
        // create a group with options
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("a").withDescription("option A")
                .withLongOpt("longA").create());
        options.add(OptionBuilder.withName("b").withDescription("option B")
                .withLongOpt("longB").create());
        final GroupImpl group = new GroupImpl(options, "myGroup", "My Group", 1, 2);

        // create a parser
        final CommandLineParser parser = new DefaultParser();

        // parse a command line with too many options
        try {
            parser.parse(group.getCommandLineDescription(), new String[]{"-a", "-b", "-c"});
            Assert.fail("An OptionException should have been thrown");
        } catch (OptionException e) {
            // expected
        }

        // test help output
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("java.exe", group.getCommandLineDescription(), true);
        Assert.assertTrue(formatter.getHelpMessage().contains("My Group"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option A"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option B"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option A and option B are allowed"));
        Assert.assertTrue(formatter.getHelpMessage().contains("too many options: c"));
    }

    @Test
    public void testGroupWithAnonymousArgument() {
        // create a group with an anonymous argument
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("a").withDescription("option A")
                .withLongOpt("longA").create());
        final List anonymous = new ArrayList();
        anonymous.add(OptionBuilder.withName("arg").withDescription("argument")
                .isRequired(true).createArgument());
        final GroupImpl group = new GroupImpl(options, "myGroup", "My Group", 1, 1, anonymous);

        // create a parser
        final CommandLineParser parser = new DefaultParser();

        // parse a command line with an anonymous argument
        final CommandLine commandLine = parser.parse(group.getCommandLineDescription(), new String[]{"-a", "argValue"});

        // assert that the argument has been processed correctly
        Assert.assertEquals("argValue", commandLine.getArgument("arg"), null);

        // test help output
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("java.exe", group.getCommandLineDescription(), true);
        Assert.assertTrue(formatter.getHelpMessage().contains("My Group"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option A"));
        Assert.assertTrue(formatter.getHelpMessage().contains("argument"));
    }

    @Test
    public void testGroupWithAnonymousArgumentMissing() {
        // create a group with an anonymous argument
        final List options = new ArrayList();
        options.add(OptionBuilder.withName("a").withDescription("option A")
                .withLongOpt("longA").create());
        final List anonymous = new ArrayList();
        anonymous.add(OptionBuilder.withName("arg").withDescription("argument")
                .isRequired(true).createArgument());
        final GroupImpl group = new GroupImpl(options, "myGroup", "My Group", 1, 1, anonymous);

        // create a parser
        final CommandLineParser parser = new DefaultParser();

        // parse a command line without an anonymous argument
        try {
            parser.parse(group.getCommandLineDescription(), new String[]{"-a"});
            Assert.fail("An OptionException should have been thrown");
        } catch (OptionException e) {
            // expected
        }

        // test help output
        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("java.exe", group.getCommandLineDescription(), true);
        Assert.assertTrue(formatter.getHelpMessage().contains("My Group"));
        Assert.assertTrue(formatter.getHelpMessage().contains("option A"));
        Assert.assertTrue(formatter.getHelpMessage().contains("argument is required"));
    }
}