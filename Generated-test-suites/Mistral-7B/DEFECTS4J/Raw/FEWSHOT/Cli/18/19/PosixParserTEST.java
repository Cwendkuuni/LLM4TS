package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleArg() {
        Options options = new Options();
        String[] args = {"foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testDoubleDashArg() {
        Options options = new Options();
        String[] args = {"--foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testSingleHyphenArg() {
        Options options = new Options();
        String[] args = {"-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f"}, result);
    }

    @Test
    public void testDoubleHyphenArg() {
        Options options = new Options();
        String[] args = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testOptionWithArg() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        String[] args = {"-f", "input.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f", "input.txt"}, result);
    }

    @Test
    public void testOptionWithLongArg() {
        Options options = new Options();
        options.addOption("i", "input", true, "The input file");
        String[] args = {"--input", "input.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--input", "input.txt"}, result);
    }

    @Test
    public void testOptionWithArgAndRemainingArgs() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        String[] args = {"-f", "input.txt", "output.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f", "input.txt", "output.txt"}, result);
    }

    @Test
    public void testMultipleOptionsWithArgs() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        options.addOption("o", "output", true, "The output file");
        String[] args = {"-f", "input.txt", "-o", "output.txt", "test.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f", "input.txt", "-o", "output.txt", "test.txt"}, result);
    }

    @Test
    public void testOptionWithShortArgAndSingleHyphen() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        String[] args = {"-f", "-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals("-", result[2]);
    }

    @Test
    public void testOptionWithShortArgAndLongArg() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        String[] args = {"-f", "input.txt", "--", "output.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f", "input.txt", "--", "output.txt"}, result);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        String[] args = {"foo", "-f", "input.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"foo", "-f", "input.txt"}, result);
    }

    @Test
    public void testBurstTokenWithOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        String[] args = {"-ffoo", "input.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f", "foo", "input.txt"}, result);
    }

    @Test
    public void testBurstTokenWithNoOption() {
        Options options = new Options();
        String[] args = {"-abcd"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--", "-a", "-b", "-c", "-d"}, result);
    }

    @Test
    public void testBurstTokenWithLongOption() {
        Options options = new Options();
        String[] args = {"--long-option-with-many-args"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long-option-with-many-args"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenWithInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        String[] args = {"--", "-f", "input.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--", "-f", "input.txt"}, result);
    }

    @Test
    public void testBurstTokenWithInvalidOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "The input file");
        String[] args = {"--invalid--", "-f", "input.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--invalid--", "-f", "input.txt"}, result);
    }
}