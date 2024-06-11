package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] expected = {};

        String[] result =parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSpecialToken() {
        Options options = new Options();
        String[] arguments = {"--foo", "--bar=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"--foo", "--bar", "value"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-f", "--bar=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-f", "--bar", "value"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionToken() {
        Options options = new Options().addOption("f", false, "help", "Print help message");
        String[] arguments = {"-f", "--bar=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-f", "--bar", "value"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
        assertEquals(options.findOption("f"), parser.currentOption);
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options().addOption("f", true, "file", "file to open");
        String[] arguments = {"-f", "file.txt", "--bar=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-f", "file.txt", "--bar", "value"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
        assertEquals(options.findOption("f"), parser.currentOption);
    }

    @Test
    public void testFlattenOptionWithArgs() {
        Options options = new Options().addOption("f", true, "file", "file to open");
        String[] arguments = {"-f", "file1.txt", "-f", "file2.txt", "--bar=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-f", "file1.txt", "-f", "file2.txt", "--bar", "value"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
        assertEquals(options.findOption("f"), parser.currentOption);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"arg1", "--foo=value", "arg2"};
        PosixParser parser = new PosixParser();

        String[] expected = {"arg1", "--foo", "value", "arg2"};

        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstToken() {
        Options options = new Options().addOption("f", true, "file", "file to open");
        String[] arguments = {"-ffile", "file1.txt", "file2.txt", "--bar=value"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-f", "file", "file1.txt", "file2.txt", "--bar", "value"};

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
        assertEquals(options.findOption("f"), parser.currentOption);
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOption() {
        Options options = new Options().addOption("f", true, "file", "file to open");
        String[] arguments = {"-ffile", "file1.txt", "file2.txt", "--foo=value", "arg"};
        PosixParser parser = new PosixParser();

        String[] expected = {"-f", "file", "file1.txt", "file2.txt", "--foo", "value", "arg"};

        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
        assertNull(parser.currentOption);
    }

    @Test(expected = AssertionError.class)
    public void testFlattenNullArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, null, false);
    }
}