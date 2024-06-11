package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class GroupImplTest {

    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(OptionBuilder.withLongOpt("group1")
                .withDescription("Description for group1")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("group2")
                .withDescription("Description for group2")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .create());

        options = new Options();
        options.addGroup(new GroupImpl(groupOptions, "Group", null, 1, 1));
        formatter = new HelpFormatter();
    }

    @Test
    public void testCanProcess_ValidOption() {
        CommandLine commandLine = new CommandLine("-", null);
        Assert.assertTrue(new GroupImpl(options.getOptions(), null, null, 1, 1)
                .canProcess(commandLine, "--group1"));
    }

    @Test
    public void testCanProcess_InvalidOption() {
        CommandLine commandLine = new CommandLine("-", null);
        Assert.assertFalse(new GroupImpl(options.getOptions(), null, null, 1, 1)
                .canProcess(commandLine, "--invalid"));
    }

    @Test
    public void testCanProcess_BurstableOption() {
        CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(new PosixParser().parse(options));
        Assert.assertTrue(new GroupImpl(options.getOptions(), null, null, 1, 1)
                .canProcess(commandLine, "--option2"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_TooManyOptions() {
        CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(new PosixParser().parse(options));
        commandLine.parse(new PosixParser().parse(options));
        new GroupImpl(options.getOptions(), null, null, 1, 1).canProcess(commandLine, "--option1");
    }

    @Test
    public void testCanProcess_AnonymousArgument() {
        CommandLine commandLine = new CommandLine("-", null);
        final Option optionAnonymous = OptionBuilder.withLongOpt("anonymous")
                .withDescription("Description for anonymous")
                .isRequired(false)
                .create();
        options.addOption(optionAnonymous);

        commandLine.parse(new PosixParser().parse(options));
        Assert.assertTrue(new GroupImpl(options.getOptions(), null, null, 1, 1)
                .canProcess(commandLine, "anonymousValue"));
    }

    @Test
    public void testProcess_ValidOption() {
        CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(new PosixParser().parse(options));

        final GroupImpl group = new GroupImpl(options.getOptions(), null, null, 1, 1);
        group.process(commandLine, commandLine.iterator());

        Assert.assertEquals("--group1", commandLine.getOptionValue("group1"));
        Assert.assertEquals("--option1", commandLine.getOptionValue("option1"));
        Assert.assertNull(commandLine.getOptionValue("option2"));
        Assert.assertNull(commandLine.getOptionValue("anonymous"));
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOption() {
        CommandLine commandLine = new CommandLine("-", null);
        new GroupImpl(options.getOptions(), null, null, 1, 1).process(commandLine, commandLine.iterator());
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidAnonymousArgument() {
        CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(new PosixParser().parse(options));

        final GroupImpl group = new GroupImpl(options.getOptions(), null, null, 1, 1);
        group.process(commandLine, commandLine.iterator());
        group.process(commandLine, commandLine.iterator());

        Assert.assertEquals("--group1", commandLine.getOptionValue("group1"));
        Assert.assertEquals("--option1", commandLine.getOptionValue("option1"));

        // The test will fail if there is no error
        group.process(commandLine, commandLine.iterator());
    }

    @Test
    public void testValidate() {
        CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(new PosixParser().parse(options));

        new GroupImpl(options.getOptions(), null, null, 1, 1).validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_MissingOption() {
        CommandLine commandLine = new CommandLine("-", null);
        new GroupImpl(options.getOptions(), null, null, 1, 1).validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooManyOptions() {
        CommandLine commandLine = new CommandLine("-", null);
        commandLine.parse(new PosixParser().parse(options));
        commandLine.parse(new PosixParser().parse(options));
        new GroupImpl(options.getOptions(), null, null, 1, 1).validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(new StringWriter(stringWriter));

        new GroupImpl(options.getOptions(), null, null, 1, 1).appendUsage(printWriter, null, null);
        printWriter.flush();

        final String expectedUsage = "group:\n" +
                "  Description for group1\n" +
                "  Description for group2\n" +
                "options:\n" +
                "  option1:\n" +
                "    Description for option1\n" +
                "  option2:\n" +
                "    Description for option2\n";

        Assert.assertEquals(expectedUsage, stringWriter.toString());
    }

    @Test
    public void testHelpLines() {
        final List<String> helpLines = new GroupImpl(options.getOptions(), null, null, 1, 1)
                .helpLines(0, null, null);

        final String expectedHelpLines = "group:\n" +
                "Description for group1\n" +
                "Description for group2\n" +
                "\noptions:\n" +
                "option1:\n" +
                "Description for option1\n" +
                "option2:\n" +
                "Description for option2\n";

        Assert.assertEquals(expectedHelpLines, StringUtils.join(helpLines, System.lineSeparator()));
    }

    @Test
    public void testGetOptions() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(OptionBuilder.withLongOpt("group1")
                .withDescription("Description for group1")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("group2")
                .withDescription("Description for group2")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .create());

        List<Option> actualOptions = new GroupImpl(groupOptions, "Group", null, 1, 1).getOptions();
        List<Option> expectedOptions = new ArrayList<>(groupOptions);
        expectedOptions.remove(0); // Remove the group option itself

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetAnonymous() {
        final Option optionAnonymous = OptionBuilder.withLongOpt("anonymous")
                .withDescription("Description for anonymous")
                .isRequired(false)
                .create();
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(OptionBuilder.withLongOpt("group1")
                .withDescription("Description for group1")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("group2")
                .withDescription("Description for group2")
                .create());
        groupOptions.add(optionAnonymous);
        groupOptions.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .create());

        List<Option> actualAnonymous = new GroupImpl(groupOptions, "Group", null, 1, 1).getAnonymous();
        List<Option> expectedAnonymous = new ArrayList<>();
        expectedAnonymous.add(optionAnonymous);

        Assert.assertEquals(expectedAnonymous, actualAnonymous);
    }

    @Test
    public void testFindOption() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(OptionBuilder.withLongOpt("group1")
                .withDescription("Description for group1")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("group2")
                .withDescription("Description for group2")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .create());
        groupOptions.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .create());

        Option actualOption = new GroupImpl(groupOptions, "Group", null, 1, 1)
                .findOption("option1");
        Option expectedOption = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .create();

        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testGetMinimum() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        Assert.assertEquals(0, group.getMinimum());

        group = new GroupImpl(Collections.emptyList(), null, null, 1, 1);
        Assert.assertEquals(1, group.getMinimum());

        group = new GroupImpl(Collections.emptyList(), null, null, 2, 2);
        Assert.assertEquals(2, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        Assert.assertEquals(0, group.getMaximum());

        group = new GroupImpl(Collections.emptyList(), null, null, 1, 1);
        Assert.assertEquals(1, group.getMaximum());

        group = new GroupImpl(Collections.emptyList(), null, null, 2, 2);
        Assert.assertEquals(2, group.getMaximum());

        group = new GroupImpl(Collections.emptyList(), null, null, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        Assert.assertFalse(group.isRequired());

        group = new GroupImpl(Collections.emptyList(), null, null, 1, 1);
        Assert.assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        CommandLine commandLine = new CommandLine("-", null);
        new GroupImpl(Collections.emptyList(), null, null, 0, 0).defaults(commandLine);

        Assert.assertNull(commandLine.getOptionValue("option1"));
        Assert.assertNull(commandLine.getOptionValue("option2"));
        Assert.assertNull(commandLine.getOptionValue("anonymous"));
    }
}