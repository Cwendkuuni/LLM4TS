package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GroupImplTest {

    private HelpFormatter formatter;
    private List<Option> options;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
        options = List.of(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("Group option 1")
                        .create(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Group option 2")
                        .create(),
                OptionBuilder.withLongOpt("option1")
                        .withDescription("Option 1")
                        .isRequired()
                        .create(),
                OptionBuilder.withLongOpt("option2")
                        .withDescription("Option 2")
                        .create(),
                OptionBuilder.withLongOpt("anonymous")
                        .withDescription("Anonymous argument")
                        .create()
        );
    }

    @Test
    public void testValidUsage() throws ParseException {
        final Group group = new GroupImpl(options, "MyGroup", null, 1, 1);

        final CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(group, new String[]{"--group1", "--option1", "arg1", "--option2", "--anonymous", "arg2"});

        formatter.printHelp("MyGroup", group.getOptions(), true);

        Assert.assertEquals("--group1\n" +
                "Group option 1\n" +
                "--option1 <arg>   Option 1\n" +
                "--option2 <arg>\n" +
                "--anonymous <arg>\n", formatter.getHelpText(true));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        final Group group = new GroupImpl(options, "MyGroup", null, 1, 1);

        final CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(group, new String[]{"--group1"});
    }

    @Test(expected = ParseException.class)
    public void testTooManyOptions() throws ParseException {
        final Group group = new GroupImpl(options, "MyGroup", null, 1, 1);

        final CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(group, new String[]{"--group1", "--option1", "arg1", "--option1", "--option2", "--anonymous", "arg2"});
    }

    @Test
    public void testUsageWithHelp() throws ParseException {
        final Group group = new GroupImpl(options, "MyGroup", null, 1, 1);

        final CommandLine commandLine = new CommandLine("-h");
        commandLine.parse(group, new String[0]);

        formatter.printHelp("MyGroup", group.getOptions(), true);

        Assert.assertEquals("Usage: MyGroup [options] [arguments]\n" +
                "\n" +
                "Options:\n" +
                "   --group1      Group option 1\n" +
                "   --option1 <arg>   Option 1\n" +
                "   --option2 <arg>\n" +
                "   --anonymous <arg>\n" +
                "\n" +
                "Examples:\n" +
                "  MyGroup --group1 --option1 arg1 --option2 arg2 --anonymous arg3\n", formatter.getHelpText(true));
    }

    @Test
    public void testHelpWithSpecificOption() throws ParseException {
        final Group group = new GroupImpl(options, "MyGroup", null, 1, 1);

        final CommandLine commandLine = new CommandLine("-g");
        commandLine.parse(group, new String[0]);

        final HelpLine helpLine = group.findOption("group1").getHelpLine();

        formatter.printHelp("MyGroup", Collections.singletonList(helpLine), true);

        Assert.assertEquals("--group1\n" +
                "Group option 1\n", formatter.getHelpText(true));
    }

    @Test
    public void testHelpWithAnonymousArgument() throws ParseException {
        final Group group = new GroupImpl(options, "MyGroup", null, 1, 1);

        final CommandLine commandLine = new CommandLine("-a");
        commandLine.parse(group, new String[0]);

        final HelpLine helpLine = group.findOption("anonymous").getHelpLine();

        formatter.printHelp("MyGroup", Collections.singletonList(helpLine), true);

        Assert.assertEquals("--anonymous <arg>\n" +
                "Anonymous argument\n", formatter.getHelpText(true));
    }
}