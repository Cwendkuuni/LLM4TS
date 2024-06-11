package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    @Test
    public void testFlatten() {
        String[] args = new String[]{"-Dprop=val", "--foo", "-ab", "-c", "-d", "arg1", "arg2", "--", "arg3", "arg4"};
        Options options = new Options();
        options.addOption("a", true, "Option A", "a");
        options.addOption("b", true, "Option B", "b");
        options.addOption("c", true, "Option C", "c");
        options.addOption("d", true, "Option D", "d");
        options.addOption("e", true, "Option E", "e");
        options.addOption("f", true, "Option F", "f");
        options.addOption("foo", false, "Option Foo", "f");
        options.addOption("help", false, "Print this help message.", "h");

        String[] expected = {"-D", "prop", "val", "--", "foo", "-a", "-b", "-c", "-d", "arg1", "arg2", "--", "arg3", "arg4"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);

        args = new String[]{"-Dprop=val", "--foo", "-ab", "-c", "-d", "arg1", "arg2", "--", "arg3", "arg4", "nonOption"};
        expected = new String[]{"-D", "prop", "val", "--", "foo", "-a", "-b", "-c", "-d", "arg1", "arg2", "--", "arg3", "arg4", "nonOption"};
        result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);

        args = new String[]{"-Dprop=val", "--foo", "-ab", "-c", "-d", "arg1", "arg2", "--", "arg3", "arg4"};
        expected = new String[]{"-D", "prop", "val", "--", "foo", "-a", "-b", "-c", "-d", "arg1", "arg2", "--", "arg3", "arg4"};
        result = new GnuParser().flatten(options, args, true);

        assertArrayEquals(expected, result);

        try {
            new GnuParser().flatten(null, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Invalid parser configuration. Options must not be null.", e.getMessage());
        }

        try {
            new GnuParser().flatten(options, null, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Arguments must not be null.", e.getMessage());
        }
    }
}