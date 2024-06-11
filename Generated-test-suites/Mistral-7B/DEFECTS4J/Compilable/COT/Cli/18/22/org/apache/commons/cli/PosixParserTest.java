package org.apache.commons.cli; // make sure to import necessary packages

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with empty arguments.
     */
    @Test
    public void testEmptyArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = new String[0];
        String[] expected = new String[0];
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with simple arguments.
     */
    @Test
    public void testSimpleArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with -- token.
     */
    @Test
    public void testDoubleHyphen() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--", "arg1"};
        String[] expected = {"--"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with --token=value token.
     */
    @Test
    public void testDoubleHyphenWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("token", true, "description");
        String[] args = {"--token", "value"};
        String[] expected = {"--token", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with single hyphen token.
     */
    @Test
    public void testSingleHyphen() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"-"};
        String[] expected = {"--"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid option token.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"-xxxx"};
        parser.flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with option token with value.
     */
    @Test
    public void testOptionWithValue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "description");
        String[] args = {"-option", "value"};
        String[] expected = {"-option", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with option token with multiple values.
     */
    @Test
    public void testOptionWithMultipleValues() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "description");
        String[] args = {"-option", "value1", "value2"};
        String[] expected = {"-option", "value1", "value2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with stopAtNonOption flag.
     */
    @Test
    public void testStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"arg1", "--nonOption", "arg2"};
        String[] expected = {"arg1", "--nonOption"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with -- and value after an option.
     */
    @Test
    public void testOptionWithValueAfterDoubleHyphen() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "description");
        String[] args = {"-option", "--", "value"};
        String[] expected = {"-option", "--", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid argument after an option.
     */
    @Test(expected = ParseException.class)
    public void testInvalidArgumentAfterOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "description");
        String[] args = {"-option", "--", "value--"};
        parser.flatten(options, args, false);
    }

    

}