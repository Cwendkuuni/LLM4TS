package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for GnuParser
 */
public class GnuParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        Options options = new Options();
        String[] expected = {};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        String[] args = {"arg"};
        Options options = new Options();
        String[] expected = {"arg"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = {"-o"};
        Options options = new Options();
        String[] expected = {"-o"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionWithValue() {
        String[] args = {"-o", "value"};
        Options options = new Options();
        String[] expected = {"-o", "value"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValueBeforeOption() {
        String[] args = {"value", "-o"};
        Options options = new Options();
        String[] expected = {"value", "-o"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleOption() {
        String[] args = {"--option"};
        Options options = new Options();
        String[] expected = {"--option"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleOptionWithValue() {
        String[] args = {"--option", "value"};
        Options options = new Options();
        String[] expected = {"--option", "value"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_SingleChar() {
        String[] args = {"-a"};
        Options options = new Options();
        new GnuParser().flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_LongerThanTwoChars() {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        new GnuParser().flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_WithValue() {
        String[] args = {"--invalidOption", "value"};
        Options options = new Options();
        new GnuParser().flatten(options, args, false);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] args = {"--"};
        Options options = new Options();
        String[] expected = {"--"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-o", "value", "nonOption"};
        Options options = new Options();
        String[] expected = {"-o", "value"};
        String[] result = new GnuParser().flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleArguments() {
        String[] args = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }
}