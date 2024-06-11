package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    @Test
    public void testFlatten_noOptions() {
        String[] arguments = new String[]{"arg1", "arg2", "arg3"};
        String[] expected = new String[]{"arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleOption() {
        String[] arguments = new String[]{"-a", "arg1", "arg2", "arg3"};
        String[] expected = new String[]{"-a", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_optionWithValue() {
        String[] arguments = new String[]{"-Dproperty=value", "arg1", "arg2", "arg3"};
        String[] expected = new String[]{"-D", "property", "value", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_multipleOptions() {
        String[] arguments = new String[]{"-a", "--optionB", "arg1", "arg2", "arg3"};
        String[] expected = new String[]{"-a", "--optionB", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_invalidOption() {
        String[] arguments = new String[]{"--invalidOption", "arg1", "arg2", "arg3"};
        GnuParser parser = new GnuParser();

        parser.flatten(new Options(), arguments, false);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] arguments = new String[]{"arg1", "-a", "arg3", "arg4"};
        String[] expected = new String[]{"arg1", "-a"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_doubleDash() {
        String[] arguments = new String[]{"--", "arg1", "arg2", "arg3"};
        String[] expected = new String[]{"--", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_emptyArguments() {
        String[] arguments = new String[]{};
        String[] expected = new String[]{};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(new Options(), arguments, false);

        assertArrayEquals(expected, result);
    }
}