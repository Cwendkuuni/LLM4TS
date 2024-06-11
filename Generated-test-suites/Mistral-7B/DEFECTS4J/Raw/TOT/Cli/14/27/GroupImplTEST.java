package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithOptions() throws ParseException {
        final String[] args = {"--group", "--option1", "--option2"};

        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        OptionBuilder.withLongOpt("option1")
                                .withDescription("Option 1")
                                .createOption(),
                        OptionBuilder.withLongOpt("option2")
                                .withDescription("Option 2")
                                .createOption()),
                "group",
                "A group containing options",
                Integer.MIN_VALUE,
                Integer.MAX_VALUE);
        options.addOption(group);

        final CommandLine commandLine = new CommandLine(options);
        final HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine.parse(args);
            Assert.assertEquals(Arrays.asList("option1", "option2"), commandLine.getOptionValues("group"));
        } catch (ParseException e) {
            System.out.println(formatter.formatHelp("test", options, true));
            throw e;
        }
    }

    @Test
    public void testGroupWithMinimumAndMaximum() throws ParseException {
        final String[] args = {"--group", "--option1", "--option2", "--option3"};

        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        OptionBuilder.withLongOpt("option1")
                                .withDescription("Option 1")
                                .createOption(),
                        OptionBuilder.withLongOpt("option2")
                                .withDescription("Option 2")
                                .createOption(),
                        OptionBuilder.withLongOpt("option3")
                                .withDescription("Option 3")
                                .createOption()),
                "group",
                "A group containing options",
                2,
                3);
        options.addOption(group);

        final CommandLine commandLine = new CommandLine(options);
        final HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine.parse(args);
            Assert.assertEquals(Arrays.asList("option1", "option2", "option3"), commandLine.getOptionValues("group"));
        } catch (ParseException e) {
            if (e.getMessage().contains("minimum: The option 'group' requires 2 values")) {
                System.out.println(formatter.formatHelp("test", options, true));
                throw e;
            }
            if (e.getMessage().contains("maximum: The option 'group' value 'option1, option2, option3' is limited to 3 values")) {
                System.out.println(formatter.formatHelp("test", options, true));
                throw e;
            }
            throw e;
        }
    }

    @Test(expected = ParseException.class)
    public void testGroupWithMinimumButNotEnoughOptions() throws ParseException {
        final String[] args = {"--group", "--option1"};

        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        OptionBuilder.withLongOpt("option1")
                                .withDescription("Option 1")
                                .createOption(),
                        OptionBuilder.withLongOpt("option2")
                                .withDescription("Option 2")
                                .createOption()),
                "group",
                "A group containing options",
                2,
                Integer.MAX_VALUE);
        options.addOption(group);

        final CommandLine commandLine = new CommandLine(options);
        final HelpFormatter formatter = new HelpFormatter();

        commandLine.parse(args);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testGroupWithMaximumButTooManyOptions() throws ParseException {
        final String[] args = {"--group", "--option1", "--option2", "--option3", "--option4"};

        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        OptionBuilder.withLongOpt("option1")
                                .withDescription("Option 1")
                                .createOption(),
                        OptionBuilder.withLongOpt("option2")
                                .withDescription("Option 2")
                                .createOption(),
                        OptionBuilder.withLongOpt("option3")
                                .withDescription("Option 3")
                                .createOption()),
                "group",
                "A group containing options",
                2,
                3);
        options.addOption(group);

        final CommandLine commandLine = new CommandLine(options);
        final HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine.parse(args);
            Assert.fail();
        } catch (ParseException e) {
            if (e.getMessage().contains("maximum: The option 'group' value 'option1, option2, option3, option4' is limited to 3 values")) {
                System.out.println(formatter.formatHelp("test", options, true));
                throw e;
            }
            throw e;
        }
    }

    @Test
    public void testAnonymousArgs() throws ParseException {
        final String[] args = {"--group", "arg1", "arg2"};

        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        OptionBuilder.withLongOpt("group")
                                .withDescription("Group")
                                .createOption(),
                        OptionBuilder.withLongOpt("arg")
                                .withDescription("Anonymous argument")
                                .hasArg()
                                .createOption()),
                null,
                null,
                0,
                0);
        group.setAnonymousValues(Collections.singletonList("arg"));
        options.addOption(group);

        final CommandLine commandLine = new CommandLine(options);
        final HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine.parse(args);
            Assert.assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getOptionValuesForOption("group.arg"));
        } catch (ParseException e) {
            System.out.println(formatter.formatHelp("test", options, true));
            throw e;
        }
    }

    @Test
    public void testAnonymousArgsWithOption() throws ParseException {
        final String[] args = {"--group", "--option", "arg1", "arg2"};

        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        OptionBuilder.withLongOpt("group")
                                .withDescription("Group")
                                .createOption(),
                        OptionBuilder.withLongOpt("option")
                                .withDescription("An option")
                                .hasArg()
                                .createOption(),
                        OptionBuilder.withLongOpt("arg")
                                .withDescription("Anonymous argument")
                                .hasArg()
                                .createOption()),
                null,
                null,
                0,
                0);
        group.setAnonymousValues(Collections.singletonList("arg"));
        options.addOption(group);

        final CommandLine commandLine = new CommandLine(options);
        final HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine.parse(args);
            Assert.assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getOptionValuesForOption("group.option.arg"));
        } catch (ParseException e) {
            System.out.println(formatter.formatHelp("test", options, true));
            throw e;
        }
    }
}