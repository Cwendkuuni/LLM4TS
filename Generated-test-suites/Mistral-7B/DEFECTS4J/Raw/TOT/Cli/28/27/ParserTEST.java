package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Tests for the {@link Parser} class.
 */
public class ParserTest
{
    private Parser parser;
    private HelpFormatter formatter;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
        formatter = new HelpFormatter();
    }

    /**
     * Tests the parsing of a simple command line with no options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testNoOptions() throws ParseException
    {
        String[] args = new String[0];

        CommandLine cmd = parser.parse(new Options(), args, null, false);
        assertNull(cmd.getArgs());
    }

    /**
     * Tests the parsing of a simple command line with a single option.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testSingleOption() throws ParseException
    {
        String[] args = new String[]{"--option"};

        CommandLine cmd = parser.parse(new Options(), args, null, false);
        assertEquals("option", cmd.getOptionNames().get(0));
        assertNull(cmd.getArgs());
    }

    /**
     * Tests the parsing of a simple command line with a single option and a value.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testSingleOptionWithValue() throws ParseException
    {
        String[] args = new String[]{"--option", "value"};

        Options options = new Options();
        Option option = new Option("o", "option")
                .hasArg(true)
                .valueSeparator('=')
                .required(false);
        options.addOption(option);

        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals("value", cmd.getOptionValues("option")[0]);
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    /**
     * Tests the parsing of a command line with multiple options and values.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testMultipleOptions() throws ParseException
    {
        String[] args = new String[]{"--option1", "value1", "--option2", "value2"};

        Options options = new Options();
        Option option1 = new Option("o1", "option1")
                .hasArg(true)
                .valueSeparator('=')
                .required(false);
        Option option2 = new Option("o2", "option2")
                .hasArg(true)
                .valueSeparator('=')
                .required(false);
        options.addOption(option1);
        options.addOption(option2);

        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals("value1", cmd.getOptionValues("option1")[0]);
        assertEquals("value2", cmd.getOptionValues("option2")[0]);
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    /**
     * Tests the parsing of a command line with optional options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testOptionalOptions() throws ParseException
    {
        String[] args = new String[]{"--option"};

        Options options = new Options();
        Option option = new Option("o", "option")
                .hasArg(false)
                .required(false);
        options.addOption(option);

        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("option"));
        assertArrayEquals(new String[0], cmd.getArgs());

        args = new String[]{"--option", "value"};
        cmd = parser.parse(options, args, null, false);
        assertEquals("value", cmd.getOptionValues("option")[0]);
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    /**
     * Tests the parsing of a command line with required options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException
    {
        String[] args = new String[0];

        Options options = new Options();
        Option option = new Option("o", "option")
                .hasArg(false)
                .required(true);
        options.addOption(option);

        parser.parse(options, args, null, false);
    }

    /**
     * Tests the parsing of a command line with options in an option group.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testOptionsInGroup() throws ParseException
    {
        String[] args = new String[]{"--group", "--option1"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group")
                .required(false)
                .exclusive(false);
        Option option1 = new Option("o1", "option1")
                .hasArg(false)
                .required(false);
        Option option2 = new Option("o2", "option2")
                .hasArg(false)
                .required(false);
        group.addOption(option1);
        group.addOption(option2);
        options.addOption(group);

        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("group"));
        assertTrue(cmd.hasOption("option1"));
        assertFalse(cmd.hasOption("option2"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    /**
     * Tests the parsing of a command line with long option names.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testLongOptionNames() throws ParseException
    {
        String[] args = new String[]{"--this-is-a-really-long-option-name"};

        Options options = new Options();
        Option option = new Option("l", "this-is-a-really-long-option-name")
                .hasArg(false)
                .required(false);
        options.addOption(option);

        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("this-is-a-really-long-option-name"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    /**
     * Tests the parsing of a command line with a mixed set of short and long options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testMixedOptions() throws ParseException
    {
        String[] args = new String[]{"-o", "--option", "--long-option"};

        Options options = new Options();
        Option option = new Option("o", "option")
                .hasArg(false)
                .required(false);
        Option longOption = new Option("l", "long-option")
                .hasArg(false)
                .required(false);
        options.addOption(option);
        options.addOption(longOption);

        CommandLine cmd = parser.parse(options, args, null, false);
        assertTrue(cmd.hasOption("option"));
        assertTrue(cmd.hasOption("long-option"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    /**
     * Tests the parsing of a command line with a combination of options and arguments.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testOptionsAndArguments() throws ParseException
    {
        String[] args = new String[]{"--option", "arg1", "arg2"};

        Options options = new Options();
        Option option = new Option("o", "option")
                .hasArg(true)
                .valueSeparator(' ')
                .required(false);
        options.addOption(option);

        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals("arg1 arg2", cmd.getOptionValues("option")[0]);
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    /**
     * Tests the parsing of a command line with unrecognized options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOptions() throws ParseException
    {
        String[] args = new String[]{"--unknown-option"};

        Options options = new Options();
        options.addOption(new Option("o", "option")
                .hasArg(false)
                .required(false));

        parser.parse(options, args, null, false);
    }

    /**
     * Tests the help formatting using the default help formatter.
     */
    @Test
    public void testHelpFormatDefault()
    {
        String[] args = new String[0];

        Options options = new Options()
                .addOption(new Option("o", "option")
                        .hasArg(false)
                        .required(false));

        CommandLine cmd = parser.parse(options, args, null, true);

        formatter.printHelp("ParserTest", cmd.getOptions(), true);
        String help = formatter.getHelpMessage();

        assertTrue(help.contains("--option"));
        assertTrue(help.contains("(no value)"));
    }

    /**
     * Tests the help formatting using a custom help formatter.
     */
    @Test
    public void testHelpFormatCustom()
    {
        String[] args = new String[0];

        Options options = new Options()
                .addOption(new Option("o", "option")
                        .hasArg(false)
                        .required(false));

        CommandLine cmd = parser.parse(options, args, null, true);

        HelpFormatter formatter = new HelpFormatter()
        {
            @Override
            public String getHelpMessage()
            {
                return "ParserTest:\n\n" + super.getHelpMessage();
            }
        };

        formatter.printHelp("ParserTest", cmd.getOptions(), true);
        String help = formatter.getHelpMessage();

        assertTrue(help.contains("ParserTest:"));
        assertTrue(help.contains("--option"));
        assertTrue(help.contains("(no value)"));
    }
}