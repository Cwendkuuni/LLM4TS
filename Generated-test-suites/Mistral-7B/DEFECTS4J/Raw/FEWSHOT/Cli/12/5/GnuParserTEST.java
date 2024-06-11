package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();
        String[] arguments = new String[] {"arg1", "arg2", "arg3"};
        String[] expected = new String[] {"arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information");
        String[] arguments = new String[] {"-D", "arg1", "arg2", "arg3"};
        String[] expected = new String[] {"-D", "debug", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Print long option information");
        String[] arguments = new String[] {"--longOption", "arg1", "arg2", "arg3"};
        String[] expected = new String[] {"--longOption", "longOption", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information");
        options.addOption("l", "list", true, "Print list of options");
        String[] arguments = new String[] {"-D", "--list", "arg1", "-l", "arg2", "arg3"};
        String[] expected = new String[] {"-D", "debug", "--list", "list", "arg1", "-l", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithValues() {
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information");
        options.addOption("l", "list", true, "Print list of options");
        String[] arguments = new String[] {"-D", "--list=value1,value2", "arg1", "-l", "arg2", "arg3"};
        String[] expected = new String[] {"-D", "debug", "--list", "list", "value1,value2", "arg1", "-l", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug information");
        String[] arguments = new String[] {"-D", "--list", "arg1", "-l", "arg2", "arg3"};
        String[] expected = new String[] {"-D", "debug", "arg1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDashDash() {
        Options options = new Options();
        String[] arguments = new String[] {"--", "arg1", "arg2", "arg3"};
        String[] expected = new String[] {"--", "arg1", "arg2", "arg3"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = new String[] {};
        String[] expected = new String[] {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNullArguments() {
        Options options = new Options();
        String[] arguments = null;
        String[] expected = new String[] {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNullOptions() {
        String[] arguments = new String[] {"arg1", "arg2", "arg3"};
        GnuParser parser = new GnuParser();

        try {
            parser.flatten(null, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}