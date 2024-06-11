package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.GroupImpl;
import org.apache.commons.cli2.OptionImpl;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ArgumentBuilder;
import org.apache.commons.cli2.util.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        commandLine = new Arguments();

        Option option1 = OptionBuilder
                .withLongOpt("option1")
                .withDescription("Option 1")
                .withArgName("arg1")
                .isRequired()
                .create(false);
        options.add(option1);

        Option option2 = OptionBuilder
                .withLongOpt("option2")
                .withDescription("Option 2")
                .withArgName("arg2")
                .withMinValues(2)
                .withMaxValues(3)
                .create(false);
        options.add(option2);

        Argument arg1 = ArgumentBuilder
                .withLongOpt("argument1")
                .withDescription("Argument 1")
                .isRequired()
                .create(true);
        options.add(arg1);

        Argument arg2 = ArgumentBuilder
                .withLongOpt("argument2")
                .withDescription("Argument 2")
                .isOptional()
                .create(true);
        options.add(arg2);

        group = new GroupImpl(options, "Group", "Description", 1, 3);
    }

    @Test
    public void testCanProcess() {
        // Test with valid options
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));

        // Test with invalid options
        assertFalse(group.canProcess(commandLine, "invalidOption"));

        // Test with anonymously provided arguments
        commandLine.parse("--option1 arg1 --option2 arg1 arg2");
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option2"));

        assertFalse(group.canProcess(commandLine, "invalidOption"));

        // Test with an argument that matches an option trigger
        commandLine.parse("--option2 arg1 arg2 arg3");
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option2"));

        assertFalse(group.canProcess(commandLine, "invalidOption"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        List<String> triggerList = new ArrayList<>();
        triggerList.addAll(Arrays.asList(option1.getLongOpt(), option2.getLongOpt()));
        Collections.sort(triggerList);
        assertEquals(triggerList, triggers);
    }

    @Test
    public void testProcess() throws OptionException {
        // Test with valid options
        commandLine.parse("--option1 arg1 --option2 arg1 arg2");
        group.process(commandLine, commandLine.argumentIterator());

        assertEquals("arg1", commandLine.getValue("option1"));
        assertEquals("arg1", commandLine.getValue("option2"));
        assertEquals("arg2", commandLine.getValue("option2"));
        assertEquals("arg1", commandLine.getValue("option2"));

        // Test with an argument that matches an option trigger
        commandLine.parse("--option2 arg1 arg2 arg3");
        group.process(commandLine, commandLine.argumentIterator());

        assertEquals("arg1", commandLine.getValue("option2"));
        assertEquals("arg2", commandLine.getValue("option2"));
        assertEquals("arg3", commandLine.getValue("option2"));
    }

    @Test
    public void testValidate() throws OptionException {
        // Test with valid options
        commandLine.parse("--option1 arg1 --option2 arg1 arg2");
        group.validate(commandLine);

        // Test with an argument that matches an option trigger
        commandLine.parse("--option2 arg1 arg2 arg3");
        group.validate(commandLine);

        // Test with invalid options
        commandLine.parse("--option1 arg1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        commandLine.parse("--option2 arg1 arg2");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("Group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Description", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Map<DisplaySetting, Boolean> helpSettings = new HashMap<>();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_NAME, true);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_EXPANDED, true);

        group.appendUsage(buffer, helpSettings, null);

        String expected = "--GROUP\n" +
                "Description:\n" +
                "  Group\n" +
                "\n" +
                "--option1 <arg1>\n" +
                "Option 1\n" +
                "\n" +
                "--option2 <arg1> <arg2>\n" +
                "Option 2\n" +
                "\n";

        assertEquals(expected, buffer.toString());

        helpSettings.put(DisplaySetting.DISPLAY_GROUP_ARGUMENT, true);

        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);

        expected = "--GROUP\n" +
                "Description:\n" +
                "  Group\n" +
                "\n" +
                "--option1 <arg1>\n" +
                "Option 1\n" +
                "\n" +
                "--option2 <arg1>\n" +
                "Option 2\n" +
                "\n" +
                "  Argument 1:\n" +
                "\n" +
                "--argument1\n" +
                "Argument 1\n" +
                "\n" +
                "--option2 <arg1> [<arg2>]\n" +
                "Option 2\n" +
                "\n" +
                "  Argument 2:\n" +
                "";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        StringBuffer buffer = new StringBuffer();
        Map<DisplaySetting, Boolean> helpSettings = new HashMap<>();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_NAME, true);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_EXPANDED, true);

        List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);

        assertEquals(1, helpLines.size());

        HelpLine helpLine = helpLines.get(0);

        assertEquals("--GROUP", helpLine.getOpt());
        assertEquals("Description", helpLine.getLongOpt());
        assertEquals("Group", helpLine.getName());
        assertEquals("Description", helpLine.getDescription());

        List<HelpLine> subHelpLines = helpLine.getSubHelpLines();

        assertEquals(2, subHelpLines.size());

        HelpLine option1HelpLine = subHelpLines.get(0);

        assertEquals("--option1 <arg1>", option1HelpLine.getOpt());
        assertEquals("Option 1", option1HelpLine.getName());
        assertEquals("Option 1", option1HelpLine.getShortOpt());
        assertEquals("Option 1", option1HelpLine.getDescription());

        HelpLine option2HelpLine = subHelpLines.get(1);

        assertEquals("--option2 <arg1> <arg2>", option2HelpLine.getOpt());
        assertEquals("Option 2", option2HelpLine.getName());
        assertEquals("option2", option2HelpLine.getShortOpt());
        assertEquals("Option 2", option2HelpLine.getDescription());

        helpSettings.put(DisplaySetting.DISPLAY_GROUP_ARGUMENT, true);

        helpLines = group.helpLines(0, helpSettings, null);

        assertEquals(3, helpLines.size());

        helpLine = helpLines.get(0);

        subHelpLines = helpLine.getSubHelpLines();

        assertEquals(3, subHelpLines.size());

        option1HelpLine = subHelpLines.get(0);

        assertEquals("--option1 <arg1>", option1HelpLine.getOpt());
        assertEquals("Option 1", option1HelpLine.getName());
        assertEquals("option1", option1HelpLine.getShortOpt());
        assertEquals("Option 1", option1HelpLine.getDescription());

        HelpLine arg1HelpLine = subHelpLines.get(1);

        assertEquals("--argument1", arg1HelpLine.getOpt());
        assertEquals("Argument 1", arg1HelpLine.getName());
        assertEquals("", arg1HelpLine.getShortOpt());
        assertEquals("Argument 1", arg1HelpLine.getDescription());

        option2HelpLine = subHelpLines.get(2);

        assertEquals("--option2 <arg1> <arg2>", option2HelpLine.getOpt());
        assertEquals("Option 2", option2HelpLine.getName());
        assertEquals("option2", option2HelpLine.getShortOpt());
        assertEquals("Option 2", option2HelpLine.getDescription());

        subHelpLines = option2HelpLine.getSubHelpLines();

        assertEquals(2, subHelpLines.size());

        HelpLine arg2HelpLine = subHelpLines.get(1);

        assertEquals("", arg2HelpLine.getOpt());
        assertEquals("Argument 2", arg2HelpLine.getName());
        assertEquals("", arg2HelpLine.getShortOpt());
        assertEquals("", arg2HelpLine.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> groupOptions = group.getOptions();

        assertEquals(3, groupOptions.size());

        Option option1 = groupOptions.get(0);

        assertEquals("option1", option1.getLongOpt());
        assertEquals("Option 1", option1.getName());
        assertTrue(option1.isRequired());

        Option option2 = groupOptions.get(1);

        assertEquals("option2", option2.getLongOpt());
        assertEquals("Option 2", option2.getName());
        assertFalse(option2.isRequired());
        assertEquals(2, option2.getMinValues());
        assertEquals(3, option2.getMaxValues());

        Option arg1 = groupOptions.get(2);

        assertEquals("argument1", arg1.getLongOpt());
        assertEquals("Argument 1", arg1.getName());
        assertTrue(arg1.isRequired());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> groupAnonymous = group.getAnonymous();

        assertEquals(2, groupAnonymous.size());

        Option arg1 = groupAnonymous.get(0);

        assertEquals("argument1", arg1.getLongOpt());
        assertEquals("Argument 1", arg1.getName());
        assertTrue(arg1.isRequired());

        Option arg2 = groupAnonymous.get(1);

        assertEquals("argument2", arg2.getLongOpt());
        assertEquals("Argument 2", arg2.getName());
        assertFalse(arg2.isRequired());
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("option1");

        assertEquals("option1", foundOption.getLongOpt());
        assertEquals("Option 1", foundOption.getName());

        foundOption = group.findOption("option2");

        assertEquals("option2", foundOption.getLongOpt());
        assertEquals("Option 2", foundOption.getName());

        foundOption = group.findOption("invalidOption");

        assertNull(foundOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        assertNull(commandLine.getValue("option1"));
        assertNull(commandLine.getValue("option2"));
        assertNull(commandLine.getValue("argument1"));
        assertNull(commandLine.getValue("argument2"));
    }
}

class ReverseStringComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o2.compareTo(o1);
    }
}