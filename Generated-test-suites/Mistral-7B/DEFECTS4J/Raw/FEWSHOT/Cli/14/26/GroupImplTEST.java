package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
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

import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testCreateGroupWithEmptyList() {
        final List emptyList = Collections.emptyList();
        final Group group = new GroupImpl(emptyList, null, null, 0, 0);

        assertNull(group.getDescription());
        assertNull(group.getName());
        assertEquals(Collections.emptyList(), group.getOptions());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertEquals(Collections.emptyMap(), group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());
        assertEquals(0, group.getMinimum());
        assertEquals(0, group.getMaximum());
    }

    @Test
    public void testCreateGroupWithSingleOption() {
        final Option option = new Option("opt", "option", true, "Description");
        final List options = Arrays.asList(option);
        final Group group = new GroupImpl(options, null, null, 0, 0);

        assertEquals("option", group.getPreferredName());
        assertNull(group.getDescription());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertEquals(Collections.singletonMap("opt", option), group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());
        assertEquals(0, group.getMinimum());
        assertEquals(0, group.getMaximum());
    }

    @Test
    public void testCreateGroupWithSingleArgument() {
        final Argument arg = new Argument("arg", 1, "description", false);
        final List options = Collections.singletonList(arg);
        final Group group = new GroupImpl(options, null, null, 0, 0);

        assertEquals("arg", group.getPreferredName());
        assertNull(group.getDescription());
        assertEquals(Collections.emptyList(), group.getOptions());
        assertEquals(Collections.unmodifiableList(Arrays.asList(arg)), group.getAnonymous());
        assertEquals(Collections.emptyMap(), group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());
        assertEquals(0, group.getMinimum());
        assertEquals(0, group.getMaximum());
    }

    @Test
    public void testCreateGroupWithSingleOptionAndArgument() {
        final Option option = new Option("opt", "option", true, "Description");
        final Argument arg = new Argument("arg", 1, "description", false);
        final List options = Arrays.asList(option, arg);
        final Group group = new GroupImpl(options, null, null, 0, 0);

        assertEquals("option", group.getPreferredName());
        assertNull(group.getDescription());
        assertEquals(Collections.unmodifiableList(Arrays.asList(option)), group.getOptions());
        assertEquals(Collections.unmodifiableList(Arrays.asList(arg)), group.getAnonymous());
        assertEquals(Collections.singletonMap("opt", option), group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());
        assertEquals(0, group.getMinimum());
        assertEquals(0, group.getMaximum());
    }

    @Test
    public void testCanProcess() {
        final Option option = new Option("opt", "option", true, "Description");
        final Group group = new GroupImpl(Arrays.asList(option), null, null, 0, 0);

        assertFalse(group.canProcess(null, null));
        assertFalse(group.canProcess(null, "opt"));
        assertFalse(group.canProcess(null, "arg"));
        assertFalse(group.canProcess(null, "x"));

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions());

        assertFalse(group.canProcess(commandLine, null));
        assertTrue(group.canProcess(commandLine, "opt"));
        assertFalse(group.canProcess(commandLine, "arg"));
        assertFalse(group.canProcess(commandLine, "x"));
    }

    @Test
    public void testGetPrefixes() {
        final Set prefixes = new HashSet();
        prefixes.add("pfx");

        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        option1.setPrefixes(prefixes);

        final Option option2 = new Option("opt2", "option2", true, "Description 2");
        option2.setPrefixes(prefixes);

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 0, 0);

        assertEquals(prefixes, group.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 0, 0);

        assertEquals(Collections.unmodifiableSet(new HashSet(Arrays.asList("opt1", "opt2"))), group.getTriggers());
    }

    @Test
    public void testProcess() throws OptionException {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 0, 0);

        final CommandLine commandLine = new CommandLine();

        commandLine.parse(group.getOptions());
        group.process(commandLine, commandLine.args());

        final Option option1Processed = commandLine.getOption("opt1");
        final Option option2Processed = commandLine.getOption("opt2");

        assertEquals(true, option1Processed.getValue());
        assertEquals(true, option2Processed.getValue());
    }

    @Test
    public void testValidate() throws OptionException {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 1);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions());

        group.validate(commandLine);

        final Option option1Processed = commandLine.getOption("opt1");
        final Option option2Processed = commandLine.getOption("opt2");

        assertEquals(1, commandLine.getNumOptions());
        assertEquals(true, option1Processed.getValue());
        assertEquals(true, option2Processed.getValue());

        final Option option1Missing = commandLine.getOption("opt3");
        assertNull(option1Missing);
    }

    @Test
    public void testValidateWithTooManyOptions() throws OptionException {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 2);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions());

        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (final OptionException e) {
            assertEquals(ResourceConstants.TOO_MANY_OPTIONS, e.getMessage());
        }
    }

    @Test
    public void testValidateWithTooFewOptions() throws OptionException {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 2, 2);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions());

        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (final OptionException e) {
            assertEquals(ResourceConstants.TOO_FEW_OPTIONS, e.getMessage());
        }
    }

    @Test
    public void testGetMinimum() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 2);

        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 1);

        assertTrue(group.isRequired());
    }

    @Test
    public void testGetName() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, "groupName", null, 1, 1);

        assertEquals("groupName", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, "groupDescription", 1, 1);

        assertEquals("groupDescription", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 1);

        final StringBuilder buffer = new StringBuilder();
        final Map<String, Integer> helpSettings = new LinkedHashMap<>();

        group.appendUsage(buffer, helpSettings, null);

        final String expectedUsage = "usage: GroupImpl [-opt1] [--option1=VALUE] "
                + "[-opt2] [--option2=VALUE]\n";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testAppendUsageWithSeparators() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 1);

        final StringBuilder buffer = new StringBuilder();
        final Map<String, Integer> helpSettings = new LinkedHashMap<>();

        group.appendUsage(buffer, helpSettings, "|");

        final String expectedUsage = "usage: GroupImpl [--|\n"
                + "  [--opt1=VALUE]\n"
                + "  [--opt2=VALUE]\n"
                + "  |]\n";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 1);

        final List helpLines = group.helpLines(1, Collections.emptySet(), null);

        assertEquals(2, helpLines.size());

        final String expectedHelpLine1 = "  -opt1, --option1 VALUE\n"
                + "                   Description 1\n";
        final String expectedHelpLine2 = "  -opt2, --option2 VALUE\n"
                + "                   Description 2\n";
        assertTrue(helpLines.contains(expectedHelpLine1));
        assertTrue(helpLines.contains(expectedHelpLine2));
    }

    @Test
    public void testGetOptions() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        final Argument arg1 = new Argument("arg1", 1, "Description 1", false);
        final Argument arg2 = new Argument("arg2", 1, "Description 2", false);

        final List options = Arrays.asList(arg1, arg2);
        final Group group = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        group.addAllOptions(options);

        assertEquals(Arrays.asList(arg1, arg2), group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        final Option option1 = new Option("opt1", "option1", true, "Description 1");
        final Option option2 = new Option("opt2", "option2", true, "Description 2");

        final List options = Arrays.asList(option1, option2);
        final Group group = new GroupImpl(options, null, null, 1, 1);

        final Option optionFound = group.findOption("opt1");

        assertSame(option1, optionFound);
    }
}