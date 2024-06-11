package org.apache.commons.cli;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    @Test
    public void testFlattenWithOptionsAndArguments() throws ParseException {
        String[] inputArguments = {"-Dproperty1=value1", "--foo", "arg1", "--bar=value2", "arg2", "--", "arg3", "arg4"};
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information.");
        options.addOption("f", "foo", true, "Set the foo property.");
        options.addOption("b", "bar", true, "Set the bar property.");

        GnuParser parser = new GnuParser();
        String[] expectedOutput = {"-D", "property1", "value1", "--", "f", "foo", "--", "arg1", "--", "b", "bar", "--", "value2", "--", "arg2", "--", "--", "arg3", "arg4"};

        String[] output = parser.flatten(options, inputArguments, false);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testFlattenWithoutOptions() throws ParseException {
        String[] inputArguments = {"arg1", "arg2", "arg3"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] expectedOutput = {"arg1", "arg2", "arg3"};

        String[] output = parser.flatten(options, inputArguments, false);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testFlattenWithNonOption() throws ParseException {
        String[] inputArguments = {"--", "arg1", "arg2", "arg3"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] expectedOutput = {"--", "arg1", "arg2", "arg3"};

        String[] output = parser.flatten(options, inputArguments, true); // stop at non-option
        assertArrayEquals(expectedOutput, output);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] inputArguments = {"--invalidOption"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, inputArguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithMissingOption() throws ParseException {
        String[] inputArguments = {"-DinvalidOption", "value"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, inputArguments, false);
    }
}