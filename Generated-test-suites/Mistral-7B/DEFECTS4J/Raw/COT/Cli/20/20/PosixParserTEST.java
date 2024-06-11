package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 2, "option B with an integer argument");
        options.addOption("c", 3, "option C with a three-letter argument");
    }

    @Test
    public void testFlatten_noOptions() throws ParseException {
        String[] args = new String[]{"test"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"test"}, result);
    }

    @Test
    public void testFlatten_singleOption() throws ParseException {
        String[] args = new String[]{"--a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a"}, result);
    }

    @Test
    public void testFlatten_optionWithArgument() throws ParseException {
        String[] args = new String[]{"--a", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "value"}, result);
    }

    @Test
    public void testFlatten_optionWithLongArgument() throws ParseException {
        String[] args = new String[]{"--a", "long value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "long value"}, result);
    }

    @Test
    public void testFlatten_multipleOptions() throws ParseException {
        String[] args = new String[]{"--a", "--b", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "--b", "1"}, result);
    }

    @Test
    public void testFlatten_optionWithArgumentAndOtherArgs() throws ParseException {
        String[] args = new String[]{"--a", "value", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "value", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_singleDash() throws ParseException {
        String[] args = new String[]{"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_doubleDash() throws ParseException {
        String[] args = new String[]{"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_invalidOption() throws ParseException {
        String[] args = new String[]{"--x"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_stopAtNonOption() throws ParseException {
        String[] args = new String[]{"--a", "value", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("--a", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_longOptionWithValue() throws ParseException {
        String[] args = new String[]{"--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlatten_optionWithShortAndLongArgument() throws ParseException {
        String[] args = new String[]{"-a", "short-arg", "--long-option", "long-arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "short-arg", "--long-option", "long-arg"}, result);
    }

    @Test
    public void testFlatten_optionWithVariableArgument() throws ParseException {
        options.addOption("x", 1, "option X", true);
        String[] args = new String[]{"--x", "value1", "--x", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--x", "value1", "--x", "value2"}, result);
    }

    @Test
    public void testFlatten_optionWithMinimumArgument() throws ParseException {
        options.addOption("y", 1, "option Y", true);
        String[] args = new String[]{"--y"};
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFlatten_optionWithMaximumArgument() throws ParseException {
        options.addOption("z", 1, "option Z", true);
        String[] args = new String[]{"--z", "value1", "--z", "value2", "--z", "value3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--z", "value1", "--z", "value2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), Arrays.copyOfRange(result, 0, 3));
    }

    @Test
    public void testFlatten_optionWithMinMaxArgument() throws ParseException {
        options.addOption("w", 0, "option W", true);
        String[] args = new String[]{"--w"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--w"}, result);

        String[] args2 = new String[]{"--w", "value"};
        PosixParser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options, args2, false);
        Assert.assertArrayEquals(new String[]{"--w", "value"}, result2);
    }
}