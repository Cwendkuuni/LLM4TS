package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.ArgumentType;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.Groups;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link GroupImpl}.
 */
public class GroupImplTest {

    private List<Option> options;
    private Group group;

    /**
     * Sets up test fixtures.
     */
    @Before
    public void setUp() {
        final List<Option> options = new ArrayList<>();

        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .isRequired(false)
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .isRequired(false)
                .create());
        options.add(OptionBuilder.withLongOpt("option3")
                .withDescription("Option 3")
                .isRequired(false)
                .create());
        options.add(OptionBuilder.withLongOpt("option4")
                .withDescription("Option 4")
                .isRequired(false)
                .create());

        group = new GroupImpl(options, "MyGroup", "MyGroup description", 1, Integer.MAX_VALUE);
    }

    /**
     * Tests that the group can be created successfully.
     */
    @Test
    public void testGroupCanBeCreated() {
        assertNotNull(group);
    }

    /**
     * Tests that the group can process a valid command line.
     */
    @Test
    public void testGroupCanProcessValidCommandLine() {
        final WriteableCommandLine commandLine = new WriteableCommandLine("test");

        final List<String> arguments = Arrays.asList("--option1", "--option2");

        group.process(commandLine, arguments.listIterator());

        assertEquals(2, commandLine.getOptions().size());
        assertNotNull(commandLine.getOptionValues("option1"));
        assertNotNull(commandLine.getOptionValues("option2"));
    }

    /**
     * Tests that the group cannot process an invalid command line.
     */
    @Test
    public void testGroupCannotProcessInvalidCommandLine() {
        final WriteableCommandLine commandLine = new WriteableCommandLine("test");

        assertFalse(group.canProcess(commandLine, null));

        final List<String> arguments = Arrays.asList("invalid");

        assertThrows(OptionException.class, () -> group.process(commandLine, arguments.listIterator()));

        assertEquals(0, commandLine.getOptions().size());
    }

    /**
     * Tests that the group validate a valid command line.
     */
    @Test
    public void testGroupValidatesValidCommandLine() {
        final WriteableCommandLine commandLine = new WriteableCommandLine("test");

        final List<String> arguments = Arrays.asList("--option1", "--option2");

        group.validate(commandLine);

        assertEquals(2, commandLine.getOptions().size());
        assertNotNull(commandLine.getOptionValues("option1"));
        assertNotNull(commandLine.getOptionValues("option2"));
    }

    /**
     * Tests that the group throws an exception when presented with an invalid command line.
     */
    @Test
    public void testGroupThrowsExceptionForInvalidCommandLine() {
        final WriteableCommandLine commandLine = new WriteableCommandLine("test");

        final List<String> arguments = Arrays.asList("--option1");

        assertThrows(OptionException.class, () -> group.validate(commandLine));

        assertEquals(0, commandLine.getOptions().size());
    }

    /**
     * Tests that the group can handle multiple instances of the same option.
     */
    @Test
    public void testGroupHandlesMultipleInstancesOfSameOption() {
        final WriteableCommandLine commandLine = new WriteableCommandLine("test");

        final List<String> arguments = Arrays.asList("--option1", "--option1", "--option2");

        group.process(commandLine, arguments.listIterator());

        assertEquals(2, commandLine.getOptions().size());
        assertNotNull(commandLine.getOptionValues("option1"));
        assertEquals(2, commandLine.getOptionValues("option1").length);
        assertNull(commandLine.getOptionValues("option2"));
    }

    /**
     * Tests that the group returns the correct description when asked.
     */
    @Test
    public void testGroupReturnsCorrectDescription() {
        assertEquals("MyGroup description", group.getDescription());
    }

    /**
     * Tests that the group returns the correct name when asked.
     */
    @Test
    public void testGroupReturnsCorrectName() {
        assertEquals("MyGroup", group.getName());
    }

    /**
     * Tests that the group returns the correct options when asked.
     */
    @Test
    public void testGroupReturnsCorrectOptions() {
        assertEquals(3, group.getOptions().size());
    }

    /**
     * Tests that the group returns the correct anonymous arguments when asked.
     */
    @Test
    public void testGroupReturnsCorrectAnonymousArguments() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("anonymous1")
                .withDescription("Anonymous Argument 1")
                .isRequired(false)
                .create());
        options.add(OptionBuilder.withLongOpt("anonymous2")
                .withDescription("Anonymous Argument 2")
                .isRequired(false)
                .create());

        final Group group = new GroupImpl(options, "MyGroup", "MyGroup description", 1, Integer.MAX_VALUE);

        assertEquals(2, group.getAnonymous().size());
    }

    /**
     * Tests that the group returns the correct number of minimum options.
     */
    @Test
    public void testGroupReturnsCorrectMinimumNumberOfOptions() {
        assertEquals(1, group.getMinimum());
    }

    /**
     * Tests that the group returns the correct number of maximum options.
     */
    @Test
    public void testGroupReturnsCorrectMaximumNumberOfOptions() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    /**
     * Tests that a group can be created with a list of options and an empty list of anonymous arguments.
     */
    @Test
    public void testGroupCanBeCreatedWithEmptyListOfAnonymousArguments() {
        final List<Option> options = Arrays.asList(
                OptionBuilder.withLongOpt("option1")
                        .withDescription("Option 1")
                        .isRequired(false)
                        .create(),
                OptionBuilder.withLongOpt("option2")
                        .withDescription("Option 2")
                        .isRequired(false)
                        .create());

        final Group group = new GroupImpl(options, "MyGroup", "MyGroup description", 1, Integer.MAX_VALUE);

        assertEquals(2, group.getOptions().size());
        assertEquals(0, group.getAnonymous().size());
    }

    /**
     * Tests that a group can be created with a list of options and a list of anonymous arguments.
     */
    @Test
    public void testGroupCanBeCreatedWithListOfAnonymousArguments() {
        final List<Option> options = Arrays.asList(
                OptionBuilder.withLongOpt("option1")
                        .withDescription("Option 1")
                        .isRequired(false)
                        .create(),
                OptionBuilder.withLongOpt("option2")
                        .withDescription("Option 2")
                        .isRequired(false)
                        .create(),
                OptionBuilder.withLongOpt("anonymous1")
                        .withDescription("Anonymous Argument 1")
                        .isRequired(false)
                        .create(),
                OptionBuilder.withLongOpt("anonymous2")
                        .withDescription("Anonymous Argument 2")
                        .isRequired(false)
                        .create());

        final Group group = new GroupImpl(options, "MyGroup", "MyGroup description", 1, Integer.MAX_VALUE);

        assertEquals(4, group.getOptions().size());
        assertEquals(2, group.getAnonymous().size());
    }

    /**
     * Tests that the group's help method returns the correct help lines.
     */
    @Test
    public void testGroupHelpMethodReturnsCorrectHelpLines() {
        final List<String> expectedHelpLines = Arrays.asList(
                "MyGroup:",
                "Options:",
                "   --option1 <value>...",
                "      Option 1",
                "   --option2 <value>...",
                "      Option 2",
                "Arguments:",
                "   --anonymous1 <value>...",
                "      Anonymous Argument 1",
                "   --anonymous2 <value>...",
                "      Anonymous Argument 2",
                "");

        final List<String> helpLines = Groups.helpLines(group, new HashMap<>());

        assertEquals(expectedHelpLines.size(), helpLines.size());
        for (int i = 0; i < helpLines.size(); i++) {
            assertEquals(expectedHelpLines.get(i), helpLines.get(i));
        }
    }
}