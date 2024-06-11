import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithMinimumAndMaximum() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .isRequired(true)
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create());

        final GroupImpl group = new GroupImpl(options, "Group", "Test Group", 1, 2);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseOptions());

        Assert.assertEquals("Option 1 and Option 2 should be present", 2, commandLine.getOptions().size());
    }

    @Test
    public void testGroupWithMinimumAndMaximumWithMoreOptions() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .isRequired(true)
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create());
        options.add(OptionBuilder.withLongOpt("option3")
                .withDescription("Option 3")
                .create());

        final GroupImpl group = new GroupImpl(options, "Group", "Test Group", 1, 3);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseOptions());

        Assert.assertEquals("Option 1, Option 2 and Option 3 should be present", 3, commandLine.getOptions().size());
    }

    @Test(expected = ParseException.class)
    public void testGroupWithMinimumButNoOptions() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create());

        final GroupImpl group = new GroupImpl(options, "Group", "Test Group", 2, 2);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseOptions());

        // This test should fail as the minimum is set to 2
    }

    @Test
    public void testGroupWithMaximumButMoreOptions() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .isRequired(true)
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .isRequired(true)
                .create());
        options.add(OptionBuilder.withLongOpt("option3")
                .withDescription("Option 3")
                .isRequired(true)
                .create());
        options.add(OptionBuilder.withLongOpt("option4")
                .withDescription("Option 4")
                .isRequired(true)
                .create());

        final GroupImpl group = new GroupImpl(options, "Group", "Test Group", 3, 3);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseOptions());

        Assert.assertEquals("Option 1, Option 2 and Option 3 should be present", 3, commandLine.getOptions().size());

        // This test should not fail as the maximum is set to 3
    }

    @Test
    public void testGroupWithAnonymousArguments() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .isRequired(true)
                .create());
        options.add(OptionBuilder.withLongOpt("group")
                .withDescription("Test Group")
                .withArgName("arg")
                .isRequired(true)
                .setValueSeparator(' ')
                .createArg()
                .create());

        final GroupImpl group = new GroupImpl(options, "Test Group", null, 1, 1);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseOptions());

        Assert.assertTrue("Anonymous argument should be present", commandLine.hasOption("group"));
        Assert.assertNotNull("Anonymous argument value should not be null", commandLine.getOptionValue("group"));
        Assert.assertEquals("Anonymous argument value should be correctly parsed", Arrays.asList("arg1", "arg2"), commandLine.getValuesForOption("group"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.setAutoHelp(false);
        formatter.printHelp("Test", group.parseOptions(), true);
        Assert.assertTrue("Help message should show anonymous argument", formatter.getHelpMessage().contains("arg [arg]"));
    }

    @Test
    public void testGroupWithEdgeCases() throws ParseException {
        final List options = new ArrayList();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .isRequired(true)
                .create());
        options.add(OptionBuilder.withLongOpt("group")
                .withDescription("Test Group")
                .withArgName("arg")
                .isRequired(false)
                .setValueSeparator(' ')
                .createArg()
                .create());

        final GroupImpl group = new GroupImpl(options, "Test Group", null, 1, 10);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseOptions());

        Assert.assertEquals("No anonymous arguments should be present when none is provided", 0, commandLine.getValuesForOption("group").size());

        final CommandLine commandLineWithOneArgument = new CommandLine();
        commandLineWithOneArgument.parse(group.parseOptions());
        commandLineWithOneArgument.getOptions().add(OptionBuilder.withLongOpt("group")
                .withValues(new String[]{"arg1"})
                .create());

        Assert.assertEquals("One anonymous argument should be present", 1, commandLineWithOneArgument.getValuesForOption("group").size());

        final CommandLine commandLineWithMultipleArguments = new CommandLine();
        commandLineWithMultipleArguments.parse(group.parseOptions());
        commandLineWithMultipleArguments.getOptions().add(OptionBuilder.withLongOpt("group")
                .withValues(new String[]{"arg1", "arg2"})
                .create());

        Assert.assertEquals("Two anonymous arguments should be present", 2, commandLineWithMultipleArguments.getValuesForOption("group").size());
    }
}