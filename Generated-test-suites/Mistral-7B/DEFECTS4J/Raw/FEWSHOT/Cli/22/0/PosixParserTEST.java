package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmpty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSimple() {
        Options options = new Options();
        String[] arguments = { "--foo" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "foo" }, result);
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] arguments = { "--foo" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "foo" }, result);
    }

    @Test
    public void testLongOptionWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] arguments = { "--foo=bar" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "foo", "bar" }, result);
    }

    @Test
    public void testShortOption() {
        Options options = new Options();
        options.addOption("f", true);
        String[] arguments = { "-f" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "f" }, result);
    }

    @Test
    public void testShortOptionWithValue() {
        Options options = new Options();
        options.addOption("f", true);
        String[] arguments = { "-f", "bar" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "f", "bar" }, result);
    }

    @Test
    public void testBurstShortOption() {
        Options options = new Options();
        options.addOption("ab", true);
        String[] arguments = { "-abcd" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "ab", "cd" }, result);
    }

    @Test
    public void testBurstLongOption() {
        Options options = new Options();
        options.addOption("abc", true);
        String[] arguments = { "--abcdefg" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "abc", "defg" }, result);
    }

    @Test
    public void testBurstShortOptionWithValue() {
        Options options = new Options();
        options.addOption("ab", true);
        String[] arguments = { "-abcd", "123" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "ab", "cd", "123" }, result);
    }

    @Test
    public void testBurstLongOptionWithValue() {
        Options options = new Options();
        options.addOption("abc", true);
        String[] arguments = { "--abcdefg=123" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "abc", "123" }, result);
    }

    @Test
    public void testBurstMixed() {
        Options options = new Options();
        options.addOption("ab", true);
        options.addOption("cd", true);
        String[] arguments = { "--abcd", "1", "-ef" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[] { "ab", "cd", "1", "ef" }, result);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("ab", true);
        String[] arguments = { "--abcd", "1", "-ef", "non-option" };
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = new ArrayList<>();
        expected.add("--abcd");
        expected.add("1");
        expected.add("-ef");
        expected.add("non-option");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}