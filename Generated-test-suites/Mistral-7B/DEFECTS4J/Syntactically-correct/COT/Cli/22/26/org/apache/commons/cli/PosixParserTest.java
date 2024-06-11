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
     * with valid options and arguments.
     */
    @Test
    public void testFlattenValidOptionsArguments()
    {
        String[] argList = {"--option1", "--option2=value2", "--option3", "-o", "output.txt"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("option3", false, "Option 3");
        options.addOption("o", "outputFile", true, "Output file");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, argList, false);

        List expectedResult = Arrays.asList(new String[]{"option1", "option2", "value2", "option3", "outputFile", "output.txt"});
        Assert.assertArrayEquals(expectedResult.toArray(), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with long options and no arguments.
     */
    @Test
    public void testFlattenLongOptionsNoArguments()
    {
        String[] argList = {"--long-option1", "--long-option2"};
        Options options = new Options();
        options.addOption("longOption1", false, "Long Option 1");
        options.addOption("longOption2", false, "Long Option 2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, argList, false);

        List expectedResult = Arrays.asList(new String[]{"longOption1", "longOption2"});
        Assert.assertArrayEquals(expectedResult.toArray(), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with long options and arguments.
     */
    @Test
    public void testFlattenLongOptionsWithArguments()
    {
        String[] argList = {"--long-option1=value1", "--long-option2=value2"};
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");
        options.addOption("longOption2", true, "Long Option 2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, argList, false);

        List expectedResult = Arrays.asList(new String[]{"longOption1=value1", "longOption2=value2"});
        Assert.assertArrayEquals(expectedResult.toArray(), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with single hyphen.
     */
    @Test
    public void testFlattenSingleHyphen()
    {
        String[] argList = {"-", "--option1"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, argList, false);

        List expectedResult = Arrays.asList(new String[]{"--", "option1"});
        Assert.assertArrayEquals(expectedResult.toArray(), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with stopAtNonOption.
     */
    @Test
    public void testFlattenStopAtNonOption()
    {
        String[] argList = {"--option1", "-n", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("n", "noOption", false, "No Option");
        options.addOption("option2", true, "Option 2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, argList, true);

        List expectedResult = Arrays.asList(new String[]{"option1", "--", "noOption"});
        Assert.assertArrayEquals(expectedResult.toArray(), result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with invalid option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFlattenInvalidOption()
    {
        String[] argList = {"--invalid-option"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, argList, false);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with empty argument list.
     */
    @Test
    public void testFlattenEmptyArgumentList()
    {
        String[] argList = {};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, argList, false);

        List expectedResult = Arrays.asList(new String[]{"--"});
        Assert.assertArrayEquals(expectedResult.toArray(), result);
    }
}