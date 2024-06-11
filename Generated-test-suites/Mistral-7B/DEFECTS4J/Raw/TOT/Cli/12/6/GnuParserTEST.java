package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test class for the GnuParser class.
 */
public class GnuParserTest {

    /**
     * Test flatten method with valid options and arguments.
     */
    @Test
    public void testFlattenValid() {
        Options options = new Options();
        options.addOption("d", "debug", false, "Print debug information.");
        options.addOption("h", "help", false, "Print this help message.");

        String[] arguments = {"-D", "property=value1", "-h", "--", "arg1", "--option=value2", "arg3", "--option2=value3"};
        String[] expected = {"-D", "property", "help", "-", "arg1", "--option", "value2", "", "arg3", "--option2", "value3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with stopAtNonOption flag set to true.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("d", "debug", false, "Print debug information.");
        options.addOption("h", "help", false, "Print this help message.");

        String[] arguments = {"-D", "property=value1", "-h", "--", "arg1", "--option=value2", "arg3", "--option2=value3", "nonOption"};
        String[] expected = {"-D", "property", "help", "-", "arg1", "--option", "value2", "", "arg3", "--option2", "value3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalid() {
        Options options = new Options();
        options.addOption("d", "debug", false, "Print debug information.");
        options.addOption("h", "help", false, "Print this help message.");

        String[] arguments = {"-D", "property=value1", "-h", "--", "arg1", "--option=value2", "arg3", "--invalid"};
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test flatten method with no options.
     */
    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}