package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

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
        String[] arguments = {"-Dproperty=value", "--foo", "-h", "arg1", "arg2--arg3", "-A", "arg4", "--"};
        String[] expected = {"-D", "property", "value", "--", "foo", "-A", "arg4"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with stopAtNonOption = true.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo", "-h", "arg1", "arg2--arg3", "-A", "arg4", "--", "nonOption"};
        String[] expected = {"-D", "property", "value", "--", "foo"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with empty arguments.
     */
    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with only non-option arguments.
     */
    @Test
    public void testFlattenWithOnlyNonOptionArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with only options.
     */
    @Test
    public void testFlattenWithOnlyOptions() {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo", "-h"};
        String[] expected = {"-D", "property", "value", "--", "foo", "-h"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with an unknown option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithUnknownOption() {
        Options options = new Options();
        String[] arguments = {"-UunknownOption=value", "-Dproperty=value", "--foo", "-h"};
        new GnuParser().flatten(options, arguments, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with an empty option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyOption() {
        Options options = new Options();
        String[] arguments = {"--", "--"};
        new GnuParser().flatten(options, arguments, false);
    }
}