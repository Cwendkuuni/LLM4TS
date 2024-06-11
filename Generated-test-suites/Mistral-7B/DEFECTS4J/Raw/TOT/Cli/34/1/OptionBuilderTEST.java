package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Tests the creation of a required Option with no arguments.
     */
    @Test
    public void testRequiredOptionNoArgs()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                                     .isRequired()
                                     .create();

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1", "arg1"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1", "arg2"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--no-option"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1"});
            assertTrue(cmd.hasOption("option1"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Tests the creation of a required Option with one argument.
     */
    @Test
    public void testRequiredOptionOneArg()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                                     .isRequired()
                                     .hasArg()
                                     .withArgName("arg")
                                     .create();

        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1", "arg1"});
            assertTrue(cmd.hasOption("option1"));
            assertEquals("arg1", cmd.getOptionValue("option1"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1", "arg2"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Tests the creation of an Optional Option with one argument.
     */
    @Test
    public void testOptionalOptionOneArg()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                                     .hasArg()
                                     .withArgName("arg")
                                     .create();

        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1", "arg1"});
            assertTrue(cmd.hasOption("option1"));
            assertEquals("arg1", cmd.getOptionValue("option1"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1"});
            assertFalse(cmd.hasOption("option1"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1", "arg2"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Tests the creation of an Optional Option with multiple arguments.
     */
    @Test
    public void testOptionalOptionMultipleArgs()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                                     .hasArgs(2)
                                     .withArgName("arg1 arg2")
                                     .create();

        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1", "arg1", "arg2"});
            assertTrue(cmd.hasOption("option1"));
            String[] args = cmd.getOptionValues("option1");
            assertEquals(2, args.length);
            assertEquals("arg1", args[0]);
            assertEquals("arg2", args[1]);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1"});
            assertFalse(cmd.hasOption("option1"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1", "arg2"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1,arg2"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Tests the creation of an Option with a value separator.
     */
    @Test
    public void testOptionWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                                     .hasArg()
                                     .withArgName("arg")
                                     .withValueSeparator('=')
                                     .create();

        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1"});
            assertTrue(cmd.hasOption("option1"));
            assertEquals("arg1", cmd.getOptionValue("option1"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1=arg1,arg2"});
            assertTrue(cmd.hasOption("option1"));
            String[] args = cmd.getOptionValues("option1");
            assertEquals(2, args.length);
            assertEquals("arg1", args[0]);
            assertEquals("arg2", args[1]);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--option1", "arg1", "arg2"});
            fail("Expected an IllegalArgumentException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    /**
     * Tests the creation of an Option with a long option name that has a '-' character.
     */
    @Test
    public void testOptionWithLongNameWithDash()
    {
        Option option = OptionBuilder.withLongOpt("long-option")
                                     .create();

        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(new String[]{"--long-option"});
            assertTrue(cmd.hasOption("long-option"));
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }
}