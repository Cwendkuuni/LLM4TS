package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Options.
 */
public class OptionsTest
{
    @Test
    public void testSimpleOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "agent", false, "agent option");
        options.addOption("b", "boolValue", true, "boolean value option");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "--boolValue"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertNull(cmd.getOptionValue("a"));
        assertTrue(cmd.hasOptionValue("boolValue"));
        assertEquals("true", cmd.getOptionValue("boolValue"));
    }

    @Test
    public void testRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "agent", true, "agent option");

        CommandLineParser parser = new DefaultParser();
        String[] args = {};

        try
        {
            parser.parse(options, args);
            fail("ParseException expected");
        }
        catch (ParseException e)
        {
            // expected exception
        }

        args = new String[]{"-a"};

        try
        {
            parser.parse(options, args);
            fail("ParseException expected");
        }
        catch (ParseException e)
        {
            // expected exception
        }

        args = new String[]{"-a", "value"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOptionValue("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testOptionGroups() throws ParseException
    {
        Options options = new Options();

        OptionGroup groupOne = new OptionGroup("group1", "group one");
        groupOne.setRequired(true);
        groupOne.addOption("g1a", "group1a", true, "group1a option");

        options.addOptionGroup(groupOne);

        CommandLineParser parser = new DefaultParser();
        String[] args = {};

        try
        {
            parser.parse(options, args);
            fail("ParseException expected");
        }
        catch (ParseException e)
        {
            // expected exception
        }

        args = new String[]{"--g1a"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("g1a"));
        assertTrue(cmd.hasOptionValue("g1a"));
        assertEquals("true", cmd.getOptionValue("g1a"));
    }

    @Test
    public void testLongOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("longOption", true, "long option");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--longOption"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOption"));
        assertTrue(cmd.hasOptionValue("longOption"));
        assertEquals("true", cmd.getOptionValue("longOption"));
    }

    @Test
    public void testMixedOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "agent", false, "agent option");
        options.addOption("b", "boolValue", true, "boolean value option");
        options.addOption("c", "constant", false, "constant option");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "--boolValue=false", "--c"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertNull(cmd.getOptionValue("a"));
        assertFalse(cmd.hasOptionValue("boolValue"));
        assertFalse(cmd.hasOptionValue("c"));
    }
}