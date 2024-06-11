package org.junit.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("t", "test", false, "Test flag"));
        Assert.assertTrue(commandLine.hasOption("t"));
        Assert.assertTrue(commandLine.hasOption('t'));
        Assert.assertFalse(commandLine.hasOption("x"));
        Assert.assertFalse(commandLine.hasOption('x'));
    }

    @Test
    public void testHasOptionNullInput() {
        CommandLine commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption(null));
    }

    @Test
    public void testHasOptionEmptyCommandLine() {
        CommandLine commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption(""));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("i", "input", true, "Input file"));
        commandLine.addArg("input.txt");
        String[] values = commandLine.getOptionValues("i");
        Assert.assertArrayEquals(new String[]{"input.txt"}, values);
    }

    @Test
    public void testGetOptionValuesNullInput() {
        CommandLine commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionValues(null));
    }

    @Test
    public void testGetOptionValuesEmptyOption() {
        CommandLine commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionValues(""));
    }

    @Test
    public void testGetOptionValuesNoValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("i", "input", true, "Input file"));
        Assert.assertNull(commandLine.getOptionValues("i"));
    }

    // Add other test cases here

}