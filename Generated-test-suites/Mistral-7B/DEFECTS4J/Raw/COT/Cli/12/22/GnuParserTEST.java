package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for GnuParser
 */
public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[]{"-Dproperty=value", "--foo", "-a", "-b--", "ARG1", "ARG2"};
        GnuParser parser = new GnuParser();

        try {
            String[] result = parser.flatten(options, arguments, false);
            List<String> expected = Arrays.asList(
                    "-D", "property", "value",
                    "--",
                    "foo",
                    "a",
                    "--",
                    "ARG1",
                    "ARG2"
            );
            Assert.assertArrayEquals(expected.toArray(), result);
        } catch (ParseException e) {
            // This method is not expected to throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-Dproperty=value", "--foo", "-a", "-b--", "ARG1", "ARG2", "nonOption"};
        GnuParser parser = new GnuParser();

        try {
            String[] result = parser.flatten(options, arguments, true);
            List<String> expected = Arrays.asList(
                    "-D", "property", "value",
                    "--",
                    "foo",
                    "a"
            );
            Assert.assertArrayEquals(expected.toArray(), Arrays.copyOf(result, 3));
        } catch (ParseException e) {
            // This method is not expected to throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithNullArguments() {
        Options options = new Options();
        String[] arguments = null;
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyOptionName() {
        Options options = new Options();
        String[] arguments = {"-", ""};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyArgument() {
        Options options = new Options();
        String[] arguments = {"-D", ""};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyArgumentsAndStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOptionFormat() {
        Options options = new Options();
        String[] arguments = {"--foo--bar"};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }
}