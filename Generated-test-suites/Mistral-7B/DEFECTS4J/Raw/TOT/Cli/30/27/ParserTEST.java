package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ParserTest
{
    @Test
    public void testParseWithNoOptionsAndNoArguments() throws ParseException
    {
        Options options = new Options();
        String[] args = new String[0];
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testParseWithOptionalOption() throws ParseException
    {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Some option");
        options.addOption(opt);
        String[] args = new String[]{"-o", "value"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
        assertEquals(1, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertEquals("value", cmd.getOptionValues("o")[0]);
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException
    {
        Options options = new Options();
        Option opt = new Option("r", "requiredOption", true, "Some required option");
        opt.isRequired = true;
        options.addOption(opt);
        String[] args = new String[0];
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args, false);
            fail("MissingRequiredOptionException expected");
        }
        catch (MissingOptionException e)
        {
            String[] missingOptions = e.getMissingOptions();
            assertEquals(1, missingOptions.length);
            assertEquals("requiredOption", missingOptions[0]);
        }

        args = new String[]{"-r"};
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args, false);
            fail("MissingArgumentException expected");
        }
        catch (MissingArgumentException e)
        {
            assertEquals("requiredOption", e.getOption().getLongOpt());
        }

        args = new String[]{"-r", "value"};
        CommandLine cmd = parser.parse(options, args, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertEquals("value", cmd.getOptionValues("r")[0]);
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group");
        group.setRequired(true);
        Option opt1 = new Option("o1", "Option 1");
        Option opt2 = new Option("o2", "Option 2");
        group.addOption(opt1);
        group.addOption(opt2);
        options.addOptionGroup(group);
        String[] args = new String[0];
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args, false);
            fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            String[] missingOptions = e.getMissingOptions();
            assertEquals(2, missingOptions.length);
            assertArrayEquals(new String[]{"o1", "o2"}, missingOptions);
        }

        args = new String[]{"--g"};
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args, false);
            fail("MissingOptionException expected");
        }
        catch (MissingOptionException e)
        {
            String[] missingOptions = e.getMissingOptions();
            assertEquals(1, missingOptions.length);
            assertEquals("g", missingOptions[0]);
        }

        args = new String[]{"--g", "o1"};
        CommandLine cmd = parser.parse(options, args, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(2, cmd.getOptions().size());
        assertEquals(1, cmd.getOptions(group.getName()).size());
        assertEquals("o1", cmd.getOptionValues("o1")[0]);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Options options = new Options();
        Option opt = new Option("p", "property", true, "Some property");
        options.addOption(opt);
        Properties properties = new Properties();
        properties.setProperty("p", "value");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, properties, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals(1, cmd.getOptions().size());
        assertEquals("value", cmd.getOptionValues("p")[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        String[] args = new String[]{"--unrecognized"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
        fail("UnrecognizedOptionException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseWithNonOption() throws ParseException
    {
        Options options = new Options();
        String[] args = new String[]{"--nonOption"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
        fail("MissingOptionException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseWithExtraArguments() throws ParseException
    {
        Options options = new Options();
        String[] args = new String[]{"--option", "value1", "--option", "value2", "extraArg1", "extraArg2"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionValue()
            throws ParseException
    {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Some option");
        options.addOption(opt);
        String[] args = new String[]{"-o", "invalidValue"};
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
        fail("InvalidOptionValueException expected");
    }
}