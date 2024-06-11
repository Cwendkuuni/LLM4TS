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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.Arguments;
import org.apache.commons.cli2.util.Options;

import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private GroupImpl group;
    private WriteableCommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        final List list = new ArrayList();

        final Argument arg1 = new Argument("a", "an argument", false);
        final Argument arg2 = new Argument("b", "another argument", true);
        final Argument arg3 = new Argument("c", "a third argument", false);

        list.add(arg1);
        list.add(arg2);
        list.add(arg3);

        final Option opt1 = new Option("o1", "Option 1", false);
        final Option opt2 = new Option("o2", "Option 2", true)
                                        .required(true)
                                        .longOpt("longOpt1");

        final List optionsList = new ArrayList();
        optionsList.add(opt1);
        optionsList.add(opt2);

        this.group = new GroupImpl(optionsList, "Group", "Test Group", 0, 1) {
            @Override
            public void validate(WriteableCommandLine commandLine)
                    throws OptionException {
                super.validate(commandLine);
            }

            @Override
            public void process(WriteableCommandLine commandLine,
                                ListIterator<String> arguments)
                    throws OptionException {
                super.process(commandLine, arguments);
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      String arg) {
                return super.canProcess(commandLine, arg);
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                super.defaults(commandLine);
            }
        };

        this.commandLine = new WriteableCommandLine("test", false);
        this.options = new Options();
        this.options.addAll(group.getOptions());
    }

    @Test
    public void testGroupCanProcess() {
        // Test canProcess when no arguments are passed
        assertFalse(group.canProcess(commandLine, null));

        // Test canProcess with a valid argument
        assertTrue(group.canProcess(commandLine, "o1"));

        // Test canProcess with an invalid argument
        assertFalse(group.canProcess(commandLine, "invalid"));

        // Test canProcess with an invalid argument that matches an option trigger
        assertFalse(group.canProcess(commandLine, "o2"));

        // Test canProcess with an argument that matches an option trigger and can be processed
        assertTrue(group.canProcess(commandLine, "longOpt1"));

        // Test canProcess with an argument that matches an anonymous argument
        assertTrue(group.canProcess(commandLine, "a"));

        // Test canProcess with an argument that matches an anonymous argument
        assertTrue(group.canProcess(commandLine, "b"));

        // Test canProcess with an argument that matches an anonymous argument and has a value
        assertFalse(group.canProcess(commandLine, "c"));

        // Test canProcess with an argument that requires bursting
        assertTrue(group.canProcess(commandLine, "o2"));
    }

    @Test
    public void testGroupProcess() {
        // Test process when no arguments are passed
        group.process(commandLine, null);
        assertEquals("", commandLine.getArgument(0));

        // Test process with a valid argument
        group.process(commandLine, "o1");
        assertNull(commandLine.getArgument(0));

        // Test process with an invalid argument that doesn't match any option trigger
        group.process(commandLine, "invalid");
        assertEquals("invalid", commandLine.getArgument(0));

        // Test process with an argument that matches an option trigger but doesn't have any value
        group.process(commandLine, "o2");
        assertNull(commandLine.getArgument(0));

        // Test process with an argument that matches an option trigger and has a value
        group.process(commandLine, "longOpt1=value");
        assertEquals("value", commandLine.getArgument(0));

        // Test process with an argument that matches an anonymous argument
        group.process(commandLine, "a");
        assertEquals(arg1.getValue(), commandLine.getArgument(0));

        // Test process with an argument that matches an anonymous argument and has a value
        group.process(commandLine, "a=value");
        assertEquals("value", commandLine.getArgument(0));

        // Test process with an argument that matches an anonymous argument and requires bursting
        group.process(commandLine, "b");
        assertNull(commandLine.getArgument(0));
        assertEquals(arg2.getValue(), commandLine.getArgument(1));

        // Test process with multiple arguments that match anonymous arguments
        group.process(commandLine, "a=value1");
        group.process(commandLine, "a=value2");
        assertEquals("value2", commandLine.getArgument(1));
    }

    @Test
    public void testGroupValidate() {
        // Test validate with no arguments
        group.validate(commandLine);

        // Test validate with the required number of arguments
        group.validate(commandLine);

        // Test validate with fewer arguments than required
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test validate with more arguments than allowed
        assertThrows(OptionException.class, () -> group.validate(commandLine, "extra"));

        // Test validate with arguments that match anonymous arguments
        group.validate(commandLine, "a=value1");
        group.validate(commandLine, "a=value2");
        group.validate(commandLine, "b=value1");

        // Test validate with an argument that matches an option trigger
        group.validate(commandLine, "o1");

        // Test validate with an argument that matches an option trigger and has a value
        group.validate(commandLine, "longOpt1=value");

        // Test validate with an argument that doesn't match any option or anonymous argument
        assertThrows(OptionException.class, () -> group.validate(commandLine, "invalid"));
    }

    @Test
    public void testGroupFindOption() {
        assertNull(group.findOption("invalid"));
        assertSame(opt1, group.findOption("o1"));
        assertSame(opt2, group.findOption("longOpt1"));
    }

    @Test
    public void testGroupGetMinimumAndMaximum() {
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testGroupIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testGroupHelpLines() {
        final Map<String, HelpLine> helpLines = new LinkedHashMap<>();

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null, "");
        helpLines.put("", group.helpLines(0, Collections.emptySet(), null).get(0));

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null, "|");
        helpLines.put("|", group.helpLines(1, Collections.emptySet(), null).get(0));

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null,
                          "| Option 1\n" +
                          "|   Option 1\n" +
                          "|   Option 1 (longOpt1)\n" +
                          "| Option 2\n" +
                          "|   Option 2\n" +
                          "|   Option 2 (longOpt1)\n" +
                          "| an argument\n" +
                          "| another argument\n" +
                          "| a third argument\n");
        helpLines.put("Option 1", group.helpLines(2, Collections.emptySet(), null).get(0));
        helpLines.put("Option 1 (longOpt1)",
                      group.helpLines(3, Collections.emptySet(), null).get(0));
        helpLines.put("Option 2", group.helpLines(4, Collections.emptySet(), null).get(0));
        helpLines.put("Option 2 (longOpt1)",
                      group.helpLines(5, Collections.emptySet(), null).get(0));
        helpLines.put("an argument", group.helpLines(6, Collections.emptySet(), null).get(0));

        final Set<String> expected = new TreeSet<>();
        expected.add("|");
        expected.add("Option 1");
        expected.add("Option 1 (longOpt1)");
        expected.add("Option 2");
        expected.add("Option 2 (longOpt1)");
        expected.add("an argument");

        assertEquals(expected, new TreeSet<>(helpLines.keySet()));

        for (final Map.Entry<String, HelpLine> entry : helpLines.entrySet()) {
            assertEquals(entry.getValue().getLines().get(0), entry.getKey());
        }
    }

    @Test
    public void testGroupUsage() {
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null, "");
        assertEquals("", buffer.toString());

        group.appendUsage(buffer, Collections.emptySet(), null, "|");
        assertEquals("|", buffer.toString());

        group.appendUsage(buffer, Collections.emptySet(), null,
                          "| Option 1\n" +
                          "|   Option 1\n" +
                          "|   Option 1 (longOpt1)\n" +
                          "| Option 2\n" +
                          "|   Option 2\n" +
                          "|   Option 2 (longOpt1)\n" +
                          "| an argument\n" +
                          "| another argument\n" +
                          "| a third argument\n");
        assertEquals(
                "| Option 1\n" +
                "|   Option 1\n" +
                "|   Option 1 (longOpt1)\n" +
                "| Option 2\n" +
                "|   Option 2\n" +
                "|   Option 2 (longOpt1)\n" +
                "| an argument\n" +
                "| another argument\n" +
                "| a third argument\n",
                buffer.toString());
    }
}