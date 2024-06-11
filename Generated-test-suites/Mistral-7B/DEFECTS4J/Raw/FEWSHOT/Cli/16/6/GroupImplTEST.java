package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.TestUtil;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final String argName = "arg";
        final String argDescription = "test argument";
        final String groupName = "group";
        final String groupDescription = "test group";
        final int min = 2;
        final int max = 5;

        final List<Option> options = new ArrayList<>();
        final Option opt1 = TestUtil.createOption("opt1", "t1", "t1d", "desc1");
        opt1.setRequired(true);
        options.add(opt1);

        final Option opt2 = TestUtil.createOption("opt2", "t2", null, "desc2");
        options.add(opt2);

        final Option opt3 = TestUtil.createOption("opt3", "t3", null, "desc3");
        options.add(opt3);

        final Option opt4 = TestUtil.createOption("opt4", "t4", null, "desc4");
        options.add(opt4);

        final Option opt5 = TestUtil.createOption("opt5", "t5", null, "desc5");
        options.add(opt5);

        final Argument arg = TestUtil.createArgument(argName, argDescription);
        arg.setRequired(true);

        final List<Argument> anonymousArgs = new ArrayList<>();
        anonymousArgs.add(arg);

        final SortedMap<String, Option> optionMap = new TreeMap<>(Comparator.reverseOrder());
        for (final Option option : options) {
            optionMap.put(option.getLongOpt(), option);
        }

        final Set<String> prefixes = new HashSet<>();
        prefixes.add("g");

        final List<Option> groupOptions = Collections.unmodifiableList(options);
        final List<Argument> groupAnonymousArgs = Collections.unmodifiableList(anonymousArgs);
        final Group group = new GroupImpl(groupOptions, groupName, groupDescription, min, max, anonymousArgs, optionMap, prefixes);

        assertEquals(groupName, group.getPreferredName());
        assertEquals(groupDescription, group.getDescription());
        assertEquals(groupOptions, group.getOptions());
        assertEquals(min, group.getMinimum());
        assertEquals(max, group.getMaximum());
        assertEquals(groupAnonymousArgs, group.getAnonymous());
        assertEquals(optionMap, group.getOptionMap());
        assertEquals(prefixes, group.getPrefixes());

        assertFalse(group.canProcess(new WriteableCommandLine(), null));

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("opt1");
        commandLine.addOption("opt2");
        commandLine.addOption("opt3");
        commandLine.addOption("opt4");
        commandLine.addOption("opt5");
        commandLine.addOption("arg");
        commandLine.addOption("arg");

        assertTrue(group.canProcess(commandLine, argName));

        final CommandLine parsed = new CommandLine();
        final List<Object> parsedArgs = new ArrayList<>();
        final List<Object> args = new ArrayList<>();
        parsed.setArgs(args);
        group.process(parsed, parsedArgs.iterator());

        assertSame(opt1, parsed.findOption("opt1"));
        assertSame(opt2, parsed.findOption("opt2"));
        assertSame(opt3, parsed.findOption("opt3"));
        assertSame(opt4, parsed.findOption("opt4"));
        assertSame(opt5, parsed.findOption("opt5"));
        assertSame(arg, parsed.findOption(argName));
        assertEquals(6, parsed.getArgs().size());

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Should have succeeded validating the command line");
        }

        final Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(9, helpLines.size());
        for (final HelpLine helpLine : helpLines) {
            assertTrue(helpLine.hasOption());
            assertTrue(helpLine.hasDescription());
            assertTrue(helpLine.hasLongOpt());
            assertTrue(helpLine.hasShortOpt());
        }

        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, helpSettings, null);
        assertEquals(groupName, buffer.substring(0, groupName.length() + 1));
        assertEquals(groupDescription, buffer.substring(groupName.length() + 1, buffer.length()));

        assertEquals(groupName, group.getName());
        assertEquals(groupDescription, group.getDescription());
    }

    @Test
    public void testGroupImplWithArgumentOnly() {
        final String groupName = "group";
        final String groupDescription = "test group";
        final int min = 1;
        final int max = 1;

        final Argument arg = TestUtil.createArgument("arg", "test argument");
        arg.setRequired(true);

        final List<Argument> anonymousArgs = new ArrayList<>();
        anonymousArgs.add(arg);

        final SortedMap<String, Option> optionMap = new TreeMap<>(Comparator.reverseOrder());

        final Group group = new GroupImpl(Collections.emptyList(), groupName, groupDescription, min, max, anonymousArgs, optionMap, Collections.emptySet());

        assertEquals(groupName, group.getPreferredName());
        assertEquals(groupDescription, group.getDescription());
        assertEquals(Collections.emptyList(), group.getOptions());
        assertEquals(min, group.getMinimum());
        assertEquals(max, group.getMaximum());
        assertEquals(anonymousArgs, group.getAnonymous());
        assertEquals(optionMap, group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());

        assertFalse(group.canProcess(new WriteableCommandLine(), null));

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("arg");

        try {
            group.validate(commandLine);
            fail("Should have failed validating the command line");
        } catch (OptionException e) {
            // expected
        }

        final Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(1, helpLines.size());
        final HelpLine helpLine = helpLines.get(0);
        assertEquals(arg.getLongOpt(), helpLine.getLongOpt());
        assertEquals(arg.getShortOpt(), helpLine.getShortOpt());
        assertEquals(arg.getName(), helpLine.getOptionStr());
        assertEquals(arg.getDescription(), helpLine.getDescription());

        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, helpSettings, null);
        assertEquals(groupName, buffer.substring(0, groupName.length() + 1));
        assertEquals(groupDescription, buffer.substring(groupName.length() + 1, buffer.length()));

        assertEquals(groupName, group.getName());
        assertEquals(groupDescription, group.getDescription());
    }

    @Test
    public void testGroupImplWithNoOptions() {
        final String groupName = "group";
        final String groupDescription = "test group";
        final int min = 1;
        final int max = 1;

        final List<Option> options = Collections.emptyList();

        final List<Argument> anonymousArgs = new ArrayList<>();

        final SortedMap<String, Option> optionMap = new TreeMap<>(Comparator.reverseOrder());

        final Group group = new GroupImpl(options, groupName, groupDescription, min, max, anonymousArgs, optionMap, Collections.emptySet());

        assertEquals(groupName, group.getPreferredName());
        assertEquals(groupDescription, group.getDescription());
        assertEquals(options, group.getOptions());
        assertEquals(min, group.getMinimum());
        assertEquals(max, group.getMaximum());
        assertEquals(anonymousArgs, group.getAnonymous());
        assertEquals(optionMap, group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());

        assertFalse(group.canProcess(new WriteableCommandLine(), null));

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("arg");

        try {
            group.validate(commandLine);
            fail("Should have failed validating the command line");
        } catch (OptionException e) {
            // expected
        }

        final Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(0, helpLines.size());

        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, helpSettings, null);
        assertEquals(groupName, buffer.substring(0, groupName.length() + 1));
        assertEquals(groupDescription, buffer.substring(groupName.length() + 1, buffer.length()));

        assertEquals(groupName, group.getName());
        assertEquals(groupDescription, group.getDescription());
    }

    @Test
    public void testGroupImplWithEmptyOptionList() {
        final String groupName = "group";
        final String groupDescription = "test group";
        final int min = 1;
        final int max = 1;

        final List<Option> options = Collections.emptyList();

        final List<Argument> anonymousArgs = new ArrayList<>();

        final SortedMap<String, Option> optionMap = new TreeMap<>(Comparator.reverseOrder());

        final Group group = new GroupImpl(options, groupName, groupDescription, min, max, anonymousArgs, optionMap, Collections.emptySet());

        assertEquals(groupName, group.getPreferredName());
        assertEquals(groupDescription, group.getDescription());
        assertEquals(options, group.getOptions());
        assertEquals(min, group.getMinimum());
        assertEquals(max, group.getMaximum());
        assertEquals(anonymousArgs, group.getAnonymous());
        assertEquals(optionMap, group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());

        assertFalse(group.canProcess(new WriteableCommandLine(), null));

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("arg");

        try {
            group.validate(commandLine);
            fail("Should have failed validating the command line");
        } catch (OptionException e) {
            // expected
        }

        final Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        final List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(0, helpLines.size());

        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, helpSettings, null);
        assertEquals(groupName, buffer.substring(0, groupName.length() + 1));
        assertEquals(groupDescription, buffer.substring(groupName.length() + 1, buffer.length()));

        assertEquals(groupName, group.getName());
        assertEquals(groupDescription, group.getDescription());
    }

    @Test
    public void testGroupImplWithInvalidMin() {
        final String groupName = "group";
        final String groupDescription = "test group";
        final int min = -1;
        final int max = 1;

        final List<Option> options = new ArrayList<>();
        final Option opt1 = TestUtil.createOption("opt1", "t1", "t1d", "desc1");
        options.add(opt1);

        final List<Argument> anonymousArgs = new ArrayList<>();

        final SortedMap<String, Option> optionMap = new TreeMap<>(Comparator.reverseOrder());
        for (final Option option : options) {
            optionMap.put(option.getLongOpt(), option);
        }

        try {
            new GroupImpl(options, groupName, groupDescription, min, max, anonymousArgs, optionMap, Collections.emptySet());
            fail("Should have failed to create the group with invalid minimum");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGroupImplWithInvalidMax() {
        final String groupName = "group";
        final String groupDescription = "test group";
        final int min = 1;
        final int max = -1;

        final List<Option> options = new ArrayList<>();
        final Option opt1 = TestUtil.createOption("opt1", "t1", "t1d", "desc1");
        opt1.setRequired(true);
        options.add(opt1);

        final List<Argument> anonymousArgs = new ArrayList<>();

        final SortedMap<String, Option> optionMap = new TreeMap<>(Comparator.reverseOrder());
        for (final Option option : options) {
            optionMap.put(option.getLongOpt(), option);
        }

        try {
            new GroupImpl(options, groupName, groupDescription, min, max, anonymousArgs, optionMap, Collections.emptySet());
            fail("Should have failed to create the group with invalid maximum");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGroupImplWithDuplicateTrigger() {
        final String groupName = "group";
        final String groupDescription = "test group";
        final int min = 1;
        final int max = 1;

        final List<Option> options = new ArrayList<>();
        final Option opt1 = TestUtil.createOption("opt1", "t1", "t1d", "desc1");
        options.add(opt1);
        final Option opt2 = TestUtil.createOption("opt1", "t1", "t1d2", "desc2");
        options.add(opt2);

        final List<Argument> anonymousArgs = new ArrayList<>();

        final SortedMap<String, Option> optionMap = new TreeMap<>(Comparator.reverseOrder());
        for (final Option option : options) {
            optionMap.put(option.getLongOpt(), option);
        }

        try {
            new GroupImpl(options, groupName, groupDescription, min, max, anonymousArgs, optionMap, Collections.emptySet());
            fail("Should have failed to create the group with duplicate trigger");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}