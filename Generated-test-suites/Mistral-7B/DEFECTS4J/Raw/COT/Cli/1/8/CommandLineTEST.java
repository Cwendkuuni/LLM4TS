import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", false, "Description for option A"));
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", true, "Description for option A"));
        commandLine.parse("--optionA value");
        Assert.assertEquals("value", commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("b"));
    }

    @Test(expected = AssertionError.class)
    public void testGetOptionValueWithNoValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", false, "Description for option A"));
        commandLine.parse("--optionA");
        Assert.assertNull(commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", true, "Description for option A"));
        commandLine.parse("--optionA value1 value2");
        String[] values = commandLine.getOptionValues("a");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test(expected = AssertionError.class)
    public void testGetOptionValuesWithNoValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", false, "Description for option A"));
        commandLine.parse("--optionA");
        String[] values = commandLine.getOptionValues("a");
        Assert.assertArrayEquals(new String[0], values);
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("arg1 arg2 arg3");
        String[] args = commandLine.getArgs();
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", false, "Description for option A"));
        commandLine.parse("--invalidOption");
    }

    @Test
    public void testParseWithEmptyArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("");
        Assert.assertEquals(0, commandLine.getArgList().size());
    }
}