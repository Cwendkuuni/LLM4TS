package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with typical usage
     */
    @Test
    public void testTypicalUsage() throws Exception {
        Options options = new Options();
        options.addOption("o", true, "output");
        options.addOption("i", true, "input");
        options.addOption("h", false, "help");

        PosixParser parser = new PosixParser();
        String[] args = {"-o", "output.txt", "-i", "input.txt", "--"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-o", "output.txt", "-i", "input.txt", "--"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with typical usage and no output option
     */
    @Test
    public void testTypicalUsageWithoutOutputOption() throws Exception {
        Options options = new Options();
        options.addOption("i", true, "input");
        options.addOption("h", false, "help");

        PosixParser parser = new PosixParser();
        String[] args = {"-i", "input.txt", "--"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-i", "input.txt", "--"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: option with long name
     */
    @Test
    public void testLongOptionName() throws Exception {
        Options options = new Options();
        options.addOption("long-option-name", true, "long option name");

        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name", "value"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-name", "value"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: long option name with equal sign
     */
    @Test
    public void testLongOptionNameWithEqualSign() throws Exception {
        Options options = new Options();
        options.addOption("long-option-name=", true, "long option name with equal sign");

        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name=value"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-name=value"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: invalid option
     */
    @Test(expected = ParseException.class)
    public void testInvalidOption() throws Exception {
        Options options = new Options();
        options.addOption("o", true, "output");
        options.addOption("i", true, "input");

        PosixParser parser = new PosixParser();
        String[] args = {"-x", "value"};
        parser.flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: multiple invalid options
     */
    @Test(expected = ParseException.class)
    public void testMultipleInvalidOptions() throws Exception {
        Options options = new Options();
        options.addOption("o", true, "output");
        options.addOption("i", true, "input");

        PosixParser parser = new PosixParser();
        String[] args = {"-x", "-y", "value"};
        parser.flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: option with no value
     */
    @Test
    public void testOptionWithoutValue() throws Exception {
        Options options = new Options();
        options.addOption("o", false, "output");

        PosixParser parser = new PosixParser();
        String[] args = {"--o"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--o"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: option with an empty value
     */
    @Test
    public void testOptionWithEmptyValue() throws Exception {
        Options options = new Options();
        options.addOption("o", true, "output");

        PosixParser parser = new PosixParser();
        String[] args = {"--o", ""};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--o"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: option with a value that consists of multiple words
     */
    @Test
    public void testOptionWithMultiWordValue() throws Exception {
        Options options = new Options();
        options.addOption("o", true, "output");

        PosixParser parser = new PosixParser();
        String[] args = {"--o", "output file name"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--o", "output file name"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: multiple options with the same long name
     */
    @Test(expected = ParseException.class)
    public void testMultipleOptionsWithSameLongName() throws Exception {
        Options options = new Options();
        options.addOption("long-option-name", true, "long option name");
        options.addOption("long-option-name", true, "long option name");

        PosixParser parser = new PosixParser();
        String[] args = {"--long-option-name", "value1", "--long-option-name", "value2"};
        parser.flatten(options, args, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: option with the same short name but different long names
     */
    @Test
    public void testOptionWithSameShortNameButDifferentLongNames() throws Exception {
        Options options = new Options();
        options.addOption("o", true, "output1");
        options.addOption("o", true, "output2");

        PosixParser parser = new PosixParser();
        String[] args = {"-o", "output1Value", "-o", "output2Value"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-o", "output1Value", "-o", "output2Value"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: stop at non-option
     */
    @Test
    public void testStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption("o", true, "output");
        options.addOption("i", true, "input");

        PosixParser parser = new PosixParser();
        String[] args = {"-o", "output.txt", "--", "nonOption"};
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"-o", "output.txt", "--", "nonOption"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: all arguments are non-options
     */
    @Test
    public void testAllArgumentsAreNonOptions() throws Exception {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with edge case: no options are present
     */
    @Test
    public void testNoOptions() throws Exception {
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] result = parser.flatten(null, args, false);

        Assert.assertArrayEquals(new String[0], result);
    }
}