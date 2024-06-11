package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    private WriteableCommandLine commandLine;
    private GroupImpl group;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine();
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Option A"));
        options.add(new Option("b", "optionB", true, "Option B"));
        options.add(new Option("c", "optionC", false, "Option C"));
        group = new GroupImpl(options, "MyGroup", "My Group", 1, 1, false);
    }

    @Test
    public void testCanProcessRequiredOption() throws OptionException {
        String[] args = {"--MyGroup"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        assertTrue(commandLine.hasOption("MyGroup"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessMoreThanMaximumOption() throws OptionException {
        String[] args = {"--MyGroup", "--MyGroup", "--MyGroup"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        fail("Should have thrown OptionException for MyGroup");
    }

    @Test(expected = OptionException.class)
    public void testCanProcessMissingOption() throws OptionException {
        String[] args = {"--MyGroup2"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        fail("Should have thrown OptionException for MyGroup2");
    }

    @Test
    public void testValidateRequiredOption() throws OptionException {
        String[] args = {};
        group.validate(commandLine);
        fail("Should have thrown OptionException for MyGroup");
    }

    @Test
    public void testValidateMinimumOption() throws OptionException {
        String[] args = {"--MyGroup"};
        group.validate(commandLine);
        assertTrue(commandLine.isValid());
    }

    @Test(expected = OptionException.class)
    public void testValidateMinimumMissingOption() throws OptionException {
        String[] args = {};
        group.validate(commandLine);
        fail("Should have thrown OptionException for MyGroup");
    }

    @Test
    public void testAppendUsage() {
        StringWriter writer = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        group.appendUsage(writer, null, null);
        String usage = writer.toString();
        assertTrue(usage.contains("My Group"));
        assertTrue(usage.contains("optionA"));
        assertTrue(usage.contains("optionB"));
        assertTrue(usage.contains("optionC"));
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, null, null);
        String expectedHelpText = "My Group:\n" +
                "  optionA\n" +
                "  optionB\n" +
                "  optionC\n";
        for (HelpLine helpLine : helpLines) {
            assertEquals(expectedHelpText, helpLine.getHelpText());
        }
    }

    @Test
    public void testProcessOptionalOption() throws OptionException {
        String[] args = {"--MyGroup"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        assertTrue(commandLine.isValid());
    }

    @Test
    public void testProcessAnonymousArgument() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(new Option("g", "group", false, "My Group"));
        options.add(new Argument("a"));
        group = new GroupImpl(options, "MyGroup", "My Group", 0, 1, false);

        String[] args = {"--MyGroup", "arg"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        assertEquals("arg", commandLine.getArgument("a"));
    }

    @Test(expected = OptionException.class)
    public void testProcessAnonymousMissingArgument() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(new Option("g", "group", false, "My Group"));
        options.add(new Argument("a"));
        group = new GroupImpl(options, "MyGroup", "My Group", 0, 1, false);

        String[] args = {"--MyGroup"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        fail("Should have thrown OptionException for anonymous argument");
    }

    @Test
    public void testProcessAnonymousMultipleArguments() throws OptionException {
        List<Option> options = new ArrayList<>();
        options.add(new Option("g", "group", false, "My Group"));
        options.add(new Argument("a"));
        options.add(new Argument("b"));
        group = new GroupImpl(options, "MyGroup", "My Group", 0, 1, false);

        String[] args = {"--MyGroup", "arg1", "arg2"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        assertEquals("arg1", commandLine.getArgument("a"));
        assertEquals("arg2", commandLine.getArgument("b"));
    }
}