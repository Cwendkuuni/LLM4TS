package org.apache.commons.cli.test;

import org.apache.commons.cli.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    /**
     * Test method for constructor Parser().
     */
    @Test
    public void testParser()
    {
        Parser parser = new DefaultParser();
        assertNotNull(parser);
    }

    /**
     * Test method for abstract method flatten(Options, String[], boolean).
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testFlatten() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "--option", "value"};
        parser.parse(options, arguments, false);
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "--option", "value"};
        assertArrayEquals(expected, actual);
    }

    /**
     * Test method for method parse(Options, String[]).
     * @throws ParseException
     */
    @Test
    public void testParseOptions() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "--option=value"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("-a", cmd.getArgs().get(0));
        assertEquals("-b", cmd.getArgs().get(1));

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("option", option.getLongOpt());
        assertEquals("value", option.getValue());
    }

    /**
     * Test method for method parse(Options, String[], Properties).
     * @throws ParseException
     */
    @Test
    public void testParseOptionsWithProperties() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "--option=value"};
        Properties properties = new Properties();
        properties.setProperty("--option", "otherValue");

        CommandLine cmd = parser.parse(options, arguments, properties, false);

        assertEquals(0, cmd.getArgs().size());
        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("option", option.getLongOpt());
        assertEquals("otherValue", option.getValue());
    }

    /**
     * Test method for method parse(Options, String[], boolean).
     * @throws ParseException
     */
    @Test
    public void testParseOptionsWithStopAtNonOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "--option=value", "unknownOption", "arg"};

        CommandLine cmd = parser.parse(options, arguments, false);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("-a", cmd.getArgs().get(0));
        assertEquals("-b", cmd.getArgs().get(1));

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option option = optionsList.get(0);
        assertEquals("option", option.getLongOpt());
        assertEquals("value", option.getValue());

        assertEquals("unknownOption", cmd.getArgs().get(2));
        assertEquals("arg", cmd.getArgs().get(3));
    }

    /**
     * Test method for method parse(Options, String[], Properties, boolean).
     * @throws ParseException
     */
    @Test
    public void testParseOptionsWithPropertiesAndStopAtNonOption() throws ParseException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "--option=value", "unknownOption"};
        Properties properties = new Properties();
        properties.setProperty("--option", "otherValue");

        CommandLine cmd = parser.parse(options, arguments, properties, true);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("unknownOption", cmd.getArgs().get(0));

        List<Option> optionsList = cmd.getOptions();
        assertEquals(0, optionsList.size());
    }

    /**
     * Test method for method parse(Options, String[]).
     * @throws ParseException
     * @throws MissingOptionException
     * @throws MissingArgumentException
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException, MissingOptionException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a"};
        options.addOption("option", true, "o");

        Parser testParser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
            {
                return super.flatten(opts, arguments, stopAtNonOption);
            }

            @Override
            public CommandLine parse(Options options, String[] arguments) throws ParseException
            {
                checkRequiredOptions();
                return super.parse(options, arguments);
            }
        };

        try
        {
            testParser.parse(options, arguments);
            fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            // expected
        }
    }

    /**
     * Test method for method parse(Options, String[]).
     * @throws ParseException
     * @throws MissingOptionException
     * @throws MissingArgumentException
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseMissingRequiredArgument() throws ParseException, MissingOptionException, MissingArgumentException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("option2", true, "o2");
        String[] arguments = {"-a", "-o"};

        try
        {
            parser.parse(options, arguments);
            fail("MissingArgumentException expected");
        }
        catch (MissingArgumentException e)
        {
            // expected
        }
    }

    /**
     * Test method for method parse(Options, String[]).
     * @throws ParseException
     * @throws UnrecognizedOptionException
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException, UnrecognizedOptionException
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--unknownoption"};

        try
        {
            parser.parse(options, arguments);
            fail("UnrecognizedOptionException expected");
        }
        catch (UnrecognizedOptionException e)
        {
            // expected
        }
    }
}