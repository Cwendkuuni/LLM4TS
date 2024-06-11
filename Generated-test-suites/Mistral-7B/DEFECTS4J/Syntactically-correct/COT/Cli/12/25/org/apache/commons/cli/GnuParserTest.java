package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo", "-bar", "baz", "--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        // Basic test for functionality
        assertArrayEquals(new String[]{"-D", "property", "value", "--", "foo", "-bar", "baz"}, result);

        // Edge cases and exception handling tests follow
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with empty arguments array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};

        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with null options.
     */
    @Test(expected = NullPointerException.class)
    public void testNullOptions() {
        String[] arguments = {"-Dproperty=value", "--foo", "-bar", "baz", "--"};

        GnuParser parser = new GnuParser();
        parser.flatten(null, arguments, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, null, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with empty option list.
     */
    @Test
    public void testEmptyOptionList() {
        String[] arguments = {"-Dproperty=value", "--foo", "-bar", "baz", "--"};

        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-Dproperty=value", "--foo", "-bar", "baz"}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with invalid option format.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOptionFormat() {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo-", "-bar", "baz", "--"};

        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with stopping at non-option.
     */
    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo", "-bar", "baz", "nonOption", "--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-Dproperty=value", "--foo", "-bar"}, result);
    }
}