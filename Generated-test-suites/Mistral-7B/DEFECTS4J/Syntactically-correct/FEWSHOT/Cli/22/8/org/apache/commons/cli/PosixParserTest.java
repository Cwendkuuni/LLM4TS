package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = new String[0];
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option");
        String[] arguments = {"--opt"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--opt"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenSingleValueOption() {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option");
        String[] arguments = {"--opt=value"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--opt", "value"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option");
        String[] arguments = {"--opt", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--opt", "arg1", "arg2"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("o1", "opt1", false, "Option 1");
        options.addOption("o2", "opt2", true, "Option 2");
        String[] arguments = {"--opt1", "--opt2", "arg2"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--opt1", "--opt2", "arg2"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("o", "opt", true, "Option");
        String[] arguments = {"--opt=longOption"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--opt", "longOption"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option");
        String[] arguments = {"--opt", "longOption"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--opt", "longOption"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenSingleDash() {
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], actual);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenDoubleDashWithArg() {
        Options options = new Options();
        String[] arguments = {"--", "arg"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "arg"}, actual);
    }

    @Test
    public void testFlattenTwoDoubleDashes() {
        Options options = new Options();
        String[] arguments = {"--", "--"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "--"}, actual);
    }

    @Test
    public void testFlattenLongOptionWithArgAndValue() {
        Options options = new Options();
        options.addOption("l", "longOption", true, "Long option with value");
        String[] arguments = {"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--longOption", "value"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithArgAndValueLongerThanOneCharacter() {
        Options options = new Options();
        options.addOption("l", "longOption", true, "Long option with value");
        String[] arguments = {"--longOption=longValue"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--longOption", "longValue"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithArgAndValueAndTrailingDoubleDash() {
        Options options = new Options();
        options.addOption("l", "longOption", true, "Long option with value");
        String[] arguments = {"--longOption=longValue--"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"--longOption", "longValue"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenBurstableToken() {
        Options options = new Options();
        String[] arguments = {"-ab"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenBurstableTokenWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "arg"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "arg"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenBurstableTokenWithArgAndValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "arg", "value"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "arg", "value"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenBurstableTokenWithValueAndTrailingDoubleDash() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "value--"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "value"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenBurstableTokenWithTwoValues() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "value1", "value2"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option");
        String[] arguments = {"--opt", "-f", "--foo", "arg"};
        PosixParser parser = new PosixParser();
        String[] actual = parser.flatten(options, arguments, true);
        String[] expected = {"--opt", "-f"};
        assertArrayEquals(expected, actual);
    }
}