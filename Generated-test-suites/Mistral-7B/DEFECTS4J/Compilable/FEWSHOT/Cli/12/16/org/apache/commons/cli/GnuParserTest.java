package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimpleArguments() {
        Options options = new Options();
        String[] arguments = {"-D", "property", "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-D", "property", "value"}, result);
    }

    @Test
    public void testFlattenWithOptions() {
        Options options = new Options();
        options.addOption("d", "debug", false, "Print debug information.");
        String[] arguments = {"--debug", "value1", "-Dproperty=value2", "--", "-a", "arg1"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--debug", "-D", "property", "value2", "--", "-a", "arg1"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("d", "debug", false, "Print debug information.");
        String[] arguments = {"--debug", "value1", "-Dproperty=value2", "--", "nonOption", "arg1"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--debug"}, result);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "--foo"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "arg1", "--foo"}, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndOption() {
        Options options = new Options();
        options.addOption("f", "foo", true, "Print foo.");
        String[] arguments = {"--", "--foo=value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--foo=value", result[0]);
    }

    @Test
    public void testFlattenWithEmptyOptionValue() {
        Options options = new Options();
        String[] arguments = {"--foo="};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithNullArguments() {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        parser.flatten(options, null, false);
    }

    @Test
    public void testFlattenWithNullOption() {
        String[] arguments = {"--", "-D", null, "value"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(null, arguments, false);
        assertNull(result);
    }

    @Test
    public void testFlattenWithNoOptions() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOptionName", true, "Print long option.");
        String[] arguments = {"--longOptionName"};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals("--longOptionName", result[0]);
    }
}