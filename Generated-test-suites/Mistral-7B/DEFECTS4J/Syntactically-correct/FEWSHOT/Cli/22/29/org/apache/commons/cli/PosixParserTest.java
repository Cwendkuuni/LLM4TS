package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_Empty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_Options() {
        String[] arguments = {"--foo", "--bar=baz", "--baz=qux", "-f", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Specify foo");
        options.addOption("bar", true, "b", "Specify bar");
        options.addOption("baz", true, "z", "Specify baz");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"foo", "bar=baz", "baz=qux", "f", "--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_Arguments() {
        String[] arguments = {"--", "-f", "arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(Arrays.copyOfRange(arguments, 1, arguments.length), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"--foo", "--bar=baz", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Specify foo");
        options.addOption("bar", true, "b", "Specify bar");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"--foo", "--bar=baz", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOption() {
        String[] arguments = {"--qux", "--", "-f", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Specify foo");
        options.addOption("bar", true, "b", "Specify bar");

        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, arguments, false);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown option: qux", e.getMessage());
        }
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] arguments = {"--foo=bar", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Specify foo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"foo=bar", "--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithMultipleValues() {
        String[] arguments = {"--foo=bar1,bar2", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Specify foo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"foo=bar1,bar2", "--", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_ValidOption() {
        String token = "-foo";
        PosixParser parser = new PosixParser();
        Option option = new Option("foo", true, "f", "Specify foo");
        parser.options.addOption(option);

        List tokens = parser.burstToken(token, false);
        assertEquals(Arrays.asList("-f", "foo"), tokens);
    }

    @Test
    public void testBurstToken_EmptyToken() {
        String token = "";
        PosixParser parser = new PosixParser();

        List tokens = parser.burstToken(token, false);
        assertEquals(Arrays.asList(""), tokens);
    }

    @Test
    public void testBurstToken_InvalidOption() {
        String token = "-qux";
        PosixParser parser = new PosixParser();

        List tokens = parser.burstToken(token, false);
        assertNull(tokens);
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        String token = "--qux";
        PosixParser parser = new PosixParser();

        List tokens = parser.burstToken(token, true);
        assertArrayEquals(new String[]{"--qux"}, tokens.toArray(new String[0]));
    }

    @Test
    public void testBurstToken_OptionWithValue() {
        String token = "--foo=bar";
        PosixParser parser = new PosixParser();
        Option option = new Option("foo", true, "f", "Specify foo");
        parser.options.addOption(option);

        List tokens = parser.burstToken(token, false);
        assertEquals(Arrays.asList("-f", "foo", "bar"), tokens);
    }

    @Test
    public void testBurstToken_OptionWithMultipleValues() {
        String token = "--foo=bar1,bar2";
        PosixParser parser = new PosixParser();
        Option option = new Option("foo", true, "f", "Specify foo");
        parser.options.addOption(option);

        List tokens = parser.burstToken(token, false);
        assertEquals(Arrays.asList("-f", "foo", "bar1,bar2"), tokens);
    }
}