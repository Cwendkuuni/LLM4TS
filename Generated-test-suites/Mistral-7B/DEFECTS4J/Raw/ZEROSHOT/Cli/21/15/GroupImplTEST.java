package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Tests for {@link GroupImpl}.
 */
public class GroupImplTest {

    private WriteableCommandLine commandLine;
    private HelpLine helpLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine();
        helpLine = new HelpLineImpl(new TestGroup("TestGroup"), 0);
    }

    @Test
    public void testCanProcess() throws OptionException {
        final List options = new ArrayList();
        final String name = "test";
        final String description = "TestGroup description";
        final int minimum = 2;
        final int maximum = 5;

        // test valid options
        options.add(createOption("option1", "opt1", null, false, null));
        options.add(createArgument("arg1", "arg1", null, false, false, null));
        options.add(createOption("option2", "opt2", null, false, null));

        final GroupImpl group = new GroupImpl(options, name, description, minimum, maximum, false);

        // test valid option processing
        for (final String option : Arrays.asList("opt1", "opt2")) {
            assertTrue(group.canProcess(commandLine, option));
        }

        // test anonymous argument processing
        assertTrue(group.canProcess(commandLine, "arg1"));

        // test invalid option processing
        assertFalse(group.canProcess(commandLine, "invalidOption"));
    }

    @Test(expected = OptionException.class)
    public void testValidate() throws OptionException {
        final List options = new ArrayList();
        final String name = "test";
        final String description = "TestGroup description";
        final int minimum = 1;
        final int maximum = 1;

        // test valid options
        options.add(createOption("option1", "opt1", null, false, null));

        new GroupImpl(options, name, description, minimum, maximum, false).validate(commandLine);

        // test minimum constraint violation
        options.clear();
        options.add(createOption("option1", "opt1", null, false, null));
        new GroupImpl(options, name, description, minimum, maximum, false).validate(commandLine);

        // test maximum constraint violation
        options.clear();
        options.add(createOption("option1", "opt1", null, false, null));
        new GroupImpl(options, name, description, minimum, maximum + 1, false).validate(commandLine);
    }

    @Test
    public void testProcess() throws OptionException {
        final List options = new ArrayList();
        final String name = "test";
        final String description = "TestGroup description";
        final int minimum = 1;
        final int maximum = 2;

        // test valid option processing
        options.add(createOption("option1", "opt1", null, false, null));
        options.add(createOption("option2", "opt2", null, false, null));

        final GroupImpl group = new GroupImpl(options, name, description, minimum, maximum, false);

        // test valid option processing
        commandLine.parse(Arrays.asList("test", "opt1", "opt2"));
        assertEquals("TestGroup", group.getPreferredName());
        assertFalse(commandLine.hasOption("invalidOption"));
        assertFalse(commandLine.hasOption("arg1"));

        // test anonymous argument processing
        commandLine.parse(Arrays.asList("test", "opt1", "arg1"));
        assertEquals("TestGroup", group.getPreferredName());
        assertTrue(commandLine.hasOption("arg1"));

        // test invalid option processing
        try {
            commandLine.parse(Arrays.asList("test", "invalidOption"));
            fail();
        } catch (OptionException e) {
            // expected
        }

        // test minimum constraint violation
        commandLine.clear();
        options.clear();
        options.add(createOption("option1", "opt1", null, false, null));
        try {
            commandLine.parse(Arrays.asList("test"));
            fail();
        } catch (OptionException e) {
            // expected
        }

        // test maximum constraint violation
        commandLine.clear();
        options.add(createOption("option1", "opt1", null, false, null));
        options.add(createOption("option1", "opt1", null, false, null));
        try {
            commandLine.parse(Arrays.asList("test", "opt1", "opt1"));
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testAppendUsage() {
        final List options = new ArrayList();
        final String name = "test";

        // test empty group
        final GroupImpl emptyGroup = new GroupImpl(Collections.emptyList(), name, null, 0, 0, false);
        final StringBuilder buffer = new StringBuilder();
        emptyGroup.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("", buffer.toString());

        // test simple group
        options.add(createOption("option1", "opt1", null, false, null));
        final GroupImpl simpleGroup = new GroupImpl(options, name, null, 0, 0, false);
        final StringBuilder buffer2 = new StringBuilder();
        simpleGroup.appendUsage(buffer2, Collections.emptySet(), null);
        assertEquals("test:\n", buffer2.toString());

        // test group with multiple options
        options.clear();
        options.add(createOption("option1", "opt1a", null, false, null));
        options.add(createOption("option1", "opt1b", null, false, null));
        final GroupImpl group = new GroupImpl(options, name, null, 0, 0, false);
        final StringBuilder buffer3 = new StringBuilder();
        group.appendUsage(buffer3, Collections.emptySet(), null);
        assertEquals(
                "test:\n" +
                        "opt1  :\n" +
                        "     opt1a\n" +
                        "      opt1b\n",
                buffer3.toString());

        // test group with an optional argument
        options.clear();
        options.add(createOption("option1", "opt1", null, false, null));
        options.add(createArgument("arg1", "arg", null, false, false, null));
        final GroupImpl groupWithArg = new GroupImpl(options, name, null, 0, 0, false);
        final StringBuilder buffer4 = new StringBuilder();
        groupWithArg.appendUsage(buffer4, Collections.emptySet(), null);
        assertEquals(
                "test:\n" +
                        "opt1  :\n" +
                        "      arg\n",
                buffer4.toString());
    }

    @Test
    public void testHelpLines() {
        final List options = new ArrayList();
        final String name = "test";

        // test empty group
        final GroupImpl emptyGroup = new GroupImpl(Collections.emptyList(), name, null, 0, 0, false);
        final List helpLines = emptyGroup.helpLines(0, Collections.emptySet(), null);
        assertEquals(0, helpLines.size());

        // test simple group
        options.add(createOption("option1", "opt1", null, false, null));
        final GroupImpl simpleGroup = new GroupImpl(options, name, null, 0, 0, false);
        final List helpLines2 = simpleGroup.helpLines(0, Collections.emptySet(), null);
        assertEquals(1, helpLines2.size());
        assertEquals(helpLine.getDescription(), ((HelpLine) helpLines2.get(0)).getDescription());

        // test group with multiple options
        options.clear();
        options.add(createOption("option1", "opt1a", null, false, null));
        options.add(createOption("option1", "opt1b", null, false, null));
        final GroupImpl group = new GroupImpl(options, name, null, 0, 0, false);
        final List helpLines3 = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(3, helpLines3.size());
        assertEquals("opt1a", ((HelpLine) helpLines3.get(1)).getOption().getPreferredName());
        assertEquals("opt1b", ((HelpLine) helpLines3.get(2)).getOption().getPreferredName());

        // test group with an optional argument
        options.clear();
        options.add(createOption("option1", "opt1", null, false, null));
        options.add(createArgument("arg1", "arg", null, false, false, null));
        final GroupImpl groupWithArg = new GroupImpl(options, name, null, 0, 0, false);
        final List helpLines4 = groupWithArg.helpLines(0, Collections.emptySet(), null);
        assertEquals(4, helpLines4.size());
        assertEquals("opt1", ((HelpLine) helpLines4.get(1)).getOption().getPreferredName());
        assertEquals("arg", ((HelpLine) helpLines4.get(3)).getArgument().getName());
    }

    private Option createOption(final String name,
                               final String description,
                               final String longOpt,
                               final boolean hasArg,
                               final String argName) {
        final OptionImpl option = new OptionImpl("option", name, longOpt, hasArg, hasArg, null, null, null);
        option.setDescription(description);
        if (argName != null) {
            option.setArgName(argName);
        }
        return option;
    }

    private Argument createArgument(final String name,
                                   final String description,
                                   final String argName) {
        return new ArgumentImpl(name, description, argName, null, null, null, null);
    }
}