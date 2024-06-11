package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.GroupImpl;
import org.apache.commons.cli2.util.OptionImpl;
import org.apache.commons.cli2.util.ReverseStringComparator;

public class GroupImplTest {

    @Test
    public void testCreateGroup() {
        final List options = new ArrayList<>();
        options.add(createOption("opt1", false, null, null));
        options.add(createOption("opt2", true, null, null));

        final Group group = new GroupImpl(options, "group", null, 1, 1);

        assertEquals("group", group.getPreferredName());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.unmodifiableSet(Collections.emptySet()), group.getTriggers());
        assertFalse(group.isRequired());
        assertEquals(Collections.unmodifiableList(Collections.emptyList()), group.getAnonymous());
    }

    @Test
    public void testCanProcess() {
        final List options = new ArrayList<>();
        options.add(createOption("opt1", false, null, null));
        options.add(createOption("opt2", true, null, null));

        final Group group = new GroupImpl(options, "group", null, 1, 1);

        // empty command line
        assertFalse(group.canProcess(null, null));

        // no argument
        assertFalse(group.canProcess(null, ""));

        // argument for option opt1
        assertTrue(group.canProcess(null, "opt1"));

        // argument for option opt2
        assertTrue(group.canProcess(null, "opt2"));

        // argument for non-existent option
        assertFalse(group.canProcess(null, "non-existent"));
    }

    @Test
    public void testProcess() throws OptionException {
        final List options = new ArrayList<>();
        options.add(createOption("opt1", false, null, null));
        options.add(createOption("opt2", true, null, null));

        final Group group = new GroupImpl(options, "group", null, 1, 1);

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // empty command line
        assertFalse(group.canProcess(commandLine, null));

        // no argument
        assertFalse(group.canProcess(commandLine, ""));

        // argument for option opt1
        commandLine.parseArgument("opt1");
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(Collections.singletonList("opt1"), commandLine.getOptions().values());

        // argument for option opt2
        commandLine.parseArgument("opt2");
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(Collections.singletonList("opt2"), commandLine.getOptions().values());

        // argument for non-existent option
        commandLine.parseArgument("non-existent");
        assertFalse(group.canProcess(commandLine, "non-existent"));
        assertEquals(Collections.emptyList(), commandLine.getOptions().values());
    }

    @Test
    public void testValidate() throws OptionException {
        final List options = new ArrayList<>();
        options.add(createOption("opt1", false, null, null));
        options.add(createOption("opt2", true, null, null));

        final Group group = new GroupImpl(options, "group", null, 1, 1);

        // empty command line
        assertFalse(group.canProcess(null, null));
        assertFalse(group.validate(null));
        assertFalse(group.validate(new WriteableCommandLine()));

        // command line with one opt1
        commandLineWithOneOption("opt1", group);
        assertTrue(group.validate(commandLine));

        // command line with one opt2
        commandLineWithOneOption("opt2", group);
        assertTrue(group.validate(commandLine));

        // command line with one non-existent option
        commandLineWithOneOption("non-existent", group);
        assertFalse(group.validate(commandLine));

        // command line with two opt1
        commandLineWithTwoOptions("opt1", group);
        assertFalse(group.validate(commandLine));

        // command line with two opt2
        commandLineWithTwoOptions("opt2", group);
        assertFalse(group.validate(commandLine));

        // command line with one opt1 and one opt2
        commandLineWithOneOption("opt1", group);
        commandLineWithOneOption("opt2", group);
        assertTrue(group.validate(commandLine));
    }

    @Test
    public void testAppendUsage() {
        final List options = new ArrayList<>();
        options.add(createOption("opt1", false, null, null));
        options.add(createOption("opt2", true, null, null));

        final Group group = new GroupImpl(options, "group", null, 1, 1);

        final StringBuilder buffer = new StringBuilder();

        // empty usage
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("", buffer.toString());

        // group usage with name
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);
        assertEquals("group", buffer.toString());

        // group usage with name and options
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, "--");
        assertEquals("group (", buffer.toString());

        // group usage with name and options sorted
        final Comparator comparator = new ReverseStringComparator();
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), comparator);
        assertEquals("group (", buffer.toString());
        assertEquals("opt2 --opt1", buffer.toString() + "--opt1 --opt2", buffer.toString());

        // group usage with name, options, and arguments
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, "--");
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null);
        assertEquals("group (", buffer.toString());
        assertEquals("opt2 --opt1 arg1 arg2", buffer.toString() + "--opt1 arg1 arg2 --opt2", buffer.toString());

        // group usage with name, options, arguments, and indentation
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, "--\n");
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null, "\n");
        assertEquals("group (\n", buffer.toString());
        assertEquals("opt2 --opt1 arg1 arg2\n", buffer.toString() + "opt2 --opt1 arg1 arg2\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List options = new ArrayList<>();
        options.add(createOption("opt1", false, null, null));
        options.add(createOption("opt2", true, null, null));

        final Group group = new GroupImpl(options, "group", null, 1, 1);

        final List helpLines = group.helpLines(0, null, null);
        assertEquals(Collections.emptyList(), helpLines);

        final Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(Collections.singletonList(new HelpLineImpl(group, 0)), helpLines);

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(Collections.emptyList(), helpLines);

        final Comparator comparator = new ReverseStringComparator();
        helpLines = group.helpLines(0, helpSettings, comparator);
        assertEquals(Collections.singletonList(new HelpLineImpl(group, 0)), helpLines);

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        helpLines = group.helpLines(0, helpSettings, comparator);
        assertEquals(2, helpLines.size());
        assertEquals("opt1", helpLines.get(0).getOptionSpec().getLongOpt());
        assertEquals("opt2", helpLines.get(1).getOptionSpec().getLongOpt());
    }

    private Option createOption(final String longName,
                               final boolean hasArg,
                               final String argName,
                               final String description) {
        return new OptionImpl(longName, hasArg, null, null, null, null, null, description);
    }

    private void commandLineWithOneOption(final String option, final Group group) {
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseOption(new Option("option", option, false, null, null, null, null, null));
        commandLine.parseArgument(null);
    }

    private void commandLineWithTwoOptions(final String option1, final Group group) {
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseOption(new Option("option", option1, false, null, null, null, null, null));
        commandLine.parseOption(new Option("option", option1, false, null, null, null, null, null));
        commandLine.parseArgument(null);
    }

    static class HelpLineImpl implements HelpLine {
        private final Group group;
        private final int indentation;

        public HelpLineImpl(final Group group, final int indentation) {
            this.group = group;
            this.indentation = indentation;
        }

        @Override
        public String getLine() {
            return "";
        }

        @Override
        public String getOpt() {
            return "";
        }

        @Override
        public String getName() {
            return group.getPreferredName();
        }

        @Override
        public String getArgument() {
            return "";
        }

        @Override
        public String getDescription() {
            return group.getDescription();
        }

        @Override
        public String getOptDescription() {
            return "";
        }

        @Override
        public String getLongOpt() {
            return group.getPreferredName();
        }

        @Override
        public int getIndentation() {
            return indentation;
        }
    }
}