package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = Arrays.asList(
            new Option("a", "arg1", true, "value1"),
            new Option("b", "arg2", false, "value2"),
            new Option("c", "arg3", true, "value3"),
            new Argument("arg4"),
            new Argument("arg5")
        );
        commandLine = new WriteableCommandLine();
        group = new GroupImpl(options, "groupName", "groupDescription", 1, 2);
    }

    @Test
    public void testConstructor() {
        assertNotNull(group);
        assertEquals("groupName", group.getPreferredName());
        assertEquals("groupDescription", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertEquals(options, group.getOptions());
        assertEquals(Collections.unmodifiableList(Arrays.asList(new Argument("arg4"), new Argument("arg5"))), group.getAnonymous());
    }

    @Test
    public void testCanProcess() {
        commandLine.addOption("a");
        commandLine.addOption("b");

        // empty argument
        assertFalse(group.canProcess(commandLine, ""));

        // option trigger
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "c"));

        // argument
        commandLine.addArgument("arg");
        assertTrue(group.canProcess(commandLine, "arg4"));
        assertTrue(group.canProcess(commandLine, "arg5"));

        // valid option with bursting
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "a"));

        // invalid option
        assertFalse(group.canProcess(commandLine, "d"));
        assertFalse(group.canProcess(commandLine, "e"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertNotNull(prefixes);
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertNotNull(triggers);
        assertEquals(options.size(), triggers.size());

        for (Option option : options) {
            assertTrue(triggers.contains(option.getTrigger()));
        }
    }

    @Test
    public void testProcess() throws OptionException {
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("a");
        commandLine.addOption("c");

        // empty argument
        assertFalse(group.canProcess(commandLine, ""));

        // option trigger
        group.process(commandLine, commandLine.listIterator());
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("c"));

        // argument
        commandLine.addArgument("arg");
        group.process(commandLine, commandLine.listIterator());
        assertTrue(commandLine.hasArgument("arg4"));
        assertTrue(commandLine.hasArgument("arg5"));

        // valid option with bursting
        group.process(commandLine, commandLine.listIterator());
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("a"));

        // invalid option
        assertFalse(group.canProcess(commandLine, "d"));
        assertFalse(group.canProcess(commandLine, "e"));
    }

    @Test
    public void testValidate() throws OptionException {
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("a");

        // valid command line
        group.validate(commandLine);

        // missing option
        commandLine.removeOption("b");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(ResourceConstants.MISSING_OPTION, e.getMessage());
        }

        // too many options
        commandLine.addOption("d");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(ResourceConstants.TOO_MANY_OPTIONS, e.getMessage());
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet(Arrays.asList(DisplaySetting.DISPLAY_GROUP_NAME, DisplaySetting.DISPLAY_GROUP_EXPANDED, DisplaySetting.DISPLAY_GROUP_ARGUMENT));

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "groupName (OPTIONS)\n" +
                "  arg1: <value1>\n" +
                "  arg2: <value2> (optional, can be repeated)\n" +
                "  arg3: <value3>\n" +
                "  arg4: <argument>\n" +
                "  arg5: <argument>\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        String depth = "  ";

        List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);

        assertEquals(2, helpLines.size());

        HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("groupName (OPTIONS)", helpLine1.getHeadText());
        assertEquals(Collections.singletonList(new HelpLine("", "OPTIONS:")), helpLine1.getOptions());
        assertEquals(Collections.singletonList(new HelpLine("", "arg1: <value1>")), helpLine1.getArguments());

        HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("  arg2: <value2> (optional, can be repeated)", helpLine2.getHeadText());
        assertEquals(Collections.emptyList(), helpLine2.getOptions());
        assertEquals(Collections.emptyList(), helpLine2.getArguments());

        helpLines = group.helpLines(2, Collections.emptySet(), null);

        assertEquals(4, helpLines.size());

        HelpLine helpLine3 = (HelpLine) helpLines.get(2);
        assertEquals("    arg3: <value3>", helpLine3.getHeadText());
        assertEquals(Collections.emptyList(), helpLine3.getOptions());
        assertEquals(Collections.emptyList(), helpLine3.getArguments());

        HelpLine helpLine4 = (HelpLine) helpLines.get(3);
        assertEquals("  arg4: <argument>", helpLine4.getHeadText());
        assertEquals(Collections.emptyList(), helpLine4.getOptions());
        assertEquals(Collections.singletonList(new HelpLine("", "arg4: <argument>")), helpLine4.getArguments());

        helpLines = group.helpLines(3, Collections.emptySet(), null);

        assertEquals(6, helpLines.size());

        HelpLine helpLine5 = (HelpLine) helpLines.get(4);
        assertEquals("    arg5: <argument>", helpLine5.getHeadText());
        assertEquals(Collections.emptyList(), helpLine5.getOptions());
        assertEquals(Collections.singletonList(new HelpLine("", "arg5: <argument>")), helpLine5.getArguments());

        HelpLine helpLine6 = (HelpLine) helpLines.get(5);
        assertEquals("", helpLine6.getHeadText());
        assertEquals(Collections.emptyList(), helpLine6.getOptions());
        assertEquals(Collections.emptyList(), helpLine6.getArguments());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(3, options.size());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertEquals(2, anonymous.size());
    }

    @Test
    public void testFindOption() {
        Option optionA = new Option("a", "arg1", true, "value1");
        commandLine.addOption(optionA);

        Option foundOption = group.findOption("a");

        assertEquals(optionA, foundOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertTrue(commandLine.hasArgument("arg4"));
        assertTrue(commandLine.hasArgument("arg5"));
    }
}