package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     * Tests basic functionality with simple options and arguments.
     */
    @Test
    public void testBasicFunctionality()
    {
        // create test command line options
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addOption("h", "help", false, "print help message");

        // create test command line arguments
        String[] args = {"--output", "output.txt", "-h", "--"};

        // create PosixParser instance and call flatten method
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        // check if parsed arguments match expected values
        Assert.assertArrayEquals(new String[]{"--output", "output.txt", "--", "-h"}, parsedArgs);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     * Tests handling of long options with arguments.
     */
    @Test
    public void testLongOptionWithArgument()
    {
        // create test command line options
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addOption("p", "prefix", true, "prefix for output file");

        // create test command line arguments
        String[] args = {"--output", "output.txt", "--prefix", "prefix_", "-h"};

        // create PosixParser instance and call flatten method
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        // check if parsed arguments match expected values
        Assert.assertArrayEquals(new String[]{"--output", "output.txt", "--prefix", "prefix_", "--"}, parsedArgs);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     * Tests handling of invalid long option.
     */
    @Test(expected = ParseException.class)
    public void testInvalidLongOption()
    {
        // create test command line options
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        // create test command line arguments
        String[] args = {"--invalid", "output.txt"};

        // create PosixParser instance and call flatten method
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     * Tests handling of an option without an argument.
     */
    @Test
    public void testOptionWithoutArgument()
    {
        // create test command line options
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addOption("h", "help", false, "print help message");

        // create test command line arguments
        String[] args = {"--output", "output.txt", "--help"};

        // create PosixParser instance and call flatten method
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        // check if parsed arguments match expected values
        Assert.assertArrayEquals(new String[]{"--output", "output.txt", "--help"}, parsedArgs);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     * Tests handling of an option with an optional argument.
     */
    @Test
    public void testOptionWithOptionalArgument()
    {
        // create test command line options
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addOption("p", "prefix", true, "prefix for output file");
        options.addOption("q", "quiet", false, "quiet mode");

        // create test command line arguments
        String[] args = {"--output", "output.txt", "--prefix", "prefix_", "--quiet"};

        // create PosixParser instance and call flatten method
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        // check if parsed arguments match expected values
        Assert.assertArrayEquals(new String[]{"--output", "output.txt", "--prefix", "prefix_", "--quiet"}, parsedArgs);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     * Tests handling of multiple invalid options.
     */
    @Test(expected = ParseException.class)
    public void testMultipleInvalidOptions()
    {
        // create test command line options
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        // create test command line arguments
        String[] args = {"--invalid1", "--invalid2", "output.txt"};

        // create PosixParser instance and call flatten method
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}