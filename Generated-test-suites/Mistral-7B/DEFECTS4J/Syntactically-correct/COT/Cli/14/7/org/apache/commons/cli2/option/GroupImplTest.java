package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Test;

/**
 * Test case for the GroupImpl class.
 */
public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Option("opt2", "Option 2"));
        options.add(new Argument("arg1", "Argument 1", true));
        options.add(new Argument("arg2", "Argument 2"));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        assertEquals("group", group.getName());
        assertEquals("Group", group.getDescription());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.unmodifiableList(Collections.singletonList("opt1")), group.getTriggers());
        assertEquals(Collections.unmodifiableSet(Collections.singleton("t")), group.getPrefixes());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());

        assertSame(options.get(0), group.findOption("opt1"));
        assertNull(group.findOption("opt3"));
    }

    @Test
    public void testCanProcess_WithOption() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("t");

        assertTrue(group.canProcess(commandLine, "opt1"));
        assertFalse(group.canProcess(commandLine, "opt2"));
    }

    @Test
    public void testCanProcess_WithArgument() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Argument("arg1", "Argument 1", true));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addStringArgument("arg1");

        assertTrue(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "opt1"));
    }

    @Test
    public void testProcess_WithValidArgument() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Argument("arg1", "Argument 1", true));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addStringArgument("arg1");

        group.process(commandLine, commandLine.listIterator());

        assertTrue(commandLine.hasOption("opt1"));
        assertFalse(commandLine.hasOption("opt2"));
    }

    @Test
    public void testProcess_WithInvalidArgument() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Argument("arg1", "Argument 1", true));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.listIterator()));
    }

    @Test
    public void testValidate_WithValidOptions() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Option("opt2", "Option 2"));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("t");
        commandLine.addOption("opt2");

        group.validate(commandLine);
    }

    @Test
    public void testValidate_WithInvalidOptions() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Option("opt2", "Option 2"));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("t");
        commandLine.addOption("opt1");

        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testValidate_WithMissingOptions() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Option("opt2", "Option 2"));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testAppendUsage() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Argument("arg1", "Argument 1", true));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final StringBuilder expected = new StringBuilder();

        expected.append("group (");
        expected.append("OPTIONS:");
        expected.append(System.lineSeparator());
        expected.append("  opt1, --opt1                       Option 1 (t)");
        expected.append(System.lineSeparator());
        expected.append("ARGUMENTS:");
        expected.append(System.lineSeparator());
        expected.append("  arg1                               Argument 1");
        expected.append(System.lineSeparator());
        expected.append(")");

        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals(expected.toString(), buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "t"));
        options.add(new Argument("arg1", "Argument 1", true));

        final GroupImpl group = new GroupImpl(options, "group", "Group", 1, 3);

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(4, helpLines.size());

        final HelpLine helpLine = (HelpLine) helpLines.get(0);
        assertEquals("group (OPTIONS:, ARGUMENTS:)", helpLine.getLine());

        final HelpLine opt1HelpLine = (HelpLine) helpLines.get(1);
        assertEquals("  opt1, --opt1                       Option 1 (t)", opt1HelpLine.getLine());

        final HelpLine arg1HelpLine = (HelpLine) helpLines.get(2);
        assertEquals("    arg1                               Argument 1", arg1HelpLine.getLine());

        final HelpLine emptyHelpLine = (HelpLine) helpLines.get(3);
        assertNull(emptyHelpLine.getLine());
    }
}