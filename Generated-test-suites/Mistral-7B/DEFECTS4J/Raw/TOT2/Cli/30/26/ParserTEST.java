package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        // create test options
        Options options = new Options();
        options.addOption("a", true, "An option that is set to true when present");

        // create parser instance
        Parser parser = new DefaultParser();

        // create test arguments
        String[] args = new String[]{"--a"};

        // parse arguments
        CommandLine cmd = parser.parse(options, args, true);

        // assert that the option is present and set to true
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a"));
    }

    @Test
    public void testOptionWithValue() throws ParseException
    {
        // create test options
        Options options = new Options();
        options.addOption("a", true, "An option that is set to true when present");
        options.addOption("b", false, "An optional option with a value");

        // create parser instance
        Parser parser = new DefaultParser();

        // create test arguments
        String[] args = new String[]{"--a", "--b=test"};

        // parse arguments
        CommandLine cmd = parser.parse(options, args, true);

        // assert that the options are present and set to the expected values
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("test", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        // create test options
        Options options = new Options();
        options.addOption("a", true, "An option that is set to true when present");

        // create parser instance
        Parser parser = new DefaultParser();

        // create test arguments
        String[] args = new String[]{"--b"};

        // parse arguments
        parser.parse(options, args, true);

        // the test should fail with a ParseException
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        // create test options
        Options options = new Options();
        options.addOption("a", true, "An option that is required");

        // create parser instance
        Parser parser = new DefaultParser();

        // create test arguments
        String[] args = new String[]{};

        // parse arguments
        try
        {
            parser.parse(options, args, true);
            fail("Expected a ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testMultipleValues() throws ParseException
    {
        // create test options
        Options options = new Options();
        options.addOption("a", true, "An option that can appear multiple times");

        // create parser instance
        Parser parser = new DefaultParser();

        // create test arguments
        String[] args = new String[]{"--a", "--a", "--a"};

        // parse arguments
        CommandLine cmd = parser.parse(options, args, true);

        // assert that the option appears multiple times
        List<String> values = cmd.getOptionValues("a");
        assertEquals(3, values.size());
        assertEquals("--a", values.get(0));
        assertEquals("--a", values.get(1));
        assertEquals("--a", values.get(2));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        // create test options
        Options options = new Options();

        // create parser instance
        Parser parser = new DefaultParser();

        // create test arguments
        String[] args = new String[]{"--invalid"};

        // parse arguments
        try
        {
            parser.parse(options, args, true);
            fail("Expected a ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }
}