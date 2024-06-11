package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten_NoOptions_NoHyphen() {
        String[] arguments = new String[]{"arg1", "arg2"};
        Options options = new Options();
        String[] expected = new String[]{"arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithLongArgument_Hyphen() {
        String[] arguments = new String[]{"-a", "longArgument"};
        Options options = new Options();
        String[] expected = new String[]{"-a", "longArgument"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithShortArgument_TwoHyphens() {
        String[] arguments = new String[]{"--option", "value"};
        Options options = new Options();
        String[] expected = new String[]{"--option", "value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValue_NoHyphen() {
        String[] arguments = new String[]{"option", "value"};
        Options options = new Options();
        String[] expected = new String[]{"option", "value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithValue_Hyphen() {
        String[] arguments = new String[]{"-option", "value"};
        Options options = new Options();
        String[] expected = new String[]{"-option", "value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_LongHyphen() {
        String[] arguments = new String[]{"-invalidOption"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_ShortHyphen() {
        String[] arguments = new String[]{"-.-invalidOption"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption_DoubleDash() {
        String[] arguments = new String[]{"--invalidOption"};
        Options options = new Options();
        new GnuParser().flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_MultipleOptions_NoValue() {
        String[] arguments = new String[]{"-a", "-b", "arg3"};
        Options options = new Options();
        String[] expected = new String[]{"-a", "-b", "arg3"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions_WithValues() {
        String[] arguments = new String[]{"-a", "value1", "-b", "value2", "arg3"};
        Options options = new Options();
        String[] expected = new String[]{"-a", "value1", "-b", "value2", "arg3"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = new String[]{"-a", "-b", "arg", "nonOption"};
        Options options = new Options();
        String[] expected = new String[]{"-a", "-b", "arg"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleDashWithValue() {
        String[] arguments = new String[]{"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] expected = new String[]{"--option", "value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleDashWithNoValue() {
        String[] arguments = new String[]{"--option"};
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] expected = new String[]{"--option"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

}