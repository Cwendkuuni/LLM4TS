package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        argument = new ArgumentImpl(
                "arg",
                "An argument test",
                1,
                2,
                ':',
                ',',
                null,
                "--values",
                Arrays.asList("value1", "value2"),
                0);
        commandLine = new CommandLine();
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        argument.processValues(commandLine, commandLine.iterator(), argument);
        Assert.assertEquals(2, commandLine.getValues(argument).size());
        Assert.assertEquals("value1", commandLine.getValues(argument).get(0));
        Assert.assertEquals("value2", commandLine.getValues(argument).get(1));
    }

    @Test
    public void testCanProcess() {
        Assert.assertTrue(argument.canProcess(commandLine, "value1"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithWrongNumberOfArguments() {
        argument.processValues(commandLine, commandLine.iterator(), argument);
        commandLine.addOption(new Option("opt", false, null, "option"));
        commandLine.addArgument(new Argument("arg", "An argument test", 1, 2, ':', ',', null, "--values", null, 1));
        argument.processValues(commandLine, commandLine.iterator(), argument);
    }

    @Test
    public void testProcessWithConsumeRemaining() {
        argument = new ArgumentImpl(
                "arg",
                "An argument test",
                1,
                2,
                ':',
                ',',
                null,
                "--values",
                Arrays.asList("value1", "value2"),
                0);

        commandLine.addOption(new Option("opt", false, null, "option"));
        commandLine.addArgument(argument);
        commandLine.addArgument("--values");
        commandLine.addArgument("value3");

        argument.processValues(commandLine, commandLine.iterator(), argument);
        Assert.assertEquals(3, commandLine.getValues(argument).size());
        Assert.assertEquals("value1", commandLine.getValues(argument).get(0));
        Assert.assertEquals("value2", commandLine.getValues(argument).get(1));
        Assert.assertEquals("value3", commandLine.getValues(argument).get(2));
    }

    @Test
    public void testGetInitialSeparator() {
        Assert.assertEquals(':', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Assert.assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        Assert.assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        Assert.assertEquals("--values", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        Assert.assertEquals(Arrays.asList("value1", "value2"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Assert.assertNull(argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidArgument() {
        argument = new ArgumentImpl(
                "arg",
                "An argument test",
                1,
                1,
                ':',
                ',',
                null,
                "--values",
                Arrays.asList("value1"),
                0);

        commandLine.addArgument(argument);
        commandLine.addArgument("value2");
        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithValidArguments() {
        argument = new ArgumentImpl(
                "arg",
                "An argument test",
                1,
                2,
                ':',
                ',',
                null,
                "--values",
                Arrays.asList("value1", "value2"),
                0);

        commandLine.addArgument(argument);
        commandLine.addArgument("value1");
        commandLine.addArgument("value2");
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(new StringWriter(stringWriter));
        HelpFormatter helpFormatter = new HelpFormatter();

        argument.appendUsage(stringWriter, Arrays.asList(DisplaySetting.DISPLAY_ARGUMENT_NAME, DisplaySetting.DISPLAY_ARGUMENT_BRACKETED), null);

        String usage = stringWriter.toString();
        printWriter.println(usage);
        printWriter.flush();

        String expectedUsage = "arg [<value1> [<value2> ...]]";
        Assert.assertEquals(expectedUsage, usage);
    }
}