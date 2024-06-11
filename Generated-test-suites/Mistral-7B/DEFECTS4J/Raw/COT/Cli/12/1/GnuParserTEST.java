package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-Dproperty=value", "--", "-foo=bar", "-a", "arg1", "arg2", "--", "-b", "--"};
        String[] expected = new String[]{"-D", "property", "value", "--", "-foo", "bar", "-a", "arg1", "arg2", "--", "-b"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with stopAtNonOption as true.
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-Dproperty=value", "--", "-foo=bar", "-a", "arg1", "arg2", "--", "-b"};
        String[] expected = new String[]{"-Dproperty=value", "-a", "arg1", "arg2", "-b"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with empty arguments.
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testFlattenWithEmptyArguments() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{};
        String[] expected = new String[]{};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with null options.
     */
    @Test(expected = NullPointerException.class)
    public void testFlattenWithNullOptions() {
        String[] arguments = new String[]{"-Dproperty=value", "--", "-foo=bar", "-a", "arg1", "arg2", "--", "-b"};
        GnuParser parser = new GnuParser();
        parser.flatten(null, arguments, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testFlattenWithNullArguments() {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        parser.flatten(options, null, false);
    }
}