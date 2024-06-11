package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.StrictModeParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * GroupImplTest
 */
public class GroupImplTest {

    @Test
    public void testGroupWithOneOption() {
        final OptionGroup group = createGroupWithOneOption();

        final CommandLineParser parser = new StrictModeParser();
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        try {
            parser.parse(group.getOptions(), commandLine, null);
        } catch (final ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }

        Assert.assertEquals("--option1", commandLine.getOptionValue("option1"));
    }

    @Test
    public void testGroupWithMultipleOptions() {
        final OptionGroup group = createGroupWithMultipleOptions();

        final CommandLineParser parser = new StrictModeParser();
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        try {
            parser.parse(group.getOptions(), commandLine, null);
        } catch (final ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }

        Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
        Assert.assertEquals("value2", commandLine.getOptionValue("option2"));
    }

    @Test
    public void testGroupWithMinimumAndMaximum() {
        final OptionGroup group = createGroupWithMinimumAndMaximum();

        final CommandLineParser parser = new StrictModeParser();
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // test parsing with minimum number of options
        try {
            parser.parse(group.getOptions(), commandLine, null);
        } catch (final ParseException e) {
            Assert.fail("Parsing with minimum number of options should have succeeded.");
        }

        // test parsing with maximum number of options
        final List<String> args = new ArrayList<>();
        args.add("--group");
        args.add("--option1");
        args.add("--option1");
        args.add("--option1");
        args.add("--option2");
        args.add("--option2");

        try {
            parser.parse(args.toArray(new String[args.size()]), commandLine, null);
        } catch (final ParseException e) {
            Assert.fail("Parsing with maximum number of options should have succeeded.");
        }

        // test parsing with more options than maximum
        final List<String> args2 = new ArrayList<>(args);
        args2.add("--option1");

        try {
            parser.parse(args2.toArray(new String[args2.size()]), commandLine, null);
            Assert.fail("Parsing with more options than maximum should have failed.");
        } catch (final ParseException e) {
            // expected
        }

        // test parsing with less options than minimum
        final List<String> args3 = new ArrayList<>();
        args3.add("--group");

        try {
            parser.parse(args3.toArray(new String[args3.size()]), commandLine, null);
            Assert.fail("Parsing with less options than minimum should have failed.");
        } catch (final ParseException e) {
            // expected
        }
    }

    @Test
    public void testGroupWithAnonymousArgument() {
        final OptionGroup group = createGroupWithAnonymousArgument();

        final CommandLineParser parser = new StrictModeParser();
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        try {
            parser.parse(group.getOptions(), commandLine, null);
        } catch (final ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }

        Assert.assertEquals("value", commandLine.getOptionValue("argument"));
    }

    @Test
    public void testGroupWithMultipleAnonymousArguments() {
        final OptionGroup group = createGroupWithMultipleAnonymousArguments();

        final CommandLineParser parser = new StrictModeParser();
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        try {
            parser.parse(group.getOptions(), commandLine, null);
        } catch (final ParseException e) {
            Assert.fail("Parsing should have succeeded.");
        }

        Assert.assertEquals("value1", commandLine.getOptionValue("argument1"));
        Assert.assertEquals("value2", commandLine.getOptionValue("argument2"));
    }

    private OptionGroup createGroupWithOneOption() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withValueSeparator(OptionBuilder.VALUE_SEPARATOR_NONE)
                .withDescription("An option with no value.")
                .createOption());

        return new OptionGroup("group", "A group with one option.", options);
    }

    private OptionGroup createGroupWithMultipleOptions() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withValueSeparator(OptionBuilder.VALUE_SEPARATOR_NONE)
                .withDescription("An option with no value.")
                .createOption());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("An option with a value.")
                .withValue("value2")
                .createOption());

        return new OptionGroup("group", "A group with multiple options.", options);
    }

    private OptionGroup createGroupWithMinimumAndMaximum() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withValueSeparator(OptionBuilder.VALUE_SEPARATOR_NONE)
                .withDescription("An option with no value.")
                .createOption());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("An option with a value.")
                .withValue("value2")
                .createOption());

        return new OptionGroup("group", "A group with minimum and maximum options.", options, 1, 2);
    }

    private OptionGroup createGroupWithAnonymousArgument() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("group")
                .withDescription("A group with an anonymous argument.")
                .createOption());
        options.add(OptionBuilder.withLongOpt("argument")
                .withDescription("An anonymous argument.")
                .createOption());

        return new OptionGroup("group", "A group with an anonymous argument.", options);
    }

    private OptionGroup createGroupWithMultipleAnonymousArguments() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("group")
                .withDescription("A group with multiple anonymous arguments.")
                .createOption());
        options.add(OptionBuilder.withLongOpt("argument1")
                .withDescription("An anonymous argument.")
                .createOption());
        options.add(OptionBuilder.withLongOpt("argument2")
                .withDescription("Another anonymous argument.")
                .createOption());

        return new OptionGroup("group", "A group with multiple anonymous arguments.", options);
    }
}