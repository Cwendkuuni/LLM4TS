package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        String[] args = new String[]{"--", "-D", "--foo", "-foo", "-bar", "foo", "--", "bar", "--baz=qux", "--", "qux"};
        Options options = new Options();
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--", "-D", "-D", "foo", "-foo", "-bar", "foo", "--", "bar", "--baz", "qux");
        List<String> expectedWithoutDashes = Arrays.asList("--", "D", "D", "foo", "foo", "bar", "foo", "--", "bar", "baz", "qux");

        assertArrayEquals(expected.toArray(new String[0]), result);
        assertArrayEquals(expectedWithoutDashes.toArray(new String[0]), parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = new String[]{"--", "-D", "--foo", "-foo", "-bar", "foo", "--", "bar", "--baz=qux", "--", "qux", "nonOption"};
        Options options = new Options();
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList("--", "D", "D", "foo", "foo", "bar", "foo", "--", "bar", "baz", "qux");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithNoDoubleDash() {
        String[] args = new String[]{"--foo", "foo", "-bar", "bar"};
        Options options = new Options();
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--foo", "foo", "-bar", "bar");
        List<String> expectedWithoutDashes = Arrays.asList("--foo", "foo", "bar", "bar");

        assertArrayEquals(expected.toArray(new String[0]), result);
        assertArrayEquals(expectedWithoutDashes.toArray(new String[0]), parser.flatten(options, args, true));
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] args = new String[]{"foo", "bar", "qux"};
        Options options = new Options();
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("foo", "bar", "qux");
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithEmptyArgs() {
        String[] args = new String[0];
        Options options = new Options();
        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList();
        assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenWithNullArgs() {
        Options options = new Options();
        GnuParser parser = new GnuParser();

        assertNull(parser.flatten(options, null, false));
    }

    @Test
    public void testFlattenWithNullOptions() {
        String[] args = new String[]{"--", "-D", "--foo", "-foo", "-bar", "foo", "--", "bar", "--baz=qux", "--", "qux"};
        GnuParser parser = new GnuParser();

        assertArrayEquals(new String[0], parser.flatten(null, args, false));
    }
}