package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * Test case for normal usage when no option found for first character and no '--' given.
     */
    @Test
    public void testFlatten_NormalUsage() {
        String[] args = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        String[] flattenedArgs = new GnuParser().flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, flattenedArgs);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * Test case for option found for first character and the rest of the argument added.
     */
    @Test
    public void testFlatten_OptionFoundForFirstCharacter() {
        String[] args = {"-a", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        String[] flattenedArgs = new GnuParser().flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "arg2", "arg3"}, flattenedArgs);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * Test case for option found for two characters and the rest of the argument added.
     */
    @Test
    public void testFlatten_OptionFoundForTwoCharacters() {
        String[] args = {"--foo", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("foo", true, "option FOO");
        String[] flattenedArgs = new GnuParser().flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "arg2", "arg3"}, flattenedArgs);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * Test case for '--' given and the rest of the arguments added.
     */
    @Test
    public void testFlatten_RestOfArgumentsAdded() {
        String[] args = {"arg1", "--", "arg4", "arg5"};
        Options options = new Options();
        String[] flattenedArgs = new GnuParser().flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"arg1", "--", "arg4", "arg5"}, flattenedArgs);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * Test case for exception handling when an invalid option is given.
     */
    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"-x", "arg2", "arg3"};
        Options options = new Options();
        new GnuParser().flatten(options, args, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * Test case for exception handling when an unknown option is given with an argument.
     */
    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOptionWithArgument() {
        String[] args = {"--unknown", "arg2", "arg3"};
        Options options = new Options();
        new GnuParser().flatten(options, args, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * Test case for exception handling when an unknown option is given without an argument.
     */
    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOptionWithoutArgument() {
        String[] args = {"--unknown"};
        Options options = new Options();
        new GnuParser().flatten(options, args, false);
    }
}