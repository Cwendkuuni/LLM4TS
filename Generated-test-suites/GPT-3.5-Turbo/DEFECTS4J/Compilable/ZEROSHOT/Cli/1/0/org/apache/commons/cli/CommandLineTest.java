package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOptionWithShortName() {
        commandLine.addOption(new Option("a", "apple", false, "Apple option"));
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testHasOptionWithLongName() {
        commandLine.addOption(new Option("a", "apple", false, "Apple option"));
        Assert.assertTrue(commandLine.hasOption("apple"));
        Assert.assertFalse(commandLine.hasOption("banana"));
    }

    @Test
    public void testHasOptionWithCharName() {
        commandLine.addOption(new Option("a", "apple", false, "Apple option"));
        Assert.assertTrue(commandLine.hasOption('a'));
        Assert.assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObjectWithStringOpt() {
        Option opt = new Option("f", "file", true, "File option");
        opt.setType(Integer.class);
        commandLine.addOption(opt);
        commandLine.addOption(new Option("n", false, "No file option"));
        Assert.assertEquals(10, commandLine.getOptionObject("file"));
        Assert.assertNull(commandLine.getOptionObject("nofile"));
    }

    @Test
    public void testGetOptionObjectWithCharOpt() {
        Option opt = new Option("f", "file", true, "File option");
        opt.setType(Float.class);
        commandLine.addOption(opt);
        commandLine.addOption(new Option("n", false, "No file option"));
        Assert.assertEquals(10.5f, commandLine.getOptionObject('f'));
        Assert.assertNull(commandLine.getOptionObject('n'));
    }

    @Test
    public void testGetOptionValueWithStringOpt() {
        Option opt = new Option("f", "file", true, "File option");
        commandLine.addOption(opt);
        commandLine.addOption(new Option("n", false, "No file option"));
        Assert.assertEquals("example.txt", commandLine.getOptionValue("file"));
        Assert.assertNull(commandLine.getOptionValue("nofile"));
    }

    @Test
    public void testGetOptionValueWithCharOpt() {
        Option opt = new Option("f", "file", true, "File option");
        commandLine.addOption(opt);
        commandLine.addOption(new Option("n", false, "No file option"));
        Assert.assertEquals("example.txt", commandLine.getOptionValue('f'));
        Assert.assertNull(commandLine.getOptionValue('n'));
    }

    @Test
    public void testGetOptionValuesWithStringOpt() {
        Option opt = new Option("d", "detail", true, "Detail option");
        commandLine.addOption(opt);
        commandLine.addOption(new Option("n", false, "No detail option"));
        String[] expected = {"one", "two", "three"};
        Assert.assertArrayEquals(expected, commandLine.getOptionValues("detail"));
        Assert.assertNull(commandLine.getOptionValues("nodetail"));
    }

    @Test
    public void testGetOptionValuesWithCharOpt() {
        Option opt = new Option("d", "detail", true, "Detail option");
        commandLine.addOption(opt);
        commandLine.addOption(new Option("n", false, "No detail option"));
        String[] expected = {"one", "two", "three"};
        Assert.assertArrayEquals(expected, commandLine.getOptionValues('d'));
        Assert.assertNull(commandLine.getOptionValues('n'));
    }

    @Test
    public void testGetOptionValueWithDefaultStringOpt() {
        Option opt = new Option("f", "file", true, "File option");
        commandLine.addOption(opt);
        commandLine.addOption(new Option("n", false, "No file option"));
        Assert.assertEquals("example.txt", commandLine.getOptionValue("file", "default.txt"));
        Assert.assertEquals("default.txt", commandLine.getOptionValue("nofile", "default.txt"));
    }

    @Test
    public void testGetOptionValueWithDefaultCharOpt() {
        Option opt = new Option("f", "file", true, "File option");
        commandLine.addOption(opt);
        commandLine.addOption(new Option("n", false, "No file option"));
        Assert.assertEquals("example.txt", commandLine.getOptionValue('f', "default.txt"));
        Assert.assertEquals("default.txt", commandLine.getOptionValue('n', "default.txt"));
    }

    @Test
    public void testGetArgs() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.addArg("arg3");
        String[] expected = {"arg1", "arg2", "arg3"};
        Assert.assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.addArg("arg3");
        String[] expected = {"arg1", "arg2", "arg3"};
        Assert.assertEquals(expected.length, commandLine.getArgList().size());
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], commandLine.getArgList().get(i));
        }
    }

    @Test
    public void testIterator() {
        Option opt1 = new Option("a", "apple", false, "Apple option");
        Option opt2 = new Option("b", "banana", false, "Banana option");
        commandLine.addOption(opt1);
        commandLine.addOption(opt2);
        Assert.assertEquals(2, commandLine.iterator().next());
    }

    @Test
    public void testGetOptions() {
        Option opt1 = new Option("a", "apple", false, "Apple option");
        Option opt2 = new Option("b", "banana", false, "Banana option");
        commandLine.addOption(opt1);
        commandLine.addOption(opt2);
        Assert.assertEquals(2, commandLine.getOptions().length);
    }
}