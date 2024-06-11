package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-D", "--foo=value", "-bar=value", "-baz", "file.txt"};
        boolean stopAtNonOption = true;
        String[] expected = {"-D", "--", "-baz", "file.txt"};

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNoOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        boolean stopAtNonOption = true;
        String[] expected = {"arg1", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionFalse() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-opt1", "value1", "arg1", "-opt2", "value2"};
        boolean stopAtNonOption = false;
        String[] expected = {"-opt1", "value1", "arg1", "-opt2", "value2"};

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithEmptyArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = true;
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }
}