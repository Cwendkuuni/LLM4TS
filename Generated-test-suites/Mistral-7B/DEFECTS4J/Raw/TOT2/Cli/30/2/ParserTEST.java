package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParser() throws ParseException
    {
        // Create test Options
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file name");
        options.addOption("o", "outputFile", true, "Output file name");
        options.addOption("h", "help", false, "Print help");

        // Create Parser instance
        CommandLineParser parser = new DefaultParser();

        // Test parsing with valid arguments
        String[] args = new String[]{"-i", "testInput.txt", "-o", "testOutput.txt"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals("testInput.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("testOutput.txt", cmd.getOptionValue("outputFile"));
        Assert.assertNull(cmd.getOptionValue("help"));

        // Test parsing with invalid arguments
        args = new String[]{"-i", "testInput.txt", "-o", "testOutput.txt", "--"};
        try
        {
            cmd = parser.parse(options, args);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Unrecognized option: --", e.getMessage());
        }

        // Test parsing with required options
        options.getOptionGroup(0).setRequired(true);
        args = new String[]{"--"};
        try
        {
            cmd = parser.parse(options, args);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            // Expected
        }

        // Test parsing with optional options
        args = new String[]{"-i", "testInput.txt", "-o", "testOutput.txt", "-h"};
        cmd = parser.parse(options, args);
        Assert.assertNull(cmd.getOptionValue("inputFile"));
        Assert.assertNull(cmd.getOptionValue("outputFile"));
        Assert.assertTrue(cmd.hasOption("help"));

        // Test parsing with properties
        Properties properties = new Properties();
        properties.setProperty("inputFile", "testInput.txt");
        properties.setProperty("outputFile", "testOutput.txt");
        cmd = parser.parse(options, new String[0], properties, false);
        Assert.assertEquals("testInput.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("testOutput.txt", cmd.getOptionValue("outputFile"));
        Assert.assertNull(cmd.getOptionValue("help"));

        // Test help
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("testParser", options);
    }
}