package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        final List options = Arrays.asList(
                new Option("opt1", "Option 1"),
                new Option("opt2", "Option 2"),
                new Option("opt3", "Option 3", true),
                new Argument("arg1", "Argument 1", true),
                new Argument("arg2", "Argument 2", false),
                new Argument("arg3", "Argument 3", false, 3)
        );

        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        assertEquals("group", group.getName());
        assertEquals("", group.getDescription());
        assertEquals(options, group.getOptions());
        assertEquals(1, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertFalse(group.isRequired());

        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertEquals(Collections.emptySortedMap(Collections.emptyMap()), group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());
    }

    @Test
    public void testCanProcess() {
        final List options = Arrays.asList(
                new Option("opt1", "Option 1"),
                new Option("opt2", "Option 2"),
                new Option("opt3", "Option 3")
        );
        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // null arg
        assertFalse(group.canProcess(commandLine, null));

        // no arg
        assertFalse(group.canProcess(commandLine, ""));

        // normal option
        assertTrue(group.canProcess(commandLine, "opt1"));

        // option with triggers
        assertTrue(group.canProcess(commandLine, "opt2--long"));
        assertTrue(group.canProcess(commandLine, "opt2"));

        // option bursting
        assertTrue(group.canProcess(commandLine, "opt3--"));
        assertTrue(group.canProcess(commandLine, "opt3--two"));

        // anonymous argument
        assertTrue(group.canProcess(commandLine, "arg1"));
    }

    @Test
    public void testProcess() throws OptionException {
        final List options = Arrays.asList(
                new Option("opt1", "Option 1", true),
                new Option("opt2", "Option 2"),
                new Option("opt3", "Option 3")
        );
        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // no arguments
        group.process(commandLine, commandLine.listIterator());

        // option
        commandLine.parse("group opt1");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("opt1", commandLine.getOptionValue("opt1"));

        // option with value
        commandLine.parse("group opt1 value");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("value", commandLine.getOptionValue("opt1"));

        // option bursting
        commandLine.parse("group opt3--value1 value2 value3");
        group.process(commandLine, commandLine.listIterator());
        assertEquals(3, commandLine.getOptionValues("opt3").size());

        // argument
        commandLine.parse("group arg1 value");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("value", commandLine.getArgumentValue(0));
    }

    @Test
    public void testValidate() throws OptionException {
        final List options = Arrays.asList(
                new Option("opt1", "Option 1", true),
                new Option("opt2", "Option 2"),
                new Option("opt3", "Option 3")
        );
        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // no arguments
        group.validate(commandLine);

        // minimum required
        commandLine.parse("group opt1 opt1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("group: The option 'opt1' can only appear 1 time in the command line", e.getMessage());
        }

        // maximum required
        commandLine.parse("group opt1 opt1 opt1 opt1 opt1 opt1 opt1 opt1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("group: The option 'opt1' can appear a maximum of 1 time in the command line", e.getMessage());
        }

        // minimum and maximum not required
        commandLine.parse("group opt1 opt1 opt1");
        group.validate(commandLine);

        // argument validation
        commandLine.parse("group arg");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("group: The argument 'arg' is missing", e.getMessage());
        }

        commandLine.parse("group arg arg arg");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("group: The argument 'arg' can only appear 3 times in the command line", e.getMessage());
        }
    }

    @Test
    public void testAppendUsage() {
        final List options = Arrays.asList(
                new Option("opt1", "Option 1", true),
                new Option("opt2", "Option 2"),
                new Option("opt3", "Option 3")
        );
        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, null, null);

        assertEquals("group:\n" +
                "  group (optional)\n" +
                "    .\n" +
                "    --opt1 VALUE Optional: Option 1\n" +
                "    --opt2 VALUE Optional: Option 2\n" +
                "    --opt3 VALUE...\n", buffer.toString());

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);

        assertEquals("group:\n" +
                "group:\n" +
                "  --group (optional)\n", buffer.toString());

        group.appendUsage(buffer, null, ReverseStringComparator.getInstance());

        assertEquals("group:\n" +
                "  group:\n" +
                "    --group (optional)\n" +
                "  --opt3 VALUE...\n" +
                "  --opt2 VALUE Optional: Option 2\n" +
                "  --opt1 VALUE Optional: Option 1\n" +
                "  .\n", buffer.toString());

        group.appendUsage(buffer, null, null, "|");

        assertEquals("group:\n" +
                "  group:\n" +
                "    --group (optional)\n" +
                "  --opt3 VALUE...\n" +
                "  --opt2 VALUE Optional: Option 2\n" +
                "  --opt1 VALUE Optional: Option 1\n" +
                "  .\n" +
                "|", buffer.toString());

        group.appendUsage(buffer, null, null, "|");
        group.appendUsage(buffer, null, null, "|");

        assertEquals("group:\n" +
                "group:\n" +
                "  --group (optional)\n" +
                "    |--opt3 VALUE...\n" +
                "    |--opt3 VALUE...\n" +
                "  --opt2 VALUE Optional: Option 2\n" +
                "  --opt1 VALUE Optional: Option 1\n" +
                "  .\n" +
                "  |--\n" +
                "|", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List options = Arrays.asList(
                new Option("opt1", "Option 1", true),
                new Option("opt2", "Option 2"),
                new Option("opt3", "Option 3")
        );
        final Group group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE, false);

        final List helpLines = group.helpLines(0, null, null);

        assertEquals(2, helpLines.size());

        final HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("group:\n" +
                "group:\n" +
                "  --group (optional)\n" +
                "    .\n" +
                "    --opt3 VALUE...\n" +
                "    --opt3 VALUE...\n" +
                "  --opt2 VALUE Optional: Option 2\n" +
                "  --opt1 VALUE Optional: Option 1\n" +
                "  .\n" +
                "  |--\n" +
                "|", helpLine1.getHelpText());

        final HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("opt3:\n" +
                "  opt3:\n" +
                "    --opt3 VALUE...\n" +
                "    .\n" +
                "    --opt3 VALUE...\n", helpLine2.getHelpText());
    }
}