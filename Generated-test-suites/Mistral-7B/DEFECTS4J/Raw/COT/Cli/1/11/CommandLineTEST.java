import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", false, "A test option"));
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption('a'));
        Assert.assertFalse(commandLine.hasOption("notAnOption"));
        Assert.assertFalse(commandLine.hasOption((char) 0));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", true, "A test option with value"));
        commandLine.parse("--optionA value");
        Assert.assertEquals("value", commandLine.getOptionValue("a"));
        Assert.assertEquals("value", commandLine.getOptionValue('a'));
        Assert.assertEquals(null, commandLine.getOptionValue("notAnOption"));
        Assert.assertEquals(null, commandLine.getOptionValue((char) 0));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("l", "list", false, "Display a list of options and exit"));
        commandLine.addOption(new Option("l", "limit", true, "Limit the size of the output"));
        commandLine.parse("--list --limit 10");
        String[] expectedValues = {"10"};
        Assert.assertArrayEquals(expectedValues, commandLine.getOptionValues("limit"));
        Assert.assertArrayEquals(new String[0], commandLine.getOptionValues("notAnOption"));
        Assert.assertArrayEquals(new String[0], commandLine.getOptionValues((char) 0));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("arg1 arg2 arg3");
        String[] args = {"arg1", "arg2", "arg3"};
        Assert.assertArrayEquals(args, commandLine.getArgs());
        Assert.assertEquals(Arrays.asList(args), commandLine.getArgList());
    }

    @Test(expected = AssertionError.class)
    public void testNegativeOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("v", "verbose", false, "Verbose output"));
        commandLine.parse("--verbose -5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("v", "verbose", false, "Verbose output"));
        commandLine.parse("--verbose null");
    }

    @Test
    public void testEmptyArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--optionA");
        Assert.assertEquals(Collections.emptyList(), commandLine.getArgList());
    }
}