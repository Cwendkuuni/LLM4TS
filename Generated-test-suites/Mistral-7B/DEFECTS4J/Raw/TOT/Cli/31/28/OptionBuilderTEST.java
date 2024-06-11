package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.util.Arrays;

public class OptionBuilderTest
{
    @org.junit.Test
    public void testOptionWithSingleArgument()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("inputFile")
                                .withDescription("input file to read from")
                                .hasArg()
                                .withArgName("FILE")
                                .isRequired();

        Option option = optionBuilder.create('i');

        assertEquals("inputFile", option.getLongOpt());
        assertEquals("input file to read from", option.getDescription());
        assertFalse(option.hasValue());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-i", "/path/to/file.txt"});
        assertEquals("/path/to/file.txt", option.getValue());

        try
        {
            parser.parse(new String[]{"-i"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            parser.parse(new String[]{"-i", "/path/to/file.txt", "invalid"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    @org.junit.Test
    public void testOptionWithMultipleArguments()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("inputFiles")
                                .withDescription("input files to read from")
                                .hasArgs()
                                .withArgName("FILE")
                                .isRequired();

        Option option = optionBuilder.create('I');

        assertEquals("inputFiles", option.getLongOpt());
        assertEquals("input files to read from", option.getDescription());
        assertTrue(option.getArgs().length > 0);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-I", "/path/to/file1.txt", "/path/to/file2.txt"});
        assertEquals(Arrays.asList("/path/to/file1.txt", "/path/to/file2.txt"), option.getValues());

        try
        {
            parser.parse(new String[]{"-I"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            parser.parse(new String[]{"-I", "/path/to/file1.txt"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }

    @org.junit.Test
    public void testOptionWithOptionalArgument()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("outputDirectory")
                                .withDescription("output directory to write to")
                                .hasOptionalArg()
                                .withArgName("DIR");

        Option option = optionBuilder.create('o');

        assertEquals("outputDirectory", option.getLongOpt());
        assertEquals("output directory to write to", option.getDescription());
        assertNull(option.getValue());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-o"});
        assertNull(option.getValue());

        cmd = parser.parse(new String[]{"-o", "/path/to/output"});
        assertEquals("/path/to/output", option.getValue());

        cmd = parser.parse(new String[]{"-o", "/path/to/output", "invalid"});
        assertEquals("/path/to/output", option.getValue());
    }

    @org.junit.Test
    public void testOptionWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("delimiters")
                                .withDescription("delimiters to use")
                                .hasArgs()
                                .withArgName("DELIMITER")
                                .withValueSeparator(',');

        Option option = optionBuilder.create('d');

        assertEquals("delimiters", option.getLongOpt());
        assertEquals("delimiters to use", option.getDescription());
        assertTrue(option.getArgs().length > 0);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-d", "comma,tab,semicolon"});
        assertEquals(Arrays.asList("comma", "tab", "semicolon"), option.getValues());

        try
        {
            parser.parse(new String[]{"-d"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            parser.parse(new String[]{"-d", "comma,tab"});
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }
    }
}