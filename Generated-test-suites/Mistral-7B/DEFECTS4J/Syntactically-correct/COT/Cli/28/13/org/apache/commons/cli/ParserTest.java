package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParse() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        options.addOption(option);

        String[] args = {"-o", "output.txt"};
        CommandLine cmd = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        }.parse(options, args, false);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("output.txt", cmd.getArgs().get(0));
        assertEquals("output file", cmd.getOptionValue("output"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @Test
    public void testParseWithProperties() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        options.addOption(option);

        Properties properties = new Properties();
        properties.put("output", "output.txt");

        String[] args = {"--output", "output.txt"};
        CommandLine cmd = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }

            @Override
            protected void processProperties(Properties properties)
            {
                super.processProperties(properties);
                Option opt = getOptions().getOption("output");
                opt.setValueForProcessing(properties.getProperty("output"));
            }
        }.parse(options, args, properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        options.addOption(option);

        String[] args = {"--input", "input.txt"};
        new Parser().parse(options, args, false);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file")
                .setRequired(true);
        options.addOption(option);

        String[] args = {};
        new Parser().parse(options, args, false);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        options.addOption(option);

        String[] args = {"--x", "value"};
        new Parser().parse(options, args, false);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOptionWithProperties() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        options.addOption(option);

        Properties properties = new Properties();
        properties.put("x", "value");

        String[] args = {"--x", "value"};
        new Parser() {
            @Override
            protected void processProperties(Properties properties)
            {
                try
                {
                    super.processProperties(properties);
                }
                catch (ParseException e)
                {
                    throw new ParseException("Failed to parse properties", e);
                }
            }
        }.parse(options, args, properties, false);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file");
        options.addOption(option);

        String[] args = {"--input", "input.txt", "--o", "output.txt"};
        CommandLine cmd = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        }.parse(options, args, true);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("input.txt", cmd.getArgs().get(0));

        assertEquals(1, cmd.getOptions().size());
        Option parsedOption = cmd.getOption("o");
        assertEquals("output file", parsedOption.getLongOpt());
        assertEquals("output.txt", parsedOption.getValueForProcessing());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParseWithStopAtNonOptionAndMissingOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "output file")
                .setRequired(true);
        options.addOption(option);

        String[] args = {"--input", "input.txt"};
        CommandLine cmd = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption)
            {
                return arguments;
            }
        }.parse(options, args, true);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("input.txt", cmd.getArgs().get(0));

        assertEquals(0, cmd.getOptions().size());

        try
        {
            assertEquals("output file", cmd.getOptionValue("output"));
            fail();
        }
        catch (MissingOptionException e)
        {
            // expected
        }
    }
}