package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test suite for {@link PosixParser} class.
 */
public class PosixParserTest
{
    /**
     * Test for basic functionality of {@link PosixParser#flatten(Options, String[], boolean)} method.
     */
    @Test
    public void testBasicFunctionality()
    {
        String[] args = {"--test", "-a", "-b", "--optionA", "-c=value", "--optionB=value2", "--optionC"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expectedArgs = Arrays.asList("--test", "-a", "--optionA", "-c=value", "--optionB=value2", "--optionC");

        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test edge case for empty arguments.
     */
    @Test
    public void testEmptyArguments()
    {
        String[] args = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        try
        {
            parser.flatten(options, args, false);
            Assert.fail();
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test edge case for single argument.
     */
    @Test
    public void testSingleArgument()
    {
        String[] args = {"arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expectedArgs = Arrays.asList("arg");

        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test edge case for long option without argument.
     */
    @Test
    public void testLongOptionWithoutArgument()
    {
        String[] args = {"--test"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("test", false, null, "Test flag");

        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expectedArgs = Arrays.asList("--test");

        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test edge case for long option with argument.
     */
    @Test
    public void testLongOptionWithArgument()
    {
        String[] args = {"--test=arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("test", true, null, "Test argument");

        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expectedArgs = Arrays.asList("--test=arg");

        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test edge case for multiple options.
     */
    @Test
    public void testMultipleOptions()
    {
        String[] args = {"-a", "--optionA", "-b", "--optionB", "--optionC"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        options.addOption("a", false, null, "Option A");
        options.addOption("optionA", false, null, "Option A");
        options.addOption("b", false, null, "Option B");
        options.addOption("optionB", false, null, "Option B");
        options.addOption("c", false, null, "Option C");

        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expectedArgs = Arrays.asList("-a", "--optionA", "-b", "--optionB", "--optionC");

        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test edge case for mixed options and arguments.
     */
    @Test
    public void testMixedOptionsAndArguments()
    {
        String[] args = {"--test", "arg1", "--optionA", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        options.addOption("test", false, null, "Test flag");
        options.addOption("optionA", true, null, "Option argument");

        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expectedArgs = Arrays.asList("--test", "arg1", "--optionA=arg2");

        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test exception handling for invalid option.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOption()
    {
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    /**
     * Test exception handling for invalid long option with argument.
     */
    @Test(expected = ParseException.class)
    public void testInvalidLongOptionWithArgument()
    {
        String[] args = {"--invalidOption=arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    /**
     * Test exception handling for invalid long option without argument.
     */
    @Test(expected = ParseException.class)
    public void testInvalidLongOptionWithoutArgument()
    {
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    /**
     * Test exception handling for an invalid token.
     */
    @Test(expected = ParseException.class)
    public void testInvalidToken()
    {
        String[] args = {"invalid"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    /**
     * Test exception handling for an empty option list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyOptionList()
    {
        String[] args = {};
        PosixParser parser = new PosixParser();

        parser.flatten(null, args, false);
    }
}