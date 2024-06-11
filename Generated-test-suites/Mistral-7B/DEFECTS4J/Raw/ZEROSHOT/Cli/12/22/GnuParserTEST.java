package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link GnuParser}.
 */
public class GnuParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addRequiredOption("d", "debug", false, "Print debug information.");
        options.addRequiredOption("h", "help", false, "Print help.");
        options.addOption("i", "input", true, "Input file.");
        options.addOption("o", "output", true, "Output file.");
    }

    @After
    public void tearDown() {
        options = null;
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] arguments = new String[]{"input.txt", "output.txt"};
        String[] expected = new String[]{"input.txt", "output.txt"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        String[] arguments = new String[]{"-d", "input.txt", "output.txt"};
        String[] expected = new String[]{"-d", "input.txt", "output.txt"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] arguments = new String[]{"-d", "-h", "input.txt", "output.txt"};
        String[] expected = new String[]{"-d", "-h", "input.txt", "output.txt"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithUnknownOption() {
        String[] arguments = new String[]{"-x", "input.txt", "output.txt"};
        new GnuParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithLongOption() {
        String[] arguments = new String[]{"--long-option", "input.txt", "output.txt"};
        new GnuParser().flatten(options, arguments, false);
    }

    @Test
    public void testFlattenWithBooleanOption() {
        String[] arguments = new String[]{"-d"};
        String[] expected = new String[]{"-d"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithValueOption() {
        String[] arguments = new String[]{"-i", "input.txt", "-o", "output.txt"};
        String[] expected = new String[]{"-i", "input.txt", "-o", "output.txt"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithValueOptionWithValue() {
        String[] arguments = new String[]{"-i", "input.txt", "-o", "output.txt", "--"};
        String[] expected = new String[]{"-i", "input.txt", "-o", "output.txt"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithValueOptionWithValueLong() {
        String[] arguments = new String[]{"-i", "input.txt", "--", "output.txt"};
        String[] expected = new String[]{"-i", "input.txt", "--", "output.txt"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithTwoValueOptions() {
        String[] arguments = new String[]{"-i", "input1.txt", "-o", "output1.txt", "-i", "input2.txt", "-o", "output2.txt"};
        String[] expected = new String[]{"-i", "input1.txt", "-o", "output1.txt", "-i", "input2.txt", "-o", "output2.txt"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithValueOptionWithValueLongAndValue() {
        String[] arguments = new String[]{"-i", "input.txt", "--", "output.txt=longoutput.txt"};
        String[] expected = new String[]{"-i", "input.txt", "--", "output.txt=longoutput.txt"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithTwoValueOptionsAndOneNonOption() {
        String[] arguments = new String[]{"-i", "input1.txt", "-o", "output1.txt", "nonoption"};
        String[] expected = new String[]{"-i", "input1.txt", "-o", "output1.txt", "nonoption"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithTwoValueOptionsAndOneNonOptionWithStopAtNonOption() {
        String[] arguments = new String[]{"-i", "input1.txt", "-o", "output1.txt", "nonoption"};
        String[] expected = new String[]{"-i", "input1.txt"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithEmptyArgument() {
        String[] arguments = new String[]{"--", ""};
        String[] expected = new String[]{"--"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        String[] arguments = new String[]{"--"};
        String[] expected = new String[]{"--"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndSingleOption() {
        String[] arguments = new String[]{"--", "-d"};
        String[] expected = new String[]{"--", "-d"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndMultipleOptions() {
        String[] arguments = new String[]{"--", "-d", "-h"};
        String[] expected = new String[]{"--", "-d", "-h"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}