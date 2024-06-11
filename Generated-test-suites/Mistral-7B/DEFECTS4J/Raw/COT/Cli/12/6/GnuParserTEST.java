package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test cases for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten_Basic() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "arg1", "--", "-option1", "-option2", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("arg1");
        expected.add("--");
        expected.add("option1");
        expected.add("option2");
        expected.add("arg2");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten_SingleCharacterOption() throws ParseException {
        String[] args = new String[]{"-D", "property=value", "arg1", "-o", "option1", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("arg1");
        expected.add("-o");
        expected.add("option1");
        expected.add("arg2");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten_DoubleDashOption() throws ParseException {
        String[] args = new String[]{"-D", "--", "arg1", "--foo=value", "-option1", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("--");
        expected.add("arg1");
        expected.add("--foo");
        expected.add("value");
        expected.add("option1");
        expected.add("arg2");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten_StopAtNonOption() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "arg1", "--", "-option1", "arg2", "nonOption"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("arg1");
        expected.add("--");
        expected.add("option1");
        expected.add("arg2");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "arg1", "--", "-x", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);

        // Since -x is not a valid option in this case, an exception should be thrown
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten_EmptyTokens() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "", "--", "", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("arg2");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten_MultipleEmptyTokens() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "", "--", "", "", "", "arg2"};
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("arg2");

        Assert.assertArrayEquals(expected.toArray(), result);
    }
}