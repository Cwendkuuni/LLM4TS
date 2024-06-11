package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithRequiredOption() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
            Arrays.asList(
                new Option("g", "group", true, "Group Name", "Group description"),
                new Option("a", "arg", true, "Argument description")
            ),
            "group",
            "Group description",
            1,
            1,
            true);
        options.addOptionGroup(group);

        final HelpFormatter formatter = new HelpFormatter();

        try {
            final CommandLine commandLine = new CommandLine();
            group.parse(commandLine, args());
            Assert.fail();
        } catch (ParseException expected) {
            // Expected
        }

        formatter.printHelp("TestGroup", "TestGroup [options]", true);
        System.out.println(formatter.getHelpTemplate());
    }

    @Test
    public void testGroupWithOptionalOption() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("g", "group", true, "Group Name", "Group description"),
                        new Option("a", "arg", true, "Argument description")
                ),
                "group",
                "Group description",
                0,
                1,
                false);
        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine();
        group.parse(commandLine, args());

        Assert.assertNull(commandLine.getOptionValue("g"));
        Assert.assertNull(commandLine.getOptionValue("a"));

        formatter.printHelp("TestGroup", "TestGroup [options]", true);
        System.out.println(formatter.getHelpTemplate());
    }

    @Test
    public void testGroupWithMandatoryOption() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("g", "group", true, "Group Name", "Group description"),
                        new Option("a", "arg", true, "Argument description")
                ),
                "group",
                "Group description",
                1,
                1,
                false);
        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine();
        group.parse(commandLine, args(new String[]{"TestGroup"}));

        Assert.assertNotNull(commandLine.getOptionValue("g"));
        Assert.assertNotNull(commandLine.getOptionValue("a"));

        formatter.printHelp("TestGroup", "TestGroup [options]", true);
        System.out.println(formatter.getHelpTemplate());
    }

    @Test
    public void testGroupWithMultipleOptions() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("g", "group", true, "Group Name", "Group description"),
                        new Option("a", "arg", true, "Argument description")
                ),
                "group",
                "Group description",
                1,
                1,
                false);
        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine();
        group.parse(commandLine, args(new String[]{"TestGroup", "--group", "TestGroupName", "--arg", "TestArgValue1", "--arg", "TestArgValue2"}));

        Assert.assertEquals("TestGroupName", commandLine.getOptionValue("g"));
        Assert.assertEquals("TestArgValue1", commandLine.getOptionValue("a"));
        Assert.assertEquals("TestArgValue2", commandLine.getOptionValue("a"));

        formatter.printHelp("TestGroup", "TestGroup [options]", true);
        System.out.println(formatter.getHelpTemplate());
    }

    @Test(expected = ParseException.class)
    public void testGroupWithMinimum() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("g", "group", true, "Group Name", "Group description"),
                        new Option("a", "arg", true, "Argument description")
                ),
                "group",
                "Group description",
                2,
                1,
                false);
        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine();
        group.parse(commandLine, args(new String[]{"TestGroup", "--group", "TestGroupName"}));

        formatter.printHelp("TestGroup", "TestGroup [options]", true);
        System.out.println(formatter.getHelpTemplate());

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testGroupWithMaximum() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("g", "group", true, "Group Name", "Group description"),
                        new Option("a", "arg", true, "Argument description")
                ),
                "group",
                "Group description",
                1,
                2,
                false);
        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine();
        group.parse(commandLine, args(new String[]{"TestGroup", "--group", "TestGroupName", "--group", "TestGroupName2"}));

        formatter.printHelp("TestGroup", "TestGroup [options]", true);
        System.out.println(formatter.getHelpTemplate());

        Assert.fail();
    }

    @Test
    public void testGroupWithAnonymousArgument() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("g", "group", true, "Group Name", "Group description"),
                        new Option("a", "arg", true, "Argument description")
                ),
                "group",
                "Group description",
                1,
                1,
                false);
        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine();
        group.parse(commandLine, args(new String[]{"TestGroup", "--arg", "TestArgValue"}));

        Assert.assertEquals("TestArgValue", commandLine.getOptionValue(group.getOptions().get(0).getLongOpt()));

        formatter.printHelp("TestGroup", "TestGroup [options]", true);
        System.out.println(formatter.getHelpTemplate());
    }

    @Test
    public void testGroupWithAnonymousArgumentWithValue() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                Arrays.asList(
                        new Option("g", "group", true, "Group Name", "Group description"),
                        new Option("a", "arg", true, "Argument description")
                ),
                "group",
                "Group description",
                1,
                1,
                false);
        options.addOptionGroup(group);

        final CommandLine commandLine = new CommandLine();
        group.parse(commandLine, args(new String[]{"TestGroup", "--arg", "TestArgValue=TestArgValueValue"}));

        Assert.assertEquals("TestArgValueValue", commandLine.getOptionValue(group.getOptions().get(0).getLongOpt()));

        formatter.printHelp("TestGroup", "TestGroup [options]", true);
        System.out.println(formatter.getHelpTemplate());
    }

    private String[] args() {
        return new String[0];
    }

    private String[] args(String... args) {
        return args;
    }
}