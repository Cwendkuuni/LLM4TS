package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a required integer argument");
    }

    /**
     * Test flattening of a simple command line with no options or arguments.
     */
    @Test
    public void testEmpty() {
        String[] args = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test flattening of a command line with a single option.
     */
    @Test
    public void testSingleOption() {
        String[] args = {"--a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a"}, result);
    }

    /**
     * Test flattening of a command line with a single value option.
     */
    @Test
    public void testSingleValueOption() {
        String[] args = {"--a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "value"}, result);
    }

    /**
     * Test flattening of a command line with a single value option and an argument.
     */
    @Test
    public void testSingleValueOptionWithArgument() {
        String[] args = {"--a", "value", "argument"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "value", "argument"}, result);
    }

    /**
     * Test flattening of a command line with a multiple value option.
     */
    @Test
    public void testMultipleValueOption() {
        String[] args = {"--a", "value1", "--a", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "value1"}, result);
        Assert.assertArrayEquals(Arrays.copyOfRange(result, 1, result.length), new String[]{"--a", "value2"});
    }

    /**
     * Test flattening of a command line with a required integer argument option.
     */
    @Test
    public void testIntegerOption() {
        String[] args = {"--c", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--c", "1"}, result);
    }

    /**
     * Test handling of an invalid option.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = {"--x"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test handling of an invalid option with an argument.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOptionWithArgument() {
        String[] args = {"--x", "value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test handling of a command line with a mix of options and arguments.
     */
    @Test
    public void testMixed() {
        String[] args = {"--a", "value1", "--b", "value2", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "value1"}, result);
        Assert.assertArrayEquals(Arrays.copyOfRange(result, 1, result.length), new String[]{"--b", "value2", "arg1", "arg2"});
    }

    /**
     * Test handling of a long command line with a mix of options and arguments.
     */
    @Test
    public void testLongMixed() {
        String[] args = {"--long-option-a", "value1", "--long-option-b", "value2", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option-a", "value1"}, result);
        Assert.assertArrayEquals(Arrays.copyOfRange(result, 1, result.length), new String[]{"--long-option-b", "value2", "arg1", "arg2"});
    }
}