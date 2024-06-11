package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo", "-a", "--bar", "arg1", "--", "arg2", "arg3"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-D", "property", "value", "-", "-", "foo", null, "a", "-", "bar", "arg1", "--", "arg2", "arg3"}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with stopAtNonOption = true.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo", "-a", "--bar", "arg1", "--", "arg2", "arg3", "nonOption"};
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-D", "property", "value", "-", "-", "foo", null, "a", "-", "bar", "arg1", "--", "arg2", "arg3"}, result);

        assertEquals(11, result.length);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when arguments is empty.
     * @throws ParseException
     */
    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, new String[0], false);
        assertEquals(0, result.length);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when arguments contains only "--".
     * @throws ParseException
     */
    @Test
    public void testFlattenDoubleDash() throws ParseException {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, new String[]{"--"}, false);
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when arguments starts with "-".
     * @throws ParseException
     */
    @Test
    public void testFlattenArgumentStartsWithMinus() throws ParseException {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, new String[]{"-a"}, false);
        assertArrayEquals(new String[]{"-", "a"}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when arguments contains an empty string.
     * @throws ParseException
     */
    @Test
    public void testFlattenEmptyString() throws ParseException {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] arguments = {"", "--foo"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("", result[0]);
        assertEquals("--foo", result[1]);
    }
}