package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseOption;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link Options} class.
 */
public class OptionsTest
{
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();

        Option input = OptionBuilder.withLongOpt("input")
                .withDescription("Input file for the application")
                .hasArg()
                .isRequired()
                .create("i");

        Option output = OptionBuilder.withLongOpt("output")
                .withDescription("Output file for the application")
                .hasArg()
                .create("o");

        Option groupOption = Option.builder("g")
                .longOpt("group")
                .withDescription("An option group")
                .create(true);

        OptionGroup group = new OptionGroup("group", "Group of options");
        group.addOption(groupOption);
        group.setRequired(true);

        options.addOptionGroup(group);
        options.addOption(input);
        options.addOption(output);
    }

    @Test
    public void testHasShortOption()
    {
        assertTrue(options.hasShortOption("i"));
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("g"));
    }

    @Test
    public void testHasLongOption()
    {
        assertTrue(options.hasLongOption("input"));
        assertTrue(options.hasLongOption("output"));
        assertTrue(options.hasLongOption("group"));
    }

    @Test
    public void testHasOption()
    {
        assertTrue(options.hasOption("input"));
        assertTrue(options.hasOption("output"));
        assertTrue(options.hasOption("group"));
    }

    @Test
    public void testGetOption()
    {
        Option input = options.getOption("input");
        Option output = options.getOption("output");
        Option group = options.getOption("group");

        assertSame(input, options.getOption("i"));
        assertSame(output, options.getOption("o"));
        assertNull(options.getOption("nonExistentOption"));
    }

    @Test
    public void testGetMatchingOptions()
    {
        List<String> matchingOptions = options.getMatchingOptions("gr");
        List<String> expectedOptions = Arrays.asList("group");

        assertEquals(expectedOptions, matchingOptions);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[] { "app" };
        parser.parse(options, args);
    }

    @Test
    public void testParseWithAllOptions() throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[] { "-i", "inputFile.txt", "-o", "outputFile.txt", "-g" };
        CommandLine cmd = parser.parse(options, args);

        Option input = cmd.getOption("input");
        Option output = cmd.getOption("output");
        Option group = cmd.getOption("group");

        assertEquals("inputFile.txt", input.getValue());
        assertEquals("outputFile.txt", output.getValue());
        assertTrue(group.isSet());
    }

    @Test
    public void testParseWithOnlyShortOption() throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[] { "-i" };
        CommandLine cmd = parser.parse(options, args);

        Option input = cmd.getOption("input");
        assertNull(input.getValue());
    }

    @Test
    public void testParseWithOnlyLongOption() throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[] { "--input" };
        CommandLine cmd = parser.parse(options, args);

        Option input = cmd.getOption("input");
        assertNull(input.getValue());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidShortOption() throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[] { "-x" };
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidLongOption() throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        String[] args = new String[] { "--x" };
        parser.parse(options, args);
    }
}