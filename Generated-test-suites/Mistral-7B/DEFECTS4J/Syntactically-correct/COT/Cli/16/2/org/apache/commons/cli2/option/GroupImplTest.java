package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        List<Option> options = Arrays.asList(
            OptionBuilder.withLongOpt("longOpt1")
                .withDescription("Description for longOpt1")
                .withValueSeparator('=')
                .create(),
            OptionBuilder.withLongOpt("longOpt2")
                .withDescription("Description for longOpt2")
                .create(),
            OptionBuilder.withLongOpt("longOpt3")
                .withDescription("Description for longOpt3")
                .isRequired()
                .create(),
            OptionBuilder.withLongOpt("groupOpt")
                .withDescription("Description for groupOpt")
                .withValues(OptionBuilder.listOptionValues("value1", "value2"))
                .create(),
            OptionBuilder.withLongOpt("anonymousArg")
                .withDescription("Description for anonymousArg")
                .create()
        );

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 3);

        assertEquals("groupName", group.getPreferredName());
        assertEquals("Description for groupOpt", group.getDescription());
        assertEquals(options, group.getOptions());
        assertNotNull(group.getTriggers());
        assertEquals(Collections.singleton("groupOpt"), group.getTriggers());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertFalse(group.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupImplMinimumLessThanZero() {
        List<Option> options = Arrays.asList(
                OptionBuilder.create()
                .build()
        );

        new GroupImpl(options, null, null, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGroupImplMaximumLessThanMinimum() {
        List<Option> options = Arrays.asList(
                OptionBuilder.create()
                    .build()
        );

        new GroupImpl(options, null, null, 2, 1);
    }

    @Test
    public void testGroupImplCanProcess() {
        List<Option> options = Arrays.asList(
            OptionBuilder.withLongOpt("longOpt1")
                .create(),
            OptionBuilder.withLongOpt("longOpt2")
                .create(),
            OptionBuilder.withLongOpt("longOpt3")
                .create(),
            OptionBuilder.withLongOpt("groupOpt")
                .create(),
            OptionBuilder.withLongOpt("anonymousArg")
                .create()
        );

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 3);

        CommandLine commandLine = new CommandLine();
        List<String> args = new ArrayList<>(Arrays.asList("--groupOpt", "value1"));

        // Test option processing
        assertTrue(group.canProcess(commandLine, args.get(0)));

        // Test key processing
        assertTrue(group.canProcess(commandLine, "longOpt1"));
        assertTrue(group.canProcess(commandLine, "longOpt2"));
        assertTrue(group.canProcess(commandLine, "longOpt3"));
        assertTrue(group.canProcess(commandLine, "groupOpt"));

        // Test key processing with bursting
        assertTrue(group.canProcess(commandLine, "longOpt1"));
        assertTrue(group.canProcess(commandLine, "longOpt3"));
        assertFalse(group.canProcess(commandLine, "anonymousArg"));

        // Test argument processing
        assertTrue(group.canProcess(commandLine, args.get(1)));
    }

    @Test
    public void testGroupImplProcess() throws OptionException {
        List<Option> options = Arrays.asList(
            OptionBuilder.withLongOpt("longOpt1")
                .withValueSeparator('=')
                .create(),
            OptionBuilder.withLongOpt("longOpt2")
                .create(),
            OptionBuilder.withLongOpt("longOpt3")
                .isRequired()
                .withValues(OptionBuilder.listOptionValues("value1", "value2"))
                .create(),
            OptionBuilder.withLongOpt("groupOpt")
                .withValues(OptionBuilder.listOptionValues("value1", "value2"))
                .create(),
            OptionBuilder.withLongOpt("anonymousArg")
                .create()
        );

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 3);

        CommandLine commandLine = new CommandLine();
        List<String> args = new ArrayList<>(Arrays.asList("--groupOpt", "value1", "--longOpt1=value3", "--longOpt3=value2"));

        group.process(commandLine, commandLine.parser().parse(args.toArray(new String[0])));

        assertEquals("value3", commandLine.getOptionValue("longOpt1"));
        assertEquals("value1", commandLine.getOptionValue("groupOpt"));

        // Test that other options are processed correctly
        assertEquals("value1", commandLine.getOptionValue("longOpt2"));
        assertEquals("value2", commandLine.getOptionValue("longOpt3"));
        assertNull(commandLine.getOptionValue("anonymousArg"));
    }

    @Test
    public void testGroupImplValidate() throws OptionException {
        List<Option> options = Arrays.asList(
            OptionBuilder.withLongOpt("longOpt1")
                .isRequired()
                .create(),
            OptionBuilder.withLongOpt("longOpt2")
                .create(),
            OptionBuilder.withLongOpt("longOpt3")
                .create()
        );

        GroupImpl group = new GroupImpl(options, "groupName", null, 2, 3);

        CommandLine commandLine = new CommandLine();
        List<String> args = new ArrayList<>(Arrays.asList("--groupName"));

        try {
            group.validate(commandLine);
            fail("Invalid number of options should have thrown an exception");
        } catch (OptionException e) {
            assertEquals(OptionException.MissingOptionValue, e.getType());
            assertEquals("longOpt1", e.getOption().getLongOpt());
        }

        args = new ArrayList<>(Arrays.asList("--groupName", "--longOpt1"));

        group.validate(commandLine);
    }

    @Test
    public void testGroupImplAppendUsage() {
        List<Option> options = Arrays.asList(
            OptionBuilder.withLongOpt("longOpt1")
                .withDescription("Description for longOpt1")
                .create(),
            OptionBuilder.withLongOpt("longOpt2")
                .withDescription("Description for longOpt2")
                .create(),
            OptionBuilder.withLongOpt("groupOpt")
                .withDescription("Description for groupOpt")
                .withValues(OptionBuilder.listOptionValues("value1", "value2"))
                .create(),
            OptionBuilder.withLongOpt("anonymousArg")
                .withDescription("Description for anonymousArg")
                .create()
        );

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 3);

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet(DisplaySetting.ALL);
        Comparator comp = null;

        group.appendUsage(buffer, helpSettings, comp);

        String expectedUsage = TestUtils.getExpectedUsage(
            "groupName:",
            "Description for groupOpt (value1|value2)",
            "Description for longOpt1 [VALUE]",
            "Description for longOpt2",
            "Description for anonymousArg",
            null);

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testGroupImplHelpLines() {
        List<Option> options = Arrays.asList(
            OptionBuilder.withLongOpt("longOpt1")
                .withDescription("Description for longOpt1")
                .create(),
            OptionBuilder.withLongOpt("longOpt2")
                .withDescription("Description for longOpt2")
                .create(),
            OptionBuilder.withLongOpt("groupOpt")
                .withDescription("Description for groupOpt")
                .withValues(OptionBuilder.listOptionValues("value1", "value2"))
                .create(),
            OptionBuilder.withLongOpt("anonymousArg")
                .withDescription("Description for anonymousArg")
                .create()
        );

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 3);

        List helpLines = group.helpLines(0, new HashSet(DisplaySetting.ALL), null);

        assertEquals(5, helpLines.size());

        String expectedHelpLine1 = TestUtils.getExpectedHelpLine(
            "groupName:",
            "",
            "Description for groupOpt (value1|value2)",
            null);

        String expectedHelpLine2 = TestUtils.getExpectedHelpLine(
            "  longOpt1:",
            "  Description for longOpt1",
            "  [VALUE]",
            null);

        String expectedHelpLine3 = TestUtils.getExpectedHelpLine(
            "  longOpt2:",
            "  Description for longOpt2",
            null,
            null);

        String expectedHelpLine4 = TestUtils.getExpectedHelpLine(
            "  groupOpt:",
            "  Description for groupOpt",
            "value1|value2",
            null);

        String expectedHelpLine5 = TestUtils.getExpectedHelpLine(
            "  anonymousArg:",
            "  Description for anonymousArg",
            null,
            null);

        List<String> helpLinesList = new ArrayList<>();
        helpLinesList.addAll((List<String>) helpLines.get(0));

        for (int i = 1; i < helpLines.size(); i++) {
            helpLinesList.addAll((List<String>) helpLines.get(i));
        }

        assertEquals(expectedHelpLine1, helpLinesList.get(0));
        assertEquals(expectedHelpLine2, helpLinesList.get(1));
        assertEquals(expectedHelpLine3, helpLinesList.get(2));
        assertEquals(expectedHelpLine4, helpLinesList.get(3));
        assertEquals(expectedHelpLine5, helpLinesList.get(4));
    }

    @Test
    public void testGroupImplGetOptions() {
        List<Option> options = Arrays.asList(
            OptionBuilder.withLongOpt("longOpt1")
                .create(),
            OptionBuilder.withLongOpt("longOpt2")
                .create(),
            OptionBuilder.withLongOpt("groupOpt")
                .create(),
            OptionBuilder.withLongOpt("anonymousArg")
                .create()
        );

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 3);

        List optionsList = group.getOptions();

        List<Option> expectedOptions = Arrays.asList(
                OptionBuilder.withLongOpt("longOpt1").build(),
                OptionBuilder.withLongOpt("longOpt2").build(),
                OptionBuilder.withLongOpt("groupOpt").build(),
                OptionBuilder.withLongOpt("anonymousArg").build()
        );

        assertArrayEquals(expectedOptions.toArray(), optionsList.toArray());
    }

    @Test
    public void testGroupImplFindOption() {
        List<Option> options = Arrays.asList(
            OptionBuilder.withLongOpt("longOpt1")
                .create(),
            OptionBuilder.withLongOpt("longOpt2")
                .create(),
            OptionBuilder.withLongOpt("groupOpt")
                .create(),
            OptionBuilder.withLongOpt("anonymousArg")
                .create()
        );

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 3);

        Option longOpt1 = group.findOption("longOpt1");
        Option longOpt2 = group.findOption("longOpt2");
        Option groupOpt = group.findOption("groupOpt");
        Option anonymousArg = group.findOption("anonymousArg");

        assertNotNull(longOpt1);
        assertNotNull(longOpt2);
        assertNotNull(groupOpt);
        assertNotNull(anonymousArg);
    }
}