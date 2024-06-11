package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_EmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleArgument() {
        Options options = new Options();
        String[] arguments = {"arg"};
        String[] expected = {"arg"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        Option opt = new Option("o", "option", false, "Option description");
        options.addOption(opt);
        String[] arguments = {"-o"};
        String[] expected = {"-o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option description");
        options.addOption(opt);
        String[] arguments = {"-o", "value"};
        String[] expected = {"-o", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", false, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option"};
        String[] expected = {"--long-option"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option", "value"};
        String[] expected = {"--long-option", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        Options options = new Options();
        String[] arguments = {"--"};
        String[] expected = {"--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphenAndSingleArgument() {
        Options options = new Options();
        String[] arguments = {"--", "arg"};
        String[] expected = {"--", "arg"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphenAndLongArgument() {
        Options options = new Options();
        String[] arguments = {"--", "long-argument"};
        String[] expected = {"--", "long-argument"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphenAndOptions() {
        Options options = new Options();
        Option opt = new Option("o", "option", false, "Option description");
        options.addOption(opt);
        String[] arguments = {"--", "-o"};
        String[] expected = {"--", "-o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphenAndOptionWithValue() {
        Options options = new Options();
        Option opt = new Option("o", "option", true, "Option description");
        options.addOption(opt);
        String[] arguments = {"--", "-o", "value"};
        String[] expected = {"--", "-o", "value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndDoubleHyphen() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option", "--"};
        String[] expected = {"--long-option", "--"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndSingleArgument() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option", "arg"};
        String[] expected = {"--long-option", "arg"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndLongArgument() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option", "long-argument"};
        String[] expected = {"--long-option", "long-argument"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndDoubleHyphenAndSingleArgument() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option", "--", "arg"};
        String[] expected = {"--long-option", "--", "arg"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndDoubleHyphenAndLongArgument() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option", "--", "long-argument"};
        String[] expected = {"--long-option", "--", "long-argument"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndDoubleHyphenAndOptions() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        Option opt2 = new Option("o", "option2", false, "Option description 2");
        options.addOption(opt2);
        String[] arguments = {"--long-option", "--", "-o"};
        String[] expected = {"--long-option", "--", "-o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndDoubleHyphenAndLongOption() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        Option opt2 = new Option("l2", "long-option2", false, "Long option description 2");
        options.addOption(opt2);
        String[] arguments = {"--long-option", "--", "--long-option2"};
        String[] expected = {"--long-option", "--", "--long-option2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        Option opt = new Option("o", "option", false, "Option description");
        options.addOption(opt);
        String[] arguments = {"arg1", "arg2", "-o", "arg3"};
        String[] expected = {"arg1", "arg2", "-o"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleHyphenAndStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--", "arg1", "arg2"};
        String[] expected = {"--", "arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndStopAtNonOption() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option", "arg1", "arg2"};
        String[] expected = {"--long-option", "arg1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValueAndDoubleHyphenAndStopAtNonOption() {
        Options options = new Options();
        Option opt = new Option("l", "long-option", true, "Long option description");
        options.addOption(opt);
        String[] arguments = {"--long-option", "--", "arg1", "arg2"};
        String[] expected = {"--long-option", "--", "arg1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_NullArguments() {
        Options options = new Options();
        String[] arguments = null;
        GnuParser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_NullOptions() {
        String[] arguments = {"arg1", "arg2"};
        GnuParser parser = new GnuParser();
        parser.flatten(null, arguments, false);
    }
}