package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        String[] arguments = new String[]{"-D", "foo"};
        String[] expected = new String[]{"-D", "foo"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-f"};
        String[] expected = new String[]{"-f"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};
        String[] expected = new String[]{"--"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "bar"};
        String[] expected = new String[]{"--foo", "bar"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        String[] arguments = new String[]{"-"};
        String[] expected = new String[]{"-"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleHyphenWithOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-f"};
        String[] expected = new String[]{"-f"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleHyphenAndOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo"};
        String[] expected = new String[]{"--foo"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleHyphenAndArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "bar"};
        String[] expected = new String[]{"--foo", "bar"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionAndArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"-Dfoo", "bar"};
        String[] expected = new String[]{"-Dfoo", "bar"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionAndLongOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-Dfoo=123", "bar"};
        String[] expected = new String[]{"-Dfoo=123", "bar"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionWithValueAndArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"-Dfoo=value", "bar", "baz"};
        String[] expected = new String[]{"-Dfoo=value", "bar", "baz"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=value", "bar", "baz"};
        String[] expected = new String[]{"--foo=value", "bar", "baz"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndValue() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=value"};
        String[] expected = new String[]{"--foo=value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo"};
        String[] expected = new String[]{"--foo"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndEmptyValue() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo="};
        String[] expected = new String[]{"--foo="};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndNoValue() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo"};
        String[] expected = new String[]{"--foo"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndValueAndArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=value", "bar", "baz"};
        String[] expected = new String[]{"--foo=value", "bar", "baz"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndValueAndSingleArg() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=value", "arg"};
        String[] expected = new String[]{"--foo=value", "arg"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndValueAndMultipleArgs() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=value", "arg1", "arg2"};
        String[] expected = new String[]{"--foo=value", "arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndValueAndEmptyArg() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo=value", ""};
        String[] expected = new String[]{"--foo=value", ""};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"arg"};
        String[] expected = new String[]{"arg"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithTwoArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2"};
        String[] expected = new String[]{"arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithThreeArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2", "arg3"};
        String[] expected = new String[]{"arg1", "arg2", "arg3"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNonOptionArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"--", "arg"};
        String[] expected = new String[]{"arg"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNonOptionArgumentAndSingleOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "arg"};
        String[] expected = new String[]{"--foo", "arg"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNonOptionArgumentAndMultipleOptions() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "--bar", "arg"};
        String[] expected = new String[]{"--foo", "--bar", "arg"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMaximumLengthArgument() {
        final int maxLength = 1024 * 1024;
        Options options = new Options();
        String arg = new String(new char[maxLength]).replace('\0', 'a');
        String[] arguments = new String[]{arg};
        String[] expected = new String[]{arg};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testFlattenWithInvalidArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"--", null};
        new GnuParser().flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithNullOptions() {
        String[] arguments = new String[]{"--", "arg"};
        new GnuParser().flatten(null, arguments, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "--bar", "arg"};
        String[] expected = new String[]{"--foo"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndSingleArgument() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "arg"};
        String[] expected = new String[]{"--foo"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndMultipleArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "arg1", "arg2"};
        String[] expected = new String[]{"--foo"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLastArgumentIsOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "--bar"};
        String[] expected = new String[]{"--foo"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndLastArgumentIsDoubleDash() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "--"};
        String[] expected = new String[]{"--foo"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndEmptyArguments() {
        Options options = new Options();
        String[] arguments = new String[]{""};
        String[] expected = new String[]{""};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionAndNoArguments() {
        Options options = new Options();
        String[] arguments = new String[]{"--"};
        String[] expected = new String[]{"--"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
}