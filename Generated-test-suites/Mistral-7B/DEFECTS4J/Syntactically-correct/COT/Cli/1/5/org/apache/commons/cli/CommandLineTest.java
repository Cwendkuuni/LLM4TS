import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "arg", true, "argument for option a"));
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption('a'));
        Assert.assertFalse(commandLine.hasOption("b"));
        Assert.assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "arg", true, "argument for option a"));
        commandLine.parse("--a value");
        Assert.assertEquals("value", commandLine.getOptionValue("a"));
        Assert.assertEquals("value", commandLine.getOptionValue('a'));
        Assert.assertNull(commandLine.getOptionValue("b"));
        Assert.assertNull(commandLine.getOptionValue('b'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", false, "Specify arg1 for option a"));
        commandLine.addOption(new Option("b", true, "Specify arg2 for option b"));
        commandLine.parse("--a --b value1 value2");
        String[] expected = {"value1", "value2"};
        Assert.assertArrayEquals(expected, commandLine.getOptionValues("b"));
        Assert.assertArrayEquals(expected, commandLine.getOptionValues('b'));
        Assert.assertNull(commandLine.getOptionValues("c"));
        Assert.assertNull(commandLine.getOptionValues('c'));
    }

    @Test(expected = AssertionError.class)
    public void testGetOptionValueWithUnrecognizedOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--unrecognized-option arg");
        String optionValue = commandLine.getOptionValue("unrecognized-option");
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("arg1 arg2 --option arg3");
        String[] expected = {"arg1", "arg2"};
        Assert.assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("arg1 arg2 --option arg3");
        List expected = new LinkedList();
        expected.add("arg1");
        expected.add("arg2");
        Assert.assertEquals(expected, commandLine.getArgList());
    }
}