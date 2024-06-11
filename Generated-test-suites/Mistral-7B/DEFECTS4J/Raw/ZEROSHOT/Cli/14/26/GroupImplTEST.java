package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImpl() {
        // Create test data
        final List<Option> optionsList = new ArrayList<>();
        optionsList.add(createOption1());
        optionsList.add(createOption2());
        optionsList.add(createOption3());
        optionsList.add(createArgument1());

        final Group group = new GroupImpl(optionsList, "TestGroup", "Test group", 1, 3);

        // Can process
        final WriteableCommandLine commandLine = new TestCommandLine();
        final String arg1 = "test";
        final String arg2 = "test1";
        final String arg3 = "test2";
        final String arg4 = "test3";

        // Test canProcess with valid arguments
        assertTrue(group.canProcess(commandLine, arg1));
        assertTrue(group.canProcess(commandLine, arg2));
        assertTrue(group.canProcess(commandLine, arg3));

        // Test canProcess with invalid arguments
        assertFalse(group.canProcess(commandLine, "invalid"));

        // Test canProcess with anonymous argument
        commandLine.addArgument(arg4);
        assertTrue(group.canProcess(commandLine, arg3));

        // Test getPrefixes
        assertEquals(Collections.singletonList("test"), group.getPrefixes());

        // Test getTriggers
        List<String> triggers = new ArrayList<>();
        triggers.add("test1");
        triggers.add("test2");
        triggers.add("test3");
        assertEquals(triggers, group.getTriggers());

        // Test process
        final List<String> arguments = new ArrayList<>();
        arguments.add(arg1);
        arguments.add(arg2);
        arguments.add(arg3);

        try {
            group.process(commandLine, arguments.listIterator());
            assertFalse(commandLine.hasOption("invalid"));
            assertFalse(commandLine.hasOption("test4"));
            assertTrue(commandLine.hasOption("test1"));
            assertTrue(commandLine.hasOption("test2"));
            assertTrue(commandLine.hasOption("test3"));
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }

        // Test validate
        commandLine.clear();
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }

        // Test getPreferredName
        assertEquals("TestGroup", group.getPreferredName());

        // Test getDescription
        assertEquals("Test group", group.getDescription());

        // Test appendUsage
        final StringBuilder builder = new StringBuilder();
        group.appendUsage(builder, Collections.emptySet(), null);
        assertEquals("TestGroup:\n" +
                "\tTest group\n" +
                "\t\t[test]\tTest option 1\n" +
                "\t\t[test1]\tTest option 2 (optional)\n" +
                "\t\t[test2]\tTest option 3 (optional)\n", builder.toString());

        // Test helpLines
        final List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);
        assertEquals(3, helpLines.size());
        assertEquals("TestGroup:\n" +
                "\tTest group\n" +
                "\t\t[test]\tTest option 1\n" +
                "\t\t[test1]\tTest option 2 (optional)\n" +
                "\t\t[test2]\tTest option 3 (optional)\n" +
                "\t\t\t[arg]\tTest argument 1\n", helpLines.get(0).getHelp());
        assertEquals("Test option 1:\n" +
                "\tTest option 1\n", helpLines.get(1).getHelp());
        assertEquals("Test option 2:\n" +
                "\tTest option 2 (optional)\n", helpLines.get(2).getHelp());

        // Test getOptions
        assertEquals(optionsList, group.getOptions());

        // Test getAnonymous
        assertEquals(Collections.singletonList(createArgument1()), group.getAnonymous());

        // Test findOption
        assertEquals(createOption1(), group.findOption("test"));
        assertNull(group.findOption("test4"));

        // Test getMinimum and isRequired
        assertEquals(1, group.getMinimum());
        assertTrue(group.isRequired());

        // Test getMaximum
        assertEquals(3, group.getMaximum());

        // Test defaults
        group.defaults(commandLine);
        assertFalse(commandLine.hasOption("test"));
        assertFalse(commandLine.hasOption("test1"));
        assertFalse(commandLine.hasOption("test2"));
        assertFalse(commandLine.hasOption("test3"));
        assertTrue(commandLine.hasOption("arg"));

        // Test findOption with argument
        assertEquals(createArgument1(), group.findOption("arg"));
    }

    private Option createOption1() {
        final Option option = new OptionImpl("test", "test", false, "t");
        option.setRequired(false);
        return option;
    }

    private Option createOption2() {
        final Option option = new OptionImpl("test1", "test1", false, "t1");
        option.setRequired(false);
        return option;
    }

    private Option createOption3() {
        final Option option = new OptionImpl("test2", "test2", false, "t2");
        option.setRequired(false);
        return option;
    }

    private Argument createArgument1() {
        final Argument argument = new ArgumentImpl("arg", "arg", false, "a");
        return argument;
    }

    private static class TestCommandLine extends WriteableCommandLine {
    }
}