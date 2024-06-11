package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link Parser}.
 */
public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addRequiredOption(new Option("a", true, "a")
                .longOpt("arg")
                .valueSeparator('=')
                .requiredArg()
                .desc("some argument"));
        options.addOption(new Option("h", false, "h")
                .longOpt("help")
                .desc("print help"));
        parser = new DefaultParser();
    }

    /**
     * Test parsing with all required options present.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testParseAllOptions() throws ParseException
    {
        String[] args = {"-a", "arg1", "--arg=arg2"};
        CommandLine commandLine = parser.parse(options, args);
        Assert.assertEquals("arg1", commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("h"));
    }

    /**
     * Test parsing with missing required option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        String[] args = {"--arg=arg2"};
        parser.parse(options, args);
    }

    /**
     * Test parsing with invalid option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws ParseException
    {
        String[] args = {"-x"};
        parser.parse(options, args);
    }

    /**
     * Test parsing with help option.
     *
     * @throws ParseException if parsing fails.
     */
    @Test
    public void testParseHelpOption() throws ParseException
    {
        String[] args = {"-h"};
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }
}