package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
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
import org.apache.commons.cli2.util.ReverseStringComparator;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for GroupImpl class
 */
public class GroupImplTest {
    private GroupImpl group;

    @Before
    public void setUp() {
        final List options = new ArrayList();
        options.add(new Option("a", true, true, "an option"));
        options.add(new Option("b", 3, "b", "an option with number"));
        options.add(new Option("c", true, false, "an optional option"));
        options.add(new Option("d", 5, "d", "an optional option with number"));
        options.add(new Option("e", false, false, "an optional hidden option"));
        options.add(new Argument("f", "Foo argument"));
        group = new GroupImpl(options, null, null, 0, Integer.MAX_VALUE, false);
    }

    @Test
    public void testCanProcess_ValidOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        assertFalse(group.canProcess(commandLine, "a"));
        assertFalse(group.canProcess(commandLine, "b"));
        assertFalse(group.canProcess(commandLine, "c"));
        assertFalse(group.canProcess(commandLine, "d"));
        assertFalse(group.canProcess(commandLine, "e"));

        commandLine.parse("-a -b -c -d -e");
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));
        assertTrue(group.canProcess(commandLine, "d"));
        assertFalse(group.canProcess(commandLine, "e"));
        assertFalse(group.canProcess(commandLine, "f"));
    }

    @Test
    public void testCanProcess_InvalidOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test invalid option without bursting
        assertFalse(group.canProcess(commandLine, "x"));

        // Test invalid option with bursting
        for (final Option option : group.getOptions()) {
            if (option instanceof Option) {
                final Option testOption = (Option) option;
                commandLine.parse("-" + testOption.getName() + testOption.getValues().get(0));
            }
        }
        assertFalse(group.canProcess(commandLine, "x"));

        // Test invalid option with anonymous argument
        commandLine.parse("--f Foo");
        assertTrue(group.canProcess(commandLine, "f"));
        assertFalse(group.canProcess(commandLine, "x"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new HashSet<>(group.getTriggers());
        List<String> triggerList = new ArrayList<>(triggers);
        triggerList.sort(String::compareToIgnoreCase);
        List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(expectedList, triggerList);
    }

    @Test
    public void testProcess_ValidOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        // Test processing valid options
        group.process(commandLine, arguments);
        assertEquals(3, commandLine.getOptions().size());
        assertEquals(1, commandLine.getArgList().size());

        // Test processing valid options with bursting
        arguments = commandLine.getArguments().listIterator();
        arguments.next();
        group.process(commandLine, arguments);
        assertEquals(3, commandLine.getOptions().size());
        assertEquals(2, commandLine.getArgList().size());
    }

    @Test
    public void testProcess_InvalidOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        // Test processing invalid option without bursting
        assertThrows(OptionException.class, () -> {
            group.process(commandLine, arguments);
            arguments.next();
        });

        // Test processing invalid option with bursting
        commandLine.parse("-a");
        assertThrows(OptionException.class, () -> {
            group.process(commandLine, arguments);
            arguments.next();
        });

        // Test processing invalid option with anonymous argument
        commandLine.parse("--f Foo");
        group.process(commandLine, arguments);
        assertEquals(0, commandLine.getOptions().size());
        assertEquals(1, commandLine.getArgList().size());
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test with valid options
        group.validate(commandLine);

        // Test with missing options
        commandLine.clear();
        commandLine.parse("--c");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with too many options
        commandLine.clear();
        commandLine.parse("--a --a --a");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with missing anonymous argument
        commandLine.clear();
        commandLine.parse("-d");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with too many anonymous arguments
        commandLine.clear();
        commandLine.parse("--f Foo --f Bar");
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testGetPreferredName() {
        String name = group.getPreferredName();
        assertNull(name);
    }

    @Test
    public void testGetDescription() {
        String description = group.getDescription();
        assertNull(description);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        String expectedUsage = "|--a\n" +
                "|--b NUMBER\n" +
                "|--c\n" +
                "|--d NUMBER\n" +
                "|--e\n" +
                "|--f ARG\n" +
                "|";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<Option> comparator = null;
        List<HelpLine> helpLines = group.helpLines(depth, helpSettings, comparator);
        List<String> expectedLines = new ArrayList<>();
        expectedLines.add("|--a\n" +
                "|  is an option\n" +
                "|  This option takes no value.\n" +
                "|");
        expectedLines.add("|--b <NUMBER>\n" +
                "|  is an option with number\n" +
                "|  This option takes a value of type NUMBER.\n" +
                "|");
        expectedLines.add("|--c\n" +
                "|  is an optional option\n" +
                "|  This option takes no value.\n" +
                "|");
        expectedLines.add("|--d <NUMBER>\n" +
                "|  is an optional option with number\n" +
                "|  This option takes a value of type NUMBER.\n" +
                "|");
        expectedLines.add("|--e\n" +
                "|  is an optional hidden option\n" +
                "|  This option takes no value.\n" +
                "|");
        expectedLines.add("|--f <ARG>\n" +
                "|  is an argument\n" +
                "|  This argument takes a value of type ARG.\n" +
                "|");
        assertEquals(expectedLines, helpLines);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(5, options.size());
    }

    @Test
    public void testGetAnonymous() {
        List<Argument> anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
    }

    @Test
    public void testFindOption() {
        Option optionA = group.findOption("a");
        assertNotNull(optionA);
        Option optionB = group.findOption("b");
        assertNotNull(optionB);
        Option optionC = group.findOption("c");
        assertNotNull(optionC);
        Option optionD = group.findOption("d");
        assertNotNull(optionD);
        Option optionE = group.findOption("e");
        assertNotNull(optionE);
        Option optionF = group.findOption("f");
        assertNotNull(optionF);
        Option optionX = group.findOption("x");
        assertNull(optionX);
    }

    @Test
    public void testGetMinimum() {
        int minimum = group.getMinimum();
        assertEquals(0, minimum);
    }

    @Test
    public void testGetMaximum() {
        int maximum = group.getMaximum();
        assertEquals(Integer.MAX_VALUE, maximum);
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        group.defaults(commandLine);

        for (final Option option : group.getOptions()) {
            if (option instanceof Option) {
                final Option testOption = (Option) option;
                testOption.defaults(commandLine);
            }
        }

        for (final Argument argument : group.getAnonymous()) {
            argument.defaults(commandLine);
        }

        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.emptyList(), commandLine.getArgList());
    }
}