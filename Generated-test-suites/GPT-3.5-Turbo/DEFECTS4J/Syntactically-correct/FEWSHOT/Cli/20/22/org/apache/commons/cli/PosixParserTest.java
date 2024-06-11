package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("f", false, "option f");
        options.addOption("foo", true, "option foo");
        options.addOption("bar", true, "option bar");
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        String[] arguments = {"-f", "arg1", "-f", "arg2"};
        String[] expected = {"-f", "arg1", "-f", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] arguments = {"-f", "arg1", "--foo", "value1", "-bar", "value2"};
        String[] expected = {"-f", "arg1", "--foo", "value1", "-bar", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionArgument() {
        String[] arguments = {"-foo", "value"};
        String[] expected = {"-foo", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"-f", "arg1", "--", "-f", "arg2"};
        String[] expected = {"-f", "arg1", "--", "-f", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstToken() {
        String[] arguments = {"-foob", "arg1"};
        String[] expected = {"-f", "-o", "-o", "-b", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBurstTokenAndStopAtNonOption() {
        String[] arguments = {"-foob", "arg1", "-f", "-b"};
        String[] expected = {"-f", "-o", "-o", "-b", "arg1", "-f", "-b"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

}