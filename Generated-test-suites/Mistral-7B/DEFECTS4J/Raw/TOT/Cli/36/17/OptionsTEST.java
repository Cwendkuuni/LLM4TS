package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the Options class.
 */
public class OptionsTest
{
    /**
     * Test case for adding a single option without argument.
     */
    @Test
    public void testAddOptionWithoutArgument()
    {
        Options options = new Options();
        options.addOption("a", "alert", false, "Display an alert message.");

        // parse an empty command line
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(options.toArray(), new String[0]);
        }
        catch (ParseException e)
        {
            // expected as no command line arguments were provided
            Assert.assertNull(cmd);
        }

        Assert.assertTrue(options.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("a"));

        // parse a command line with the option
        String[] args = new String[]{"--alert"};
        try
        {
            cmd = parser.parse(options.toArray(), args);
        }
        catch (ParseException e)
        {
            // should not throw an exception as the option is valid
            Assert.fail("Failed to parse command line with valid option: " + e.getMessage());
        }

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b")); // no such option

        // test the help message
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("test", options.toArray(), true);
        Assert.assertNotNull(help);
        Assert.assertTrue(help.contains("Display an alert message."));
    }

    /**
     * Test case for adding a single option with an argument.
     */
    @Test
    public void testAddOptionWithArgument()
    {
        Options options = new Options();
        options.addOption("b", "base", true, "Base directory for the project.");

        // parse an empty command line
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(options.toArray(), new String[0]);
        }
        catch (ParseException e)
        {
            // expected as no command line arguments were provided
            Assert.assertNull(cmd);
        }

        Assert.assertFalse(options.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));

        // parse a command line with an optional argument
        String[] args1 = new String[]{"--base", "dir1"};
        try
        {
            cmd = parser.parse(options.toArray(), args1);
        }
        catch (ParseException e)
        {
            // should not throw an exception as the option is valid
            Assert.fail("Failed to parse command line with valid option: " + e.getMessage());
        }

        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("dir1", cmd.getOptionValue("b"));

        // parse a command line without the argument
        String[] args2 = new String[]{"--base"};
        try
        {
            cmd = parser.parse(options.toArray(), args2);
        }
        catch (ParseException e)
        {
            // should not throw an exception as the option is valid, but no value was provided
            Assert.fail("Failed to parse command line with valid option: " + e.getMessage());
        }

        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));

        // test the help message
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("test", options.toArray(), true);
        Assert.assertNotNull(help);
        Assert.assertTrue(help.contains("Base directory for the project."));
    }

    /**
     * Test case for adding an option group.
     */
    @Test
    public void testAddOptionGroup()
    {
        Options options = new Options();

        OptionGroup group = new OptionGroup("inputOptions", "Input Options:");
        group.addOption(new Option("i", "inputFile", true, "Input file name."));
        group.addOption(new Option("i", "inputStream", false, "Input stream."));

        options.addOptionGroup(group);

        // parse an empty command line
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(options.toArray(), new String[0]);
        }
        catch (ParseException e)
        {
            // expected as no command line arguments were provided
            Assert.assertNull(cmd);
        }

        Assert.assertFalse(options.hasOption("i"));
        Assert.assertNull(cmd.getOptionValue("i"));

        // parse a command line with an input file
        String[] args1 = new String[]{"--inputFile", "file1.txt"};
        try
        {
            cmd = parser.parse(options.toArray(), args1);
        }
        catch (ParseException e)
        {
            // should not throw an exception as the option is valid
            Assert.fail("Failed to parse command line with valid option: " + e.getMessage());
        }

        Assert.assertTrue(cmd.hasOption("i"));
        Assert.assertEquals("file1.txt", cmd.getOptionValue("i"));
        Assert.assertFalse(cmd.hasOption("is")); // no such option

        // parse a command line with an input stream
        String[] args2 = new String[]{"--inputStream", "file:test.txt"};
        try
        {
            cmd = parser.parse(options.toArray(), args2);
        }
        catch (ParseException e)
        {
            // should not throw an exception as the option is valid
            Assert.fail("Failed to parse command line with valid option: " + e.getMessage());
        }

        Assert.assertTrue(cmd.hasOption("is"));
        Assert.assertNull(cmd.getOptionValue("i"));

        // test the help message
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("test", options.toArray(), true);
        Assert.assertNotNull(help);

        // check that the input options group is included in the help message
        Assert.assertTrue(help.contains("Input Options:"));
        Assert.assertTrue(help.contains("Input file name."));
        Assert.assertTrue(help.contains("Input stream."));
    }
}