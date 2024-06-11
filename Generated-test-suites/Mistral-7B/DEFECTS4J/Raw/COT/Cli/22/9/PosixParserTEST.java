package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no arguments and no options.
     */
    @Test
    public void testFlattenNoArguments()
    {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no arguments and valid options.
     */
    @Test
    public void testFlattenValidOptions()
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B with argument");
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--optionA", "--optionB"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with arguments and no options.
     */
    @Test
    public void testFlattenArgumentsNoOptions()
    {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(arguments, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with arguments and valid options.
     */
    @Test
    public void testFlattenArgumentsValidOptions()
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B with argument");
        String[] arguments = {"arg1", "--optionA", "arg3", "--optionB=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1", "--optionA", "--optionB", "value", "arg3"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with arguments, valid options and stopAtNonOption=true.
     */
    @Test
    public void testFlattenArgumentsValidOptionsStopAtNonOption()
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B with argument");
        String[] arguments = {"arg1", "--optionA", "arg3", "--optionB=value", "nonOptionArg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List expectedResult = Arrays.asList("--optionA", "nonOptionArg");
        Assert.assertArrayEquals(expectedResult.toArray(new String[expectedResult.size()]), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with arguments, invalid option and stopAtNonOption=true.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFlattenInvalidOptionStopAtNonOption()
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        String[] arguments = {"arg1", "--invalidOption", "arg3"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);
    }
}