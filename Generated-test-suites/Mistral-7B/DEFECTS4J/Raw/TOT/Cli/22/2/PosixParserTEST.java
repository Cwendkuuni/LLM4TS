package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link PosixParser} class.
 */
public class PosixParserTest
{
    /**
     * Test flatten method with basic options.
     */
    @Test
    public void testBasicOptions()
    {
        String[] args = {"-a", "--foo=bar", "--bip=qux", "--", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("bip", false, "Option BIP");
        options.addOption("foo", true, "Option FOO");

        DefaultParser parser = new PosixParser();
        String[] result = parser.parse(options, args, true);
        HelpFormatter formatter = new HelpFormatter();

        assertEquals(args.length, result.length);
        assertEquals("Option A", options.getOption("a").getLongOpt());
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("foo"));
        assertFalse(options.hasOption("qux"));
        assertNull(options.getOption("quz"));
        assertFalse(options.getOption("quz").getValue() != null);
        assertEquals("bar", options.getOptionValue("foo"));
        assertNull(options.getOptionValue("quz"));

        formatter.printHelp("PosixParserTest", Arrays.asList(result), true);
        System.out.println(formatter.getHelpText());
    }

    /**
     * Test flatten method with no options.
     */
    @Test
    public void testNoOptions()
    {
        String[] args = {"foo", "bar", "baz"};
        Options options = new Options();

        DefaultParser parser = new PosixParser();
        String[] result = parser.parse(options, args, false);

        assertEquals(args.length, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("baz", result[2]);
    }

    /**
     * Test flatten method with unrecognized options.
     */
    @Test(expected = ParseException.class)
    public void testUnrecognizedOptions()
    {
        String[] args = {"-q", "--qux=qux", "--quz", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("bip", false, "Option BIP");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    /**
     * Test flatten method with a mix of long and short options.
     */
    @Test
    public void testMixedOptions()
    {
        String[] args = {"-a", "--foo=bar", "--bip", "-c", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("bip", false, "Option BIP");
        options.addOption("foo", true, "Option FOO");

        DefaultParser parser = new PosixParser();
        String[] result = parser.parse(options, args, true);

        assertEquals(args.length, result.length);
        assertEquals("Option A", options.getOption("a").getLongOpt());
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("foo"));
        assertFalse(options.hasOption("bip"));
        assertNull(options.getOption("quz"));
        assertFalse(options.getOption("quz").getValue() != null);
        assertEquals("bar", options.getOptionValue("foo"));
        assertNull(options.getOptionValue("quz"));
    }
}