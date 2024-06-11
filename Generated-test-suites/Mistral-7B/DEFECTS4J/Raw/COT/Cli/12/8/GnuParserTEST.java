package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for the flatten(Options, String[], boolean) method.
     * @throws ParseException
     */
    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] args = {"-Dproperty=value", "--foo", "-bar", "baz", "--", "arg1", "arg2", "--", "arg3"};

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("foo");
        expected.add("bar");
        expected.add("baz");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("arg3");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for the flatten(Options, String[], boolean) method with stopAtNonOption = true.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] args = {"-Dproperty=value", "--foo", "-bar", "baz", "--", "arg1", "arg2", "--", "arg3", "nonOption"};

        List<String> expected = new ArrayList<>();
        expected.add("D");
        expected.add("property");
        expected.add("value");
        expected.add("foo");
        expected.add("bar");
        expected.add("baz");
        expected.add("arg1");
        expected.add("arg2");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for the flatten(Options, String[], boolean) method with a single argument.
     * @throws ParseException
     */
    @Test
    public void testFlattenSingleArgument() throws ParseException {
        Options options = new Options();
        String[] args = {"singleArgument"};

        List<String> expected = new ArrayList<>();
        expected.add("singleArgument");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for the flatten(Options, String[], boolean) method with empty arguments.
     * @throws ParseException
     */
    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        Options options = new Options();
        String[] args = {};

        List<String> expected = new ArrayList<>();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for the flatten(Options, String[], boolean) method with no options.
     * @throws ParseException
     */
    @Test
    public void testFlattenNoOptions() throws ParseException {
        Options options = new Options();
        String[] args = {"arg1", "arg2", "arg3"};

        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");
        expected.add("arg3");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for the flatten(Options, String[], boolean) method with two options.
     * @throws ParseException
     */
    @Test
    public void testFlattenTwoOptions() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] args = {"-o1", "arg1", "-o2", "arg2"};

        List<String> expected = new ArrayList<>();
        expected.add("o1");
        expected.add("arg1");
        expected.add("o2");
        expected.add("arg2");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for the flatten(Options, String[], boolean) method with an empty option.
     * @throws ParseException
     */
    @Test
    public void testFlattenEmptyOption() throws ParseException {
        Options options = new Options();
        String[] args = {"-o", ""};

        List<String> expected = new ArrayList<>();
        expected.add("o");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test method for the flatten(Options, String[], boolean) method with an invalid option.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        Options options = new Options();
        String[] args = {"-invalidOption"};

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);

        // The test should fail with a ParseException since the invalid option should not be added to the processed tokens list.
    }
}