package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for GnuParser
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with normal arguments.
     */
    @Test
    public void testFlattenNormalArguments() {
        String[] arguments = new String[]{"arg1", "arg2", "arg3"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with option arguments.
     */
    @Test
    public void testFlattenOptionArguments() {
        String[] arguments = new String[]{"-DmyProperty=value1", "-DmyProperty=value2"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-D", "myProperty", "value1", "-D", "myProperty", "value2"}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with single option.
     */
    @Test
    public void testFlattenSingleOption() {
        String[] arguments = new String[]{"-a"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with double option.
     */
    @Test
    public void testFlattenDoubleOption() {
        String[] arguments = new String[]{"--foo"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--foo"}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with empty arguments.
     */
    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = new String[]{};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with stopAtNonOption=true.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = new String[]{"arg1", "-DmyProperty=value1", "arg3"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"arg1", "-DmyProperty=value1"}, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with non-option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenNonOption() {
        String[] arguments = new String[]{"arg1", "nonOption", "arg3"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}
     * with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] arguments = new String[]{"arg1", "-invalidOption", "arg3"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }
}