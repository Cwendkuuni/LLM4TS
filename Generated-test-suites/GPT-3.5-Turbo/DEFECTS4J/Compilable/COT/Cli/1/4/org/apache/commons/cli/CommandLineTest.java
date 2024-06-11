package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    private CommandLine commandLine;

    @Test
    public void testHasOption() {
        // Test case for hasOption(String opt)
        commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption("a"));

        // Test case for hasOption(char opt)
        Assert.assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject() {
        // Test case for getOptionObject(String opt)
        commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionObject("a"));

        // Test case for getOptionObject(char opt)
        Assert.assertNull(commandLine.getOptionObject('b'));
    }

    @Test
    public void testGetOptionValue() {
        // Test case for getOptionValue(String opt)
        commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionValue("a"));

        // Test case for getOptionValue(char opt)
        Assert.assertNull(commandLine.getOptionValue('b'));

        // Test case for getOptionValues(String opt)
        Assert.assertNull(commandLine.getOptionValues("c"));

        // Test case for getOptionValues(char opt)
        Assert.assertNull(commandLine.getOptionValues('d'));
    }

    @Test
    public void testGetArgs() {
        // Test case for getArgs()
        commandLine = new CommandLine();
        String[] args = commandLine.getArgs();
        Assert.assertNotNull(args);
        Assert.assertEquals(0, args.length);
    }

    @Test
    public void testGetArgList() {
        // Test case for getArgList()
        commandLine = new CommandLine();
        Assert.assertNotNull(commandLine.getArgList());
        Assert.assertEquals(0, commandLine.getArgList().size());
    }
}