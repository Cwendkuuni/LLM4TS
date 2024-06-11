package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final List options = new ArrayList();

        final Option optA = new Option("a", "option-a", false, "Description of option A");
        options.add(optA);

        final Option optB = new Option("b", "option-b", true, "Description of option B")
                .setArgs(1);
        options.add(optB);

        final Option optC = new Option("c", "option-c", true, "Description of option C")
                .setArgs(2);
        options.add(optC);

        final Option arg = new Option("arg", "argument", true, "Description of argument")
                .isRequired(false)
                .setArgName("ARGUMENT");
        options.add(arg);

        final Group group = new GroupImpl(options, "My Group", "This is my group", 1, 3, false);

        assertNotNull(group);
        assertEquals("My Group", group.getPreferredName());
        assertEquals("This is my group", group.getDescription());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertFalse(group.isRequired());

        // test canProcess method
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("--a");
        commandLine.addOption("--b valueB");
        commandLine.addOption("--c valueC valueC");
        commandLine.addOption("--arg argValue");

        assertTrue(group.canProcess(commandLine, "--a"));
        assertTrue(group.canProcess(commandLine, "--b"));
        assertTrue(group.canProcess(commandLine, "--c"));
        assertTrue(group.canProcess(commandLine, "--arg"));

        // test getPrefixes method
        assertEquals(Collections.emptySet(), group.getPrefixes());

        // test getTriggers method
        assertEquals(Collections.unmodifiableSet(Collections.singleton("a")), group.getTriggers());

        // test process method
        final ListIterator arguments = Collections.emptyList().listIterator();
        try {
            group.process(commandLine, arguments);
            fail();
        } catch (OptionException e) {
            // expected
        }

        commandLine.clear();
        commandLine.addOption("--a");

        try {
            group.process(commandLine, arguments);
            fail();
        } catch (OptionProcessingFailedException e) {
            // expected, as the group has a minimum of 1
        }

        commandLine.addOption("--b");

        try {
            group.process(commandLine, arguments);
            fail();
        } catch (OptionException e) {
            // expected, as option B requires an argument
            assertEquals("valueB", e.getMessage().split(":")[1]);
        }

        commandLine.addOption("--c valueC valueC2");

        try {
            group.process(commandLine, arguments);
            fail();
        } catch (OptionProcessingFailedException e) {
            // expected, as option C requires exactly 2 arguments
            assertEquals("valueC, valueC2", e.getMessage().split(":")[1]);
        }

        commandLine.addOption("--arg argValue");

        try {
            group.process(commandLine, arguments);
            fail();
        } catch (OptionProcessingFailedException e) {
            // expected, as the group has a maximum of 3
        }

        // test validate method
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail();
        }

        // test appendUsage method
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, null, null);
        assertEquals(
                "My Group:\n" +
                        "This is my group\n" +
                        "[option-a]\n" +
                        "option-a: Description of option A\n" +
                        "[option-b ARGUMENT]\n" +
                        "option-b: Description of option B\n" +
                        "[option-c ARGUMENT ARGUMENT]\n" +
                        "option-c: Description of option C\n" +
                        "[argument ARGUMENT]\n" +
                        "argument: Description of argument\n",
                sb.toString());

        // test helpLines method
        final List helpLines = group.helpLines(1, null, null);
        assertEquals(Collections.singletonList(
                new HelpLineImpl(group, 1, "My Group")), helpLines);

        final List helpLinesExpanded = group.helpLines(1, null, null, "|");
        final List expectedHelpLinesExpanded = new ArrayList();
        expectedHelpLinesExpanded.add(new HelpLineImpl(optA, 2, "option-a"));
        expectedHelpLinesExpanded.add(new HelpLineImpl(optB, 3, "option-b [ARGUMENT]"));
        expectedHelpLinesExpanded.add(new HelpLineImpl(optC, 5, "option-c [ARGUMENT] [ARGUMENT]"));
        expectedHelpLinesExpanded.add(new HelpLineImpl(arg, 3, "argument [ARGUMENT]"));
        assertEquals(expectedHelpLinesExpanded, helpLinesExpanded);
    }
}

class HelpLineImpl implements HelpLine {

    private final Option option;
    private final int depth;
    private final String name;

    HelpLineImpl(final Option option, final int depth, final String name) {
        this.option = option;
        this.depth = depth;
        this.name = name;
    }

    @Override
    public String getHelpText() {
        return option.getLongOpt() + " " + option.getDescription();
    }

    @Override
    public String getShortText() {
        return option.getOpt();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDepth() {
        return depth;
    }
}