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
 * Test class for PosixParser.
 */
public class PosixParserTest {

    private static final String[] ARGUMENTS_1 = {"--foo", "bar", "--baz=qux", "-c", "--", "alpha", "-d", "--delta=epsilon"};
    private static final String[] ARGUMENTS_2 = {"--foo", "--", "-c", "delta"};
    private static final String[] ARGUMENTS_3 = {"--foo=bar", "--baz", "qux", "--", "--delta=epsilon"};
    private static final String[] ARGUMENTS_4 = {"--foo=bar", "--baz", "qux", "--delta"};
    private static final String[] ARGUMENTS_5 = {"--foo=bar", "--baz", "qux", "--delta", "-d"};
    private static final String[] ARGUMENTS_6 = {"--foo=bar", "--baz", "", "--delta"};

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("foo", "An option with an argument, FOO", true, "foo");
        options.addOption("baz", "An option with an optional argument, BAZ", true, "baz");
        options.addOption("delta", "An option with an optional argument, DELTA", true, "delta");
        options.addOption("c", "A shorthand option, C");
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] result = parser.flatten(options, new String[0], false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_EmptyArguments() {
        String[] result = parser.flatten(options, new String[0], false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_NoSpecialTokens() {
        String[] result = parser.flatten(options, ARGUMENTS_1, false);
        String[] expected = {"--foo", "bar", "--baz", "qux", "--delta=epsilon", "-c", "--alpha", "--delta=epsilon"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidLongOption() {
        String[] arguments = {"--invalidOption"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingArgument() {
        String[] arguments = {"--foo"};
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingArgumentForOptionWithArg() {
        String[] arguments = {"--foo"};
        parser.flatten(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingArgumentForOptionWithOptionalArg() {
        String[] arguments = {"--baz"};
        parser.flatten(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingArgumentForOptionWithMandatoryArg() {
        String[] arguments = {"--delta"};
        parser.flatten(options, arguments, true);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] result = parser.flatten(options, ARGUMENTS_2, true);
        Assert.assertArrayEquals(new String[]{"--foo", "--", "-c", "--delta"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_InvalidOption() {
        String[] result = parser.flatten(options, new String[]{"--invalidOption"}, true);
        Assert.assertArrayEquals(new String[]{"--invalidOption"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_InvalidOptionWithArgument() {
        String[] result = parser.flatten(options, new String[]{"--invalidOption=arg"}, true);
        Assert.assertArrayEquals(new String[]{"--invalidOption=arg"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_AfterOptionWithOptionalArg() {
        String[] result = parser.flatten(options, new String[]{"--foo", "arg1", "--baz"}, true);
        Assert.assertArrayEquals(new String[]{"--foo", "arg1", "--baz"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_StopAtNonOption_AfterInvalidOption() {
        String[] result = parser.flatten(options, new String[]{"--invalidOption"}, true);
        Assert.assertArrayEquals(new String[]{"--invalidOption"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_AfterOptionWithOptionalArgAndValue() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "--baz=arg2"}, true);
        Assert.assertArrayEquals(new String[]{"--foo=arg1", "--baz=arg2"}, result);
    }

    @Test
    public void testFlatten_BurstToken_SingleCharacterOptionWithArg() {
        String[] result = parser.flatten(options, new String[]{"-f", "arg1", "-b", "arg2"}, false);
        Assert.assertArrayEquals(new String[]{"--foo=arg1", "--baz=arg2"}, result);
    }

    @Test
    public void testFlatten_BurstToken_SingleCharacterOptionWithOptionalArg() {
        String[] result = parser.flatten(options, new String[]{"-c"}, false);
        Assert.assertArrayEquals(new String[]{"--c"}, result);
    }

    @Test
    public void testFlatten_BurstToken_LongOptionWithArg() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "--baz", "arg2"}, false);
        Assert.assertArrayEquals(new String[]{"--foo=arg1", "--baz", "--delta=arg2"}, result);
    }

    @Test
    public void testFlatten_BurstToken_LongOptionWithOptionalArgAndValue() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "--baz=arg2", "--delta"}, false);
        Assert.assertArrayEquals(new String[]{"--foo=arg1", "--baz=arg2", "--delta"}, result);
    }

    @Test
    public void testFlatten_BurstToken_LongOptionWithArgAndValue() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "arg2"}, false);
        Assert.assertArrayEquals(new String[]{"--foo=arg1", "--delta=arg2"}, result);
    }

    @Test
    public void testFlatten_BurstToken_LongOptionWithArgAndValueAndStopAtNonOption() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "arg2"}, true);
        Assert.assertArrayEquals(new String[]{"--foo=arg1", "--delta=arg2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_BurstToken_LongOptionWithoutArg() {
        String[] result = parser.flatten(options, new String[]{"--foo"}, false);
        Assert.assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_BurstToken_LongOptionWithoutArgAndStopAtNonOption() {
        String[] result = parser.flatten(options, new String[]{"--foo"}, true);
        Assert.assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_BurstToken_MultipleInvalidOptions() {
        String[] result = parser.flatten(options, new String[]{"--invalidOption1", "--invalidOption2"}, false);
        Assert.assertArrayEquals(new String[]{"--invalidOption1", "--invalidOption2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_BurstToken_MultipleInvalidOptionsWithArg() {
        String[] result = parser.flatten(options, new String[]{"--invalidOption1=arg1", "--invalidOption2=arg2"}, false);
        Assert.assertArrayEquals(new String[]{"--invalidOption1=arg1", "--invalidOption2=arg2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_BurstToken_LongOptionWithArgAndInvalidOption() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "--invalidOption"}, false);
        Assert.assertArrayEquals(new String[]{"--foo=arg1"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_BurstToken_LongOptionWithArgAndInvalidOptionWithArg() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "--invalidOption=arg2"}, false);
        Assert.assertArrayEquals(new String[]{"--foo=arg1"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_BurstToken_LongOptionWithArgAndInvalidOptionWithArgAndValue() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "--invalidOption=arg2=arg3"}, false);
        Assert.assertArrayEquals(new String[]{"--foo=arg1"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_BurstToken_LongOptionWithArgAndInvalidOptionWithArgAndValueAndStopAtNonOption() {
        String[] result = parser.flatten(options, new String[]{"--foo=arg1", "--invalidOption=arg2=arg3"}, true);
        Assert.assertArrayEquals(new String[]{"--foo=arg1"}, result);
    }
}