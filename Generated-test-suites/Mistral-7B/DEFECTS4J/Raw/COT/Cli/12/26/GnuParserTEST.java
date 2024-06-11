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

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dfoo=bar", "--foo", "-b", "-c", "--", "arg1", "arg2", "--", "arg3"};
        boolean stopAtNonOption = false;
        String[] expected = {"-D", "foo", "=", "bar", "--", "foo", "", "", "arg1", "arg2", "--", "arg3"};
        String[] result = new GnuParser().flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with stopAtNonOption = true.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dfoo=bar", "--foo", "-b", "-c", "--", "arg1", "arg2", "--", "arg3"};
        boolean stopAtNonOption = true;
        String[] expected = {"-D", "foo", "=", "bar"};
        String[] result = new GnuParser().flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when the first argument is "--".
     * @throws ParseException
     */
    @Test
    public void testFlattenWithFirstArgumentAsDoubleDash() throws ParseException {
        Options options = new Options();
        String[] arguments = {"--", "-b", "-c", "--", "arg1", "arg2", "--", "arg3"};
        boolean stopAtNonOption = false;
        String[] expected = {"--", "--"};
        String[] result = new GnuParser().flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when the first argument is "-".
     * @throws ParseException
     */
    @Test
    public void testFlattenWithFirstArgumentAsSingleDash() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-", "-b", "-c", "--", "arg1", "arg2", "--", "arg3"};
        boolean stopAtNonOption = false;
        String[] expected = {"-"};
        String[] result = new GnuParser().flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when there are no arguments.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithEmptyArgumentList() throws ParseException {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = false;
        String[] expected = new String[0];
        String[] result = new GnuParser().flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when an option has a long and a short form.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithOptionLongAndShortForm() throws ParseException {
        Options options = new Options();
        options.addOption("foo", "fooOption", true, "Short description for --foo.");
        options.addOption("f", "shortFooOption", true, "Short description for -f.");
        String[] arguments = {"-Dfoo=value", "-f", "arg1", "--foo", "arg2"};
        boolean stopAtNonOption = false;
        String[] expected = {"-D", "foo", "=", "value", "-f", "arg1", "--", "foo"};
        String[] result = new GnuParser().flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, result);
    }
}