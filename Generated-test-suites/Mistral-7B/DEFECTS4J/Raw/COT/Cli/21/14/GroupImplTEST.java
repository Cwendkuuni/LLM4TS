package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
 * Test class for GroupImpl.
 */
public class GroupImplTest {

    private List<Option> options;
    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        final List<Option> optionsList = new ArrayList<>();
        optionsList.add(new Option("a", "optA", true, "Test option A"));
        optionsList.add(new Option("b", "optB", true, "Test option B"));
        optionsList.add(new Option("c", "optC", false, "Test option C"));
        optionsList.add(new Option("d", "optD", false, "Test option D"));
        optionsList.add(new Option("e", "optE", false, "Test option E"));
        optionsList.add(new Argument("arg", "argName", true, "Test argument"));
        this.options = Collections.unmodifiableList(optionsList);

        this.group = new GroupImpl(options, "TestGroup", null, 1, Integer.MAX_VALUE, false);
        this.commandLine = new WriteableCommandLine("TestCommandLine");
    }

    /**
     * Test constructor.
     */
    @Test
    public void testGroupConstructor() {
        assertEquals("TestGroup", this.group.getPreferredName());
        assertEquals("", this.group.getDescription());
        assertEquals(this.options, this.group.getOptions());
        assertEquals(Collections.emptyList(), this.group.getAnonymous());
        assertNull(this.group.getName());
        assertFalse(this.group.isRequired());
        assertEquals(1, this.group.getMinimum());
        assertEquals(Integer.MAX_VALUE, this.group.getMaximum());

        // test process
        assertFalse(this.group.canProcess(commandLine, null));
        assertFalse(this.group.canProcess(commandLine, ""));
        assertTrue(this.group.canProcess(commandLine, "a"));
        assertTrue(this.group.canProcess(commandLine, "a"));
        assertTrue(this.group.canProcess(commandLine, "c"));
        assertTrue(this.group.canProcess(commandLine, "d"));
        assertTrue(this.group.canProcess(commandLine, "e"));
        assertTrue(this.group.canProcess(commandLine, "arg"));

        // test getPrefixes
        Set prefixes = this.group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);

        // test getTriggers
        Set triggers = this.group.getTriggers();
        assertEquals(this.options.size(), triggers.size());
        assertTrue(triggers.containsAll(Arrays.asList("a", "b", "c", "d", "e")));

        // test process
        this.commandLine.parse(this.group, null);
        List<String> args = new ArrayList<>(this.commandLine.getArgs());
        assertEquals(this.options.size(), args.size());
        for (int i = 0; i < this.options.size(); i++) {
            Option option = this.options.get(i);
            String expectedArg = option.getLongOpt();
            String actualArg = args.get(i);
            assertEquals(expectedArg, actualArg);
            assertTrue(this.commandLine.hasOption(expectedArg));
        }

        // test process with required options
        optionsList.remove(0); // remove the first option
        Group newGroup = new GroupImpl(options, "TestGroup", null, 1, 1, true);
        commandLine = new WriteableCommandLine("TestCommandLine");
        try {
            newGroup.process(commandLine, commandLine.getArguments().listIterator());
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        // test process with max number of options
        optionsList.add(new Option("f", "optF", false, "Test option F"));
        Group newGroupMax = new GroupImpl(options, "TestGroup", null, 1, 1, false);
        commandLine = new WriteableCommandLine("TestCommandLine");
        try {
            newGroupMax.process(commandLine, commandLine.getArguments().listIterator());
            newGroupMax.process(commandLine, commandLine.getArguments().listIterator());
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        // test process with min and max number of options
        Group newGroupMinMax = new GroupImpl(options, "TestGroup", null, 3, 5, false);
        commandLine = new WriteableCommandLine("TestCommandLine");
        try {
            newGroupMinMax.process(commandLine, commandLine.getArguments().listIterator());
            newGroupMinMax.process(commandLine, commandLine.getArguments().listIterator());
            newGroupMinMax.process(commandLine, commandLine.getArguments().listIterator());
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        // test validate
        try {
            this.group.validate(commandLine);
        } catch (OptionException e) {
            fail("Expected no OptionException");
        }
        this.commandLine.clear();
        try {
            this.group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        // test appendUsage
        StringBuffer buffer = new StringBuffer("");
        this.group.appendUsage(buffer, Collections.emptySet(), null);
        String expectedUsage = "TestGroup:\n" +
                "  optA <ARG>    Test option A\n" +
                "  optB <ARG>    Test option B\n" +
                "  optC         Test option C\n" +
                "  optD         Test option D\n" +
                "  optE         Test option E\n" +
                "  arg          Test argument\n";
        assertEquals(expectedUsage.trim(), buffer.toString().trim());

        // test helpLines
        List<HelpLine> helpLines = this.group.helpLines(0, Collections.emptySet(), null);
        String expectedHelpLinesUsage = "TestGroup:\n" +
                "  optA <ARG>    Test option A\n" +
                "  optB <ARG>    Test option B\n" +
                "  optC         Test option C\n" +
                "  optD         Test option D\n" +
                "  optE         Test option E\n" +
                "  arg          Test argument\n";
        String expectedHelpLinesDescription = "TestGroup\n" +
                "\n";
        String expectedHelpLinesOptions = "";
        for (Option option : this.options) {
            expectedHelpLinesOptions += option.getHelpLine(0, null, null).toString();
        }
        String expectedHelpLinesOptionsAnonymous = "";
        for (Option option : this.options) {
            if (option instanceof Argument) {
                Argument argument = (Argument) option;
                expectedHelpLinesOptionsAnonymous += argument.getHelpLine(0, null, null).toString() + "\n";
            }
        }
        String expectedHelpLines = expectedHelpLinesUsage + "\n" +
                expectedHelpLinesDescription + "\n" +
                expectedHelpLinesOptions + "\n" +
                expectedHelpLinesOptionsAnonymous;
        for (HelpLine helpLine : helpLines) {
            String helpLineString = helpLine.toString();
            assertEquals(expectedHelpLines, helpLineString);
        }
    }

    /**
     * Test the canProcess method with groups.
     */
    @Test
    public void testCanProcessWithGroups() {
        // Test with a simple group
        List<Option> optionsListGroup1 = new ArrayList<>();
        optionsListGroup1.add(new Option("g1a", "optG1A", true, "Test option G1A"));
        optionsListGroup1.add(new Group("Group1", "Test group 1") {
            @Override
            public List options() {
                return Collections.emptyList();
            }

            @Override
            public String getPreferredName() {
                return "Group1";
            }

            @Override
            public String getDescription() {
                return "Test group 1";
            }
        });
        Group group1 = new GroupImpl(optionsListGroup1, "Group1", null, 1, 1, false);
        WriteableCommandLine commandLineGroup1 = new WriteableCommandLine("TestCommandLine");

        // Test that no group can process the empty argument list
        assertFalse(group1.canProcess(commandLineGroup1, null));

        // Test that the group can process its trigger
        assertFalse(group1.canProcess(commandLineGroup1, "g1a"));
        assertTrue(group1.canProcess(commandLineGroup1, "Group1"));

        // Test that a nested group can process one of its options
        List<Option> optionsListGroup2 = new ArrayList<>();
        optionsListGroup2.add(new Option("g2a", "optG2A", true, "Test option G2A"));
        Group group2 = new GroupImpl(optionsListGroup2, "Group2", null, 1, 1, false);
        optionsListGroup1.set(1, group2);

        // Test that the outer group can process the nested group's trigger
        assertFalse(group1.canProcess(commandLineGroup1, "g2a"));
        assertTrue(group1.canProcess(commandLineGroup1, "Group2"));

        // Test that a nested group can process its option
        List<Option> optionsListGroup3 = new ArrayList<>();
        optionsListGroup3.add(new Option("g3a", "optG3A", true, "Test option G3A"));
        Option optionG3A = new Option("g3b", "optG3B", true, "Test option G3B");
        optionsListGroup3.add(optionG3A);
        Group group3 = new GroupImpl(optionsListGroup3, "Group3", null, 1, 1, false);
        optionsListGroup2.set(0, group3);

        // Test that the outer group can process the nested group's option
        assertFalse(group1.canProcess(commandLineGroup1, "g3a"));
        assertTrue(group1.canProcess(commandLineGroup1, "Group3"));
        assertTrue(group1.canProcess(commandLineGroup1, "optG3A"));

        // Test that the outer group can process an anonymous argument
        List<Option> optionsListGroupAnonymous = new ArrayList<>();
        optionsListGroupAnonymous.add(new Argument("argG1Anonymous", "argG1Anonymous", true, "Test anonymous argument"));
        Group groupAnonymous = new GroupImpl(optionsListGroupAnonymous, null, null, 1, 1, false);
        optionsListGroup1.add(groupAnonymous);

        // Test that the outer group can process the anonymous argument
        assertFalse(group1.canProcess(commandLineGroup1, "argG1Anonymous"));
        assertTrue(group1.canProcess(commandLineGroup1, "arg"));
    }
}