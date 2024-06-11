package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        String[] arguments = {"-D"};
        String[] expected = {"-D"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        String[] arguments = {"arg"};
        String[] expected = {"arg"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithValue() {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value"};
        String[] expected = {"-D", "property", "value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("opt1", true, "h");
        options.addOption("opt2", true, "i");
        String[] arguments = {"-h", "-i"};
        String[] expected = {"-h", "-i"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleArguments() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithLongName() {
        Options options = new Options();
        String[] arguments = {"--long-option"};
        String[] expected = {"--long-option"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithLongNameAndValue() {
        Options options = new Options();
        String[] arguments = {"--long-option=value"};
        String[] expected = {"--long-option", "value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithTwoCharShortName() {
        Options options = new Options();
        String[] arguments = {"--oo"};
        String[] expected = {"--oo"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithTwoCharShortNameAndValue() {
        Options options = new Options();
        String[] arguments = {"--oo=value"};
        String[] expected = {"--oo", "value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "arg"};
        String[] expected = {"-Dproperty=value"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] arguments = {"--"};
        String[] expected = {"--"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDoubleDashWithArguments() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2"};
        String[] expected = {"--", "arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyOption() {
        Options options = new Options();
        String[] arguments = {"-"};
        String[] expected = {};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyOptionWithArguments() {
        Options options = new Options();
        String[] arguments = {"-", "arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyOptionWithArgumentsAndStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-", "arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArguments() {
        Options options = new Options();
        String[] arguments = null;
        new GnuParser().flatten(options, arguments, false);
    }

    @Test
    public void testFlattenNullOptions() {
        String[] arguments = {"-Dproperty=value"};
        assertNull(new GnuParser().flatten(null, arguments, false));
    }

    @Test
    public void testFlattenNullResults() {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value"};
        String[] expected = {"-Dproperty=value"};
        assertSame(expected, new GnuParser().flatten(options, arguments, false));
        assertArrayEquals(expected, new GnuParser().flatten(options, arguments, false));
    }

    @Test
    public void testFlattenWithLongArguments() {
        Options options = new Options();
        String[] arguments = {"--very-long-option-name-that-is-longer-than-the-default-maximum-length-of-an-argument-string-in-java"};
        String[] expected = {"--very-long-option-name-that-is-longer-than-the-default-maximum-length-of-an-argument-string-in-java"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}