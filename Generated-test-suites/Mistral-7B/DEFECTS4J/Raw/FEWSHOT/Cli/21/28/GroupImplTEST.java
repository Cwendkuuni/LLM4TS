package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Test;

public class GroupImplTest {
    @Test
    public void testCreateGroup() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        options.add(createOption("option2", "description2"));
        Group group = new GroupImpl(options, "group", "group description", 1, 2, false);

        assertEquals("group", group.getName());
        assertEquals("group description", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertNull(group.getAnonymous());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.emptySet(), group.getTriggers());
        assertEquals(Collections.emptySet(), group.getPrefixes());
    }

    @Test
    public void testCanProcessTrue() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        options.add(createArgument("arg1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 2, false);

        CommandLine cmd = new CommandLine("cmd");
        String arg = "option1";
        assertTrue(group.canProcess(cmd, arg));

        cmd.parseArgument(arg, null);
        assertFalse(group.canProcess(cmd, arg));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessFalse() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 2, false);

        CommandLine cmd = new CommandLine("cmd");
        String arg = "unknown";
        group.canProcess(cmd, arg);
    }

    @Test
    public void testProcess() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        options.add(createArgument("arg1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 2, false);

        CommandLine cmd = new CommandLine("cmd option1");
        ListIterator<String> arguments = cmd.getArguments().listIterator();

        group.process(cmd, arguments);

        assertEquals(1, cmd.getOptionValues("option1").length);
        assertEquals("", cmd.getOptionValues("option1")[0]);
        assertEquals(0, cmd.getOptionValues("arg1").length);

        arguments = cmd.getArguments().listIterator();
        assertEquals("arg1", arguments.next());
    }

    @Test
    public void testProcessWithArguments() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 2, false);

        CommandLine cmd = new CommandLine("cmd --option1 arg1 arg2");
        ListIterator<String> arguments = cmd.getArguments().listIterator();

        group.process(cmd, arguments);

        assertEquals(1, cmd.getOptionValues("option1").length);
        assertEquals("", cmd.getOptionValues("option1")[0]);
        assertEquals(2, cmd.getOptionValues("arg1").length);
        assertEquals("arg2", cmd.getOptionValues("arg1")[1]);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooManyArguments() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        CommandLine cmd = new CommandLine("cmd --option1 arg1 arg2");
        ListIterator<String> arguments = cmd.getArguments().listIterator();

        group.process(cmd, arguments);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithNotEnoughArguments() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        CommandLine cmd = new CommandLine("cmd --option1");
        ListIterator<String> arguments = cmd.getArguments().listIterator();

        group.process(cmd, arguments);
    }

    @Test
    public void testValidate() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        CommandLine cmd = new CommandLine("cmd --option1");
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        options.add(createOption("option2", "description2"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        CommandLine cmd = new CommandLine("cmd --option1 --option2");
        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        CommandLine cmd = new CommandLine("cmd");
        group.validate(cmd);
    }

    @Test
    public void testAppendUsage() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<String> comp = null;

        group.appendUsage(buffer, helpSettings, comp);

        assertEquals("group (group description)\n", buffer.toString());
    }

    @Test
    public void testAppendUsageWithExpanded() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        Comparator<String> comp = null;

        group.appendUsage(buffer, helpSettings, comp);

        assertEquals("group (group description)\n" +
                "  option1\n", buffer.toString());
    }

    @Test
    public void testAppendUsageWithArguments() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);
        List<Option> anonymousOptions = new ArrayList<>();
        anonymousOptions.add(createArgument("arg1", "description1"));
        group.setAnonymous(anonymousOptions);

        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<String> comp = null;

        group.appendUsage(buffer, helpSettings, comp);

        assertEquals("group (group description)\n" +
                "  option1\n" +
                "  arg1\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("group (group description)\n" +
                "  option1\n", helpLine.getHelp());
    }

    @Test
    public void testHelpLinesWithExpanded() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        List<HelpLine> helpLines = group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null);

        assertEquals(2, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("group (group description)\n", helpLine.getHelp());
        helpLine = helpLines.get(1);
        assertEquals("  option1\n", helpLine.getHelp());
    }

    @Test
    public void testHelpLinesWithArguments() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "description1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);
        List<Option> anonymousOptions = new ArrayList<>();
        anonymousOptions.add(createArgument("arg1", "description1"));
        group.setAnonymous(anonymousOptions);

        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(3, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("group (group description)\n" +
                "  option1\n" +
                "  arg1\n", helpLine.getHelp());
        helpLine = helpLines.get(1);
        assertEquals("  option1\n", helpLine.getHelp());
        helpLine = helpLines.get(2);
        assertEquals("  arg1\n", helpLine.getHelp());
    }

    private Option createOption(String name, String description) {
        Option option = new OptionImpl(name, description, true, null, null);
        option.setRequired(false);
        return option;
    }

    private Argument createArgument(String name, String description) {
        Argument argument = new ArgumentImpl(name, description, true, null);
        return argument;
    }
}