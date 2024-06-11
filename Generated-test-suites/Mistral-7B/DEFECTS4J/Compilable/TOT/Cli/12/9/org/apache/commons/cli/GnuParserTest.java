package org.apache.commons.cli;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    /**
     * Test flatten method with no options and no arguments.
     */
    @Test
    public void testFlattenNoOptionsOrArguments() {
        String[] arguments = new String[0];
        Options options = new Options();
        String[] expected = new String[0];

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with options and arguments.
     */
    @Test
    public void testFlattenOptionsAndArguments() {
        String[] arguments = {"-Dproperty=value", "--foo", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("D", "property", true, "Property value");
        options.addOption("f", "foo", true, "Process this option.");

        String[] expected = {"-D", "property", "value", "--", "f", "foo"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with only non-option arguments.
     */
    @Test
    public void testFlattenNonOptionArguments() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        Options options = new Options();

        String[] expected = {"arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with a "--" argument to stop parsing options.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-Dproperty=value", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("D", "property", true, "Property value");

        String[] expected = {"-D", "property", "value", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] arguments = {"--invalidOption"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }
}