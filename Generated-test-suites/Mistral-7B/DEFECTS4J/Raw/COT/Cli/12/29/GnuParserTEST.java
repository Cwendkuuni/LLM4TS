package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)}.
     * @throws ParseException
     */
    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"-Dproperty=value", "--foo", "arg1", "-a", "--", "arg3", "-ab=value"};
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("D", true, "option D");
        options.addOption("b", true, "option b");
        options.addOption("foo", false, "option foo");

        String[] expected = {"-D", "property", "value", "--", "foo", "arg1", "-a"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} with stopAtNonOption = true.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"-Dproperty=value", "--foo", "arg1", "-a", "nonOption", "arg3", "-ab=value"};
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("D", true, "option D");
        options.addOption("b", true, "option b");
        options.addOption("foo", false, "option foo");

        String[] expected = {"-D", "property", "value", "nonOption", "arg3", "-ab", "value"};
        String[] result = new GnuParser().flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when all arguments start with hyphen.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithAllArgumentsStartingWithHyphen() throws ParseException {
        String[] args = {"--", "-", "--foo=value", "-bar=value"};
        Options options = new Options();

        String[] expected = {"--", "-", "--foo=value", "--", "bar=value"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when there is no argument.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithNoArgument() throws ParseException {
        String[] args = {};
        Options options = new Options();

        String[] expected = {};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link GnuParser#flatten(Options, String[], boolean)} when options have long opt name.
     * @throws ParseException
     */
    @Test
    public void testFlattenWithLongOptName() throws ParseException {
        String[] args = {"--long-option", "--foo", "arg1", "-a", "--long-option-2", "arg3", "-ab=value"};
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("foo", false, "option foo");

        String[] expected = {"--long-option", "--long-option-2", "--", "foo", "arg1", "-a"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }
}