package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * The class PosixParserTest provides test cases for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Test case 1: Empty arguments
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] expected = {};
        Options options = new Options();

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test case 2: Single argument
        arguments = new String[]{"arg1"};
        expected = new String[]{"arg1"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test case 3: Special tokens
        arguments = new String[]{"--flag"};
        expected = new String[]{"--flag"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"--flag=value"};
        expected = new String[]{"--flag", "value"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"--flag1=value1", "--flag2=value2"};
        expected = new String[]{"--flag1", "value1", "--flag2", "value2"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test case 4: Single hyphen
        arguments = new String[]{"-"};
        expected = new String[]{"-"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test case 5: Option with no argument
        arguments = new String[]{"-o"};
        expected = new String[]{"-o"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test case 6: Option with required argument
        options.addRequiredOption("o", "optOption", false, "requiredArg");
        arguments = new String[]{"-o", "requiredArg"};
        expected = new String[]{"-o", "requiredArg"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-o", "arg1", "requiredArg"};
        assertArrayEquals(new String[]{"-o", "arg1"}, result);

        // Test case 7: Option with optional argument
        options.addOption("o", "optOption", true, "optionalArg");
        arguments = new String[]{"-o", "arg1", "optionalArg"};
        expected = new String[]{"-o", "arg1", "optionalArg"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-o", "arg1"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "arg1"}, result);

        // Test case 8: Option with multiple arguments
        options.addOption("o", "optOption", true, "optionalArgs");
        arguments = new String[]{"-o", "arg1", "arg2"};
        expected = new String[]{"-o", "arg1", "arg2"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-o", "arg1"};
        expected = new String[]{"-o", "arg1"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test case 9: Stop at non-option
        arguments = new String[]{"-o", "arg1", "nonOption"};
        expected = new String[]{"-o", "arg1"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);

        // Test case 10: Bursting
        arguments = new String[]{"-o", "arg1", "-ab"};
        expected = new String[]{"-o", "arg1", "-a", "-b"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-o", "arg1", "-a", "-b"};
        expected = new String[]{"-o", "arg1", "-a", "-b"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-o", "arg1", "-a--flag", "-b"};
        expected = new String[]{"-o", "arg1", "-a--flag", "-b"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-o", "arg1", "-a--flag=value", "-b"};
        expected = new String[]{"-o", "arg1", "-a--flag=value"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        // Test case 11: Bursting with stop at non-option
        arguments = new String[]{"-o", "arg1", "-a--flag", "-b"};
        expected = new String[]{"-o", "arg1", "-a"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-o", "arg1", "-a--flag=value", "-b"};
        expected = new String[]{"-o", "arg1", "-a--flag"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
}