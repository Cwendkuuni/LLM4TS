package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest
{
    @Test
    public void testSimpleOptions() throws ParseException
    {
        String[] args = new String[]{"--foo", "--bar=baz"};
        Options options = new Options();
        options.addOption("foo", true, "Foo Option");
        options.addOption("bar", true, "Bar Option");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        assertTrue(options.isSetOption("foo"));
        assertTrue(options.isSetOption("bar"));
        assertEquals("baz", options.getOptionValue("bar"));
    }

    @Test
    public void testInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();

        DefaultParser parser = new DefaultParser();

        try
        {
            parser.parse(options, args, false);
            fail("An invalid option should have caused a ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }

        assertFalse(options.isSetOption("invalid"));
    }

    @Test
    public void testLongOptions() throws ParseException
    {
        String[] args = new String[]{"--long-option", "--long-option2=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        options.addOption("longOption2", true, "Long Option 2");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        assertTrue(options.isSetOption("longOption"));
        assertTrue(options.isSetOption("longOption2"));
        assertEquals("value", options.getOptionValue("longOption2"));
    }

    @Test
    public void testMixedOptions() throws ParseException
    {
        String[] args = new String[]{"--foo", "--bar=baz", "-x", "--long-option"};
        Options options = new Options();
        options.addOption("foo", true, "Foo Option");
        options.addOption("bar", true, "Bar Option");
        options.addOption("x", false, "X Option");
        options.addOption("longOption", true, "Long Option");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        assertTrue(options.isSetOption("foo"));
        assertTrue(options.isSetOption("bar"));
        assertEquals("baz", options.getOptionValue("bar"));
        assertTrue(options.isSetOption("x"));
        assertTrue(options.isSetOption("longOption"));
    }

    @Test
    public void testEmptyArgs() throws ParseException
    {
        String[] args = new String[0];
        Options options = new Options();

        DefaultParser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            fail("No arguments should have caused a ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }

        assertFalse(options.isParsed());
    }

    @Test
    public void testHelp()
    {
        String[] args = new String[]{"--help"};
        Options options = new Options();

        DefaultParser parser = new DefaultParser();

        try
        {
            parser.parse(options, args, true);
        }
        catch (ParseException e)
        {
            // Ignore
        }

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }
}