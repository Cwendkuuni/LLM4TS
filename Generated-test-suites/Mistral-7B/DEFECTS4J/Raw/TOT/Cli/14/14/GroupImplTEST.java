package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setup() {
        List<Option> optionsList = new ArrayList<>();
        options = new Options();

        // create a group with a name and description
        Option groupOption = new Option("g", "group", true, "test group")
                .desc("This is a group option")
                .setValueRequired(true)
                .setMinValues(2)
                .setMaxValues(5)
                .setAnonymous(true)
                .setOptionalArg(true)
                .createGroup();

        Option option1 = new Option("a", "arg1", true, "argument 1 for group")
                .setValueRequired(true)
                .create();

        Option option2 = new Option("a", "arg2", true, "argument 2 for group")
                .create();

        groupOption.addOption(option1);
        groupOption.addOption(option2);

        optionsList.add(groupOption);
        options.addOptions(optionsList);

        formatter = new HelpFormatter();
    }

    @Test
    public void testValidUsage() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseOptions(options, new String[]{"g", "test", "arg1", "arg2"});

        Assert.assertEquals("test", commandLine.getOptionValue("group"));
        Assert.assertEquals("arg1", commandLine.getOptionValue("arg1"));
        Assert.assertEquals("arg2", commandLine.getOptionValue("arg2"));

        formatter.printHelp("Test", options);
        System.out.println(formatter.getHelpText());
    }

    @Test(expected = ParseException.class)
    public void testInvalidUsage() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseOptions(options, new String[]{"g", "test", "arg1"});
    }

    @Test(expected = ParseException.class)
    public void testInvalidUsageWithTooManyArguments() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseOptions(options, new String[]{"g", "test", "arg1", "arg2", "arg3"});
    }

    @Test(expected = ParseException.class)
    public void testInvalidUsageWithNoArguments() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseOptions(options, new String[]{"g", "test"});
    }

    @Test(expected = ParseException.class)
    public void testInvalidUsageWithInvalidArgument() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseOptions(options, new String[]{"g", "test", "invalidArg"});
    }

    @Test
    public void testHelp() {
        String helpText = formatter.printHelp("Test", options);
        System.out.println(helpText);
    }

    @Test
    public void testHelpWithLongOptions() {
        options.addOption(new Option("long-g", "long-group", true, "long group option")
                .createGroup());

        String helpText = formatter.printHelp("Test", options);
        System.out.println(helpText);
    }

    @Test
    public void testHelpWithLongOptionArgument() {
        Option longOption = new Option("long-a", "long-arg", true, "long argument for group")
                .create();

        Option groupOption = new Option("long-g", "long-group", true, "long group option")
                .desc("This is a long group option")
                .addOption(longOption)
                .createGroup();

        options.addOption(groupOption);

        String helpText = formatter.printHelp("Test", options);
        System.out.println(helpText);
    }

    @Test
    public void testHelpWithShortAndLongOptions() {
        Option shortOption = new Option("s", "short-option", true, "short option")
                .create();

        Option longOption = new Option("long-s", "long-short-option", true, "long short option")
                .create();

        Option groupOption = new Option("g", "group", true, "test group")
                .desc("This is a group option")
                .setValueRequired(true)
                .setMinValues(2)
                .setMaxValues(5)
                .setAnonymous(true)
                .addOption(shortOption)
                .addOption(longOption)
                .createGroup();

        options.addOption(groupOption);

        String helpText = formatter.printHelp("Test", options);
        System.out.println(helpText);
    }
}