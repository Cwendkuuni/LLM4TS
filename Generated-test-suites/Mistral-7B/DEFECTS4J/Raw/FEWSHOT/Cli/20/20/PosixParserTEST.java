package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testPosixParserEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};
        String[] result = parser.flatten(new Options(), args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserSimple() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--foo", "bar"};
        String[] expected = {"--foo", "bar"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserSingleOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        String[] args = {"-f", "test.txt"};
        String[] expected = {"-f", "test.txt"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserSingleOptionWithoutArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f");
        String[] args = {"-f"};
        String[] expected = {"-f"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserMultipleOptionsWithArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        options.addOption("o", false, "o", "output");
        String[] args = {"-f", "input.txt", "-o", "output.txt"};
        String[] expected = {"-f", "input.txt", "-o", "output.txt"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserMultipleOptionsWithLongArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", false, "foo", "foo");
        options.addOption("bar", false, "bar", "bar");
        String[] args = {"--foo", "foo1", "--bar", "bar1"};
        String[] expected = {"--foo", "foo1", "--bar", "bar1"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserMultipleOptionsWithShortAndLongArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        options.addOption("o", false, "o", "output");
        options.addOption("h", true, "help", "help");
        String[] args = {"-f", "test.txt", "--o", "output.txt", "-h"};
        String[] expected = {"-f", "test.txt", "--o", "output.txt", "--help"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserMultipleOptionsWithArgsAndSingleDash() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        options.addOption("o", false, "o", "output");
        String[] args = {"-fo", "test.txt
output.txt"};
        String[] expected = {"-f", "test.txt", "-o", "output.txt"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserSingleOptionWithArgsAndSingleDash() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        String[] args = {"-farg", "test.txt"};
        String[] expected = {"-f", "test.txt", "--arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserSingleOptionWithArgsAndMultipleDashes() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        String[] args = {"--farg", "test.txt"};
        String[] expected = {"--f", "test.txt", "--arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserMultipleOptionsWithArgsAndMultipleDashes() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        options.addOption("o", false, "o", "output");
        String[] args = {"--fooarg", "arg1", "--bararg", "arg2"};
        String[] expected = {"--foo", "arg1", "--bar", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        String[] args = {"--foo", "arg1", "nonoption"};
        String[] expected = {"--foo", "arg1"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserStopAtNonOptionWithSingleDash() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        String[] args = {"--fooarg", "arg1", "nonoption"};
        String[] expected = {"--fooarg", "arg1"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserStopAtNonOptionWithMultipleDashes() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        String[] args = {"--fooarg", "arg1", "--nonoption"};
        String[] expected = {"--fooarg", "arg1"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserEmptyOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--"};
        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserEmptyOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--", "arg"};
        String[] expected = {"--arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testPosixParserNullArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = null;
        try {
            String[] result = parser.flatten(options, args, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPosixParserNullOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "arg1", "--bar", "arg2"};
        try {
            parser.flatten(null, args, false);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testPosixParserOptionsWithInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--invalid"};
        try {
            parser.flatten(options, args, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
            assertSame(options, parser.getOptions());
        }
    }

    @Test
    public void testPosixParserOptionsWithInvalidOptionArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f", "file");
        String[] args = {"--farg", "invalid"};
        try {
            parser.flatten(options, args, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
            assertSame(options, parser.getOptions());
        }
    }

    @Test
    public void testPosixParserOptionsWithInvalidOptionNoArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("f", false, "f");
        String[] args = {"--f"};
        try {
            parser.flatten(options, args, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
            assertSame(options, parser.getOptions());
        }
    }
}