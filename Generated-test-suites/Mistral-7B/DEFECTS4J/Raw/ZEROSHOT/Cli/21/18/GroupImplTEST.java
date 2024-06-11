package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImpl() throws OptionException {
        // Test case for a simple group
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("a", "arg1", false, "Argument 1");
        Option option2 = new Option("b", "arg2", true, "Argument 2");
        Option option3 = new Option("c", "arg3", false, "Argument 3");
        options.add(option1);
        options.add(option2);
        options.add(option3);

        Group group = new GroupImpl(options, "Group", "Group description", 1, 3, true);

        // Test canProcess method
        WriteableCommandLine commandLine = new WriteableCommandLine();
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));
        assertFalse(group.canProcess(commandLine, "d"));

        // Test process method
        commandLine.parse("--arg1 --arg2 --arg3 --arg1");
        List<String> arguments = commandLine.getArguments();
        assertEquals(2, arguments.size());
        assertEquals("arg1", arguments.get(0));
        assertEquals("arg1", arguments.get(1));

        // Test validate method
        commandLine.clear();
        group.validate(commandLine);

        // Test getPreferredName method
        assertEquals("Group", group.getPreferredName());

        // Test appendUsage method
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        String usage = sb.toString();
        assertEquals("Group description\n" +
                "[--arg1 ARG1]\n" +
                "[--arg2 ARG2]\n" +
                "[--arg3 ARG3]\n", usage);

        // Test helpLines method
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(3, helpLines.size());
        HelpLine helpLine1 = helpLines.get(0);
        assertEquals("Group description\n" +
                "[--arg1 ARG1]\n", helpLine1.getHelp());
        HelpLine helpLine2 = helpLines.get(1);
        assertEquals("--arg2 ARG2\n" +
                "Optional: true\n", helpLine2.getHelp());
        HelpLine helpLine3 = helpLines.get(2);
        assertEquals("--arg3 ARG3\n" +
                "Optional: true\n", helpLine3.getHelp());

        // Test getOptions method
        List<Option> groupOptions = group.getOptions();
        assertEquals(3, groupOptions.size());
        assertEquals(option1, groupOptions.get(0));
        assertEquals(option2, groupOptions.get(1));
        assertEquals(option3, groupOptions.get(2));

        // Test findOption method
        Option foundOption = group.findOption("a");
        assertEquals(option1, foundOption);

        // Test getMinimum and getMaximum methods
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());

        // Test isRequired method
        assertTrue(group.isRequired());
    }

    @org.junit.Test
    public void testGroupImplWithArguments() throws OptionException {
        // Test case for a group with arguments
        List<Option> options = new ArrayList<>();
        Option groupOption = new Option("g", "group", false, "Group");
        options.add(groupOption);
        Argument arg1 = new Argument("arg1", true, "Argument 1");
        Argument arg2 = new Argument("arg2", true, "Argument 2");
        groupOption.setValue(new ArrayList<>(Arrays.asList(arg1, arg2)));

        Group group = new GroupImpl(options, "Group", "Group description", 1, 1, false);

        // Test canProcess method
        WriteableCommandLine commandLine = new WriteableCommandLine();
        assertTrue(group.canProcess(commandLine, "g"));
        assertTrue(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "arg2"));

        // Test process method
        commandLine.parse("--group arg1 arg2");
        List<String> arguments = commandLine.getArguments();
        assertEquals(2, arguments.size());
        assertEquals("arg1", arguments.get(0));
        assertEquals("arg2", arguments.get(1));

        // Test validate method
        commandLine.clear();
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            assertEquals("Group: Missing required option 'arg1' or 'arg2'", e.getMessage());
            throw e;
        }

        // Test getPreferredName method
        assertEquals("Group", group.getPreferredName());

        // Test appendUsage method
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        String usage = sb.toString();
        assertEquals("Group description\n" +
                "[--group]\n" +
                "Group: Arguments may be repeated:\n" +
                "  arg1 ARG1 (Argument 1)\n" +
                "  arg2 ARG2 (Argument 2)\n", usage);

        // Test helpLines method
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(4, helpLines.size());
        HelpLine helpLine1 = helpLines.get(0);
        assertEquals("Group description\n" +
                "[--group]\n" +
                "Group: Arguments may be repeated:\n", helpLine1.getHelp());
        HelpLine helpLine2 = helpLines.get(1);
        assertEquals("--group\n" +
                "Optional: false\n" +
                "Group: Arguments may be repeated:\n", helpLine2.getHelp());
        HelpLine helpLine3 = helpLines.get(2);
        assertEquals("arg1 ARG1 (Argument 1)\n" +
                "Optional: true\n", helpLine3.getHelp());
        HelpLine helpLine4 = helpLines.get(3);
        assertEquals("arg2 ARG2 (Argument 2)\n" +
                "Optional: true\n", helpLine4.getHelp());

        // Test getOptions method
        List<Option> groupOptions = group.getOptions();
        assertEquals(1, groupOptions.size());
        assertEquals(groupOption, groupOptions.get(0));

        // Test findOption method
        Option foundOption = group.findOption("g");
        assertEquals(groupOption, foundOption);

        // Test getMinimum and getMaximum methods
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());

        // Test isRequired method
        assertFalse(group.isRequired());
    }
}