package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException if an error occurs while parsing the options
     */
    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"-Dproperty=value", "--foo", "bar", "-a", "--", "baz", "qux"};
        Options options = new Options();
        List expectedTokens = new ArrayList();
        expectedTokens.add("-D");
        expectedTokens.add("property");
        expectedTokens.add("value");
        expectedTokens.add("--");
        expectedTokens.add("foo");
        expectedTokens.add("bar");
        expectedTokens.add("-a");
        expectedTokens.add("baz");
        expectedTokens.add("qux");

        List actualTokens = new GnuParser().flatten(options, args, false);
        Assert.assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());

        String[] args2 = {"-Dproperty=value", "--foo", "bar", "-a", "--quux", "baz", "qux"};
        List actualTokens2 = new GnuParser().flatten(options, args2, false);
        Assert.assertArrayEquals(expectedTokens.toArray(), actualTokens2.toArray());

        String[] args3 = {"--foo", "bar", "-a", "baz", "qux"};
        List actualTokens3 = new GnuParser().flatten(options, args3, true);
        Assert.assertArrayEquals(Arrays.asList("-", "foo", "bar", "a", "baz", "qux").toArray(), actualTokens3.toArray());
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException if an error occurs while parsing the options
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidArgument() throws ParseException {
        String[] args = {"--invalid"};
        Options options = new Options();

        new GnuParser().flatten(options, args, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException if an error occurs while parsing the options
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithEmptyArgument() throws ParseException {
        String[] args = {"--", ""};
        Options options = new Options();

        new GnuParser().flatten(options, args, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException if an error occurs while parsing the options
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithNoOptions() throws ParseException {
        String[] args = {"bar", "baz", "qux"};
        Options options = new Options();

        new GnuParser().flatten(options, args, false);
    }
}