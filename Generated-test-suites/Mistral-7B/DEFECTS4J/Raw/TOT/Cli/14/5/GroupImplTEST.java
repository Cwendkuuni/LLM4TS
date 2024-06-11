package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private WriteableCommandLine commandLine;
    private GroupImpl group;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine();

        final List options = new ArrayList();
        options.add(new Option("a", "argument-a", true, "an argument for option a"));
        options.add(new Option("b", null, true, "an argument for option b"));
        options.add(new Option("c", "option-c", false, "an option with no argument"));
        options.add(new Argument("arg1", "an anonymous argument 1"));
        options.add(new Argument("arg2", "an anonymous argument 2"));

        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 0, Integer.MAX_VALUE);
    }

    @Test
    public void testCanProcessWithNoArguments() {
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessWithOneArgument() {
        commandLine.addOption("a", "value-a");
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessWithTwoArguments() {
        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithTwoArgumentsButMaximumIsOne() {
        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 1, 1);

        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        group.process(commandLine, commandLine.listIterator());
    }

    @Test
    public void testCanProcessWithThreeArgumentsButMaximumIsTwo() {
        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 2, 2);

        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        commandLine.addOption("c", "value-c");

        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessWithAnonymousArgument() {
        commandLine.addOption("a", "value-a");
        claimAnonymousArgumentIsProcessed("arg1");
    }

    @Test
    public void testCanProcessWithAnonymousArgumentAndAnOption() {
        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        claimAnonymousArgumentIsProcessed("arg1");
    }

    @Test
    public void testCanProcessWithAnonymousArguments() {
        commandLine.addOption("a", "value-a");
        commandLine.addArgument("arg1");
        commandLine.addArgument("arg2");
        group.process(commandLine, commandLine.listIterator());
        claimAnonymousArgumentsAreProcessed("arg1", "arg2");
    }

    @Test
    public void testValidateWithNoOptions() {
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithNoAnonymousArguments() {
        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 0, Integer.MAX_VALUE);

        group.validate(commandLine);
        commandLine.addOption("a", "value-a");
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewAnonymousArguments() {
        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 2, Integer.MAX_VALUE);

        group.validate(commandLine);
        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyAnonymousArguments() {
        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 2, 2);

        group.validate(commandLine);
        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        commandLine.addArgument("arg3");
    }

    @Test
    public void testProcessWithNoArguments() {
        assertFalse(group.process(commandLine, commandLine.listIterator()));
    }

    @Test
    public void testProcessWithOneArgument() {
        commandLine.addOption("a", "value-a");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("an argument for option a", commandLine.getOptionValue("a"));
    }

    @Test
    public void testProcessWithTwoArguments() {
        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("an argument for option a", commandLine.getOptionValue("a"));
        assertEquals("an argument for option b", commandLine.getOptionValue("b"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTwoArgumentsButMaximumIsOne() {
        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 1, 1);

        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        group.process(commandLine, commandLine.listIterator());
    }

    @Test
    public void testProcessWithAnonymousArgument() {
        commandLine.addOption("a", "value-a");
        commandLine.addArgument("arg1");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("an anonymous argument 1", commandLine.getArgument(0));
    }

    @Test
    public void testProcessWithAnonymousArgumentAndAnOption() {
        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        commandLine.addArgument("arg1");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("an anonymous argument 1", commandLine.getArgument(0));
        assertEquals("an argument for option a", commandLine.getOptionValue("a"));
    }

    @Test
    public void testProcessWithAnonymousArguments() {
        commandLine.addOption("a", "value-a");
        commandLine.addArgument("arg1");
        commandLine.addArgument("arg2");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("an anonymous argument 1", commandLine.getArgument(0));
        assertEquals("an anonymous argument 2", commandLine.getArgument(1));
    }

    @Test
    public void testAppendUsageWithNoArguments() {
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testAppendUsageWithOneArgument() {
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        assertEquals("", buffer.toString());

        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 0, Integer.MAX_VALUE);
        commandLine.addOption("a", "value-a");
        group.appendUsage(buffer, null, null);
        assertEquals("[--a VALUE]", buffer.toString());
    }

    @Test
    public void testAppendUsageWithTwoArguments() {
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        assertEquals("", buffer.toString());

        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 0, Integer.MAX_VALUE);
        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        group.appendUsage(buffer, null, null);
        assertEquals("[--a VALUE]\n[--b VALUE]", buffer.toString());
    }

    @Test
    public void testHelpLinesWithNoArguments() {
        final List helpLines = group.helpLines(0, null, null);
        assertEmpty(helpLines);
    }

    @Test
    public void testHelpLinesWithOneArgument() {
        final List helpLines = group.helpLines(0, null, null);
        assertEmpty(helpLines);

        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 0, Integer.MAX_VALUE);
        commandLine.addOption("a", "value-a");
        final List helpLines2 = group.helpLines(0, null, null);
        assertEquals(1, helpLines2.size());
        assertEquals("OPTIONS:\n--a <VALUE>  an argument for option a\n", helpLines2.get(0).toString());
    }

    @Test
    public void testHelpLinesWithTwoArguments() {
        final List helpLines = group.helpLines(0, null, null);
        assertEmpty(helpLines);

        group = new GroupImpl(options, "group", "A group with options and anonymous arguments", 0, Integer.MAX_VALUE);
        commandLine.addOption("a", "value-a");
        commandLine.addOption("b", "value-b");
        final List helpLines2 = group.helpLines(0, null, null);
        assertEquals(2, helpLines2.size());
        assertEquals("OPTIONS:\n--a <VALUE>  an argument for option a\n--b <VALUE>  an argument for option b\n", helpLines2.get(0).toString());
    }

    private void claimAnonymousArgumentIsProcessed(final String argument) {
        commandLine.addArgument(argument);
        group.process(commandLine, commandLine.listIterator());
        assertEquals(argument, commandLine.getArgument(0));
    }

    private void claimAnonymousArgumentsAreProcessed(final String... arguments) {
        for (final String argument : arguments) {
            commandLine.addArgument(argument);
        }
        group.process(commandLine, commandLine.listIterator());
        for (int i = 0; i < arguments.length; i++) {
            assertEquals(arguments[i], commandLine.getArgument(i));
        }
    }
}