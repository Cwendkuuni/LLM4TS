package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @org.junit.Test
    public void testParser_simple_usage() throws Exception
    {
        Parser parser = new DefaultParser();

        String[] args = {"-a", "1", "--b", "2"};
        Options options = new Options();
        options.addRequiredOption("a", true, "a help message");
        options.addOption("b", true, "b help message");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParser_missing_required_option() throws Exception
    {
        Parser parser = new DefaultParser();

        String[] args = {"--c", "3"};
        Options options = new Options();
        options.addRequiredOption("a", true, "a help message");
        options.addOption("b", true, "b help message");

        parser.setOptions(options);

        parser.parse(options, args, false);
        fail("Expected ParseException");
    }

    @org.junit.Test(expected = MissingOptionException.class)
    public void testParser_missing_required_options() throws Exception
    {
        Parser parser = new DefaultParser();

        String[] args = {"--a"};
        Options options = new Options();
        options.addRequiredOption("a", true, "a help message");
        options.addRequiredOption("b", true, "b help message");

        parser.setOptions(options);

        try
        {
            parser.parse(options, args, false);
        }
        catch (ParseException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertEquals(2, missingOptions.size());
            assertTrue(missingOptions.containsAll(Arrays.asList("b", "a")));
            throw e;
        }
    }

    @org.junit.Test(expected = UnrecognizedOptionException.class)
    public void testParser_unrecognized_option() throws Exception
    {
        Parser parser = new DefaultParser();

        String[] args = {"--x", "3"};
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("b", true, "b help message");

        parser.setOptions(options);

        parser.parse(options, args, false);
        fail("Expected UnrecognizedOptionException");
    }

    @org.junit.Test(expected = MissingArgumentException.class)
    public void testParser_missing_option_argument() throws Exception
    {
        Parser parser = new DefaultParser();

        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "a help message").hasArg(true);

        parser.setOptions(options);

        parser.parse(options, args, false);
        fail("Expected MissingArgumentException");
    }
}