package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.option.GroupImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "a"));
        options.add(new Option("b", "option B", false, "b"));
        options.add(new Option("c", "option C", false, "c"));
        options.add(new Argument("arg", "an argument"));
        group = new GroupImpl(options, "group", "A group", 1, 1);
    }

    @Test
    public void testConstructor() {
        assertNotNull(group);
        assertEquals("group", group.getPreferredName());
        assertEquals("A group", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals(options, group.getOptions());
        assertFalse(group.isSet());
        assertFalse(group.getValue().isSet());
    }

    @Test
    public void testCanProcess() {
        CommandLine commandLine = TestUtils.createDefaultCommandLine();

        // Test with valid options
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));

        // Test with invalid options
        assertFalse(group.canProcess(commandLine, "d"));
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, null));

        // Test with anonymous arguments
        String[] args = {"arg"};
        commandLine.parse(args);
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArguments() {
        CommandLine commandLine = TestUtils.createDefaultCommandLine();

        // Test with too many options
        commandLine.parse(new String[]{"a", "a", "b"});
        group.process(commandLine, commandLine.listIterator());

        // Test with too few options
        commandLine.parse(new String[]{"a"});
        group.process(commandLine, commandLine.listIterator());
    }

    @Test
    public void testValidate() {
        CommandLine commandLine = TestUtils.createDefaultCommandLine();

        // Test with valid options
        commandLine.parse(new String[]{"a"});
        group.validate(commandLine);

        // Test with too many options
        commandLine.parse(new String[]{"a", "a"});
        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // Expected
        }

        // Test with too few options
        commandLine.parse(new String[]{"a"});
        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // Expected
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.emptySet(), null);

        String expected = "group:\n" +
                "  A group\n" +
                "\n" +
                "Options:\n" +
                "========\n" +
                "a\n" +
                "  option A\n" +
                "  This option takes an argument.\n" +
                "b\n" +
                "  option B\n" +
                "  This option doesn't take an argument.\n" +
                "c\n" +
                "  option C\n" +
                "  This option doesn't take an argument.\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();

        List<String> helpLines = group.helpLines(0, Collections.emptySet(), null);

        String expected = "group:\n" +
                "  A group\n" +
                "\n" +
                "Options:\n" +
                "========\n" +
                "a\n" +
                "  option A\n" +
                "  This option takes an argument.\n" +
                "b\n" +
                "  option B\n" +
                "  This option doesn't take an argument.\n" +
                "c\n" +
                "  option C\n" +
                "  This option doesn't take an argument.\n";

        formatter.printHelp("test", "", helpLines.toArray(new String[helpLines.size()]), true);

        String output = System.out.getBuffer().toString();
        assertEquals(expected, output);
    }

    @Test
    public void testFindOption() {
        Option optionA = group.findOption("a");
        Option optionB = group.findOption("b");
        Option optionC = group.findOption("c");
        Option argument = group.findOption("arg");

        assertNotNull(optionA);
        assertNotNull(optionB);
        assertNotNull(optionC);
        assertNull(argument);
    }

    @Test
    public void testGetMinimumAndMaximum() {
        GroupImpl group1 = new GroupImpl(options, "group1", "A group1", 1, 1);
        GroupImpl group2 = new GroupImpl(options, "group2", "A group2", 2, 2);

        assertEquals(1, group1.getMinimum());
        assertEquals(1, group1.getMaximum());
        assertEquals(2, group2.getMinimum());
        assertEquals(2, group2.getMaximum());
    }
}