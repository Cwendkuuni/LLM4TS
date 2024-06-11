package org.apache.commons.cli;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");
        options.addOption("h", "help", false, "Print this help message.");
    }

    @Test
    public void testParseWithValidArguments() throws ParseException
    {
        String[] arguments = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
        parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        String[] arguments = new String[]{"-i", "/path/to/input.txt"};
        parser = new DefaultParser();
        parser.parse(options, arguments);
        Assert.fail("No MissingOptionException was thrown.");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException
    {
        String[] arguments = new String[]{"-i", "", "-o", "/path/to/output.txt"};
        parser = new DefaultParser();
        parser.parse(options, arguments);
        Assert.fail("No MissingArgumentException was thrown.");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException
    {
        String[] arguments = new String[]{"-x", "/path/to/input.txt", "-o", "/path/to/output.txt"};
        parser = new DefaultParser();
        parser.parse(options, arguments);
        Assert.fail("No UnrecognizedOptionException was thrown.");
    }
}