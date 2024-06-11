package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupWithOptionalAndRequiredOptions() {
        final List options = new ArrayList();
        options.add(createOption("a", "optionA", false, "A value for option A"));
        options.add(createOption("b", "optionB", true, "B value for option B"));
        options.add(createOption("c", "optionC", false, "C value for option C"));

        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final CommandLine cmd = new CommandLine();
        cmd.addOption("a");
        cmd.addOption("b");

        try {
            group.validate(cmd);
            Assert.assertEquals("group", cmd.getOptions().get(0).getName());
            Assert.assertFalse(cmd.hasOption("c"));

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n  optionA, -a OPTIONAL\n    A value for option A\n  optionB, -b REQUIRED\n    B value for option B\n  optionC, -c OPTIONAL\n    C value for option C\n", helpLine.getHelp());
    }

    @Test
    public void testGroupWithMinimumAndMaximumOptions() {
        final List options = new ArrayList();
        options.add(createOption("a", "optionA", false, "A value for option A"));
        options.add(createOption("b", "optionB", true, "B value for option B"));
        options.add(createOption("c", "optionC", false, "C value for option C"));

        final Group group = new GroupImpl(options, "group", null, 2, 3, false);

        final CommandLine cmd = new CommandLine();
        cmd.addOption("a");
        cmd.addOption("a");
        cmd.addOption("b");

        try {
            group.validate(cmd);
            Assert.assertEquals("group", cmd.getOptions().get(0).getName());
            Assert.assertFalse(cmd.hasOption("c"));

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n  optionA, -a OPTIONAL\n    A value for option A\n  optionB, -b REQUIRED\n    B value for option B\n  optionC, -c OPTIONAL\n    C value for option C\n", helpLine.getHelp());

        final CommandLine cmd2 = new CommandLine();
        cmd2.addOption("a");
        cmd2.addOption("a");
        cmd2.addOption("b");
        cmd2.addOption("c");

        try {
            group.validate(cmd2);
            Assert.assertEquals("group", cmd2.getOptions().get(0).getName());
            Assert.assertTrue(cmd2.hasOption("c"));

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine2 = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n  optionA, -a OPTIONAL\n    A value for option A\n  optionB, -b REQUIRED\n    B value for option B\n  optionC, -c OPTIONAL\n    C value for option C\n", helpLine2.getHelp());

        final CommandLine cmd3 = new CommandLine();
        cmd3.addOption("a");
        cmd3.addOption("a");
        cmd3.addOption("a");
        cmd3.addOption("b");

        try {
            group.validate(cmd3);
            Assert.assertEquals("group", cmd3.getOptions().get(0).getName());
            Assert.assertTrue(cmd3.hasOption("b"));

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine3 = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n  optionA, -a OPTIONAL\n    A value for option A\n  optionB, -b REQUIRED\n    B value for option B\n  optionC, -c OPTIONAL\n    C value for option C\n", helpLine3.getHelp());

        final CommandLine cmd4 = new CommandLine();
        cmd4.addOption("a");
        cmd4.addOption("a");
        cmd4.addOption("a");
        cmd4.addOption("a");

        try {
            group.validate(cmd4);
            Assert.assertEquals("group", cmd4.getOptions().get(0).getName());
            Assert.assertTrue(cmd4.hasOption("a"));

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine4 = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n  optionA, -a OPTIONAL\n    A value for option A\n  optionB, -b REQUIRED\n    B value for option B\n  optionC, -c OPTIONAL\n    C value for option C\n", helpLine4.getHelp());
    }

    @Test
    public void testGroupWithAnonymousArgument() {
        final List options = new ArrayList();
        options.add(createOption("a", "optionA", false, "A value for option A"));
        options.add(createArg("argA", "anonymous argument A"));

        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final CommandLine cmd = new CommandLine();
        cmd.addOption("a");
        cmd.addOption("argA", "valueA");

        try {
            group.validate(cmd);
            Assert.assertEquals("group", cmd.getOptions().get(0).getName());
            Assert.assertFalse(cmd.hasOption("a"));

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n  optionA, -a OPTIONAL\n    A value for option A\nargA, ARGUMENT\n    anonymous argument A\n", helpLine.getHelp());
    }

    @Test
    public void testGroupWithOptionAndAnonymousArgument() {
        final List options = new ArrayList();
        options.add(createOption("a", "optionA", false, "A value for option A"));
        options.add(createArg("argA", "anonymous argument A"));

        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final CommandLine cmd = new CommandLine();
        cmd.addOption("a");
        cmd.addOption("a");
        cmd.addOption("argA", "valueA");

        try {
            group.validate(cmd);
            Assert.assertEquals("group", cmd.getOptions().get(0).getName());
            Assert.assertFalse(cmd.hasOption("a"));

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n  optionA, -a OPTIONAL\n    A value for option A\nargA, ARGUMENT\n    anonymous argument A\n", helpLine.getHelp());
    }

    @Test
    public void testGroupWithMultipleAnonymousArguments() {
        final List options = new ArrayList();
        options.add(createOption("a", "optionA", false, "A value for option A"));
        options.add(createArg("argA1", "anonymous argument A1"));
        options.add(createArg("argA2", "anonymous argument A2"));

        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final CommandLine cmd = new CommandLine();
        cmd.addOption("a");
        cmd.addOption("a");
        cmd.addOption("argA1", "valueA1");
        cmd.addOption("argA2", "valueA2");

        try {
            group.validate(cmd);
            Assert.assertEquals("group", cmd.getOptions().get(0).getName());
            Assert.assertFalse(cmd.hasOption("a"));

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n  optionA, -a OPTIONAL\n    A value for option A\nargA1, ARGUMENT\n    anonymous argument A1\nargA2, ARGUMENT\n    anonymous argument A2\n", helpLine.getHelp());
    }

    @Test
    public void testGroupWithEmptyList() {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final CommandLine cmd = new CommandLine();

        try {
            group.validate(cmd);
            Assert.assertEquals("group", cmd.getOptions().get(0).getName());
            Assert.assertFalse(cmd.hasOptions());

            final HelpFormatter formatter = new HelpFormatter();
            group.printHelp("test", formatter);
        } catch (OptionException e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }

        final HelpLine helpLine = group.getHelpLine();
        Assert.assertEquals("name: group\ndescription:\noptions:\n", helpLine.getHelp());
    }

    private Option createOption(String name, String longOptName, boolean required, String description) {
        return new OptionBuilder()
                .withName(name)
                .withLongOpt(longOptName)
                .withDescription(description)
                .withRequiredOption(required)
                .create();
    }

    private Argument createArg(String name, String description) {
        return new ArgumentBuilder()
                .withName(name)
                .withDescription(description)
                .isUnicodeAllowed(true)
                .create();
    }
}