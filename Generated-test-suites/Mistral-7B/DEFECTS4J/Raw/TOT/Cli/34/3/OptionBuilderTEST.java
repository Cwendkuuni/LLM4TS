package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest
{
    @Test
    public void testOptionalOption()
    {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .create();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--option", "value"});

        assertEquals("value", cmd.getOptionValue("option"));
        assertNull(cmd.getOptionValue("nonexistent"));

        option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .isRequired()
                .create();

        try
        {
            parser.parse(new String[]{"--option"});
            assert false;
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            parser.parse(new String[]{"--option", "value", "--nonexistent"});
            assert false;
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testMultipleOptionalOptions()
    {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArgs()
                .isRequired()
                .create();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try
        {
            cmd = parser.parse(new String[]{"--option", "value1", "--option", "value2"});
            assertTrue(cmd.getOptions().size() == 1);
            assertEquals("value1 value2", cmd.getOptionValue("option"));
        }
        catch (ParseException e)
        {
            // expected, multiple options with the same name
        }

        try
        {
            cmd = parser.parse(new String[]{"--option", "value1", "--option", "value2", "--option", "value3"});
            assertTrue(cmd.getOptions().size() == 1);
            assertEquals("value1 value2 value3", cmd.getOptionValue("option"));
        }
        catch (ParseException e)
        {
            // expected, multiple options with the same name
        }

        option = OptionBuilder.withLongOpt("option")
                .hasArgs(2)
                .isRequired()
                .create();

        try
        {
            cmd = parser.parse(new String[]{"--option", "value1", "--option", "value2", "--option", "value3"});
            assertTrue(cmd.getOptions().size() == 1);
            assertFalse(cmd.getOptionValues("option").length == 1);
            assertEquals("value1", cmd.getOptionValues("option")[0]);
            assertEquals("value2 value3", cmd.getOptionValue("option"));
        }
        catch (ParseException e)
        {
            // expected, multiple options with the same name
        }
    }

    @Test
    public void testEmptyOption()
    {
        Option option = OptionBuilder.withLongOpt("option")
                .create();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--option"});

        assertNull(cmd.getOptionValue("option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.create();
    }
}