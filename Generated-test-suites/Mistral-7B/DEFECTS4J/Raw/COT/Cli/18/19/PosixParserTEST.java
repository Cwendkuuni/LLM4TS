package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlatten_SimpleUsage() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--foo", "bar");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_NoOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("arg1", "arg2");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "arg"};
        Options options = new Options();
        options.addOption("f", true, "A boolean option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-f", "arg");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_OptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=bar"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--foo=bar");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_OptionWithLongName() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name", "value"};
        Options options = new Options();
        options.addOption("longOptionName", true, "A boolean option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--long-option-name=value");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "--bar=value", "arg"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");
        options.addOption("bar", true, "Another boolean option with value");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--foo", "--bar=value", "arg");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");

        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--foo", "arg1", "arg2");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid-option"};
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name=value"};
        Options options = new Options();
        options.addOption("longOptionName", true, "A boolean option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--long-option-name=value");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name"};
        Options options = new Options();
        options.addOption("longOptionName", false, "A boolean option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--long-option-name");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_LongOptionWithShortName() {
        PosixParser parser = new PosixParser();
        String[] args = {"--lo", "value"};
        Options options = new Options();
        options.addOption("lo", true, "A boolean option with short name 'lo'");
        options.addOption("longOptionName", false, "A boolean option with long name 'longOptionName'");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--lo", "value");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_MixedShortAndLongOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-b", "arg1", "--bar=value", "arg2"};
        Options options = new Options();
        options.addOption("foo", false, "A boolean option with short name 'f' and long name 'foo'");
        options.addOption("b", true, "A boolean option with short name 'b' and no long name");
        options.addOption("bar", true, "Another boolean option with long name 'bar'");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--foo", "-b", "--bar=value", "arg2");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}